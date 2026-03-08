# jimgui

⚠️ This is a toy project at the moment and shouldn't be used for anything real! ⚠️

Java bindings for [Dear ImGui](https://github.com/ocornut/imgui) using the Java
Foreign Function & Memory API (Project Panama). Designed for use with
[LibGDX](https://libgdx.com/) LWJGL3 projects.

The binding wraps [cimgui](https://github.com/cimgui/cimgui), a generated C
wrapper around the Dear ImGui C++ API, so the Java layer can call it via FFM
without JNI.

Most of the initial setup was produced by Claude Sonnet 4.6
via the coding agent from [badlogic/pi-mono](https://github.com/badlogic/pi-mono).

---

## Project structure

| Module | Contents |
|---|---|
| `jimgui-native` | Pre-built native libraries (`cimgui.dll` / `.so` / `.dylib`) + `NativeLoader` |
| `jimgui-binding` | jextract-generated FFM bindings (committed to source) |
| `jimgui-core` | `ImGuiManager` and `ImGuiInput` — the LibGDX integration layer |

---

## Building

### Prerequisites

- Java 25 (the project toolchain targets JDK 25)
- **Windows:** Visual Studio 2022 with the C++ workload
- **Linux/macOS:** GCC or Clang + CMake

### Step 1 — Clone and set up

```bash
git clone <this-repo>
cd jimgui
```

### Step 2 — Build the native library

This compiles `cimgui.dll` (Windows), `libcimgui.so` (Linux), or
`libcimgui.dylib` (macOS) and places it in
`jimgui-native/src/main/resources/natives/<platform>/`.

```bash
./gradlew buildNatives
```

On Windows this uses the Visual Studio 17 2022 generator and finds the MSVC
toolchain automatically — no VS developer prompt required.

### Step 3 — Build the JARs

```bash
./gradlew build
```

This produces three JARs under each module's `build/libs/`:

```
jimgui-native/build/libs/jimgui-native-0.1.0-SNAPSHOT.jar
jimgui-binding/build/libs/jimgui-binding-0.1.0-SNAPSHOT.jar
jimgui-core/build/libs/jimgui-core-0.1.0-SNAPSHOT.jar
```

---

### Regenerating bindings (optional)

The FFM binding sources are committed to source control and do not need to be
regenerated for normal use. Regenerate only when upgrading Dear ImGui / cimgui.

```bash
# One-time: download jextract into tools/jextract/
./gradlew downloadJextract

# Regenerate binding sources from cimgui headers
./gradlew generateBindings

# Then commit the result
git add jimgui-binding/src/main/java
git commit -m "regen bindings for cimgui <version>"
```

To pull a newer version of the cimgui headers before regenerating:

```bash
./gradlew refreshHeaders
```

---

## Adding jimgui to a gdx-liftoff project

A gdx-liftoff project has (at minimum) a `core` module and an `lwjgl3` module.

### 1. Copy the JARs

All three JARs go into a single `libs/` folder at the **project root**:

```
my-game/
  libs/
    jimgui-native-0.1.0-SNAPSHOT.jar
    jimgui-binding-0.1.0-SNAPSHOT.jar
    jimgui-core-0.1.0-SNAPSHOT.jar
```

There is **no need** to place `cimgui.dll` anywhere on disk — `NativeLoader`
extracts it from inside `jimgui-native` at runtime into a temporary directory
and loads it automatically.

### 2. Root `build.gradle`

No changes required. jimgui has no Gradle plugins or repositories of its own
that need to be declared at the root level.

### 3. `core/build.gradle`

Add the three JARs as a compile-time dependency:

```groovy
dependencies {
    // ... existing LibGDX dependencies ...
    api fileTree(dir: '../libs', include: ['*.jar'])
}
```

### 4. `lwjgl3/build.gradle`

Add the same `fileTree` dependency so the JARs (and their transitive natives)
are on the runtime classpath when you run the desktop launcher:

```groovy
dependencies {
    // ... existing LibGDX dependencies ...
    api fileTree(dir: '../libs', include: ['*.jar'])
}
```

That is all. Do **not** add a `jvmArgs "-Djava.library.path=..."` block —
`NativeLoader` uses `System.load()` with an absolute path to the extracted
temp file and does not rely on the library search path.

---

## Minimal integration

### `create()`

```java
@Override
public void create() {
    // Initialise ImGui and the OpenGL3 render backend.
    // Must be called after the OpenGL context exists (i.e. inside create(),
    // not before Lwjgl3Application is constructed).
    ImGuiManager.init();

    // Route input events to ImGui. Chain with InputMultiplexer if your game
    // also needs its own InputProcessor.
    Gdx.input.setInputProcessor(new ImGuiInput());
}
```

### `render()`

```java
@Override
public void render() {
    // Clear screen however you normally do it...
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // --- ImGui frame ---
    ImGuiManager.newFrame();

    try (var arena = Arena.ofConfined()) {
        // Begin a window
        cimgui_h.igBegin(arena.allocateFrom("My Window"), MemorySegment.NULL, 0);

        // Text (note: igText is variadic and not bound — use igText0 instead)
        cimgui_h.igText0(arena.allocateFrom("Hello from jimgui!"));

        cimgui_h.igEnd();
    }

    ImGuiManager.render();
    // --- End ImGui frame ---
}
```

### `dispose()`

```java
@Override
public void dispose() {
    ImGuiManager.dispose();
}
```

### Required imports

```java
import net.bplo.libs.jimgui.ImGuiManager;
import net.bplo.libs.jimgui.ImGuiInput;
import net.bplo.libs.jimgui.binding.cimgui_h;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
```

---

## Notes

### `igText` vs `igText0`

`igText` is a variadic C function (`printf`-style) and is not generated by
jextract. Use `igText0` for plain string output — cimgui generates a
`0`-suffixed non-variadic overload for every variadic function:

```java
cimgui_h.igText0(arena.allocateFrom("plain string"));
```

The same pattern applies to `igTextColored` → `igTextColored0`,
`igTextDisabled` → `igTextDisabled0`, `igTextWrapped` → `igTextWrapped0`, etc.

### `igBegin` — the `p_open` argument

To create a window with no close button, pass `MemorySegment.NULL` for
`p_open`. To make a closeable window, allocate a `bool` in your arena and
check it each frame:

```java
// Closeable window
var pOpen = arena.allocate(cimgui_h.C_BOOL);
MemorySegment.copy(MemorySegment.ofArray(new byte[]{1}), 0, pOpen, 0, 1);

if (cimgui_h.igBegin(arena.allocateFrom("Closeable"), pOpen, 0)) {
    cimgui_h.igText0(arena.allocateFrom("Content here"));
}
cimgui_h.igEnd();
```

### Input handling

`ImGuiInput` implements LibGDX's `InputProcessor`. If your game also needs
input, use `InputMultiplexer` and check `wantsMouseInput()` /
`wantsKeyboardInput()` to avoid passing events to game logic when ImGui has
focus:

```java
var imguiInput = new ImGuiInput();
Gdx.input.setInputProcessor(new InputMultiplexer(imguiInput, myGameInput) {
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (imguiInput.wantsMouseInput()) return imguiInput.touchDown(x, y, pointer, button);
        return super.touchDown(x, y, pointer, button);
    }
});
```

### Arena lifecycle

All strings and structs passed to `cimgui_h` functions must remain valid for
the duration of the call. A `try`-with-resources `Arena.ofConfined()` around
the ImGui frame (as shown above) is the idiomatic approach.
