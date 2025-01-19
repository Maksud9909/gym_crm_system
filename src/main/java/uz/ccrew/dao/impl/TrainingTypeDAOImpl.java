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
    public static final String FIND_BY_NAME = "FROM training_types WHERE trainingTypeName = :name";

    @Autowired
    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.debug("TrainingTypeDAO instantiated");
    }

    @Override
    public Optional<TrainingType> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        TrainingType trainingType = session.createQuery(FIND_BY_NAME, TrainingType.class)
                .setParameter("name", name)
                .uniqueResult();
        if (trainingType != null) {
            return Optional.of(trainingType);
        } else {
            log.warn("No TrainingType found with name={}", name);
            return Optional.empty();
        }
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
