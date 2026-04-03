package letscode.hnreader.domain;

import java.util.Arrays;

public enum RenderingMode {
    THYMELEAF,
    JTE;

    public static RenderingMode fromString(String mode) {
        return Arrays.stream(values())
            .filter(m -> m.toString().equalsIgnoreCase(mode))
            .findFirst()
            .orElse(THYMELEAF);
    }
}
