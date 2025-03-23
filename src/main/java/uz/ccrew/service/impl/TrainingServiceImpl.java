package uz.ccrew.service.impl;

import org.springframework.http.ResponseEntity;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.dao.TrainingDAO;
import uz.ccrew.entity.Training;
import uz.ccrew.enums.ActionType;
import uz.ccrew.service.TrainingService;
import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.service.TrainerWorkloadClient;
import uz.ccrew.dto.training.TrainerWorkloadDTO;
import uz.ccrew.exp.exp.EntityNotFoundException;
import uz.ccrew.dto.training.summary.TrainerMonthlySummaryDTO;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TrainingDAO trainingDAO;
    private final TrainerWorkloadClient trainerWorkloadClient;

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
            trainerWorkloadClient.sendTrainingData(workloadDTO);
            log.info("Successfully sent training data to trainer-workload-service for Action-Type ADD");
        } catch (Exception e) {
            log.error("Failed to send training data to trainer-workload-service", e);
            throw e;
        }
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "trainerWorkloadCB", fallbackMethod = "fallbackDeleteTrainingData")
    public void deleteTraining(Long trainingId) {
        Training training = trainingDAO.findById(trainingId)
                .orElseThrow(() -> new EntityNotFoundException("Training with id=" + trainingId + " not found"));

        Trainer trainer = training.getTrainer();

        TrainerWorkloadDTO workloadDTO = new TrainerWorkloadDTO(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getIsActive(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                ActionType.DELETE
        );

        trainingDAO.delete(training);

        try {
            log.info("Sending training data to trainer-workload-service to Action-Type DELETE");
            trainerWorkloadClient.sendTrainingData(workloadDTO);
            log.info("Successfully sent training data to trainer-workload-service for Action-Type DELETE");
        } catch (Exception e) {
            log.error("Failed to send DELETE request to trainer-workload-service", e);
        }
    }


    @Override
    @CircuitBreaker(name = "trainerWorkloadCB", fallbackMethod = "fallGetMonthlyWorkload")
    public List<TrainerMonthlySummaryDTO> getMonthlyWorkload(String username) {
        ResponseEntity<List<TrainerMonthlySummaryDTO>> trainerMonthlySummaryDTOS = trainerWorkloadClient.getMonthlyWorkload(username);
        return trainerMonthlySummaryDTOS.getBody();
    }

    private void fallbackGetMonthlyWorkload(String username, Throwable ex) {
        log.error("Fallback triggered: trainer-workload-service is unavailable for getting Monthly Workload", ex);
    }

    private void fallbackSendTrainingData(TrainingDTO dto, Throwable ex) {
        log.error("Fallback triggered: trainer-workload-service is unavailable for sending Training data", ex);
    }

    private void fallbackDeleteTrainingData(Long id, Throwable ex) {
        log.error("Fallback triggered: trainer-workload-service is unavailable for deleting Training data", ex);
    }
}
