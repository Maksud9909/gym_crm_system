package uz.ccrew.dao.impl;

import uz.ccrew.dao.TrainingTypeDAO;
import uz.ccrew.entity.TrainingType;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Getter
@Slf4j
@Repository
public class TrainingTypeDAOImpl implements TrainingTypeDAO {
    private final SessionFactory sessionFactory;
    public static final String FIND_ALL = "FROM training_types";

    @Autowired
    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.debug("TrainingTypeDAO instantiated");
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        TrainingType trainingType = session.get(TrainingType.class, id);
        if (trainingType != null) {
            log.info("Found TrainingType {} with ID={}", trainingType.getTrainingTypeName(), id);
            return Optional.of(trainingType);
        } else {
            log.warn("No TrainingType found with ID={}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<TrainingType> findAll() {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(FIND_ALL, TrainingType.class).list();
    }
}
