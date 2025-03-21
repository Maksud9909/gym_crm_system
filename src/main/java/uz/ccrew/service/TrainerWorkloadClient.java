package uz.ccrew.service;

import uz.ccrew.dto.training.TrainerWorkloadDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "trainer-workload-service",
        url = "http://localhost:8081",
        path = "api/v1/trainings/workload")
public interface TrainerWorkloadClient {

    @PostMapping("/process")
    ResponseEntity<String> sendTrainingData(@RequestBody TrainerWorkloadDTO trainingDTO);
}
