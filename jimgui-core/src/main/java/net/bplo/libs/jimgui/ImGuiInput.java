package net.bplo.libs.jimgui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import net.bplo.libs.jimgui.binding.ImGuiIO;
import net.bplo.libs.jimgui.binding.cimgui_h;

import java.lang.foreign.MemorySegment;

/**
 * A LibGDX {@link InputProcessor} that forwards input events to Dear ImGui.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // In ApplicationListener.create(), after ImGuiManager.init():
 * Gdx.input.setInputProcessor(new ImGuiInput());
 *
 * // If you also need your own InputProcessor, chain them:
 * Gdx.input.setInputProcessor(
 *     new InputMultiplexer(new ImGuiInput(), yourProcessor));
 * }</pre>
 *
 * <p>When ImGui wants to capture mouse or keyboard input (e.g. the cursor is
 * over an ImGui window), call {@link #wantsMouseInput()} and
 * {@link #wantsKeyboardInput()} to decide whether to pass events to your
 * own game logic.
 */
public final class ImGuiInput implements InputProcessor {

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        cimgui_h.ImGuiIO_AddMousePosEvent(io(), screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cimgui_h.ImGuiIO_AddMousePosEvent(io(), screenX, screenY);
        cimgui_h.ImGuiIO_AddMouseButtonEvent(io(), button, true);
        return wantsMouseInput();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cimgui_h.ImGuiIO_AddMouseButtonEvent(io(), button, false);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cimgui_h.ImGuiIO_AddMousePosEvent(io(), screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // LibGDX scroll direction is inverted vs ImGui on the Y axis.
        cimgui_h.ImGuiIO_AddMouseWheelEvent(io(), amountX, -amountY);
        return wantsMouseInput();
    }

    @Override
    public boolean keyDown(int keycode) {
        int imguiKey = gdxKeyToImGui(keycode);
        if (imguiKey >= 0) {
            cimgui_h.ImGuiIO_AddKeyEvent(io(), imguiKey, true);
        }
        return wantsKeyboardInput();
    }

    @Override
    public boolean keyUp(int keycode) {
        int imguiKey = gdxKeyToImGui(keycode);
        if (imguiKey >= 0) {
            cimgui_h.ImGuiIO_AddKeyEvent(io(), imguiKey, false);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character != 0) {
            cimgui_h.ImGuiIO_AddInputCharacter(io(), character);
        }
        return wantsKeyboardInput();
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    // ---------------------------------------------------------------------------
    // ImGui capture queries — use these to gate your own input handling
    // ---------------------------------------------------------------------------

    /** Returns true if ImGui wants to capture mouse input this frame. */
    public boolean wantsMouseInput() {
        return ImGuiIO.WantCaptureMouse(io());
    }

    /** Returns true if ImGui wants to capture keyboard input this frame. */
    public boolean wantsKeyboardInput() {
        return ImGuiIO.WantCaptureKeyboard(io());
    }

    // ---------------------------------------------------------------------------
    // Internals
    // ---------------------------------------------------------------------------

    private MemorySegment io() {
        return cimgui_h.igGetIO_Nil();
    }

    /** Maps a LibGDX key code to the corresponding ImGui key enum value. */
    private static int gdxKeyToImGui(int gdxKey) {
        return switch (gdxKey) {
            case Input.Keys.TAB           -> cimgui_h.ImGuiKey_Tab();
            case Input.Keys.LEFT          -> cimgui_h.ImGuiKey_LeftArrow();
            case Input.Keys.RIGHT         -> cimgui_h.ImGuiKey_RightArrow();
            case Input.Keys.UP            -> cimgui_h.ImGuiKey_UpArrow();
            case Input.Keys.DOWN          -> cimgui_h.ImGuiKey_DownArrow();
            case Input.Keys.PAGE_UP       -> cimgui_h.ImGuiKey_PageUp();
            case Input.Keys.PAGE_DOWN     -> cimgui_h.ImGuiKey_PageDown();
            case Input.Keys.HOME          -> cimgui_h.ImGuiKey_Home();
            case Input.Keys.END           -> cimgui_h.ImGuiKey_End();
            case Input.Keys.INSERT        -> cimgui_h.ImGuiKey_Insert();
            case Input.Keys.FORWARD_DEL   -> cimgui_h.ImGuiKey_Delete();
            case Input.Keys.DEL           -> cimgui_h.ImGuiKey_Backspace(); // DEL == BACKSPACE in LibGDX
            case Input.Keys.SPACE         -> cimgui_h.ImGuiKey_Space();
            case Input.Keys.ENTER         -> cimgui_h.ImGuiKey_Enter();
            case Input.Keys.ESCAPE        -> cimgui_h.ImGuiKey_Escape();
            case Input.Keys.APOSTROPHE    -> cimgui_h.ImGuiKey_Apostrophe();
            case Input.Keys.COMMA         -> cimgui_h.ImGuiKey_Comma();
            case Input.Keys.MINUS         -> cimgui_h.ImGuiKey_Minus();
            case Input.Keys.PERIOD        -> cimgui_h.ImGuiKey_Period();
            case Input.Keys.SLASH         -> cimgui_h.ImGuiKey_Slash();
            case Input.Keys.SEMICOLON     -> cimgui_h.ImGuiKey_Semicolon();
            case Input.Keys.EQUALS        -> cimgui_h.ImGuiKey_Equal();
            case Input.Keys.LEFT_BRACKET  -> cimgui_h.ImGuiKey_LeftBracket();
            case Input.Keys.BACKSLASH     -> cimgui_h.ImGuiKey_Backslash();
            case Input.Keys.RIGHT_BRACKET -> cimgui_h.ImGuiKey_RightBracket();
            case Input.Keys.GRAVE         -> cimgui_h.ImGuiKey_GraveAccent();
            case Input.Keys.CONTROL_LEFT,
                 Input.Keys.CONTROL_RIGHT -> cimgui_h.ImGuiKey_LeftCtrl();
            case Input.Keys.SHIFT_LEFT,
                 Input.Keys.SHIFT_RIGHT   -> cimgui_h.ImGuiKey_LeftShift();
            case Input.Keys.ALT_LEFT,
                 Input.Keys.ALT_RIGHT     -> cimgui_h.ImGuiKey_LeftAlt();
            case Input.Keys.A             -> cimgui_h.ImGuiKey_A();
            case Input.Keys.C             -> cimgui_h.ImGuiKey_C();
            case Input.Keys.V             -> cimgui_h.ImGuiKey_V();
            case Input.Keys.X             -> cimgui_h.ImGuiKey_X();
            case Input.Keys.Y             -> cimgui_h.ImGuiKey_Y();
            case Input.Keys.Z             -> cimgui_h.ImGuiKey_Z();
            default                       -> -1;
        };
    }
}
