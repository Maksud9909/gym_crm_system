package uz.ccrew.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ccrew.entity.base.BaseEntity;
import uz.ccrew.entity.base.UserAware;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "trainees")
public class Trainee extends BaseEntity implements UserAware {
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "address")
    private String address;
    @ManyToMany(mappedBy = "trainees")
    private List<Trainer> trainers = new ArrayList<>();
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> training = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
