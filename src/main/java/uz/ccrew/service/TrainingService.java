package uz.ccrew.service;

import uz.ccrew.dto.training.TrainingDTO;
import uz.ccrew.dto.training.summary.TrainerMonthlySummaryDTO;

import java.util.List;

public interface TrainingService {
    void addTraining(TrainingDTO dto);

    List<TrainerMonthlySummaryDTO> getMonthlyWorkload(String username);
}
