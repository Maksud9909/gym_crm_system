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
    public ResponseEntity<Object> getMetricsHeapCommitted() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics/jvm.heap.max")
    @Operation(summary = "JVM Heap Max metrics")
    public ResponseEntity<Object> getMetricsHeapMax() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics/jvm.heap.usage")
    @Operation(summary = "JVM Heap Usage metrics")
    public ResponseEntity<Object> getMetricsHeapUsage() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics/jvm.heap.used")
    @Operation(summary = "JVM Heap Used metrics")
    public ResponseEntity<Object> getMetricsHeapUsed() {
        return ResponseEntity.ok().build();
    }
}
