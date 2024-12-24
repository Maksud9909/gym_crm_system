package uz.ccrew.dao;

import uz.ccrew.entity.*;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dao.base.AbstractAdvancedBaseDAO;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class TraineeDAO extends AbstractAdvancedBaseDAO<Trainee, TraineeCreateDTO> {
    private static final String ENTITY_NAME = "Trainee";

    private final UserDAO userDAO;

    @Autowired
    public TraineeDAO(SessionFactory sessionFactory, UserDAO userDAO) {
        super(sessionFactory, Trainee.class);
        this.userDAO = userDAO;
        log.debug("TraineeDAO instantiated");
    }

    @Override
    public Long create(TraineeCreateDTO dto) {
        Session session = getSessionFactory().getCurrentSession();
        String firstName = dto.firstName();
        String lastName = dto.lastName();


        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(generateUniqueUsername(firstName, lastName, getSessionFactory()))
                .password(generateRandomPassword())
                .isActive(Boolean.TRUE).build();

        Long userId = userDAO.create(user);
        user = userDAO.findById(userId).orElseThrow();


        Trainee trainee = Trainee.builder()
                .user(user)
                .dateOfBirth(dto.birthOfDate())
                .address(dto.address())
                .build();

        session.persist(trainee);

        log.info("Created {}: ID={}, Trainee={}", getEntityName(), trainee.getId(), dto);

        return trainee.getId();
    }

    @Override
    public void update(Long id, TraineeCreateDTO dto) {
        Session session = getSessionFactory().getCurrentSession();

        Trainee trainee = session.get(Trainee.class, id);
        if (trainee == null) {
            log.warn("Trainee with ID={} not found", id);
            throw new IllegalArgumentException("Trainee not found");
        }

        User user = trainee.getUser();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setUsername(generateUniqueUsername(dto.firstName(), dto.lastName(), getSessionFactory()));

        trainee.setDateOfBirth(dto.birthOfDate());
        trainee.setAddress(dto.address());

        session.merge(user);
        session.merge(trainee);

        log.info("Updated {}: ID={}, Trainee={}", getEntityName(), id, dto);
    }


    public void deleteByUsername(String username) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "FROM Trainee t WHERE t.username = :username";
            Trainee trainee = session.createQuery(hql, Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();

            if (trainee != null) {
                session.remove(trainee);
                log.info("Deleted Trainee profile with username={}", username);
            } else {
                log.warn("Trainee with username={} not found", username);
            }
        }
    }

    public List<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                              LocalDate toDate, String trainerName, TrainingType trainingType) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "SELECT t FROM Training t " +
                    "JOIN t.trainee trainee " +
                    "JOIN t.trainer trainer " +
                    "WHERE trainee.username = :traineeUsername " +
                    "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
                    "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
                    "AND (:trainerName IS NULL OR trainer.firstName || ' ' || trainer.lastName = :trainerName) " +
                    "AND (:trainingType IS NULL OR t.trainingType = :trainingType)";

            return session.createQuery(hql, Training.class)
                    .setParameter("traineeUsername", traineeUsername)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("trainerName", trainerName)
                    .setParameter("trainingType", trainingType)
                    .list();
        }
    }

    public void updateTraineeTrainers(Long traineeId, List<Long> newTrainerIds) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            Trainee trainee = session.get(Trainee.class, traineeId);
            if (trainee != null) {
                List<Trainer> newTrainers = session.createQuery(
                                "FROM Trainer t WHERE t.id IN :ids", Trainer.class)
                        .setParameter("ids", newTrainerIds)
                        .list();

                trainee.getTrainers().clear();
                trainee.getTrainers().addAll(newTrainers);

                session.merge(trainee);
                log.info("Updated trainers list for Trainee ID={}", traineeId);
            } else {
                log.warn("Trainee with ID={} not found", traineeId);
            }
        } catch (Exception e) {
            log.warn("Failed to update trainers list for Trainee ID={}", traineeId, e);
            throw e;
        }
    }


    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
