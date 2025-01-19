package uz.ccrew.dao.impl;

import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private final SessionFactory sessionFactory;
    private static final String FIND_ALL = "SELECT t from Trainer t";
    private static final String FIND_BY_USERNAME = "FROM Trainer tr where tr.user.username = :username";
    private static final String FIND_UNASSIGNED_TRAINERS = """
                SELECT tr
                FROM Trainer tr
                WHERE tr.id NOT IN (
                    SELECT trn.id
                    FROM Training t
                    JOIN t.trainer trn
                    JOIN t.trainee tn
                    WHERE tn.user.username = :traineeUsername
                ) AND tr.user.isActive = true
            """;
    private static final String DELETE_BY_ID = "DELETE FROM Trainer tr where tr.id = :id";
    private static final String FIND_TRAINERS_BY_USERNAMES = "FROM Trainer t WHERE t.user.username IN :usernames";

    @Autowired
    public TrainerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.debug("TrainerDAO instantiated");
    }

    @Override
    public Long create(Trainer trainer) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(trainer);
        log.info("Created Trainer:{} with ID:{}", trainer, trainer.getId());
        return trainer.getId();
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        return Optional.ofNullable(session.get(Trainer.class, id));
    }

    @Override
    public List<Trainer> findAll() {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(FIND_ALL, Trainer.class).getResultList();
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
            log.info("Deleted Trainer with ID:{}", id);
        } else {
            log.error("Trainer with ID:{} not found for delete", id);
            throw new EntityNotFoundException("Trainer with ID:" + id + " not found for delete");
        }
    }

    @Override
    public void update(Trainer trainer) {
        Session session = getSessionFactory().getCurrentSession();
        Trainer existingTrainer = session.get(Trainer.class, trainer.getId());
        if (existingTrainer == null) {
            log.error("Trainer with ID={} not found", trainer.getId());
            throw new EntityNotFoundException("Trainer with ID=" + trainer.getId() + " not found for update");
        }

        session.merge(trainer);
        log.info("Updated Trainer:{} with ID:{}", trainer, trainer.getId());
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        Trainer trainer = session.createQuery(FIND_BY_USERNAME, Trainer.class)
                .setParameter("username", username)
                .uniqueResult();
        return Optional.ofNullable(trainer);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        Session session = getSessionFactory().getCurrentSession();
        User user = session.get(User.class, id);

        if (user != null) {
            user.setPassword(newPassword);
            session.merge(user);
            log.info("Password updated for User with ID={}", id);
        } else {
            log.error("User with ID={} not found to change password", id);
            throw new EntityNotFoundException("User with ID=" + id + " not found to change password");
        }
    }

    @Override
    public void activateDeactivate(Long id, Boolean isActive) {
        Session session = getSessionFactory().getCurrentSession();
        User user = session.get(User.class, id);

        if (user != null) {
            user.setIsActive(isActive);
            session.merge(user);
            log.info("Updated isActive={} for User with ID={}", isActive, id);
        } else {
            log.error("User with ID={} not found to update isActive", id);
            throw new EntityNotFoundException("User with ID=" + id + " not found to activate and deactivate profile");
        }
    }

    @Override
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(FIND_UNASSIGNED_TRAINERS, Trainer.class)
                .setParameter("traineeUsername", traineeUsername)
                .list();
    }

    @Override
    public List<Trainer> findByTrainerUsername(List<String> usernames) {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(
                        FIND_TRAINERS_BY_USERNAMES, Trainer.class)
                .setParameter("usernames", usernames)
                .getResultList();
    }
}
