package uz.ccrew.controller;

import org.springframework.http.ResponseEntity;
import uz.ccrew.entity.Training;
import uz.ccrew.service.TrainingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.ccrew.util.Response;
import uz.ccrew.util.ResponseMaker;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping("/create")
    public ResponseEntity<Response<Long>> create(@RequestBody Training training) {
        Long id = trainingService.create(training);
        return ResponseMaker.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Training>> getById(@PathVariable Long id) {
        Training training = trainingService.findById(id);
        return ResponseMaker.ok(training);
    }

    @GetMapping("/list")
    public ResponseEntity<Response<List<Training>>> getAll() {
        List<Training> trainings = trainingService.findAll();
        return ResponseMaker.ok(trainings);
    }
}
