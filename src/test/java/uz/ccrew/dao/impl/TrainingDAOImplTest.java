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
import org.mockito.MockedStatic;
import uz.ccrew.entity.Training;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.utils.QueryBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TrainingDAOImpl trainingDAO;

    private Training training;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        training = Training.builder()
                .id(1L)
                .trainingName("Test Training")
                .trainingDate(LocalDate.now())
                .trainingDuration(2.0)
                .trainee(Trainee.builder().id(1L).build())
                .trainer(Trainer.builder().id(1L).build())
                .build();
    }

    @Test
    void create_ShouldPersistTrainingAndReturnId() {
        Long resultId = trainingDAO.create(training);
        verify(session, times(1)).persist(training);
        assertEquals(1L, resultId);
    }

    @Test
    void getTraineeTrainings_ShouldReturnListOfTrainings() {
        String username = "trainee_user";
        LocalDate fromDate = LocalDate.now().minusDays(5);
        LocalDate toDate = LocalDate.now();
        String trainerName = "trainer_name";
        String trainingTypeName = "type_name";

        Query<Training> query = mock(Query.class);
        try (MockedStatic<QueryBuilder> mockedStatic = mockStatic(QueryBuilder.class)) {
            mockedStatic.when(() -> QueryBuilder.buildAndSetTraineeTrainingsQuery(session, username, fromDate, toDate, trainerName, trainingTypeName))
                    .thenReturn(query);

            when(query.getResultList()).thenReturn(List.of(training));

            List<Training> result = trainingDAO.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingTypeName);

            assertEquals(1, result.size());
            assertEquals(training, result.get(0));
        }
    }

    @Test
    void getTrainerTrainings_ShouldReturnListOfTrainings() {
        String username = "trainer_user";
        LocalDate fromDate = LocalDate.now().minusDays(5);
        LocalDate toDate = LocalDate.now();
        String traineeName = "trainee_name";

        Query<Training> query = mock(Query.class);
        try (MockedStatic<QueryBuilder> mockedStatic = mockStatic(QueryBuilder.class)) {
            mockedStatic.when(() -> QueryBuilder.buildAndSetTrainerTrainingsQuery(session, username, fromDate, toDate, traineeName))
                    .thenReturn(query);

            when(query.getResultList()).thenReturn(List.of(training));

            List<Training> result = trainingDAO.getTrainerTrainings(username, fromDate, toDate, traineeName);

            assertEquals(1, result.size());
            assertEquals(training, result.get(0));
        }
    }

    @Test
    void findById_ShouldReturnTraining_WhenTrainingExists() {
        when(session.get(Training.class, 1L)).thenReturn(training);

        Optional<Training> result = trainingDAO.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Test Training", result.get().getTrainingName());
        verify(session, times(1)).get(Training.class, 1L);
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenTrainingDoesNotExist() {
        when(session.get(Training.class, 1L)).thenReturn(null);

        Optional<Training> result = trainingDAO.findById(1L);

        assertFalse(result.isPresent());
        verify(session, times(1)).get(Training.class, 1L);
    }

    @Test
    void update_ShouldMergeTraining() {
        trainingDAO.update(training);

        verify(session, times(1)).merge(training);
    }
}
