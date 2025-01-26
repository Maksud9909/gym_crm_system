package uz.ccrew.service.impl;

import org.springframework.transaction.annotation.Transactional;
import uz.ccrew.dao.UserDAO;
import uz.ccrew.utils.JwtUtil;
import uz.ccrew.exp.UnauthorizedException;
import uz.ccrew.service.AuthService;
import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.dto.user.UserCredentials;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final UserDAO userDAO;

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();

        userDAO.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
