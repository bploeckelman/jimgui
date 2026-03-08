package net.bplo.libs.jimgui.natives;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NativeLoaderTest {

    @TempDir
    Path tempDir;

    // ---------------------------------------------------------------------------
    // extractTo — happy path
    // ---------------------------------------------------------------------------

    @Test
    void extractCopiesResourceContentToTempFile() throws IOException {
        // Arrange: create a fake "native library" on the classpath via a real resource.
        // We use a test resource placed at the expected path.
        String content = "fake-native-bytes";
        Path fakeLib = tempDir.resolve("imgui.dll");
        Files.writeString(fakeLib, content);

        InputStream fakeStream = Files.newInputStream(fakeLib);

        // Act
        Path extracted = NativeLoader.extractTo(fakeStream, tempDir, "imgui.dll");

        // Assert
        assertTrue(Files.exists(extracted), "Extracted file should exist");
        assertEquals(content, Files.readString(extracted),
                "Extracted content should match the source stream");
    }

    @Test
    void extractedFileHasExpectedName() throws IOException {
        InputStream empty = InputStream.nullInputStream();
        Path extracted = NativeLoader.extractTo(empty, tempDir, "libimgui.so");
        assertEquals("libimgui.so", extracted.getFileName().toString());
    }

    // ---------------------------------------------------------------------------
    // extractTo — error path
    // ---------------------------------------------------------------------------

    @Test
    void extractThrowsWhenStreamIsNull() {
        assertThrows(NullPointerException.class,
                () -> NativeLoader.extractTo(null, tempDir, "imgui.dll"));
    }

    // ---------------------------------------------------------------------------
    // resourceStreamFor — happy path (uses real test resource)
    // ---------------------------------------------------------------------------

    @Test
    void resourceStreamForReturnsStreamForKnownResource() throws IOException {
        // We ship a tiny sentinel file at the windows-x64 path just for tests.
        // This validates that NativeLoader can open a real classpath resource.
        try (InputStream is = NativeLoader.resourceStreamFor("/natives/test-sentinel.txt")) {
            assertNotNull(is, "Should find the test sentinel resource");
            String content = new String(is.readAllBytes());
            assertEquals("sentinel", content.strip());
        }
    }

    @Test
    void resourceStreamForThrowsDescriptiveErrorWhenMissing() {
        var ex = assertThrows(NativeLoadException.class,
                () -> NativeLoader.resourceStreamFor("/natives/does-not-exist/imgui.dll"));
        assertTrue(ex.getMessage().contains("/natives/does-not-exist/imgui.dll"),
                "Exception should include the resource path");
    }
}
