package uz.ccrew.dao;

import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainee;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dao.base.advancedBase.AbstractAdvancedUserBaseCRUDDAO;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static uz.ccrew.utils.UserUtils.*;

@Slf4j
@Repository
public class TraineeDAO extends AbstractAdvancedUserBaseCRUDDAO<Trainee, TraineeCreateDTO> {
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
                .username(generateUniqueUsername(firstName, lastName))
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
        user.setUsername(generateUniqueUsername(dto.firstName(), dto.lastName()));

        trainee.setDateOfBirth(dto.birthOfDate());
        trainee.setAddress(dto.address());

        session.merge(user);
        session.merge(trainee);

        log.info("Updated {}: ID={}, Trainee={}", getEntityName(), id, dto);
    }

    @Transactional
    public void deleteByUsername(String username) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "FROM Trainee t JOIN FETCH t.user u WHERE u.username = :username";
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

    @Transactional
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
