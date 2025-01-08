package uz.ccrew.entity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import uz.ccrew.entity.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"trainees", "training"})
@Entity
@Table(name = "trainers")
public class Trainer extends BaseEntity {
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id"))
    private List<Trainee> trainees = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private List<Training> training = new ArrayList<>();
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
