package uz.ccrew.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ccrew.entity.base.BaseEntity;
import uz.ccrew.entity.base.UserAware;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "trainers")
public class Trainer extends BaseEntity implements UserAware {
    @ManyToMany
    @JoinTable(name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id"))
    private List<Trainee> trainees = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;
    @OneToMany(mappedBy = "trainer")
    private List<Training> training = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
