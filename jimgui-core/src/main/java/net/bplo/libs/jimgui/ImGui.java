package net.bplo.libs.jimgui;

import net.bplo.libs.jimgui.binding.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

@SuppressWarnings("resource") // For arena refs, ignoring auto-closeable
public final class ImGui {

    private static Arena frameArena() {
        return ImGuiManager.frameArena;
    }

    private static Arena persistentArena() {
        return ImGuiManager.persistentArena;
    }

    // ------------------------------------------------------------------------
    // Windows
    // ------------------------------------------------------------------------

    public static boolean begin(String title) {
        return cimgui_h.igBegin(frameArena().allocateFrom(title), MemorySegment.NULL, 0);
    }

    public static void end() {
        cimgui_h.igEnd();
    }

    public static boolean beginChild(int id, float width, float height) {
        return beginChild(id, width, height, 0, 0);
    }

    public static boolean beginChild(int id, float width, float height, int childFlags, int windowFlags) {
        return cimgui_h.igBeginChild_ID(id, ImVec2_c.allocate(frameArena()), childFlags, windowFlags);
    }

    public static boolean beginChild(String id, float width, float height) {
        return beginChild(id, width, height, 0, 0);
    }

    public static boolean beginChild(String id, float width, float height, int childFlags, int windowFlags) {
        return cimgui_h.igBeginChild_Str(frameArena().allocateFrom(id), ImVec2_c.allocate(frameArena()), childFlags, windowFlags);
    }

    public static void endChild() {
        cimgui_h.igEndChild();
    }

    // ------------------------------------------------------------------------
    // Text
    // - some functions only have variadic versions which require special handling
    //   - jextract doesn't create 'nice' versions of variadic functions so we manually create invokers which are cached across calls
    //   - since any '%' character in the provided format string args would cause problems due to attempted sprintf'ing, we defensively escape them
    // ------------------------------------------------------------------------

    public static void textUnformatted(String text) {
        cimgui_h.igTextUnformatted(frameArena().allocateFrom(text), MemorySegment.NULL);
    }

    // NOTE: Text and TextV are for varargs, we don't need those here since users will just use Java string formatting.
    public static void text(String text) {
        textUnformatted(text);
    }

    private static final cimgui_h.igTextColored TEXT_COLORED = cimgui_h.igTextColored.makeInvoker();

    public static void textColored(float r, float g, float b, float a, String text) {
        var color = imVec4(r, g, b, a);
        var safeText = text.replaceAll("%", "%%");
        TEXT_COLORED.apply(color, frameArena().allocateFrom(safeText));
    }

    private static final cimgui_h.igTextDisabled TEXT_DISABLED = cimgui_h.igTextDisabled.makeInvoker();

    public static void textDisabled(String text) {
        var safeText = text.replaceAll("%", "%%");
        TEXT_DISABLED.apply(frameArena().allocateFrom(safeText));
    }

    private static final cimgui_h.igTextWrapped TEXT_WRAPPED = cimgui_h.igTextWrapped.makeInvoker();

    public static void textWrapped(String text) {
        var safeText = text.replaceAll("%", "%%");
        TEXT_WRAPPED.apply(frameArena().allocateFrom(safeText));
    }

    private static final cimgui_h.igLabelText LABEL_TEXT = cimgui_h.igLabelText.makeInvoker();

    public static void labelText(String label, String text) {
        var safeText = text.replaceAll("%", "%%");
        LABEL_TEXT.apply(frameArena().allocateFrom(label), frameArena().allocateFrom(safeText));
    }

    private static final cimgui_h.igBulletText BULLET_TEXT = cimgui_h.igBulletText.makeInvoker();

    public static void bulletText(String text) {
        var safeText = text.replaceAll("%", "%%");
        BULLET_TEXT.apply(frameArena().allocateFrom(safeText));
    }

    public static void separatorText(String label) {
        cimgui_h.igSeparatorText(frameArena().allocateFrom(label));
    }

    // ------------------------------------------------------------------------
    // Widgets
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Drag, Slider, Input
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Layout
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // ID Stack
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Style
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Menus
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Popups
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Query
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Util
    // ------------------------------------------------------------------------

    private static MemorySegment imVec4(float x, float y, float z, float w) {
        var vec = ImVec4_c.allocate(frameArena());
        ImVec4_c.x(vec, x);
        ImVec4_c.y(vec, y);
        ImVec4_c.z(vec, z);
        ImVec4_c.w(vec, w);
        return vec;
    }
}
