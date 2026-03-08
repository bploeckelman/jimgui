package net.bplo.libs.jimgui.natives;

/**
 * Thrown when the current OS/architecture combination has no pre-built native library.
 */
public final class UnsupportedPlatformException extends RuntimeException {
    public UnsupportedPlatformException(String message) {
        super(message);
    }
}
