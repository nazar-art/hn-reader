package letscode.hnreader.domain;

import java.util.Arrays;

public enum RoutingMode {
    THYMELEAF,
    JTE;

    public static RoutingMode fromString(String mode) {
        return Arrays.stream(values())
            .filter(v -> v.toString().equalsIgnoreCase(mode))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Illegal routing mode: " + mode));
    }
}
