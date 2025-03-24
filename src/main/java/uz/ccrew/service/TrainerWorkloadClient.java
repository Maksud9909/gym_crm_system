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
        path = "/api/v1/trainers")
public interface TrainerWorkloadClient {

    @PostMapping("/")
    ResponseEntity<String> sendTrainingData(@RequestBody TrainerWorkloadDTO trainingDTO);

    @GetMapping("/{username}/workload")
    ResponseEntity<List<TrainerMonthlySummaryDTO>> getMonthlyWorkload(@PathVariable("username") String username);
}
