package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import uz.ccrew.entity.base.UserAware;

import java.util.List;
import java.util.ArrayList;

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
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;
    @OneToMany(mappedBy = "trainer")
    private List<Training> training = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
