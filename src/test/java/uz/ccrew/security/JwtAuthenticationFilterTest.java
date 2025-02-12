package uz.ccrew.security;

import uz.ccrew.entity.User;
import uz.ccrew.security.jwt.JwtUtil;
import uz.ccrew.security.user.UserDetailsImpl;
import uz.ccrew.service.impl.TokenBlacklistService;
import uz.ccrew.security.user.UserDetailsServiceImpl;
import uz.ccrew.security.jwt.JwtAuthenticationFilter;

import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import jakarta.servlet.FilterChain;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService, tokenBlacklistService);
    }

    @Test
    void shouldAuthenticateUser_WhenValidTokenProvided() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(tokenBlacklistService.isBlacklisted("validToken")).thenReturn(false);
        when(jwtUtil.isTokenExpired("validToken")).thenReturn(false);
        when(jwtUtil.extractUsernameFromAccessToken("validToken")).thenReturn("testUser");

        UserDetailsImpl userDetails = new UserDetailsImpl(User.builder().username("testUser").build());
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("testUser", ((UserDetailsImpl) authentication.getPrincipal()).getUsername());

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
