package uz.ccrew.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.entity.User;
import uz.ccrew.exp.exp.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private UserDAOImpl userDAO;

    private User user;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        user = User.builder()
                .id(1L)
                .username("test_user")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .isActive(true)
                .build();
    }

    @Test
    void isUsernameExists_ShouldReturnTrue_WhenUsernameExists() {
        Query<Long> query = mock(Query.class);
        when(session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)).thenReturn(query);
        when(query.setParameter("username", "test_user")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result = userDAO.isUsernameExists("test_user");

        assertTrue(result);
    }

    @Test
    void isUsernameExists_ShouldReturnFalse_WhenUsernameDoesNotExist() {
        Query<Long> query = mock(Query.class);
        when(session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)).thenReturn(query);
        when(query.setParameter("username", "nonexistent_user")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(0L);

        boolean result = userDAO.isUsernameExists("nonexistent_user");

        assertFalse(result);
    }

    @Test
    void findByUsernameAndPassword_ShouldReturnOptionalUser_WhenExists() {
        Query<User> query = mock(Query.class);
        when(session.createQuery("FROM User u WHERE u.username = :username AND u.password = :password", User.class)).thenReturn(query);
        when(query.setParameter("username", "test_user")).thenReturn(query);
        when(query.setParameter("password", "password123")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        Optional<User> result = userDAO.findByUsernameAndPassword("test_user", "password123");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByUsernameAndPassword_ShouldReturnEmptyOptional_WhenNotFound() {
        Query<User> query = mock(Query.class);
        when(session.createQuery("FROM User u WHERE u.username = :username AND u.password = :password", User.class)).thenReturn(query);
        when(query.setParameter("username", "nonexistent_user")).thenReturn(query);
        when(query.setParameter("password", "wrongpassword")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        Optional<User> result = userDAO.findByUsernameAndPassword("nonexistent_user", "wrongpassword");

        assertFalse(result.isPresent());
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenUserExists() {
        when(session.get(User.class, 1L)).thenReturn(user);

        userDAO.changePassword(1L, "newpassword123");

        verify(session, times(1)).merge(user);
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    void changePassword_ShouldThrowException_WhenUserNotFound() {
        when(session.get(User.class, 1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userDAO.changePassword(1L, "newpassword123"));

        verify(session, never()).merge(any(User.class));
    }

    @Test
    void findByUsername_ShouldReturnOptionalUser_WhenExists() {
        Query<User> query = mock(Query.class);
        when(session.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", "test_user")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        Optional<User> result = userDAO.findByUsername("test_user");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenNotFound() {
        Query<User> query = mock(Query.class);
        when(session.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", "nonexistent_user")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        Optional<User> result = userDAO.findByUsername("nonexistent_user");

        assertFalse(result.isPresent());
    }

    @Test
    void activateDeactivate_ShouldUpdateIsActive_WhenUserExists() {
        when(session.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(mock(Query.class));
        Query<User> query = session.createQuery("FROM User u WHERE u.username = :username", User.class);
        when(query.setParameter("username", "test_user")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        userDAO.activateDeactivate("test_user", false);

        verify(session, times(1)).merge(user);
        assertFalse(user.getIsActive());
    }

    @Test
    void activateDeactivate_ShouldThrowException_WhenUserNotFound() {
        Query<User> query = mock(Query.class);
        when(session.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", "nonexistent_user")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userDAO.activateDeactivate("nonexistent_user", false));

        verify(session, never()).merge(any(User.class));
    }
}
