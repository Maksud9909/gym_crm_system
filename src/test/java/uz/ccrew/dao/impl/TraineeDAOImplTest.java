package uz.ccrew.dao.impl;

import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainee;
import uz.ccrew.exp.exp.EntityNotFoundException;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.hibernate.query.MutationQuery;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TraineeDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TraineeDAOImpl traineeDAO;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        trainee = Trainee.builder()
                .id(1L)
                .address("Some address")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .user(new User())
                .build();
    }

    @Test
    void create_ShouldPersistTraineeAndReturnId() {
        Long resultId = traineeDAO.create(trainee);
        verify(session, times(1)).persist(trainee);
        assertEquals(1L, resultId);
    }

    @Test
    void delete_ShouldDeleteTrainee_WhenTraineeExists() {
        traineeDAO.delete(trainee);
        verify(session, times(1)).remove(trainee);
    }

    @Test
    void delete_ShouldThrowException_WhenTraineeNotFound() {
        doThrow(new EntityNotFoundException("Trainee not found")).when(session).remove(any(Trainee.class));

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            traineeDAO.delete(trainee);
        });

        assertEquals("Trainee not found", thrown.getMessage());
    }

    @Test
    void update_ShouldUpdateTrainee_WhenExists() {
        Trainee updateCandidate = Trainee.builder()
                .id(1L)
                .address("New address")
                .build();
        when(session.get(Trainee.class, 1L)).thenReturn(trainee);
        traineeDAO.update(updateCandidate);
        verify(session, times(1)).merge(updateCandidate);
    }

    @Test
    void update_ShouldThrowException_WhenTraineeDoesNotExist() {
        Trainee trainee = Trainee.builder()
                .id(999L)
                .address("Some address")
                .build();
        when(session.get(Trainee.class, 999L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> traineeDAO.update(trainee));
        verify(session, never()).merge(any(Trainee.class));
    }

    @Test
    void findByUsername_ShouldReturnOptionalTrainee_WhenExists() {
        String username = "test_user";
        Trainee trainee = Trainee.builder().user(new User()).build();
        Query<Trainee> query = mock(Query.class);
        when(session.createQuery("FROM Trainee t where t.user.username = :username", Trainee.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);
        Optional<Trainee> result = traineeDAO.findByUsername(username);
        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenNotFound() {
        String username = "non_existent_user";
        Query<Trainee> query = mock(Query.class);
        when(session.createQuery("FROM Trainee t where t.user.username = :username", Trainee.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);
        Optional<Trainee> result = traineeDAO.findByUsername(username);
        assertFalse(result.isPresent());
    }
}
