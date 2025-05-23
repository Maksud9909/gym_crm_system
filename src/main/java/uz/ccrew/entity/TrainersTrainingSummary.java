package uz.ccrew.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.time.Month;

@Document(collation = "trainer_summaries")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrainersTrainingSummary {
    @Id
    private String id;
    private String username;
    private String trainerName;
    private String trainerLastName;
    private Boolean status;
    private Map<Integer, Map<Month, Integer>> summary;
}
