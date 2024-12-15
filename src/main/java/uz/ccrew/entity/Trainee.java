package uz.ccrew.entity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "trainees")
public class Trainee extends User {
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    @Column(name = "address")
    private String address;
    @ManyToMany(mappedBy = "trainees")
    private List<Trainer> trainers = new ArrayList<>();
    @OneToMany(mappedBy = "trainee")
    private List<Training> training = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
