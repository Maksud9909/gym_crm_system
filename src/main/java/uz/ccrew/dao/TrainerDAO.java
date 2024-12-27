package uz.ccrew.dao;

import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dao.base.advancedBase.AbstractAdvancedUserBaseCRUDDAO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static uz.ccrew.utils.UserUtils.*;

@Slf4j
@Repository
@Transactional
public class TrainerDAO extends AbstractAdvancedUserBaseCRUDDAO<Trainer, TrainerCreateDTO> {
    private static final String ENTITY_NAME = "Trainer";
    private final UserDAO userDAO;

    @Autowired
    public TrainerDAO(SessionFactory sessionFactory, UserDAO userDAO) {
        super(sessionFactory, Trainer.class);
        this.userDAO = userDAO;
        log.debug("TrainerDAO instantiated");
    }

    @Override
    public Long create(TrainerCreateDTO dto) {
        Session session = getSessionFactory().getCurrentSession();

        String firstName = dto.firstName();
        String lastName = dto.lastName();
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(generateUniqueUsername(firstName, lastName, getSessionFactory()))
                .password(generateRandomPassword())
                .isActive(Boolean.TRUE)
                .build();

        Long userId = userDAO.create(user);
        user = userDAO.findById(userId).orElseThrow();

        Trainer trainer = Trainer.builder()
                .user(user)
                .trainingType(dto.trainingType())
                .build();

        session.persist(trainer);

        log.info("Created {}: ID={}, Trainer={}", getEntityName(), trainer.getId(), dto);

        return trainer.getId();
    }

    @Override
    public void update(Long id, TrainerCreateDTO dto) {
        Session session = getSessionFactory().getCurrentSession();

        Trainer trainer = session.get(Trainer.class, id);
        if (trainer == null) {
            log.warn("Trainer with ID={} not found", id);
            throw new IllegalArgumentException("Trainer not found");
        }

        User user = trainer.getUser();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setUsername(generateUniqueUsername(dto.firstName(), dto.lastName(), getSessionFactory()));

        trainer.setTrainingType(dto.trainingType());

        session.merge(user);
        session.merge(trainer);

        log.info("Updated {}: ID={}, Trainer={}", getEntityName(), id, dto);
    }


    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = """
                        SELECT t FROM Training t
                        JOIN FETCH t.trainer trainer
                        JOIN FETCH t.trainee trainee
                        WHERE trainer.user.username = :trainerUsername
                        AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
                        AND (:toDate IS NULL OR t.trainingDate <= :toDate)
                        AND (:traineeName IS NULL OR CONCAT(trainee.user.firstName, ' ', trainee.user.lastName) = :traineeName)
                    """;

            return session.createQuery(hql, Training.class)
                    .setParameter("trainerUsername", trainerUsername)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("traineeName", traineeName)
                    .list();
        }
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = """
                        SELECT tr FROM Trainer tr
                        WHERE tr.id NOT IN (
                            SELECT trainer.id FROM Trainee trainee
                            JOIN trainee.trainers trainer
                            WHERE trainee.user.username = :traineeUsername
                        )
                    """;

            return session.createQuery(hql, Trainer.class)
                    .setParameter("traineeUsername", traineeUsername)
                    .list();
        }
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
