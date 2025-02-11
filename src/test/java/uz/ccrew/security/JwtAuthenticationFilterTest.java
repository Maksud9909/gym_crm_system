package uz.ccrew.security;

import uz.ccrew.entity.User;
import uz.ccrew.security.jwt.JwtUtil;
import uz.ccrew.security.user.UserDetailsImpl;
import uz.ccrew.service.impl.TokenBlacklistService;
import uz.ccrew.security.jwt.JwtAuthenticationFilter;
import uz.ccrew.security.user.UserDetailsServiceImpl;

import org.mockito.InjectMocks;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest {

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldAuthenticateUser_WhenValidTokenProvided() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer: validToken");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(tokenBlacklistService.isBlacklisted("validToken")).thenReturn(false);
        when(jwtUtil.isTokenExpired("validToken")).thenReturn(false);
        when(jwtUtil.extractUsernameFromAccessToken("validToken")).thenReturn("testUser");

        UserDetailsImpl userDetails = new UserDetailsImpl(User.builder().username("testUser").build());
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        System.out.println("Authentication after filter: " + SecurityContextHolder.getContext().getAuthentication());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Extracted username: " + jwtUtil.extractUsernameFromAccessToken("validToken"));
        assertNotNull(authentication);
        assertEquals("testUser", ((UserDetailsImpl) authentication.getPrincipal()).getUsername());

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
