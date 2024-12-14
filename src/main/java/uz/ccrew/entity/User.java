package uz.ccrew.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import uz.ccrew.entity.base.BaseEntity;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
