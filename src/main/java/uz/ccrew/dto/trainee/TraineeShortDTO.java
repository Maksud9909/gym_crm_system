package uz.ccrew.dto.trainee;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "Trainees")
public class TraineeShortDTO {
    private String username;
    private String firstName;
    private String lastName;
}