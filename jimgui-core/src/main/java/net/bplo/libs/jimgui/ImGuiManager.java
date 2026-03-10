package net.bplo.libs.jimgui;

import com.badlogic.gdx.Gdx;
import net.bplo.libs.jimgui.binding.ImGuiIO;
import net.bplo.libs.jimgui.binding.ImVec2_c;
import net.bplo.libs.jimgui.binding.cimgui_h;
import net.bplo.libs.jimgui.natives.NativeLoader;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages the Dear ImGui context and OpenGL3 render backend.
 *
 * <h2>LibGDX usage</h2>
 * <pre>{@code
 * // In ApplicationListener.create():
 * ImGuiManager.init();
 * Gdx.input.setInputProcessor(new ImGuiInput());
 *
 * // In ApplicationListener.render():
 * ImGuiManager.newFrame();
 *   // ... your ImGui calls via cimgui_h ...
 * ImGuiManager.render();
 *
 * // In ApplicationListener.dispose():
 * ImGuiManager.dispose();
 * }</pre>
 *
 * <p>All methods must be called from the LibGDX render thread.
 */
public final class ImGuiManager {

    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    static Arena frameArena;
    static Arena persistentArena;

    private ImGuiManager() {}

    /**
     * Initialises ImGui: loads the native library, creates a context, and
     * sets up the OpenGL3 render backend.
     *
     * <p>Safe to call only once. Call from {@code ApplicationListener.create()}.
     *
     * @param glslVersion the GLSL version string passed to the OpenGL3 backend,
     *                    e.g. {@code "#version 150"} for OpenGL 3.2 core profile
     *                    (LWJGL3 default), or {@code "#version 330 core"}.
     */
    public static void init(String glslVersion) {
        if (!initialized.compareAndSet(false, true)) {
            throw new IllegalStateException("ImGuiManager.init() called more than once.");
        }

        NativeLoader.load();

        cimgui_h.igCreateContext(MemorySegment.NULL);
        cimgui_h.igStyleColorsDark(MemorySegment.NULL);

        try (Arena arena = Arena.ofConfined()) {
            var version = arena.allocateFrom(glslVersion);
            var ok = cimgui_h.ImGui_ImplOpenGL3_Init(version);
            if (!ok) {
                throw new IllegalStateException(
                    "ImGui_ImplOpenGL3_Init failed. Ensure an OpenGL context is current " +
                    "before calling ImGuiManager.init().");
            }
        }

        frameArena = null;
        persistentArena = Arena.ofConfined();
    }

    /**
     * Convenience overload using the LWJGL3/LibGDX default GLSL version.
     */
    public static void init() {
        init("#version 150");
    }

    /**
     * Begins a new ImGui frame. Call at the start of each render cycle,
     * before any {@code cimgui_h.ig*()} widget calls.
     */
    public static void newFrame() {
        // Without a GLFW platform backend, we must feed DisplaySize and DeltaTime
        // to ImGui ourselves every frame. Without DisplaySize, ImGui thinks the
        // window is 0x0 and renders nothing.
        var io = cimgui_h.igGetIO_Nil();
        var displaySize = ImGuiIO.DisplaySize(io);
        ImVec2_c.x(displaySize, Gdx.graphics.getWidth());
        ImVec2_c.y(displaySize, Gdx.graphics.getHeight());
        ImGuiIO.DeltaTime(io, Gdx.graphics.getDeltaTime());

        frameArena = Arena.ofConfined();
        cimgui_h.ImGui_ImplOpenGL3_NewFrame();
        cimgui_h.igNewFrame();
    }

    /**
     * Finalises the ImGui frame and issues draw calls to OpenGL.
     * Call after all ImGui widget calls, before swapping buffers.
     */
    public static void render() {
        cimgui_h.igRender();
        cimgui_h.ImGui_ImplOpenGL3_RenderDrawData(cimgui_h.igGetDrawData());
        frameArena.close();
        frameArena = null;
    }

    /**
     * Shuts down the ImGui OpenGL3 backend and destroys the context.
     * Call from {@code ApplicationListener.dispose()}.
     */
    public static void dispose() {
        if (!initialized.get()) return;
        persistentArena.close();
        cimgui_h.ImGui_ImplOpenGL3_Shutdown();
        cimgui_h.igDestroyContext(MemorySegment.NULL);
        initialized.set(false);
    }
}
