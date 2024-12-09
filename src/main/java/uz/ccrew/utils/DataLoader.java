package uz.ccrew.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Function;

@Slf4j
public class DataLoader {

    public static <T> void loadData(String filePath, Map<Long, T> storage, Function<String[], T> mapper, Function<T, Long> idExtractor) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(DataLoader.class.getResourceAsStream(filePath))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                T entity = mapper.apply(data);
                Long id = idExtractor.apply(entity);
                storage.put(id, entity);
            }
            log.info("Loaded data from {}", filePath);
        } catch (Exception e) {
            log.error("Error loading data from file: {}", filePath, e);
        }
    }

}
