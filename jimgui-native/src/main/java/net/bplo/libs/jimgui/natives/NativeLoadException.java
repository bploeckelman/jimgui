package net.bplo.libs.jimgui.natives;

/**
 * Thrown when a native library cannot be found in the classpath resources
 * or fails to load.
 */
public final class NativeLoadException extends RuntimeException {
    public NativeLoadException(String message) {
        super(message);
    }

    public NativeLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
