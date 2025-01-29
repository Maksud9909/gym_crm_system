package uz.ccrew.dto.trainee;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uz.ccrew.dto.trainer.TrainerDTO;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileUsernameDTO {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate datOfBirth;
    private String address;
    private Boolean isActive;
    List<TrainerDTO> trainers;
}
