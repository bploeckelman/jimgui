package net.bplo.libs.jimgui;

import net.bplo.libs.jimgui.binding.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Objects;
import java.util.Optional;

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

    public static boolean button(String label, float width, float height) {
        var size = imVec2(width, height);
        return cimgui_h.igButton(frameArena().allocateFrom(label), size);
    }

    public static boolean smallButton(String label) {
        return cimgui_h.igSmallButton(frameArena().allocateFrom(label));
    }

    public static boolean invisibleButton(String id, float width, float height) {
        return invisibleButton(id, width, height, 0);
    }

    public static boolean invisibleButton(String id, float width, float height, int buttonFlags) {
        var size = imVec2(width, height);
        return cimgui_h.igInvisibleButton(frameArena().allocateFrom(id), size, buttonFlags);
    }

    // TODO: dir -> ImGuiDir dir... I assume ImGuiDir is a directional enum for the arrow direction
    public static boolean arrowButton(String id, int dir) {
        return cimgui_h.igArrowButton(frameArena().allocateFrom(id), dir);
    }

    public record Bool(boolean[] value) {
        public Bool()           { this(new boolean[] { false }); }
        public Bool(boolean v)  { this(new boolean[] { v }); }
        public boolean get()       { return value[0]; }
        public void set(boolean v) { value[0] = v; }
    }

    public static boolean checkbox(String label, Bool v) {
        var pV = frameArena().allocate(cimgui_h.C_BOOL);
        pV.set(cimgui_h.C_BOOL, 0, v.get());
        var clicked = cimgui_h.igCheckbox(frameArena().allocateFrom(label), pV);
        v.set(pV.get(cimgui_h.C_BOOL, 0));
        return clicked;
    }

    public record Int(int[] value) {
        public Int()           { this(new int[] { 0 }); }
        public Int(int v)      { this(new int[] { v }); }
        public int get()       { return value[0]; }
        public void set(int v) { value[0] = v; }
    }

    public static boolean checkboxFlags(String label, Int flags, int flagValue) {
        var pFlags = frameArena().allocate(cimgui_h.C_INT);
        pFlags.set(cimgui_h.C_INT, 0, flags.get());
        var clicked = cimgui_h.igCheckboxFlags_IntPtr(frameArena().allocateFrom(label), pFlags, flagValue);
        flags.set(pFlags.get(cimgui_h.C_INT, 0));
        return clicked;
    }

    public static boolean radioButton(String label, boolean active) {
        return cimgui_h.igRadioButton_Bool(frameArena().allocateFrom(label), active);
    }

    public static boolean radioButton(String label, Int v, int vButton) {
        var pV = frameArena().allocate(cimgui_h.C_INT);
        pV.set(cimgui_h.C_INT, 0, v.get());
        var clicked = cimgui_h.igRadioButton_IntPtr(frameArena().allocateFrom(label), pV, vButton);
        v.set(pV.get(cimgui_h.C_INT, 0));
        return clicked;
    }

    public static void progressBar(float fraction, float width, float height) {
        progressBar(fraction, width, height, null);
    }

    public static void progressBar(float fraction, float width, float height, String overlay) {
        var size = imVec2(width, height);
        var overlayStr = (overlay == null) ? MemorySegment.NULL : frameArena().allocateFrom(overlay);
        cimgui_h.igProgressBar(fraction, size, overlayStr);
    }

    public static void bullet() {
        cimgui_h.igBullet();
    }

    public static boolean textLink(String label) {
        return cimgui_h.igTextLink(frameArena().allocateFrom(label));
    }

    public static boolean textLinkOpenURL(String label, String url) {
        var urlStr = (url == null) ? MemorySegment.NULL : frameArena().allocateFrom(url);
        return cimgui_h.igTextLinkOpenURL(frameArena().allocateFrom(label), urlStr);
    }

    // TODO: images, combo box / dropdown

    // ------------------------------------------------------------------------
    // Drag, Slider, Input
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // Layout
    // ------------------------------------------------------------------------

    public static void sameLine() {
        sameLine(0, -1);
    }

    public static void sameLine(float offsetFromStartX, float spacing) {
        cimgui_h.igSameLine(offsetFromStartX, spacing);
    }

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

    private static MemorySegment imVec2(float x, float y) {
        var vec = ImVec2_c.allocate(frameArena());
        ImVec2_c.x(vec, x);
        ImVec2_c.y(vec, y);
        return vec;
    }

    private static MemorySegment imVec4(float x, float y, float z, float w) {
        var vec = ImVec4_c.allocate(frameArena());
        ImVec4_c.x(vec, x);
        ImVec4_c.y(vec, y);
        ImVec4_c.z(vec, z);
        ImVec4_c.w(vec, w);
        return vec;
    }
}
