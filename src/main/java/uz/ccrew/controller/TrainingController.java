package uz.ccrew.controller;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
@Tag(name = "Training Controller", description = "Training API")
public class TrainingController {
}
