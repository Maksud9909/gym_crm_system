package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.dto.trainingType.TrainingTypeIdDTO;
import uz.ccrew.entity.TrainingType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Yoga");
    }

    @Test
    void findAll_ShouldReturnListOfTrainingTypes() {
        when(trainingTypeDAO.findAll()).thenReturn(List.of(trainingType));

        List<TrainingTypeIdDTO> result = trainingTypeService.findAll();

        assertEquals(1, result.size());
        assertEquals("Yoga", result.get(0).getTrainingTypeName());
        verify(trainingTypeDAO, times(1)).findAll();
    }
}
