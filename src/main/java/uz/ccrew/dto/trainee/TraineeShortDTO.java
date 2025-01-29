package uz.ccrew.dto.trainee;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeShortDTO {
    private String username;
    private String firstName;
    private String lastName;
}