package uz.ccrew.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/actuator")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Monitoring Controller", description = "Monitoring API")
public class MonitoringController {

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics/jvm.heap.committed")
    @Operation(summary = "JVM Heap Commited metrics")
    public ResponseEntity<Object> getMetrics() {
        return ResponseEntity.ok().build();
    }
}
