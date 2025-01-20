package uz.ccrew.dao.impl;

import lombok.RequiredArgsConstructor;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Repository
@RequiredArgsConstructor
public class TrainerDAOImpl implements TrainerDAO {
    private final SessionFactory sessionFactory;
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
    private static final String FIND_TRAINERS_BY_USERNAMES = "FROM Trainer t WHERE t.user.username IN :usernames";

    @Override
    public Long create(Trainer trainer) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(trainer);
        log.info("Created Trainer:{} with ID:{}", trainer, trainer.getId());
        return trainer.getId();
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
