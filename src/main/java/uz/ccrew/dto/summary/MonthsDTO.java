package uz.ccrew.dto.summary;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Month;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MonthsDTO implements Serializable {
    private Month month;
    private double totalDuration;
}
