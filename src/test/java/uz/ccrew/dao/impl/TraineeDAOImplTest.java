package uz.ccrew.dao.impl;

import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.exp.EntityNotFoundException;

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
        MutationQuery mutationQuery = mock(MutationQuery.class);
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(eq("id"), anyLong())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(1);
        traineeDAO.delete(1L);
        verify(session, times(1)).flush();
        verify(session, times(1)).clear();
    }

    @Test
    void delete_ShouldThrowException_WhenTraineeNotFound() {
        MutationQuery mutationQuery = mock(MutationQuery.class);
        when(session.createMutationQuery(anyString())).thenReturn(mutationQuery);
        when(mutationQuery.setParameter(eq("id"), anyLong())).thenReturn(mutationQuery);
        when(mutationQuery.executeUpdate()).thenReturn(0);
        assertThrows(EntityNotFoundException.class, () -> traineeDAO.delete(1L));
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

    @Test
    void updateTraineeTrainers_ShouldUpdateTrainers() {
        String traineeUsername = "trainee_user";
        Training training1 = new Training();
        training1.setId(101L);
        Training training2 = new Training();
        training2.setId(102L);
        Trainee trainee = Trainee.builder()
                .id(1L)
                .training(new ArrayList<>(List.of(training1, training2)))
                .build();
        Query<Trainee> traineeQuery = mock(Query.class);
        when(session.createQuery("FROM Trainee t where t.user.username = :username", Trainee.class))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", traineeUsername)).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(trainee);
        List<String> newTrainerUsernames = List.of("trainer1", "trainer2");
        Trainer trainer1 = new Trainer();
        trainer1.setId(201L);
        Trainer trainer2 = new Trainer();
        trainer2.setId(202L);
        List<Trainer> newTrainers = List.of(trainer1, trainer2);
        Query<Trainer> trainerQuery = mock(Query.class);
        when(session.createQuery("FROM Trainer t WHERE t.user.username IN :usernames", Trainer.class))
                .thenReturn(trainerQuery);
        when(trainerQuery.setParameter("usernames", newTrainerUsernames)).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(newTrainers);
        traineeDAO.updateTraineeTrainers(traineeUsername, newTrainerUsernames);
        assertEquals(201L, training1.getTrainer().getId());
        assertEquals(202L, training2.getTrainer().getId());
        verify(session, times(1)).merge(trainee);
    }

    @Test
    void updateTraineeTrainers_ShouldThrowException_WhenTraineeNotFound() {
        String traineeUsername = "non_existent_trainee";
        Query<Trainee> traineeQuery = mock(Query.class);
        when(session.createQuery("FROM Trainee t where t.user.username = :username", Trainee.class))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", traineeUsername)).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(null);
        assertThrows(EntityNotFoundException.class,
                () -> traineeDAO.updateTraineeTrainers(traineeUsername, List.of("trainer1")));
    }

    @Test
    void updateTraineeTrainers_ShouldThrowException_WhenNoTrainersFound() {
        String traineeUsername = "trainee_user";
        Trainee trainee = Trainee.builder()
                .id(1L)
                .training(new ArrayList<>())
                .build();
        Query<Trainee> traineeQuery = mock(Query.class);
        when(session.createQuery("FROM Trainee t where t.user.username = :username", Trainee.class))
                .thenReturn(traineeQuery);
        when(traineeQuery.setParameter("username", traineeUsername)).thenReturn(traineeQuery);
        when(traineeQuery.uniqueResult()).thenReturn(trainee);
        Query<Trainer> trainerQuery = mock(Query.class);
        when(session.createQuery("FROM Trainer t WHERE t.user.username IN :usernames", Trainer.class))
                .thenReturn(trainerQuery);
        when(trainerQuery.setParameter("usernames", List.of("non_existent_trainer"))).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class,
                () -> traineeDAO.updateTraineeTrainers(traineeUsername, List.of("non_existent_trainer")));
    }
}
