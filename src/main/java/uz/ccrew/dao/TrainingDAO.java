package uz.ccrew.dao;

import uz.ccrew.entity.Training;
import uz.ccrew.dao.base.AbstractBaseDAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
@Repository
public class TrainingDAO extends AbstractBaseDAO<Training> {
    private static final String ENTITY_NAME = "Training";

    public TrainingDAO() {
        log.info("TrainingDAO initialized");
    }

    @Autowired
    public void setTrainingStorage(Map<Long, Training> trainingStorage) {
        setStorage(trainingStorage);
        log.info("Training storage injected into TrainingDAO");
    }

    @Override
    public Long create(Training training) {
        Long id = getNextId();
        training.setId(id);
        getStorage().put(id, training);
        log.info("Created Training: ID={}, Training={}", id, training);
        return id;
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
