package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.base.AbstractCRUDBaseDAO;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
public class TraineeDAO extends AbstractCRUDBaseDAO<Trainee> {
    private static final String ENTITY_NAME = "Trainee";
    private Set<String> existingUsernames = new HashSet<>();

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        setStorage(traineeStorage);
        log.info("Trainee storage injected into TraineeDAO");
    }

    @Autowired
    public void setExistingUsernames(Set<String> existingUsernames) {
        this.existingUsernames = existingUsernames;
        log.info("existingUsernames injected into TraineeDAO");
    }

    @Override
    public Long create(Trainee trainee) {
        String username = generateUniqueUsername(trainee.getFirstName(), trainee.getLastName(), existingUsernames);
        trainee.setUsername(username);
        trainee.setPassword(generateRandomPassword());

        Long id = getNextId();
        trainee.setId(id);
        getStorage().put(id, trainee);
        log.info("Created Trainee: ID={}, Trainee={}", id, trainee);
        return id;
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
