package uz.ccrew.config.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.management.MemoryUsage;
import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;

@Configuration
public class JvmMetricsConfiguration {

    @Bean
    public MeterBinder jvmHeapMetrics() {
        return registry -> {
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

            Gauge.builder("jvm.heap.used", memoryBean,
                            this::getHeapMemoryUsed)
                    .description("JVM heap memory used")
                    .baseUnit("bytes")
                    .register(registry);

            Gauge.builder("jvm.heap.max", memoryBean,
                            this::getHeapMemoryMax)
                    .description("JVM heap memory max")
                    .baseUnit("bytes")
                    .register(registry);

            Gauge.builder("jvm.heap.committed", memoryBean,
                            this::getHeapMemoryCommitted)
                    .description("JVM heap memory committed")
                    .baseUnit("bytes")
                    .register(registry);

            Gauge.builder("jvm.heap.usage", memoryBean,
                            this::getHeapMemoryPercentage)
                    .description("JVM heap memory usage percentage")
                    .baseUnit("percent")
                    .register(registry);
        };
    }

    private double getHeapMemoryUsed(MemoryMXBean memoryBean) {
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
        return memoryUsage.getUsed();
    }

    private double getHeapMemoryMax(MemoryMXBean memoryBean) {
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
        return memoryUsage.getMax();
    }

    private double getHeapMemoryCommitted(MemoryMXBean memoryBean) {
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
        return memoryUsage.getCommitted();
    }

    private double getHeapMemoryPercentage(MemoryMXBean memoryBean) {
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();
        return (double) memoryUsage.getUsed() / memoryUsage.getMax() * 100;
    }
}
