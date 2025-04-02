package uz.ccrew.service.impl;

import jakarta.jms.Destination;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.enums.ActionType;
import uz.ccrew.service.TrainingService;
import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.training.TrainerWorkloadDTO;
import uz.ccrew.exp.exp.EntityNotFoundException;
import uz.ccrew.dto.summary.TrainerMonthlySummaryDTO;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final JmsTemplate jmsTemplate;

    @Override
    @Transactional
    @CircuitBreaker(name = "trainerWorkloadCB", fallbackMethod = "fallbackSendTrainingData")
    public void addTraining(TrainingDTO dto) {
        Trainee trainee = traineeDAO.findByUsername(dto.getTraineeUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainee with username=" + dto.getTraineeUsername() + " not found"));

        Trainer trainer = trainerDAO.findByUsername(dto.getTrainerUsername())
                .orElseThrow(() -> new EntityNotFoundException("Trainer with username=" + dto.getTrainerUsername() + " not found"));
        log.info("Trainer: {}", trainer);
        log.info("Trainer User: {}", trainer.getUser());

        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(dto.getTrainingDate())
                .trainingName(dto.getTrainingName())
                .trainingDuration(dto.getTrainingDuration())
                .trainingType(trainer.getTrainingType())
                .build();

        TrainerWorkloadDTO workloadDTO = new TrainerWorkloadDTO(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getIsActive(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                ActionType.ADD
        );
        trainingDAO.create(training);

        try {
            log.info("Sending training data to trainer-workload-service for Action-Type ADD");
            jmsTemplate.convertAndSend("trainer.workload.queue", workloadDTO);
            log.info("Successfully sent training data to trainer-workload-service for Action-Type ADD");
        } catch (Exception e) {
            log.error("Failed to send training data to trainer-workload-service", e);
            throw e;
        }
    }

    @Override
    @CircuitBreaker(name = "trainerWorkloadCB", fallbackMethod = "fallbackGetMonthlyWorkload")
    @SneakyThrows
    public List<TrainerMonthlySummaryDTO> getMonthlyWorkload(String username) {
        Destination replyQueue = new ActiveMQQueue("trainer.workload.reply." + UUID.randomUUID());

        log.info("Sending request for username: {} with reply queue: {}", username, replyQueue);

        jmsTemplate.convertAndSend("trainer.workload.request", username, msg -> {
            msg.setJMSReplyTo(replyQueue);
            return msg;
        });

        jmsTemplate.setReceiveTimeout(5000);
        Message replyMessage = jmsTemplate.receive(replyQueue);

        if (replyMessage instanceof ObjectMessage objectMessage) {
            List<TrainerMonthlySummaryDTO> reply = (List<TrainerMonthlySummaryDTO>) objectMessage.getObject();
            log.info("Received reply for username: {} -> {}", username, reply);
            return reply;
        } else {
            log.error("No valid reply received for username: {}", username);
            return Collections.emptyList();
        }
    }

    private List<TrainerMonthlySummaryDTO> fallbackGetMonthlyWorkload(String username, Throwable ex) {
        log.error("Fallback triggered: trainer-workload-service is unavailable for getting Monthly Workload", ex);
        return Collections.emptyList();
    }

    private void fallbackSendTrainingData(TrainingDTO dto, Throwable ex) {
        log.error("Fallback triggered: trainer-workload-service is unavailable for sending Training data", ex);
    }
}
