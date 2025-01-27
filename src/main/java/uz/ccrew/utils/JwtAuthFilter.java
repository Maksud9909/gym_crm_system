package uz.ccrew.utils;

import jakarta.servlet.FilterChain;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private static final String[] ALLOWED_APIS = {
            "/api/v1/user/login",
            "/api/v1/trainee/create",
            "/api/v1/trainer/create"
    };

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();

        for (String allowedPath : ALLOWED_APIS) {
            if (path.equals(allowedPath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, "Authentication failed");
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractAccessTokenUsername(token);
            request.setAttribute("username", username);
        } catch (Exception e) {
            sendError(response, "Authentication failed");
        }

        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"errors\": [\"" + message + "\"]}");
    }
}
