package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;
    @Column(name = "training_name", nullable = false)
    private String trainingName;
    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingTypeEntity trainingType;
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;
    @Column(name = "training_duration", nullable = false)
    private Double trainingDuration;
}
