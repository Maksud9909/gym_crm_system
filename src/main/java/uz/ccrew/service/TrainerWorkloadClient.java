package uz.ccrew.service;

import uz.ccrew.dto.training.TrainerWorkloadDTO;
import uz.ccrew.dto.training.summary.TrainerMonthlySummaryDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "trainer-workload-service",
        url = "http://localhost:8081",
        path = "api/v1/trainings/workload")
public interface TrainerWorkloadClient {

    @PostMapping("/process")
    ResponseEntity<String> sendTrainingData(@RequestBody TrainerWorkloadDTO trainingDTO);

    @GetMapping("{username}")
    ResponseEntity<List<TrainerMonthlySummaryDTO>> getMonthlyWorkload(@PathVariable("username") String username);
}
