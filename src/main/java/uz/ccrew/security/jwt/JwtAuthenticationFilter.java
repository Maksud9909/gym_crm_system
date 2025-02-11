package uz.ccrew.security.jwt;

import uz.ccrew.exp.exp.unauthorized.BlacklistedTokenException;
import uz.ccrew.security.user.UserDetailsImpl;
import uz.ccrew.security.user.UserDetailsServiceImpl;
import uz.ccrew.exp.exp.unauthorized.TokenExpiredException;

import jakarta.servlet.FilterChain;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import uz.ccrew.service.impl.TokenBlacklistService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Qualifier("handlerExceptionResolver")
    @Autowired
    private HandlerExceptionResolver exceptionResolver;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = bearerToken.substring(7);

            if (tokenBlacklistService.isBlacklisted(token)) {
                exceptionResolver.resolveException(request, response, null, new BlacklistedTokenException("Token is black listed"));
                return;
            }

            if (jwtUtil.isTokenExpired(token)) {
                exceptionResolver.resolveException(request, response, null, new TokenExpiredException(jwtUtil.getTokenExpiredMessage(token)));
                return;
            }

            String username = jwtUtil.extractUsernameFromAccessToken(token);
            if (username == null || SecurityContextHolder.getContext() == null) {
                filterChain.doFilter(request, response);
                return;
            }

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            System.out.println("Before setting authentication: " + SecurityContextHolder.getContext().getAuthentication());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("After setting authentication: " + SecurityContextHolder.getContext().getAuthentication());


            filterChain.doFilter(request, response);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, new BadCredentialsException("Bad credentials"));
        }
    }
}
