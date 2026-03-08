package net.bplo.libs.jimgui.natives;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Detects the current platform, extracts the matching pre-built ImGui native
 * library from classpath resources, and loads it via {@link System#load}.
 *
 * <p>Call {@link #load()} once at application startup (e.g. from a LibGDX
 * {@code ApplicationListener} before any ImGui calls). Subsequent calls are
 * no-ops.
 *
 * <p>The native library is extracted to a temp file that is deleted on JVM
 * shutdown.
 */
public final class NativeLoader {

    private static final AtomicBoolean LOADED = new AtomicBoolean(false);

    private NativeLoader() {}

    // ---------------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------------

    /**
     * Loads the native ImGui library for the current platform.
     * Safe to call multiple times — only the first call does work.
     *
     * @throws UnsupportedPlatformException if the current OS/arch has no native
     * @throws NativeLoadException          if the resource is missing or load fails
     */
    public static void load() {
        if (!LOADED.compareAndSet(false, true)) {
            return;
        }

        Platform platform = Platform.detect();
        String resourcePath = platform.resourcePath();

        try (InputStream stream = resourceStreamFor(resourcePath)) {
            Path tempDir  = Files.createTempDirectory("jimgui-natives-");
            Path tempLib  = extractTo(stream, tempDir, platform.libName());
            tempLib.toFile().deleteOnExit();
            tempDir.toFile().deleteOnExit();
            System.load(tempLib.toAbsolutePath().toString());
        } catch (IOException e) {
            LOADED.set(false); // allow retry on transient failures
            throw new NativeLoadException(
                    "Failed to extract native library for platform: " + platform, e);
        }
    }

    // ---------------------------------------------------------------------------
    // Package-private helpers (visible to tests)
    // ---------------------------------------------------------------------------

    /**
     * Opens a classpath resource stream for the given absolute resource path.
     *
     * @throws NativeLoadException if the resource is not found
     */
    static InputStream resourceStreamFor(String resourcePath) {
        InputStream stream = NativeLoader.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new NativeLoadException(
                    "Native library not found on classpath: " + resourcePath +
                    ". Ensure jimgui-native is on the classpath and the pre-built " +
                    "library for your platform has been added under src/main/resources" +
                    resourcePath + ".");
        }
        return stream;
    }

    /**
     * Copies {@code source} into {@code targetDir}/{@code fileName} and returns
     * the resulting path.
     */
    static Path extractTo(InputStream source, Path targetDir, String fileName)
            throws IOException {
        Objects.requireNonNull(source, "source stream must not be null");
        Path target = targetDir.resolve(fileName);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}
