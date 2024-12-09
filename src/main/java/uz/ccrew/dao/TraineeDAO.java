package uz.ccrew.dao;

import uz.ccrew.entity.Trainee;
import uz.ccrew.dao.base.AbstractBaseDAO;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
public class TraineeDAO extends AbstractBaseDAO<Trainee> {
    private Set<String> existingUsernames = new HashSet<>();

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.storage = traineeStorage;
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
        storage.put(id, trainee);
        log.info("Created Trainee: ID={}, Trainee={}", id, trainee);
        return id;
    }

    public void update(Long id, Trainee trainee) {
        if (storage.containsKey(id)) {
            trainee.setId(id);
            storage.put(id, trainee);
            log.info("Updated Trainee: ID={}, Trainee={}", id, trainee);
        } else {
            log.warn("Trainee with ID={} not found for update", id);
        }
    }

    public void delete(Long id) {
        Trainee trainee = storage.remove(id);
        if (trainee != null) {
            existingUsernames.remove(trainee.getUsername());
            log.info("Deleted Trainee: ID={}, Trainee={}", id, trainee);
        } else {
            log.warn("Trainee with ID={} not found", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "Trainee";
    }
}
