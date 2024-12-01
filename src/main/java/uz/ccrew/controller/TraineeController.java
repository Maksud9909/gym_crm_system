package uz.ccrew.controller;

import org.springframework.http.ResponseEntity;
import uz.ccrew.entity.Trainee;
import uz.ccrew.service.TraineeService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.ccrew.util.Response;
import uz.ccrew.util.ResponseMaker;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping("/create")
    public ResponseEntity<Response<Long>> create(@RequestBody Trainee trainee) {
        Long id = traineeService.create(trainee);
        return ResponseMaker.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Trainee>> getById(@PathVariable Long id) {
        Trainee trainee = traineeService.findById(id);
        return ResponseMaker.ok(trainee);
    }

    @GetMapping("/list")
    public ResponseEntity<Response<List<Trainee>>> getAll() {
        List<Trainee> trainees = traineeService.findAll();
        return ResponseMaker.ok(trainees);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response<?>> update(@PathVariable Long id, @RequestBody Trainee trainee) {
        traineeService.update(id, trainee);
        return ResponseMaker.okMessage("Trainee updated successfully with ID: " + id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable Long id) {
        traineeService.delete(id);
        return ResponseMaker.okMessage("Trainee deleted successfully with ID: " + id);
    }
}
