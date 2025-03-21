package uz.ccrew.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import uz.ccrew.service.AuthService;
import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.security.jwt.JwtUtil;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.security.user.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(UserCredentials userCredentials, HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        if (loginAttemptService.isBlocked(ip)) {
            log.error("Locked exception occurred for ip {}", ip);
            throw new LockedException("Too many failed login attempts. Try again in 5 minutes.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
            );
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            loginAttemptService.loginSucceeded(ip);

            // Генерируем токены
            String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            // Создаем новый объект Authentication, в котором в credentials сохраняется accessToken
            Authentication authWithToken = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(), // объект пользователя
                    accessToken,                   // сохраняем токен в credentials
                    authentication.getAuthorities()
            );
            // Устанавливаем новый Authentication в SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authWithToken);

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            log.error("Login failed for user {}: {}", userCredentials.getUsername(), e.getMessage());
            loginAttemptService.loginFailed(ip);
            throw e;
        }
    }
}
