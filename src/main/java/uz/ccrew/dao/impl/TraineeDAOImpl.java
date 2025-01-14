package uz.ccrew.dao.impl;

import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TraineeDAO;
import uz.ccrew.entity.Training;
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
public class TraineeDAOImpl implements TraineeDAO {
    private static final String ACTIVATE_DEACTIVATE_BY_ID = "UPDATE User u SET u.isActive = :isActive WHERE u.id = :id";
    private static final String CHANGE_PASSWORD = "UPDATE User u SET u.password =:password WHERE u.id = :id";
    private final SessionFactory sessionFactory;
    private static final String NOT_FOUND_LOG = "Trainee with ID={} not found";
    private static final String FIND_ALL = "SELECT t FROM Trainee t";
    private static final String FIND_BY_USERNAME = "FROM Trainee t where t.user.username = :username";
    private static final String FIND_TRAINERS_BY_IDS = "FROM Trainer t WHERE t.id IN :ids";
    public static final String DELETE_BY_ID = "DELETE FROM Trainee t WHERE t.id = :id";

    @Autowired
    public TraineeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        log.debug("TraineeDAO instantiated");
    }

    @Override
    public Long create(Trainee trainee) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(trainee);
        log.info("Created Trainer:{} with ID:{}", trainee, trainee.getId());
        return trainee.getId();
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        return Optional.ofNullable(session.get(Trainee.class, id));
    }

    @Override
    public List<Trainee> findAll() {
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(FIND_ALL, Trainee.class).getResultList();
    }

    @Override
    public void delete(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        int deletedCount = session.createMutationQuery(DELETE_BY_ID)
                .setParameter("id", id)
                .executeUpdate();

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
        Trainee trainee = session.createQuery(FIND_BY_USERNAME, Trainee.class)
                .setParameter("username", username)
                .uniqueResult();
        return Optional.ofNullable(trainee);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        Session session = getSessionFactory().getCurrentSession();
        int updatedCont = session.createMutationQuery(CHANGE_PASSWORD)
                .setParameter("password", newPassword)
                .setParameter("id", id)
                .executeUpdate();
        if (updatedCont > 0) {
            log.info("Password updated for User with ID={}", id);
        } else {
            log.error("User with ID={} not found to change password", id);
            throw new EntityNotFoundException("User with ID=" + id + " not found to change password");
        }
    }

    @Override
    public void activateDeactivate(Long id, Boolean isActive) {
        Session session = getSessionFactory().getCurrentSession();
        int updatedCount = session.createMutationQuery(
                        ACTIVATE_DEACTIVATE_BY_ID)
                .setParameter("isActive", isActive)
                .setParameter("id", id)
                .executeUpdate();
        if (updatedCount > 0) {
            log.info("Updated isActive={} for User with ID={}", isActive, id);
        } else {
            log.error("User with ID={} not found to update isActive", id);
            throw new EntityNotFoundException("User with ID=" + id + " not found to activate and deactivate profile");
        }
    }

    @Override
    public void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds) {
        log.info("Updating trainers list for Trainee with ID={}", traineeId);
        Session session = getSessionFactory().getCurrentSession();

        Trainee trainee = session.get(Trainee.class, traineeId);
        if (trainee == null) {
            log.error(NOT_FOUND_LOG, traineeId);
            throw new EntityNotFoundException("Trainee with ID=" + traineeId + " not found for updating trainers list");
        }

        List<Trainer> newTrainers = session.createQuery(
                        FIND_TRAINERS_BY_IDS, Trainer.class)
                .setParameter("ids", newTrainerIds)
                .getResultList();

        if (newTrainers.isEmpty()) {
            log.warn("No valid trainers found for IDs: {}", newTrainerIds);
            throw new IllegalArgumentException("No valid trainers found for the provided IDs");
        }

        List<Training> existingTrainings = trainee.getTraining();
        for (int i = 0; i < existingTrainings.size() && i < newTrainers.size(); i++) {
            Trainer newTrainer = newTrainers.get(i);
            existingTrainings.get(i).setTrainer(newTrainer);
            log.info("Updated Training ID={} with new Trainer ID={}", existingTrainings.get(i).getId(), newTrainer.getId());
        }

        session.merge(trainee);
        log.info("Updated trainers list for Trainee with ID={}", traineeId);
    }
}
