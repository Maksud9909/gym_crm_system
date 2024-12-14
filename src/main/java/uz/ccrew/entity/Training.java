package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "traingings")
public class Training extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    private Long traineeId;
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Long trainerId;
    @Column(name = "training_name", nullable = false)
    private String trainingName;
    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingTypeEntity trainingType;
    @Column(name = "training_date", nullable = false)
    private LocalDateTime trainingDate;
    @Column(name = "training_duration", nullable = false)
    private Integer trainingDuration;
}
