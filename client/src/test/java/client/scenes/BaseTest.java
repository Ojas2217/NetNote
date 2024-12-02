package client.scenes;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    private static boolean isJavaFXInitialized = false;
    @BeforeAll
    public static void initJavaFX() {
        if (!isJavaFXInitialized) {
            if (!Platform.isFxApplicationThread()) {
                Platform.startup(() -> {});
            }
            isJavaFXInitialized = true;
        }
    }
}
