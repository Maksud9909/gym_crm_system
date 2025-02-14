package uz.ccrew.security.config;

import uz.ccrew.service.impl.LoginAttemptService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final LoginAttemptService loginAttemptService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (loginAttemptService.isBlocked(username)) {
            log.error("Account is locked for user {} Try again in 5 minutes", username);
            throw new LockedException(String.format("Account is locked for user %s Try again in 5 minutes.", username));
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                loginAttemptService.loginSucceeded(username);
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            }

            loginAttemptService.loginFailed(username);
            throw new BadCredentialsException("Invalid credentials");

        } catch (Exception e) {
            log.error("Error while attempting to authenticate user {}", username, e);
            loginAttemptService.loginFailed(username);
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
