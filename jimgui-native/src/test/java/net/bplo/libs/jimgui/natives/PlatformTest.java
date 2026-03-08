package net.bplo.libs.jimgui.natives;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlatformTest {

    @Test
    void detectsWindowsX64() {
        assertEquals(Platform.WINDOWS_X64, Platform.detect("Windows 10", "amd64"));
    }

    @Test
    void detectsWindowsX64FromX86_64Arch() {
        assertEquals(Platform.WINDOWS_X64, Platform.detect("Windows 11", "x86_64"));
    }

    @Test
    void detectsLinuxX64() {
        assertEquals(Platform.LINUX_X64, Platform.detect("Linux", "amd64"));
    }

    @Test
    void detectsMacOsX64() {
        assertEquals(Platform.MACOS_X64, Platform.detect("Mac OS X", "x86_64"));
    }

    @Test
    void detectsMacOsArm64() {
        assertEquals(Platform.MACOS_ARM64, Platform.detect("Mac OS X", "aarch64"));
    }

    @Test
    void throwsOnUnknownOs() {
        var ex = assertThrows(UnsupportedPlatformException.class,
                () -> Platform.detect("OS/2", "amd64"));
        assertTrue(ex.getMessage().contains("OS/2"),
                "Exception should name the unsupported OS");
    }

    @Test
    void throwsOnUnknownArch() {
        var ex = assertThrows(UnsupportedPlatformException.class,
                () -> Platform.detect("Linux", "mips"));
        assertTrue(ex.getMessage().contains("mips"),
                "Exception should name the unsupported arch");
    }

    @Test
    void resourcePathIncludesPlatformDirAndLibName() {
        assertEquals("/natives/windows-x64/cimgui.dll",
                Platform.WINDOWS_X64.resourcePath());
        assertEquals("/natives/linux-x64/libcimgui.so",
                Platform.LINUX_X64.resourcePath());
        assertEquals("/natives/macos-x64/libcimgui.dylib",
                Platform.MACOS_X64.resourcePath());
        assertEquals("/natives/macos-arm64/libcimgui.dylib",
                Platform.MACOS_ARM64.resourcePath());
    }
}
