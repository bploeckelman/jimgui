package net.bplo.libs.jimgui.natives;

/**
 * Represents a supported OS + architecture combination and knows the resource
 * path and file name of the native library for that platform.
 */
public enum Platform {

    WINDOWS_X64 ("windows-x64",  "cimgui.dll"),
    LINUX_X64   ("linux-x64",    "libcimgui.so"),
    MACOS_X64   ("macos-x64",    "libcimgui.dylib"),
    MACOS_ARM64 ("macos-arm64",  "libcimgui.dylib");

    private final String dirName;
    private final String libName;

    Platform(String dirName, String libName) {
        this.dirName = dirName;
        this.libName = libName;
    }

    /** The classpath resource path of the native library for this platform. */
    public String resourcePath() {
        return "/natives/" + dirName + "/" + libName;
    }

    /** The bare library file name (used when writing the temp file). */
    public String libName() {
        return libName;
    }

    /**
     * Detects the current platform from {@code os.name} and {@code os.arch}
     * system properties.
     *
     * @throws UnsupportedPlatformException if the combination is not supported
     */
    public static Platform detect() {
        return detect(System.getProperty("os.name"), System.getProperty("os.arch"));
    }

    /** Package-private overload used by tests. */
    static Platform detect(String osName, String osArch) {
        String os   = osName.toLowerCase();
        String arch = osArch.toLowerCase();

        boolean isX64   = arch.equals("amd64")   || arch.equals("x86_64");
        boolean isArm64 = arch.equals("aarch64") || arch.equals("arm64");

        if (os.startsWith("windows")) {
            if (isX64) return WINDOWS_X64;
        } else if (os.startsWith("linux")) {
            if (isX64) return LINUX_X64;
        } else if (os.startsWith("mac")) {
            if (isX64)   return MACOS_X64;
            if (isArm64) return MACOS_ARM64;
        }

        if (!os.startsWith("windows") && !os.startsWith("linux") && !os.startsWith("mac")) {
            throw new UnsupportedPlatformException(
                    "Unsupported OS: \"" + osName + "\". Supported: Windows, Linux, macOS.");
        }
        throw new UnsupportedPlatformException(
                "Unsupported architecture: \"" + osArch + "\" on " + osName +
                ". Supported: amd64/x86_64, aarch64.");
    }
}
