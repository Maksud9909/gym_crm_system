package uz.ccrew.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.ccrew.dto.Response;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.dto.trainee.TraineeCreateDTO;
import uz.ccrew.dto.trainer.TrainerCreateDTO;
import uz.ccrew.dto.user.UserCredentials;
import uz.ccrew.service.TrainerService;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
@Tag(name = "Trainer Controller", description = "Trainer API")
public class TrainerController {
    private final TrainerService trainerService;

    @PostMapping("/create")
    @Operation(summary = "Create a Trainer")
    public ResponseEntity<Response<UserCredentials>> create(@RequestBody TrainerCreateDTO dto) {
        UserCredentials result = trainerService.create(dto);
        return ResponseMaker.ok(result);
    }
}
