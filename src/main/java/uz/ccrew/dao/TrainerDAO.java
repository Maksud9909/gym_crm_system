package uz.ccrew.dao;

import org.springframework.beans.factory.annotation.Autowired;
import uz.ccrew.dao.base.advancedBase.AbstractAdvancedBaseDAO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.entity.Trainer;
import uz.ccrew.entity.Training;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import uz.ccrew.entity.User;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class TrainerDAO extends AbstractAdvancedBaseDAO<Trainer, TrainerCreateDTO> {
    private static final String ENTITY_NAME = "Trainer";
    private final UserDAO userDAO;

    @Autowired
    public TrainerDAO(SessionFactory sessionFactory, Class<Trainer> entityClass, UserDAO userDAO) {
        super(sessionFactory, entityClass);
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
                .specialization(dto.specialization())
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

        trainer.setSpecialization(dto.specialization());

        session.merge(user);
        session.merge(trainer);

        log.info("Updated {}: ID={}, Trainer={}", getEntityName(), id, dto);
    }


    public List<Training> getTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "SELECT t FROM Training t " +
                    "JOIN t.trainer trainer " +
                    "JOIN t.trainee trainee " +
                    "WHERE trainer.username = :trainerUsername " +
                    "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
                    "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
                    "AND (:traineeName IS NULL OR trainee.firstName || ' ' || trainee.lastName = :traineeName)";

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
            String hql = "SELECT tr FROM Trainer tr " +
                    "WHERE tr.id NOT IN (" +
                    "   SELECT t.id FROM Trainee trainee " +
                    "   JOIN trainee.trainers t " +
                    "   WHERE trainee.username = :traineeUsername" +
                    ")";

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
