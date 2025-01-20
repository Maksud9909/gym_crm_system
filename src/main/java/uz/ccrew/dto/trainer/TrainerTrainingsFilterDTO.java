package uz.ccrew.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingsFilterDTO {
    @NotBlank(message = "Username should be not empty")
    private String username;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeName;
}
