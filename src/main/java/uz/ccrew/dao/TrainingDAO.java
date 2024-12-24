package uz.ccrew.dao;

import uz.ccrew.entity.Training;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Transactional
public class TrainingDAO extends AbstractBaseDAO<Training> {
    private static final String ENTITY_NAME = "Training";

    public TrainingDAO(SessionFactory sessionFactory, Class<Training> entityClass) {
        super(sessionFactory, entityClass);
        log.debug("TrainingDAO instantiated");
    }


    @Override
    public Long create(Training training) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(training);
        Long id = training.getId();
        log.info("Created {}: ID={}, Training={}", getEntityName(), id, training);
        return id;
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
