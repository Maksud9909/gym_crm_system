package uz.ccrew.dao;

import uz.ccrew.dao.base.AbstractBaseDAO;
import uz.ccrew.entity.Trainer;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
public class TrainerDAO extends AbstractBaseDAO<Trainer> {
    private static final String ENTITY_NAME = "Trainer";
    private Set<String> existingUsernames;

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        setStorage(trainerStorage);
        log.info("Trainer storage injected into TrainerDAO");
    }

    @Autowired
    public void setExistingUsernames(Set<String> existingUsernames) {
        this.existingUsernames = existingUsernames;
        log.info("existingUsernames injected into TrainerDAO");
    }

    @Override
    public Long create(Trainer trainer) {
        String username = generateUniqueUsername(trainer.getFirstName(), trainer.getLastName(), existingUsernames);
        trainer.setUsername(username);
        trainer.setPassword(generateRandomPassword());

        Long id = getNextId();
        trainer.setId(id);
        getStorage().put(id, trainer);
        log.info("Created Trainer: ID={}, Trainer={}", id, trainer);
        return id;
    }

    public void update(Long id, Trainer trainer) {
        getStorage().put(id, trainer);
        log.info("Updated Trainer: ID={}, Trainer={}", id, trainer);
    }

    public void delete(Long id) {
        getStorage().remove(id);
        log.info("Deleted Trainer: ID={}", id);
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
