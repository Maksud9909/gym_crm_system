package uz.ccrew.dao.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.entity.Trainer;
import uz.ccrew.dao.TrainerDAO;
import uz.ccrew.entity.TrainingType;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dao.base.advancedBase.AbstractAdvancedUserBaseCRUDDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import uz.ccrew.utils.UserUtils;

import java.util.List;

@Slf4j
@Repository
public class TrainerDAOImpl extends AbstractAdvancedUserBaseCRUDDAO<Trainer, TrainerCreateDTO> implements TrainerDAO {
    private final UserDAO userDAO;
    private final UserUtils userUtils;
    private static final String ENTITY_NAME = "Trainer";
    public static final String FIND_UNASSIGNED_TRAINERS = """
                SELECT tr FROM Trainer tr
                WHERE tr.id NOT IN (
                    SELECT trainer.id FROM Trainee trainee
                    JOIN trainee.trainers trainer
                    WHERE trainee.user.username = :traineeUsername
                )
            """;

    @Autowired
    public TrainerDAOImpl(SessionFactory sessionFactory, UserDAO userDAO, UserUtils userUtils) {
        super(sessionFactory, Trainer.class);
        this.userDAO = userDAO;
        this.userUtils = userUtils;
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
                .username(userUtils.generateUniqueUsername(firstName, lastName))
                .password(userUtils.generateRandomPassword())
                .isActive(Boolean.TRUE)
                .build();

        Long userId = userDAO.create(user);
        user = userDAO.findById(userId).orElseThrow();

        TrainingType trainingType = session.get(TrainingType.class, dto.trainingTypeId());

        Trainer trainer = Trainer.builder()
                .user(user)
                .trainingType(trainingType)
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
        user.setUsername(userUtils.generateUniqueUsername(dto.firstName(), dto.lastName()));

        TrainingType trainingType = session.get(TrainingType.class, dto.trainingTypeId());

        trainer.setTrainingType(trainingType);

        session.merge(user);
        session.merge(trainer);

        log.info("Updated {}: ID={}, Trainer={}", getEntityName(), id, dto);
    }


    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        Session session = getSessionFactory().getCurrentSession();

        return session.createQuery(FIND_UNASSIGNED_TRAINERS, Trainer.class)
                .setParameter("traineeUsername", traineeUsername)
                .list();
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
