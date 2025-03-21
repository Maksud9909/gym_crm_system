package uz.ccrew.service;

import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.training.TrainerMonthlySummaryDTO;

public interface TrainingService {
    void addTraining(TrainingDTO dto);

    void deleteTraining(Long trainingId);

    TrainerMonthlySummaryDTO getMonthlyWorkload(String username, int year, int month);
}
