package uz.ccrew.service;

import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.training.summary.TrainerMonthlySummaryDTO;

import java.util.List;

public interface TrainingService {
    void addTraining(TrainingDTO dto);

    void deleteTraining(Long trainingId);

    List<TrainerMonthlySummaryDTO> getMonthlyWorkload(String username);
}
