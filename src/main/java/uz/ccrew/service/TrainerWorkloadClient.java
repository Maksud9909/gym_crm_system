package uz.ccrew.service;

import uz.ccrew.dto.summary.TrainerMonthlySummaryDTO;
import uz.ccrew.dto.training.TrainerWorkloadDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "trainer-workload-service",
        path = "/api/v1/trainers",
        url = "http://localhost:8081")
public interface TrainerWorkloadClient {

    @GetMapping("/{username}/workload")
    ResponseEntity<List<TrainerMonthlySummaryDTO>> getMonthlyWorkload(@PathVariable("username") String username);
}