package uz.ccrew.dao.impl;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Getter
@Repository
@RequiredArgsConstructor
public class TraineeDAOImpl implements TraineeDAO {
    private final SessionFactory sessionFactory;
    private static final String NOT_FOUND_LOG = "Trainee with {} not found";
    private static final String DELETE_BY_ID = "DELETE FROM Trainee t WHERE t.id = :id";
    private static final String FIND_TRAINEE_BY_USERNAME = "FROM Trainee t where t.user.username = :username";
    private static final String FIND_TRAINER_BY_USERNAME = "FROM Trainer t WHERE t.user.username = :usernames";
    public static final String FIND_TRAINING_BY_ID = "FROM Training t WHERE t.id = :id";

    @Override
    public Long create(Trainee trainee) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(trainee);
        log.info("Created Trainer:{} with ID:{}", trainee, trainee.getId());
        return trainee.getId();
    }

    @Override
    public void delete(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        int deletedCount = session.createMutationQuery(DELETE_BY_ID)
                .setParameter("id", id)
                .executeUpdate();
        session.flush();
        session.clear();

        if (deletedCount > 0) {
            log.info("Deleted Trainee with ID:{}", id);
        } else {
            log.error("Trainee with ID:{} not found for delete", id);
            throw new EntityNotFoundException("Trainee with ID:" + id + " not found for delete");
        }
    }

    @Override
    public void update(Trainee trainee) {
        Session session = getSessionFactory().getCurrentSession();
        Trainee existingTrainer = session.get(Trainee.class, trainee.getId());
        if (existingTrainer == null) {
            log.error(NOT_FOUND_LOG, trainee.getId());
            throw new EntityNotFoundException("Trainee with ID=" + trainee.getId() + " not found for update");
        }

        session.merge(trainee);
        log.info("Updated Trainee:{} with ID:{}", trainee, trainee.getId());
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        Trainee trainee = session.createQuery(FIND_TRAINEE_BY_USERNAME, Trainee.class)
                .setParameter("username", username)
                .uniqueResult();
        return Optional.ofNullable(trainee);
    }
}
