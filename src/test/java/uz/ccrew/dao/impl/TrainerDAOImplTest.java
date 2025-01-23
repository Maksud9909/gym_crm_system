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
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TrainerDAOImpl trainerDAO;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        trainer = Trainer.builder()
                .id(1L)
                .user(User.builder().username("trainer_user").build())
                .build();
    }

    @Test
    void create_ShouldPersistTrainerAndReturnId() {
        Long resultId = trainerDAO.create(trainer);
        verify(session, times(1)).persist(trainer);
        assertEquals(1L, resultId);
    }

    @Test
    void update_ShouldUpdateTrainer_WhenExists() {
        Trainer updatedTrainer = Trainer.builder()
                .id(1L)
                .user(User.builder().username("updated_user").build())
                .build();

        when(session.get(Trainer.class, 1L)).thenReturn(trainer);

        trainerDAO.update(updatedTrainer);

        verify(session, times(1)).merge(updatedTrainer);
    }

    @Test
    void update_ShouldThrowException_WhenTrainerDoesNotExist() {
        Trainer nonExistentTrainer = Trainer.builder().id(999L).build();

        when(session.get(Trainer.class, 999L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> trainerDAO.update(nonExistentTrainer));

        verify(session, never()).merge(any(Trainer.class));
    }

    @Test
    void findByUsername_ShouldReturnOptionalTrainer_WhenExists() {
        String username = "trainer_user";
        Query<Trainer> query = mock(Query.class);

        when(session.createQuery("FROM Trainer tr where tr.user.username = :username", Trainer.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainer);

        Optional<Trainer> result = trainerDAO.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
    }

    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenNotFound() {
        String username = "non_existent_user";
        Query<Trainer> query = mock(Query.class);

        when(session.createQuery("FROM Trainer tr where tr.user.username = :username", Trainer.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        Optional<Trainer> result = trainerDAO.findByUsername(username);

        assertFalse(result.isPresent());
    }

    @Test
    void getUnassignedTrainers_ShouldReturnListOfUnassignedTrainers() {
        String traineeUsername = "trainee_user";
        List<Trainer> unassignedTrainers = List.of(trainer);
        Query<Trainer> query = mock(Query.class);

        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter("traineeUsername", traineeUsername)).thenReturn(query);
        when(query.list()).thenReturn(unassignedTrainers);

        List<Trainer> result = trainerDAO.getUnassignedTrainers(traineeUsername);

        assertEquals(unassignedTrainers.size(), result.size());
        assertEquals(unassignedTrainers.get(0), result.get(0));
    }

    @Test
    void findByTrainerUsername_ShouldReturnListOfTrainers_WhenUsernamesExist() {
        List<String> usernames = List.of("trainer1", "trainer2");
        List<Trainer> trainers = List.of(trainer);
        Query<Trainer> query = mock(Query.class);

        when(session.createQuery("FROM Trainer t WHERE t.user.username IN :usernames", Trainer.class))
                .thenReturn(query);
        when(query.setParameter("usernames", usernames)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainers);

        List<Trainer> result = trainerDAO.findByTrainerUsername(usernames);

        assertEquals(trainers.size(), result.size());
        assertEquals(trainers.get(0), result.get(0));
    }
}
