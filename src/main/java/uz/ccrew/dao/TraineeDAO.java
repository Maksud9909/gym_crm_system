package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.base.AbstractBaseDAO;

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
public class TraineeDAO extends AbstractBaseDAO<Trainee> {
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

    public void update(Long id, Trainee trainee) {
        if (getStorage().containsKey(id)) {
            trainee.setId(id);
            getStorage().put(id, trainee);
            log.info("Updated Trainee: ID={}, Trainee={}", id, trainee);
        } else {
            log.warn("Trainee with ID={} not found for update", id);
        }
    }

    public void delete(Long id) {
        Trainee trainee = getStorage().remove(id);
        if (trainee != null) {
            existingUsernames.remove(trainee.getUsername());
            log.info("Deleted Trainee: ID={}, Trainee={}", id, trainee);
        } else {
            log.warn("Trainee with ID={} not found", id);
        }
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
