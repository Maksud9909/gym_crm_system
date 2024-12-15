package uz.ccrew.entity;

import uz.ccrew.entity.base.BaseEntity;

import lombok.*;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
