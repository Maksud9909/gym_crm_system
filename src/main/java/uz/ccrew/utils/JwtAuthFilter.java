package uz.ccrew.utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {
    private final JwtUtil jwtUtil;
    private static final String[] ALLOWED_APIS = {
            "/api/v1/auth/login",
            "/api/v1/trainee/create",
            "/api/v1/trainer/create"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        for (String allowedPath : ALLOWED_APIS) {
            if (path.equals(allowedPath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(httpResponse, "Access token is missing or invalid");
            return;
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractAccessTokenUsername(token);
            request.setAttribute("username", username);
        } catch (Exception e) {
            sendError(httpResponse, "Access token is invalid: " + e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"errors\": [\"" + message + "\"]}");
    }
}
