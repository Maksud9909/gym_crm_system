package uz.ccrew.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeCreateDTO {
    private String firstName;
    private String lastName;
    private LocalDate datOfBirth;
    private String address;
}
