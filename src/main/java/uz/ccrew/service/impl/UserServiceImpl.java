package uz.ccrew.service.impl;

import uz.ccrew.entity.User;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.service.UserService;
import uz.ccrew.dto.auth.ChangePasswordDTO;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    @Override
    @Transactional
    public void changePassword(ChangePasswordDTO dto) {
        log.info("Changing password for user '{}'", dto.getUsername());

        User user = userDAO.findByUsernameAndPassword(dto.getUsername(), dto.getOldPassword())
                .orElseThrow(() -> new EntityNotFoundException("Invalid username or password"));

        userDAO.changePassword(user.getId(), dto.getNewPassword());
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
}
