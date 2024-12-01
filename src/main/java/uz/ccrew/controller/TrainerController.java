package uz.ccrew.controller;

import org.springframework.http.ResponseEntity;
import uz.ccrew.entity.Trainer;
import uz.ccrew.service.TrainerService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.ccrew.util.Response;
import uz.ccrew.util.ResponseMaker;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping("/create")
    public ResponseEntity<Response<Long>> create(@RequestBody Trainer trainer) {
        Long id = trainerService.create(trainer);
        return ResponseMaker.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Trainer>> getById(@PathVariable Long id) {
        Trainer trainer = trainerService.findById(id);
        return ResponseMaker.ok(trainer);
    }

    @GetMapping("/list")
    public ResponseEntity<Response<List<Trainer>>> getAll() {
        List<Trainer> trainers = trainerService.findAll();
        return ResponseMaker.ok(trainers);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response<?>> update(@PathVariable Long id, @RequestBody Trainer trainer) {
        trainerService.update(id, trainer);
        return ResponseMaker.okMessage("Trainer updated successfully with ID: " + id);
    }
}
