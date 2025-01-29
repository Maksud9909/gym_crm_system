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
import uz.ccrew.entity.TrainingType;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDAOImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @InjectMocks
    private TrainingTypeDAOImpl trainingTypeDAO;

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Test Type");
        trainingType.setId(1L);
    }

    @Test
    void findAll_ShouldReturnListOfTrainingTypes() {
        Query<TrainingType> query = mock(Query.class);
        when(session.createQuery("FROM training_types", TrainingType.class)).thenReturn(query);
        when(query.list()).thenReturn(List.of(trainingType));

        List<TrainingType> result = trainingTypeDAO.findAll();

        assertEquals(1, result.size());
        assertEquals(trainingType, result.get(0));
    }

    @Test
    void findByName_ShouldReturnOptionalTrainingType_WhenExists() {
        String name = "Test Type";
        Query<TrainingType> query = mock(Query.class);
        when(session.createQuery("FROM training_types WHERE trainingTypeName = :name", TrainingType.class)).thenReturn(query);
        when(query.setParameter("name", name)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainingType);

        Optional<TrainingType> result = trainingTypeDAO.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(trainingType, result.get());
    }

    @Test
    void findByName_ShouldReturnEmptyOptional_WhenNotFound() {
        String name = "Nonexistent Type";
        Query<TrainingType> query = mock(Query.class);
        when(session.createQuery("FROM training_types WHERE trainingTypeName = :name", TrainingType.class)).thenReturn(query);
        when(query.setParameter("name", name)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        Optional<TrainingType> result = trainingTypeDAO.findByName(name);

        assertFalse(result.isPresent());
    }
}
