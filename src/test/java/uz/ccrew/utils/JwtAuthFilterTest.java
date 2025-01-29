package uz.ccrew.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthFilter = new JwtAuthFilter(jwtUtil);
    }

    @Test
    void shouldAllowRequestForAllowedAPI() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/api/v1/user/login");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldReturnUnauthorizedIfNoAuthHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/api/v1/trainer/update");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Authentication failed"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldReturnUnauthorizedIfTokenIsInvalid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/api/v1/trainer/update");
        request.addHeader("Authorization", "Bearer invalidToken");

        doThrow(new RuntimeException("Invalid token")).when(jwtUtil).extractAccessTokenUsername("invalidToken");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Authentication failed"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldSetUsernameAndProceedIfTokenIsValid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/api/v1/some-protected-api");
        request.addHeader("Authorization", "Bearer validToken");

        when(jwtUtil.extractAccessTokenUsername("validToken")).thenReturn("testUser");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals("testUser", request.getAttribute("username"));
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
