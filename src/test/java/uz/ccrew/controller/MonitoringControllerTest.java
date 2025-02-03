package uz.ccrew.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class MonitoringControllerTest {

    @InjectMocks
    private MonitoringController monitoringController;

    @Test
    void healthCheck_ShouldReturnOkStatus() {
        ResponseEntity<?> response = monitoringController.healthCheck();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMetricsHeapCommitted_ShouldReturnOkStatus() {
        ResponseEntity<Object> response = monitoringController.getMetricsHeapCommitted();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMetricsHeapMax_ShouldReturnOkStatus() {
        ResponseEntity<Object> response = monitoringController.getMetricsHeapMax();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMetricsHeapUsage_ShouldReturnOkStatus() {
        ResponseEntity<Object> response = monitoringController.getMetricsHeapUsage();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMetricsHeapUsed_ShouldReturnOkStatus() {
        ResponseEntity<Object> response = monitoringController.getMetricsHeapUsed();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
