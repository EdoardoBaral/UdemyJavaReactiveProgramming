package it.baral.sec10.assignment.buffer;

import java.time.LocalDateTime;
import java.util.Map;

public record RevenueReport(LocalDateTime time, Map<String, Integer> revenue) {
}
