package uz.ccrew.dto.training;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingDTO {
    private String trainingName;
    private LocalDate trainingDate;
    private String trainingType;
    private Double trainingDuration;
    private String traineeName;
}

