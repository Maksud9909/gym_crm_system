package uz.ccrew.dto.trainee;

import lombok.*;
import uz.ccrew.dto.trainer.TrainerDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileDTO {
    private String firstName;
    private String lastName;
    private LocalDate datOfBirth;
    private String address;
    private Boolean isActive;
    List<TrainerDTO> trainerDTOS;
}
