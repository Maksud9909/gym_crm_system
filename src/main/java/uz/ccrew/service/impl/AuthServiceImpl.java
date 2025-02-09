package uz.ccrew.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.ccrew.service.AuthService;
import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.security.jwt.JwtUtil;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.security.user.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return JwtResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(userDetails.getUsername()))
                .refreshToken(jwtUtil.generateRefreshToken(userDetails.getUsername()))
                .build();
    }
}
