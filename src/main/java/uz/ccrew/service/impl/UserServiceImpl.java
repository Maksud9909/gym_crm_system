package uz.ccrew.service.impl;

import uz.ccrew.entity.User;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.service.UserService;
import uz.ccrew.dto.auth.ChangePasswordDTO;
import uz.ccrew.exp.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void changePassword(ChangePasswordDTO dto) {
        log.info("Starting password change process for user '{}'",
                dto.getUsername());

        User user = userDAO.findByUsername(dto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format("User '%s' not found at", dto.getUsername())));
        validateOldPassword(user, dto.getOldPassword());

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        userDAO.changePassword(user.getId(), encodedPassword);

        log.info("Password successfully changed for user '{}'", dto.getUsername());
    }

    @Override
    @Transactional
    public void activateDeactivate(String username, Boolean isActive) {
        log.info("Activating/deactivating trainee={}", username);
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            if (user.get().getIsActive().equals(isActive)) {
                log.warn("Trainee with ID={} is already in the desired state (isActive={})", username, isActive);
                return;
            }
        }
        userDAO.activateDeactivate(username, isActive);
        log.info("Trainee with ID={} is now isActive={}", username, isActive);
    }

    private void validateOldPassword(User user, String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Invalid old password attempt for user '{}'",
                    user.getUsername());
            throw new BadCredentialsException(String.format(
                    "Invalid old password for user '%s'", user.getUsername()
            ));
        }
    }
}
