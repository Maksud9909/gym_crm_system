package uz.ccrew.service;

import uz.ccrew.dto.auth.ChangePasswordDTO;

public interface UserService {
    void changePassword(ChangePasswordDTO dto);
}
