package net.bplo.libs.jimgui;

import net.bplo.libs.jimgui.binding.*;
import org.w3c.dom.Text;

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

    // =================================================================================================================
    // region Return value types for conveniently wrapping MemorySegment values returned from method calls
    // =================================================================================================================

    public record Vec2f(float x, float y) {}
    public record Vec3f(float x, float y, float z) {}
    public record Vec4f(float x, float y, float z, float w) {}

    public record Vec2i(int x, int y) {}
    public record Vec3i(int x, int y, int z) {}
    public record Vec4i(int x, int y, int z, int w) {}

    public record Color3f(float r, float g, float b) {}
    public record Color4f(float r, float g, float b, float a) {}

    public record Color3i(int r, int g, int b) {}
    public record Color4i(int r, int g, int b, int a) {}
    //endregion

    // =================================================================================================================
    // region Output value types for method parameters that get written to in a method call
    // =================================================================================================================

    public record BoolArg(boolean[] value) {
        public BoolArg()           { this(new boolean[] { false }); }
        public BoolArg(boolean v)  { this(new boolean[] { v }); }
        public boolean get()       { return value[0]; }
        public void set(boolean v) { value[0] = v; }
    }

    public record IntArg(int[] value) {
        public IntArg()        { this(new int[] { 0 }); }
        public IntArg(int v)   { this(new int[] { v }); }
        public int get()       { return value[0]; }
        public void set(int v) { value[0] = v; }
    }

    public record FloatArg(float[] value) {
        public FloatArg()        { this(new float[] { 0 }); }
        public FloatArg(float v) { this(new float[] { v }); }
        public float get()       { return value[0]; }
        public void set(float v) { value[0] = v; }
    }

    public record DoubleArg(double[] value) {
        public DoubleArg()         { this(new double[] { 0 }); }
        public DoubleArg(double v) { this(new double[] { v }); }
        public double get()        { return value[0]; }
        public void set(double v)  { value[0] = v; }
    }

    public record StringArg(String[] value) {
        public StringArg()         { this(new String[] { "" }); }
        public StringArg(String v) { this(new String[] { v }); }
        public String get()        { return value[0]; }
        public void set(String v)  { value[0] = v; }
    }
    //endregion

    // =================================================================================================================
    // region Cached Invokers
    // =================================================================================================================

    private static final cimgui_h.igTextColored TEXT_COLORED = cimgui_h.igTextColored.makeInvoker();
    private static final cimgui_h.igTextDisabled TEXT_DISABLED = cimgui_h.igTextDisabled.makeInvoker();
    private static final cimgui_h.igTextWrapped TEXT_WRAPPED = cimgui_h.igTextWrapped.makeInvoker();
    private static final cimgui_h.igLabelText LABEL_TEXT = cimgui_h.igLabelText.makeInvoker();
    private static final cimgui_h.igBulletText BULLET_TEXT = cimgui_h.igBulletText.makeInvoker();
    private static final cimgui_h.igSetTooltip SET_TOOLTIP = cimgui_h.igSetTooltip.makeInvoker();
    private static final cimgui_h.igSetItemTooltip SET_ITEM_TOOLTIP = cimgui_h.igSetItemTooltip.makeInvoker();
    private static final cimgui_h.igLogText LOG_TEXT = cimgui_h.igLogText.makeInvoker();
    //endregion

    // =================================================================================================================
    // region Demo, Debug, Information
    // =================================================================================================================

    public static void showDemoWindow(BoolArg open) {
        var pOpen = frameArena().allocate(cimgui_h.C_BOOL);
        pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        cimgui_h.igShowDemoWindow(pOpen);
        open.set(pOpen.get(cimgui_h.C_BOOL, 0));
    }

    public static void showMetricsWindow(BoolArg open) {
        var pOpen = frameArena().allocate(cimgui_h.C_BOOL);
        pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        cimgui_h.igShowMetricsWindow(pOpen);
        open.set(pOpen.get(cimgui_h.C_BOOL, 0));
    }

    public static void showDebugLogWindow(BoolArg open) {
        var pOpen = frameArena().allocate(cimgui_h.C_BOOL);
        pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        cimgui_h.igShowDebugLogWindow(pOpen);
        open.set(pOpen.get(cimgui_h.C_BOOL, 0));
    }

    public static void showIDStackToolWindow(BoolArg open) {
        var pOpen = frameArena().allocate(cimgui_h.C_BOOL);
        pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        cimgui_h.igShowIDStackToolWindow(pOpen);
        open.set(pOpen.get(cimgui_h.C_BOOL, 0));
    }

    public static void showAboutWindow(BoolArg open) {
        var pOpen = frameArena().allocate(cimgui_h.C_BOOL);
        pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        cimgui_h.igShowAboutWindow(pOpen);
        open.set(pOpen.get(cimgui_h.C_BOOL, 0));
    }

    public static void showStyleEditor() {
        showStyleEditor(MemorySegment.NULL);
    }

    /**
     * NOTE: There aren't many tidy options for passing big structs like {@link ImGuiStyle},
     * so the best option for now is to just let users build the MemorySegment themselves and pass it through.
     * In practice typical style usage uses {@code pushStyleColor} and {@code pushStyleVar}, which are already tidy.
     */
    public static void showStyleEditor(MemorySegment imGuiStyle) {
        cimgui_h.igShowStyleEditor(imGuiStyle);
    }

    public static void showStyleSelector(String label) {
        cimgui_h.igShowStyleSelector(frameArena().allocateFrom(label));
    }

    public static void showFontSelector(String label) {
        cimgui_h.igShowFontSelector(frameArena().allocateFrom(label));
    }

    public static void showUserGuide() {
        cimgui_h.igShowUserGuide();
    }

    public static String getVersion() {
        var versionPtr = cimgui_h.igGetVersion();
        return versionPtr.getString(0);
    }
    //endregion

    // =================================================================================================================
    // region Styles
    // =================================================================================================================

    public static void styleColorsDark() {
        styleColorsDark(MemorySegment.NULL);
    }

    public static void styleColorsDark(MemorySegment imGuiStyle) {
        cimgui_h.igStyleColorsDark(imGuiStyle);
    }

    public static void styleColorsLight() {
        styleColorsLight(MemorySegment.NULL);
    }

    public static void styleColorsLight(MemorySegment imGuiStyle) {
        cimgui_h.igStyleColorsLight(imGuiStyle);
    }

    public static void styleColorsClassic() {
        styleColorsClassic(MemorySegment.NULL);
    }

    public static void styleColorsClassic(MemorySegment imGuiStyle) {
        cimgui_h.igStyleColorsClassic(imGuiStyle);
    }
    //endregion

    // =================================================================================================================
    // region Windows
    // - Begin() = push window to the stack and start appending to it. End() = pop window from the stack.
    // - Passing 'bool* p_open != NULL' shows a window-closing widget in the upper-right corner of the window,
    //   which clicking will set the boolean to false when clicked.
    // - You may append multiple times to the same window during the same frame by calling Begin()/End() pairs multiple times.
    //   Some information such as 'flags' or 'p_open' will only be considered by the first call to Begin().
    // - Begin() return false to indicate the window is collapsed or fully clipped, so you may early out and omit submitting
    //   anything to the window. Always call a matching End() for each Begin() call, regardless of its return value!
    //   [Important: due to legacy reason, Begin/End and BeginChild/EndChild are inconsistent with all other functions
    //    such as BeginMenu/EndMenu, BeginPopup/EndPopup, etc. where the EndXXX call should only be called if the corresponding
    //    BeginXXX function returned true. Begin and BeginChild are the only odd ones out. Will be fixed in a future update.]
    // - Note that the bottom of window stack always contains a window called "Debug".
    // =================================================================================================================

    public static boolean begin(String name) {
        return begin(name, null, 0);
    }

    public static boolean begin(String name, BoolArg open, int windowFlags) {
        var pOpen = MemorySegment.NULL;
        if (open != null) {
            pOpen = frameArena().allocate(cimgui_h.C_BOOL);
            pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        }
        var result = cimgui_h.igBegin(frameArena().allocateFrom(name), pOpen, windowFlags);
        if (open != null) {
            open.set(pOpen.get(cimgui_h.C_BOOL, 0));
        }
        return result;
    }

    public static void end() {
        cimgui_h.igEnd();
    }
    //endregion

    // =================================================================================================================
    // region Child Windows
    // - Use child windows to begin into a self-contained independent scrolling/clipping regions within a host window. Child windows can embed their own child.
    // - Before 1.90 (November 2023), the "ImGuiChildFlags child_flags = 0" parameter was "bool border = false".
    //   This API is backward compatible with old code, as we guarantee that ImGuiChildFlags_Borders == true.
    //   Consider updating your old code:
    //      BeginChild("Name", size, false)   -> Begin("Name", size, 0); or Begin("Name", size, ImGuiChildFlags_None);
    //      BeginChild("Name", size, true)    -> Begin("Name", size, ImGuiChildFlags_Borders);
    // - Manual sizing (each axis can use a different setting e.g. ImVec2(0.0f, 400.0f)):
    //     == 0.0f: use remaining parent window size for this axis.
    //      > 0.0f: use specified size for this axis.
    //      < 0.0f: right/bottom-align to specified distance from available content boundaries.
    // - Specifying ImGuiChildFlags_AutoResizeX or ImGuiChildFlags_AutoResizeY makes the sizing automatic based on child contents.
    //   Combining both ImGuiChildFlags_AutoResizeX _and_ ImGuiChildFlags_AutoResizeY defeats purpose of a scrolling region and is NOT recommended.
    // - BeginChild() returns false to indicate the window is collapsed or fully clipped, so you may early out and omit submitting
    //   anything to the window. Always call a matching EndChild() for each BeginChild() call, regardless of its return value.
    //   [Important: due to legacy reason, Begin/End and BeginChild/EndChild are inconsistent with all other functions
    //    such as BeginMenu/EndMenu, BeginPopup/EndPopup, etc. where the EndXXX call should only be called if the corresponding
    //    BeginXXX function returned true. Begin and BeginChild are the only odd ones out. Will be fixed in a future update.]
    // =================================================================================================================

    public static boolean beginChild(int id, float width, float height) {
        return beginChild(id, width, height, 0, 0);
    }

    public static boolean beginChild(int id, float width, float height, int childFlags, int windowFlags) {
        var size = imVec2(width, height);
        return cimgui_h.igBeginChild_ID(id, size, childFlags, windowFlags);
    }

    public static boolean beginChild(String id, float width, float height) {
        return beginChild(id, width, height, 0, 0);
    }

    public static boolean beginChild(String id, float width, float height, int childFlags, int windowFlags) {
        var size = imVec2(width, height);
        return cimgui_h.igBeginChild_Str(frameArena().allocateFrom(id), size, childFlags, windowFlags);
    }

    public static void endChild() {
        cimgui_h.igEndChild();
    }
    //endregion

    // =================================================================================================================
    // region Window Utilities
    // - 'current window' = the window we are appending into while inside a Begin()/End() block. 'next window' = next window we will Begin() into.
    // =================================================================================================================

    public static boolean isWindowAppearing() {
        return cimgui_h.igIsWindowAppearing();
    }

    public static boolean isWindowCollapsed() {
        return cimgui_h.igIsWindowCollapsed();
    }

    /**
     * Is current window focused?
     */
    public static boolean isWindowFocused() {
        return isWindowFocused(0);
    }

    /**
     * Is current window focused? or its root/child, depending on flags. see flags for options.
     * @param focusedFlags {@code ImGuiFocusedFlags}
     */
    public static boolean isWindowFocused(int focusedFlags) {
        return cimgui_h.igIsWindowFocused(focusedFlags);
    }

    /**
     * Is current window hovered and hoverable (e.g. not blocked by a popup/modal)?
     * IMPORTANT: If you are trying to check whether your mouse should be dispatched to Dear ImGui or to your underlying app,
     * you should not use this function! Use the 'io.WantCaptureMouse' boolean for that!
     * Refer to FAQ entry "How can I tell whether to dispatch mouse/keyboard to Dear ImGui or my application?" for details.
     */
    public static boolean isWindowHovered() {
        return isWindowHovered(0);
    }

    /**
     * Is current window hovered and hoverable (e.g. not blocked by a popup/modal)? See ImGuiHoveredFlags_ for options.
     * IMPORTANT: If you are trying to check whether your mouse should be dispatched to Dear ImGui or to your underlying app,
     * you should not use this function! Use the 'io.WantCaptureMouse' boolean for that!
     * Refer to FAQ entry "How can I tell whether to dispatch mouse/keyboard to Dear ImGui or my application?" for details.
     * @param hoveredFlags {@code ImGuiHoveredFlags}
     */
    public static boolean isWindowHovered(int hoveredFlags) {
        return cimgui_h.igIsWindowHovered(hoveredFlags);
    }

    /**
     * Get draw list associated to the current window, to append your own drawing primitives
     * TODO: might eventually be worth building a DrawList wrapper, the API is large but covering a decent chunk is manageable
     * - addLine
     * - addRect / addRectFilled / addRectFilledMultiColor
     * - addQuad / addQuadFilled
     * - addTriangle / addTriangleFilled
     * - addCircle / addCircleFilled
     * - addNgon / addNgonFilled
     * - addEllipse / addEllipseFilled
     * - addBezierCubic / addBezierQuadratic
     * - addText                             - use _Vec2 variant, skip _FontPtr for now
     * - addImage / addImageRounded          - needs ImTextureRef — skip until textures are handled
     * - pushClipRect / popClipRect / pushClipRectFullScreen
     * - path API; pathClear, pathLineTo, pathArcTo, pathBezierCubicCurveTo, pathBezierQuadraticCurveTo, pathRect, pathStroke, pathFillConvex, pathFillConcave
     * - Prim* (raw vertex buffer access), Channels* (draw order layering), addCallback
     */
    //public static ImDrawList* getWindowDrawList() {
    //    ...
    //}

    /**
     * Get current window position in screen space.
     * <strong>IT IS UNLIKELY YOU EVER NEED TO USE THIS.</strong>
     * Consider always using {@code GetCursorScreenPos()} and {@code GetContentRegionAvail()} instead.
     */
    public static Vec2f getWindowPos() {
        var result = cimgui_h.igGetWindowPos(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /**
     * Get current window size
     * <strong>IT IS UNLIKELY YOU EVER NEED TO USE THIS.</strong>
     * Consider always using {@code GetCursorScreenPos()} and {@code GetContentRegionAvail()} instead.
     */
    public static Vec2f getWindowSize() {
        var result = cimgui_h.igGetWindowSize(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /**
     * Get current window width
     * <strong>IT IS UNLIKELY YOU EVER NEED TO USE THIS.</strong>
     * Shortcut for {@code getWindowSize().x}
     */
    public static float getWindowWidth() {
        return cimgui_h.igGetWindowWidth();
    }

    /**
     * Get current window height
     * <strong>IT IS UNLIKELY YOU EVER NEED TO USE THIS.</strong>
     * Shortcut for {@code getWindowSize().y}
     */
    public static float getWindowHeight() {
        return cimgui_h.igGetWindowHeight();
    }
    //endregion

    // =================================================================================================================
    // region Window manipulation
    // - Prefer using SetNextXXX functions (before Begin) rather that SetXXX functions (after Begin).
    // =================================================================================================================

    /**
     * Set next window position. call before {@link #begin}.
     */
    public static void setNextWindowPos(float x, float y) {
        setNextWindowPos(x, y, 0, 0, 0);
    }

    /**
     * Set next window position. call before Begin(). use pivot=(0.5f,0.5f) to center on given point, etc.
     */
    public static void setNextWindowPos(float x, float y, int cond, float pivotX, float pivotY) {
        var pos = imVec2(x, y);
        var pivot = imVec2(pivotX, pivotY);
        cimgui_h.igSetNextWindowPos(pos, cond, pivot);
    }

    /**
     * Set next window size. Call before {@link #begin}
     */
    public static void setNextWindowSize(float width, float height) {
        setNextWindowSize(width, height, 0);
    }

    /**
     * Set next window size. set axis to 0.0f to force an auto-fit on this axis. call before {@link #begin}
     */
    public static void setNextWindowSize(float width, float height, int cond) {
        var size = imVec2(width, height);
        cimgui_h.igSetNextWindowSize(size, cond);
    }

    /**
     * Set next window size limits. use 0.0f or FLT_MAX if you don't want limits.
     * Use -1 for both min and max of same axis to preserve current size (which itself is a constraint).
     * Use callback to apply non-trivial programmatic constraints. (not currently implemented)
     */
    public static void setNextWindowSizeConstraints(float sizeMinX, float sizeMinY, float sizeMaxX, float sizeMaxY) {
        var sizeMin = imVec2(sizeMinX, sizeMinY);
        var sizeMax = imVec2(sizeMaxX, sizeMaxY);
        cimgui_h.igSetNextWindowSizeConstraints(sizeMin, sizeMax, MemorySegment.NULL, MemorySegment.NULL);
    }

    /**
     * Set next window content size (~ scrollable client area, which enforce the range of scrollbars).
     * Not including window decorations (title bar, menu bar, etc.) nor WindowPadding.
     * set an axis to 0.0f to leave it automatic. call before Begin()
     */
    public static void setNextWindowContentSize(float width, float height) {
        var size = imVec2(width, height);
        cimgui_h.igSetNextWindowContentSize(size);
    }

    /**
     * Set next window collapsed state. call before Begin()
     */
    public static void setNextWindowCollapsed(boolean collapsed) {
        setNextWindowCollapsed(collapsed, 0);
    }

    /**
     * Set next window collapsed state. call before Begin()
     */
    public static void setNextWindowCollapsed(boolean collapsed, int cond) {
        cimgui_h.igSetNextWindowCollapsed(collapsed, cond);
    }

    /**
     * Set next window to be focused / top-most. call before Begin()
     */
    public static void setNextWindowFocus() {
        cimgui_h.igSetNextWindowFocus();
    }

    /**
     * Set next window scrolling value (use < 0.0f to not affect a given axis).
     */
    public static void setNextWindowScroll(float scrollX, float scrollY) {
        var scroll = imVec2(scrollX, scrollY);
        cimgui_h.igSetNextWindowScroll(scroll);
    }

    /**
     * Set next window background color alpha.
     * helper to easily override the Alpha component of ImGuiCol_WindowBg/ChildBg/PopupBg.
     * you may also use ImGuiWindowFlags_NoBackground.
     */
    public static void setNextWindowBgAlpha(float alpha) {
        cimgui_h.igSetNextWindowBgAlpha(alpha);
    }

    /**
     * <strong>(not recommended)</strong> set current window position - call within Begin()/End().
     * prefer using SetNextWindowPos(), as this may incur tearing and side-effects.
     */
    public static void setWindowPos(float x, float y) {
        setWindowPos(x, y, 0);
    }

    /**
     * <strong>(not recommended)</strong> set current window position - call within Begin()/End().
     * prefer using SetNextWindowPos(), as this may incur tearing and side-effects.
     */
    public static void setWindowPos(float x, float y, int cond) {
        var pos = imVec2(x, y);
        cimgui_h.igSetWindowPos_Vec2(pos, cond);
    }

    /**
     * <strong>(not recommended)</strong> set current window size - call within Begin()/End().
     * set to ImVec2(0, 0) to force an auto-fit. prefer using SetNextWindowSize(), as this may incur tearing and minor side-effects.
     */
    public static void setWindowSize(float width, float height) {
        setWindowSize(width, height, 0);
    }

    /**
     * <strong>(not recommended)</strong> set current window size - call within Begin()/End().
     * set to ImVec2(0, 0) to force an auto-fit. prefer using SetNextWindowSize(), as this may incur tearing and minor side-effects.
     */
    public static void setWindowSize(float width, float height, int cond) {
        var size = imVec2(width, height);
        cimgui_h.igSetWindowSize_Vec2(size, cond);
    }

    /**
     * <strong>(not recommended)</strong> set current window collapsed state. prefer using SetNextWindowCollapsed().
     */
    public static void setWindowCollapsed(boolean collapsed) {
        setWindowCollapsed(collapsed, 0);
    }

    /**
     * <strong>(not recommended)</strong> set current window collapsed state. prefer using SetNextWindowCollapsed().
     */
    public static void setWindowCollapsed(boolean collapsed, int cond) {
        cimgui_h.igSetWindowCollapsed_Bool(collapsed, cond);
    }

    /**
     * <strong>(not recommended)</strong> set current window to be focused / top-most. prefer using SetNextWindowFocus().
     */
    public static void setWindowFocus() {
        cimgui_h.igSetWindowFocus_Nil();
    }

    /**
     * Set named window position
     */
    public static void setWindowPos(String name, float x, float y) {
        setWindowPos(name, x, y, 0);
    }

    /**
     * Set named window position
     */
    public static void setWindowPos(String name, float x, float y, int cond) {
        var pos = imVec2(x, y);
        cimgui_h.igSetWindowPos_Str(frameArena().allocateFrom(name), pos, cond);
    }

    /**
     * Set named window size. Set to ImVec2(0, 0) to force an auto-fit.
     */
    public static void setWindowSize(String name, float width, float height) {
        setWindowSize(name, width, height, 0);
    }

    /**
     * Set named window size. Set to ImVec2(0, 0) to force an auto-fit.
     */
    public static void setWindowSize(String name, float width, float height, int cond) {
        var size = imVec2(width, height);
        cimgui_h.igSetWindowSize_Str(frameArena().allocateFrom(name), size, cond);
    }

    /**
     * Set named window collapsed state.
     */
    public static void setWindowCollapsed(String name, boolean collapsed) {
        setWindowCollapsed(name, collapsed, 0);
    }

    /**
     * Set named window collapsed state.
     */
    public static void setWindowCollapsed(String name, boolean collapsed, int cond) {
        cimgui_h.igSetWindowCollapsed_Str(frameArena().allocateFrom(name), collapsed, cond);
    }

    /**
     * Set named window to be focused / top-most.
     */
    public static void setWindowFocus(String name) {
        cimgui_h.igSetWindowFocus_Str(frameArena().allocateFrom(name));
    }
    // endregion

    // =================================================================================================================
    // region Windows Scrolling
    // - Any change of Scroll will be applied at the beginning of next frame in the first call to Begin().
    // - You may instead use SetNextWindowScroll() prior to calling Begin() to avoid this delay, as an alternative to using SetScrollX()/SetScrollY().
    // =================================================================================================================

    /**
     * Get scrolling amount [0 .. GetScrollMaxX()]
     */
    public static float getScrollX() {
        return cimgui_h.igGetScrollX();
    }

    /**
     * Get scrolling amount [0 .. GetScrollMaxY()]
     */
    public static float getScrollY() {
        return cimgui_h.igGetScrollY();
    }

    /**
     * Set scrolling amount [0 .. GetScrollMaxX()]
     */
    public static void setScrollX(float scroll_x) {
        cimgui_h.igSetScrollX_Float(scroll_x);
    }

    /**
     * Set scrolling amount [0 .. GetScrollMaxY()]
     */
    public static void setScrollY(float scroll_y) {
        cimgui_h.igSetScrollY_Float(scroll_y);
    }

    /**
     * Get maximum scrolling amount ~~ ContentSize.x - WindowSize.x - DecorationsSize.x
     */
    public static float getScrollMaxX() {
        return cimgui_h.igGetScrollMaxX();
    }

    /**
     * Get maximum scrolling amount ~~ ContentSize.y - WindowSize.y - DecorationsSize.y
     */
    public static float getScrollMaxY() {
        return cimgui_h.igGetScrollMaxY();
    }

    /**
     * Adjust scrolling amount to make current cursor position visible
     * When using to make a "default/current item" visible, consider using SetItemDefaultFocus() instead.
     */
    public static void setScrollHereX() {
        setScrollHereX(0.5f);
    }

    /**
     * Adjust scrolling amount to make current cursor position visible. center_x_ratio=0.0: left, 0.5: center, 1.0: right.
     * When using to make a "default/current item" visible, consider using SetItemDefaultFocus() instead.
     */
    public static void setScrollHereX(float centerXRatio) {
        cimgui_h.igSetScrollHereX(centerXRatio);
    }

    /**
     * Adjust scrolling amount to make current cursor position visible.
     * When using to make a "default/current item" visible, consider using SetItemDefaultFocus() instead.
     */
    public static void setScrollHereY() {
        setScrollHereY(0.5f);
    }

    /**
     * Adjust scrolling amount to make current cursor position visible. center_y_ratio=0.0: top, 0.5: center, 1.0: bottom.
     * When using to make a "default/current item" visible, consider using SetItemDefaultFocus() instead.
     */
    public static void setScrollHereY(float centerYRatio) {
        cimgui_h.igSetScrollHereY(centerYRatio);
    }

    /**
     * Adjust scrolling amount to make given position visible. Generally GetCursorStartPos() + offset to compute a valid position.
     */
    public static void setScrollFromPosX(float localX) {
        setScrollFromPosX(localX, 0.5f);
    }

    /**
     * Adjust scrolling amount to make given position visible. Generally GetCursorStartPos() + offset to compute a valid position.
     */
    public static void setScrollFromPosX(float localX, float centerXRatio) {
        cimgui_h.igSetScrollFromPosX_Float(localX, centerXRatio);
    }

    /**
     * Adjust scrolling amount to make given position visible. Generally GetCursorStartPos() + offset to compute a valid position.
     */
    public static void setScrollFromPosY(float localY) {
        setScrollFromPosY(localY, 0.5f);
    }

    /**
     * Adjust scrolling amount to make given position visible. Generally GetCursorStartPos() + offset to compute a valid position.
     */
    public static void setScrollFromPosY(float localY, float centerYRatio) {
        cimgui_h.igSetScrollFromPosY_Float(localY, centerYRatio);
    }
    //endregion

    // =================================================================================================================
    // region TODO: Parameters stacks (font)
    //  - PushFont(font, 0.0f)                       // Change font and keep current size
    //  - PushFont(NULL, 20.0f)                      // Keep font and change current size
    //  - PushFont(font, 20.0f)                      // Change font and set size to 20.0f
    //  - PushFont(font, style.FontSizeBase * 2.0f)  // Change font and set size to be twice bigger than current size.
    //  - PushFont(font, font->LegacySize)           // Change font and set size to size passed to AddFontXXX() function. Same as pre-1.92 behavior.
    // *IMPORTANT* before 1.92, fonts had a single size. They can now be dynamically be adjusted.
    //  - In 1.92 we have REMOVED the single parameter version of PushFont() because it seems like the easiest way to provide an error-proof transition.
    //  - PushFont(font) before 1.92 = PushFont(font, font->LegacySize) after 1.92          // Use default font size as passed to AddFontXXX() function.
    // *IMPORTANT* global scale factors are applied over the provided size.
    //  - Global scale factors are: 'style.FontScaleMain', 'style.FontScaleDpi' and maybe more.
    // -  If you want to apply a factor to the _current_ font size:
    //  - CORRECT:   PushFont(NULL, style.FontSizeBase)         // use current unscaled size    == does nothing
    //  - CORRECT:   PushFont(NULL, style.FontSizeBase * 2.0f)  // use current unscaled size x2 == make text twice bigger
    //  - INCORRECT: PushFont(NULL, GetFontSize())              // INCORRECT! using size after global factors already applied == GLOBAL SCALING FACTORS WILL APPLY TWICE!
    //  - INCORRECT: PushFont(NULL, GetFontSize() * 2.0f)       // INCORRECT! using size after global factors already applied == GLOBAL SCALING FACTORS WILL APPLY TWICE!
    // =================================================================================================================

//    IMGUI_API void          PushFont(ImFont* font, float font_size_base_unscaled);          // Use NULL as a shortcut to keep current font. Use 0.0f to keep current size.
//    IMGUI_API void          PopFont();
//    IMGUI_API ImFont*       GetFont();                                                      // get current font
//    IMGUI_API float         GetFontSize();                                                  // get current scaled font size (= height in pixels). AFTER global scale factors applied. *IMPORTANT* DO NOT PASS THIS VALUE TO PushFont()! Use ImGui::GetStyle().FontSizeBase to get value before global scale factors.
//    IMGUI_API ImFontBaked*  GetFontBaked();                                                 // get current font bound at current size // == GetFont()->GetFontBaked(GetFontSize())
    // endregion

    // =================================================================================================================
    // region Parameters stacks (shared)
    // =================================================================================================================

    /**
     * Modify a style color. always use this if you modify the style after NewFrame().
     */
    public static void pushStyleColor(int imGuiColIdx, int color) {
        cimgui_h.igPushStyleColor_U32(imGuiColIdx, color);
    }

    /**
     * Modify a style color. always use this if you modify the style after NewFrame().
     */
    public static void pushStyleColor(int imGuiColIdx, float r, float g, float b, float a) {
        var color = imVec4(r, g, b, a);
        cimgui_h.igPushStyleColor_Vec4(imGuiColIdx, color);
    }

    /**
     * Pop one modified style color off the stack (pushed via {@link #pushStyleColor})
     */
    public static void popStyleColor() {
        popStyleColor(1);
    }

    /**
     * Pop {@code count} modified style colors off the stack (pushed via {@link #pushStyleColor})
     */
    public static void popStyleColor(int count) {
        cimgui_h.igPopStyleColor(count);
    }

    /**
     * Modify a style float variable. always use this if you modify the style after NewFrame()!
     */
    public static void pushStyleVar(int imGuiStyleVarIdx, float val) {
        cimgui_h.igPushStyleVar_Float(imGuiStyleVarIdx, val);
    }

    /**
     * Modify a style ImVec2 variable.
     */
    public static void pushStyleVar(int imGuiStyleVarIdx, float valX, float valY) {
        var val = imVec2(valX, valY);
        cimgui_h.igPushStyleVar_Vec2(imGuiStyleVarIdx, val);
    }

    /**
     * Modify X component of a style ImVec2 variable.
     */
    public static void pushStyleVarX(int imGuiStyleVarIdx, float valX) {
        cimgui_h.igPushStyleVarX(imGuiStyleVarIdx, valX);
    }

    /**
     * Modify Y component of a style ImVec2 variable.
     */
    public static void pushStyleVarY(int imGuiStyleVarIdx, float valY) {
        cimgui_h.igPushStyleVarY(imGuiStyleVarIdx, valY);
    }

    /**
     * Pop the last style var off the stack (pushed via {@link #pushStyleVar})
     */
    public static void popStyleVar() {
        popStyleVar(1);
    }

    /**
     * Pop the last {@code count} style vars off the stack (pushed via {@link #pushStyleVar})
     */
    public static void popStyleVar(int count) {
        cimgui_h.igPopStyleVar(count);
    }

    /**
     * Modify specified shared item flag, e.g. PushItemFlag(ImGuiItemFlags_NoTabStop, true)
     */
    public static void pushItemFlag(int imGuiItemFlagsOption, boolean enabled) {
        cimgui_h.igPushItemFlag(imGuiItemFlagsOption, enabled);
    }

    /**
     * Pop the last item flag pushed by {@link #pushItemFlag} off the stack.
     */
    public static void popItemFlag() {
        cimgui_h.igPopItemFlag();
    }
    // endregion

    // =================================================================================================================
    // region Parameters stacks (current window)
    // =================================================================================================================

    /**
     * Push width of items for common large "item+label" widgets.
     * >0.0f: width in pixels,
     * <0.0f align xx pixels to the right of window (so -FLT_MIN always align width to the right side).
     */
    public static void pushItemWidth(float itemWidth) {
        cimgui_h.igPushItemWidth(itemWidth);
    }

    /**
     * Pop the last item width off the stack (pushed via {@link #pushItemWidth(float)})
     */
    public static void popItemWidth() {
        cimgui_h.igPopItemWidth();
    }

    /**
     * Set width of the _next_ common large "item+label" widget.
     * >0.0f: width in pixels,
     * <0.0f align xx pixels to the right of window (so -FLT_MIN always align width to the right side)
     */
    public static void setNextItemWidth(float itemWidth) {
        cimgui_h.igSetNextItemWidth(itemWidth);
    }

    /**
     * Width of item given pushed settings and current cursor position.
     * NOT necessarily the width of last item unlike most 'Item' functions.
     */
    public static float calcItemWidth() {
        return cimgui_h.igCalcItemWidth();
    }

    /**
     * Push word-wrapping position for Text*() commands. Wrap to end of window (or column).
     */
    public static void pushTextWrapPos() {
        pushTextWrapPos(0f);
    }

    /**
     * Push word-wrapping position for Text*() commands.
     * - < 0.0f: no wrapping;
     * - 0.0f: wrap to end of window (or column);
     * - > 0.0f: wrap at 'wrap_pos_x' position in window local space
     */
    public static void pushTextWrapPos(float wrapLocalPosX) {
        cimgui_h.igPushTextWrapPos(wrapLocalPosX);
    }

    /**
     * Pop the last text wrap position off the stack (pushed via {@link #pushTextWrapPos})
     */
    public static void popTextWrapPos() {
        cimgui_h.igPopTextWrapPos();
    }
    // endregion

    // =================================================================================================================
    // region Style read access
    // - Use the ShowStyleEditor() function to interactively see/edit the colors.
    // =================================================================================================================

    /**
     * Get UV coordinate for a white pixel, useful to draw custom shapes via the ImDrawList API
     */
    public static Vec2f getFontTexUvWhitePixel() {
        var result = cimgui_h.igGetFontTexUvWhitePixel(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /**
     * Retrieve given style color with style alpha applied.
     */
    public static int getColorU32(int imGuiColIdx) {
        return getColorU32(imGuiColIdx, 1f);
    }

    /**
     * Retrieve given style color with style alpha applied and optional extra alpha multiplier, packed as a 32-bit value suitable for ImDrawList
     */
    public static int getColorU32(int imGuiColIdx, float alphaMul) {
        return cimgui_h.igGetColorU32_Col(imGuiColIdx, alphaMul);
    }

    /**
     * Retrieve given color with style alpha applied, packed as a 32-bit value suitable for ImDrawList
     */
    public static int getColorU32(float r, float g, float b, float a) {
        return cimgui_h.igGetColorU32_Vec4(imVec4(r, g, b, a));
    }

    /**
     * Retrieve given color with style alpha applied, packed as a 32-bit value suitable for ImDrawList
     */
    public static int getColorU32_Col(int col) {
        return getColorU32_Col(col, 1f);
    }

    /**
     * Retrieve given color with style alpha applied, packed as a 32-bit value suitable for ImDrawList
     */
    public static int getColorU32_Col(int col, float alphaMul) {
        return cimgui_h.igGetColorU32_Col(col, alphaMul);
    }

    /**
     * Retrieve style color as stored in ImGuiStyle structure.
     * use to feed back into PushStyleColor(), otherwise use GetColorU32() to get style color with style alpha baked in.
     */
    public static Vec4f getStyleColorVec4(int imGuiColIdx) {
        var result = cimgui_h.igGetStyleColorVec4(imGuiColIdx);
        return new Vec4f(ImVec4_c.x(result), ImVec4_c.y(result), ImVec4_c.z(result), ImVec4_c.w(result));
    }
    // endregion

    // =================================================================================================================
    // region Layout cursor positioning
    // - By "cursor" we mean the current output position.
    // - The typical widget behavior is to output themselves at the current cursor position, then move the cursor one line down.
    // - You can call SameLine() between widgets to undo the last carriage return and output at the right of the preceding widget.
    // - YOU CAN DO 99% OF WHAT YOU NEED WITH ONLY GetCursorScreenPos() and GetContentRegionAvail().
    // - Attention! We currently have inconsistencies between window-local and absolute positions we will aim to fix with future API:
    //    - Absolute coordinate:        GetCursorScreenPos(), SetCursorScreenPos(), all ImDrawList:: functions. -> this is the preferred way forward.
    //    - Window-local coordinates:   SameLine(offset), GetCursorPos(), SetCursorPos(), GetCursorStartPos(), PushTextWrapPos()
    //    - Window-local coordinates:   GetContentRegionMax(), GetWindowContentRegionMin(), GetWindowContentRegionMax() --> all obsoleted. YOU DON'T NEED THEM.
    // - GetCursorScreenPos() = GetCursorPos() + GetWindowPos(). GetWindowPos() is almost only ever useful to convert from window-local to absolute coordinates. Try not to use it.
    // =================================================================================================================

    /**
     * Cursor position, absolute coordinates.
     * <strong>THIS IS YOUR BEST FRIEND.</strong>
     * Prefer using this rather than {@code GetCursorPos()}, also more useful to work with {@code ImDrawList} API.
     */
    public static Vec2f getCursorScreenPos() {
        var result = cimgui_h.igGetCursorScreenPos(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /**
     * Cursor position, absolute coordinates.
     * <strong>THIS IS YOUR BEST FRIEND.</strong>
     */
    public static void setCursorScreenPos(float x, float y) {
        var pos = imVec2(x, y);
        cimgui_h.igSetCursorScreenPos(pos);
    }

    /**
     * Available space from current position.
     * <strong>THIS IS YOUR BEST FRIEND.</strong>
     */
    public static Vec2f getContentRegionAvail() {
        var result = cimgui_h.igGetContentRegionAvail(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /**
     * [window-local] Cursor position in window-local coordinates.
     * <strong>This is NOT your best friend.</strong>
     */
    public static Vec2f getCursorPos() {
        var result = cimgui_h.igGetCursorPos(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /**
     * [window-local] Cursor position x-coordinate in window-local coordinates.
     * <strong>This is NOT your best friend.</strong>
     */
    public static float getCursorPosX() {
        return cimgui_h.igGetCursorPosX();
    }

    /**
     * [window-local] Cursor position y-coordinate in window-local coordinates.
     * <strong>This is NOT your best friend.</strong>
     */
    public static float getCursorPosY() {
        return cimgui_h.igGetCursorPosY();
    }

    /**
     * [window-local] Set cursor position in window-local coordinates.
     * <strong>This is NOT your best friend.</strong>
     */
    public static void setCursorPos(float windowLocalX, float windowLocalY) {
        var pos = imVec2(windowLocalX, windowLocalY);
        cimgui_h.igSetCursorPos(pos);
    }

    /**
     * [window-local] Set cursor position x coordinate in window-local coordinates.
     * <strong>This is NOT your best friend.</strong>
     */
    public static void setCursorPosX(float windowLocalX) {
        cimgui_h.igSetCursorPosX(windowLocalX);
    }

    /**
     * [window-local] Set cursor position y coordinate in window-local coordinates.
     * <strong>This is NOT your best friend.</strong>
     */
    public static void setCursorPosY(float windowLocalY) {
        cimgui_h.igSetCursorPosY(windowLocalY);
    }

    /**
     * [window-local] Initial cursor position, in window-local coordinates.
     * Call GetCursorScreenPos() after Begin() to get the absolute coordinates version.
     */
    public static Vec2f getCursorStartPos() {
        var result = cimgui_h.igGetCursorStartPos(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }
    // endregion

    // =================================================================================================================
    // region Other layout functions
    // =================================================================================================================

    /**
     * Separator, generally horizontal. inside a menu bar or in horizontal layout mode, this becomes a vertical separator.
     */
    public static void separator() {
        cimgui_h.igSeparator();
    }

    /**
     * Call between widgets or groups to layout them horizontally.
     */
    public static void sameLine() {
        sameLine(0, -1);
    }

    /**
     * Call between widgets or groups to layout them horizontally. X position given in window coordinates.
     */
    public static void sameLine(float offsetFromStartX, float spacing) {
        cimgui_h.igSameLine(offsetFromStartX, spacing);
    }

    /**
     * Undo a SameLine() or force a new line when in a horizontal-layout context.
     */
    public static void newLine() {
        cimgui_h.igNewLine();
    }

    /**
     * Add vertical spacing.
     */
    public static void spacing() {
        cimgui_h.igSpacing();
    }

    /**
     * Add a dummy item of given size. unlike InvisibleButton(), Dummy() won't take the mouse click or be navigable into.
     */
    public static void dummy(float width, float height) {
        var size = imVec2(width, height);
        cimgui_h.igDummy(size);
    }

    /**
     * Move content position toward the right, by indent_w, or style. IndentSpacing if indent_w <= 0
     */
    public static void indent() {
        indent(0);
    }

    /**
     * Move content position toward the right, by indent_w, or style. IndentSpacing if indent_w <= 0
     */
    public static void indent(float indentW) {
        cimgui_h.igIndent(indentW);
    }

    /**
     * Move content position back to the left, by indent_w, or style.IndentSpacing if indent_w <= 0
     */
    public static void unindent() {
        unindent(0);
    }

    /**
     * Move content position back to the left, by indent_w, or style.IndentSpacing if indent_w <= 0
     */
    public static void unindent(float indentW) {
        cimgui_h.igUnindent(indentW);
    }

    /**
     * Lock horizontal starting position
     */
    public static void beginGroup() {
        cimgui_h.igBeginGroup();
    }

    /**
     * Unlock horizontal starting position + capture the whole group bounding box into one "item"
     * (so you can use IsItemHovered() or layout primitives such as SameLine() on whole group, etc.)
     */
    public static void endGroup() {
        cimgui_h.igEndGroup();
    }

    /**
     * Vertically align upcoming text baseline to FramePadding.y so that it will align properly to regularly framed items
     * (call if you have text on a line before a framed item)
     */
    public static void alignTextToFramePadding() {
        cimgui_h.igAlignTextToFramePadding();
    }

    /**
     * ~ FontSize
     */
    public static float getTextLineHeight() {
        return cimgui_h.igGetTextLineHeight();
    }

    /**
     * ~ FontSize + style.ItemSpacing.y (distance in pixels between 2 consecutive lines of text)
     */
    public static float getTextLineHeightWithSpacing() {
        return cimgui_h.igGetTextLineHeightWithSpacing();
    }

    /**
     * ~ FontSize + style.FramePadding.y * 2
     */
    public static float getFrameHeight() {
        return cimgui_h.igGetFrameHeight();
    }

    /**
     * ~ FontSize + style.FramePadding.y * 2 + style.ItemSpacing.y (distance in pixels between 2 consecutive lines of framed widgets)
     */
    public static float getFrameHeightWithSpacing() {
        return cimgui_h.igGetFrameHeightWithSpacing();
    }
    // endregion

    // =================================================================================================================
    // region ID stack/scopes
    // Read the FAQ (docs/FAQ.md or http://dearimgui.com/faq) for more details about how ID are handled in dear imgui.
    // - Those questions are answered and impacted by understanding of the ID stack system:
    //   - "Q: Why is my widget not reacting when I click on it?"
    //   - "Q: How can I have widgets with an empty label?"
    //   - "Q: How can I have multiple widgets with the same label?"
    // - Short version: ID are hashes of the entire ID stack. If you are creating widgets in a loop you most likely
    //   want to push a unique identifier (e.g. object pointer, loop index) to uniquely differentiate them.
    // - You can also use the "Label##foobar" syntax within widget label to distinguish them from each others.
    // - In this header file we use the "label"/"name" terminology to denote a string that will be displayed + used as an ID,
    //   whereas "str_id" denote a string that is only used as an ID and not normally displayed.
    // =================================================================================================================

    /**
     * Push string into the ID stack (will hash string).
     */
    public static void pushID(String strId) {
        cimgui_h.igPushID_Str(frameArena().allocateFrom(strId));
    }

    /**
     * Push pointer into the ID stack (will hash pointer).
     * For java, this just uses the object identity hash so any java object can be a stable identifier.
     * TODO: In the future we'll likely have java wrappers for specific native-side objects,
     *  eg. ImDrawList, ImFont, ... and we'll add overloads for those wrappers.
     */
    public static void pushID(Object obj) {
        int id = System.identityHashCode(obj);
        cimgui_h.igPushID_Int(id);
    }

    /**
     * Push integer into the ID stack (will hash integer).
     */
    public static void pushID(int id) {
        cimgui_h.igPushID_Int(id);
    }

    /**
     * Pop from the ID stack.
     */
    public static void popID() {
        cimgui_h.igPopID();
    }

    /**
     * Calculate unique ID (hash of whole ID stack + given parameter). e.g. if you want to query into ImGuiStorage yourself
     */
    public static int getID(String strId) {
        return cimgui_h.igGetID_Str(frameArena().allocateFrom(strId));
    }

    /**
     * Get the imgui id for the specified object.
     * For java, this just uses the object identity hash so any java object can be a stable identifier.
     * TODO: In the future we'll likely have java wrappers for specific native-side objects,
     *  eg. ImDrawList, ImFont, ... and we'll add overloads for those wrappers.
     */
    public static int getID(Object obj) {
        int id = System.identityHashCode(obj);
        return getID(id);
    }

    /**
     * Get the imgui id associated with the specified id.
     */
    public static int getID(int intId) {
        return cimgui_h.igGetID_Int(intId);
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Text
    // - some functions only have variadic versions which require special handling
    // - jextract doesn't create 'nice' versions of variadic functions so we manually create invokers which are cached across calls
    // - since any '%' character in the provided format string args would cause problems due to attempted sprintf'ing, we defensively escape them
    // =================================================================================================================

    /**
     * Raw text without formatting. Roughly equivalent to Text("%s", text) but:
     * A) doesn't require null terminated string if 'text_end' is specified
     * B) it's faster, no memory copy is done, no buffer size limits, recommended for long chunks of text.
     */
    public static void textUnformatted(String text) {
        cimgui_h.igTextUnformatted(frameArena().allocateFrom(text), MemorySegment.NULL);
    }

    /**
     * Formatted text... except not really because we expect text formatting ala printf to be done in java.
     * So this is actually equivalent to {@link #textUnformatted(String)}.
     */
    public static void text(String text) {
        textUnformatted(text);
    }

    /**
     * Shortcut for PushStyleColor(ImGuiCol_Text, col); Text(fmt, ...); PopStyleColor();
     */
    public static void textColored(float r, float g, float b, float a, String text) {
        var color = imVec4(r, g, b, a);
        var safeText = text.replaceAll("%", "%%");
        TEXT_COLORED.apply(color, frameArena().allocateFrom(safeText));
    }

    /**
     * Shortcut for PushStyleColor(ImGuiCol_Text, style.Colors[ImGuiCol_TextDisabled]); Text(fmt, ...); PopStyleColor();
     */
    public static void textDisabled(String text) {
        var safeText = text.replaceAll("%", "%%");
        TEXT_DISABLED.apply(frameArena().allocateFrom(safeText));
    }

    /**
     * shortcut for PushTextWrapPos(0.0f); Text(fmt, ...); PopTextWrapPos();.
     * Note that this won't work on an auto-resizing window if there's no other widgets to extend the window width,
     * you may need to set a size using SetNextWindowSize().
     */
    public static void textWrapped(String text) {
        var safeText = text.replaceAll("%", "%%");
        TEXT_WRAPPED.apply(frameArena().allocateFrom(safeText));
    }

    /**
     * Display text+label aligned the same way as value+label widgets
     */
    public static void labelText(String label, String text) {
        var safeText = text.replaceAll("%", "%%");
        LABEL_TEXT.apply(frameArena().allocateFrom(label), frameArena().allocateFrom(safeText));
    }

    /**
     * Shortcut for Bullet()+Text()
     */
    public static void bulletText(String text) {
        var safeText = text.replaceAll("%", "%%");
        BULLET_TEXT.apply(frameArena().allocateFrom(safeText));
    }

    /**
     * Formatted text with a horizontal line
     */
    public static void separatorText(String label) {
        var safeText = label.replaceAll("%", "%%");
        cimgui_h.igSeparatorText(frameArena().allocateFrom(safeText));
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Main
    // - Most widgets return true when the value has been changed or when pressed/selected
    // - You may also use one of the many IsItemXXX functions (e.g. IsItemActive, IsItemHovered, etc.) to query widget state.
    // =================================================================================================================

    /**
     * Button
     */
    public static boolean button(String label, float width, float height) {
        var size = imVec2(width, height);
        return cimgui_h.igButton(frameArena().allocateFrom(label), size);
    }

    /**
     * Button with (FramePadding.y == 0) to easily embed within text
     */
    public static boolean smallButton(String label) {
        return cimgui_h.igSmallButton(frameArena().allocateFrom(label));
    }

    /**
     * Flexible button behavior without the visuals, frequently useful to build custom behaviors using the public api
     * (along with IsItemActive, IsItemHovered, etc.)
     */
    public static boolean invisibleButton(String id, float width, float height) {
        return invisibleButton(id, width, height, 0);
    }

    /**
     * Flexible button behavior without the visuals, frequently useful to build custom behaviors using the public api
     * (along with IsItemActive, IsItemHovered, etc.)
     */
    public static boolean invisibleButton(String id, float width, float height, int buttonFlags) {
        var size = imVec2(width, height);
        return cimgui_h.igInvisibleButton(frameArena().allocateFrom(id), size, buttonFlags);
    }

    /**
     * Square button with an arrow shape. 'dir' is an {@code ImGuiDir} flag for the arrow direction.
     */
    public static boolean arrowButton(String id, int dir) {
        return cimgui_h.igArrowButton(frameArena().allocateFrom(id), dir);
    }

    public static boolean checkbox(String label, BoolArg v) {
        var pV = frameArena().allocate(cimgui_h.C_BOOL);
        pV.set(cimgui_h.C_BOOL, 0, v.get());
        var clicked = cimgui_h.igCheckbox(frameArena().allocateFrom(label), pV);
        v.set(pV.get(cimgui_h.C_BOOL, 0));
        return clicked;
    }

    public static boolean checkboxFlags(String label, IntArg flags, int flagValue) {
        var pFlags = frameArena().allocate(cimgui_h.C_INT);
        pFlags.set(cimgui_h.C_INT, 0, flags.get());
        var clicked = cimgui_h.igCheckboxFlags_IntPtr(frameArena().allocateFrom(label), pFlags, flagValue);
        flags.set(pFlags.get(cimgui_h.C_INT, 0));
        return clicked;
    }

    public static boolean radioButton(String label, boolean active) {
        return cimgui_h.igRadioButton_Bool(frameArena().allocateFrom(label), active);
    }

    /**
     * Use with e.g. if (RadioButton("one", my_value==1)) { my_value = 1; }
     */
    public static boolean radioButton(String label, IntArg v, int vButton) {
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

    /**
     * Draw a small circle + keep the cursor on the same line.
     * advance cursor x position by GetTreeNodeToLabelSpacing(), same distance that TreeNode() uses
     */
    public static void bullet() {
        cimgui_h.igBullet();
    }

    /**
     * Hyperlink text button, return true when clicked
     */
    public static boolean textLink(String label) {
        return cimgui_h.igTextLink(frameArena().allocateFrom(label));
    }

    /**
     * Hyperlink text button, automatically open file/url when clicked
     */
    public static boolean textLinkOpenURL(String label, String url) {
        var urlStr = (url == null) ? MemorySegment.NULL : frameArena().allocateFrom(url);
        return cimgui_h.igTextLinkOpenURL(frameArena().allocateFrom(label), urlStr);
    }
    // endregion

    // =================================================================================================================
    // region TODO: Widgets: Images
    // - Read about ImTextureID/ImTextureRef  here: https://github.com/ocornut/imgui/wiki/Image-Loading-and-Displaying-Examples
    // - 'uv0' and 'uv1' are texture coordinates. Read about them from the same link above.
    // - Image() pads adds style.ImageBorderSize on each side, ImageButton() adds style.FramePadding on each side.
    // - ImageButton() draws a background based on regular Button() color + optionally an inner background if specified.
    // - An obsolete version of Image(), before 1.91.9 (March 2025), had a 'tint_col' parameter which is now supported by the ImageWithBg() function.
    // =================================================================================================================

//    IMGUI_API void          Image(ImTextureRef tex_ref, const ImVec2& image_size, const ImVec2& uv0 = ImVec2(0, 0), const ImVec2& uv1 = ImVec2(1, 1));
//    IMGUI_API void          ImageWithBg(ImTextureRef tex_ref, const ImVec2& image_size, const ImVec2& uv0 = ImVec2(0, 0), const ImVec2& uv1 = ImVec2(1, 1), const ImVec4& bg_col = ImVec4(0, 0, 0, 0), const ImVec4& tint_col = ImVec4(1, 1, 1, 1));
//    IMGUI_API bool          ImageButton(const char* str_id, ImTextureRef tex_ref, const ImVec2& image_size, const ImVec2& uv0 = ImVec2(0, 0), const ImVec2& uv1 = ImVec2(1, 1), const ImVec4& bg_col = ImVec4(0, 0, 0, 0), const ImVec4& tint_col = ImVec4(1, 1, 1, 1));
    // endregion

    // =================================================================================================================
    // region Widgets: Combo Box (Dropdown)
    // - The BeginCombo()/EndCombo() api allows you to manage your contents and selection state however you want it, by creating e.g. Selectable() items.
    // - The old Combo() api are helpers over BeginCombo()/EndCombo() which are kept available for convenience purpose.
    // =================================================================================================================

    /**
     * Open a combo popup. Only call {@link #endCombo()} if this returns true.
     */
    public static boolean beginCombo(String label, String previewValue) {
        return beginCombo(label, previewValue, 0);
    }

    /** Open a combo popup with flags. */
    public static boolean beginCombo(String label, String previewValue, int flags) {
        var arena = frameArena();
        return cimgui_h.igBeginCombo(arena.allocateFrom(label), arena.allocateFrom(previewValue), flags);
    }

    /** Only call endCombo() if beginCombo() returned true. */
    public static void endCombo() {
        cimgui_h.igEndCombo();
    }

    /**
     * Simple combo from a string array. Returns true when the selection changes.
     * @param currentItem in/out index of the selected item
     */
    public static boolean combo(String label, IntArg currentItem, String[] items) {
        return combo(label, currentItem, items, -1);
    }

    /**
     * Simple combo from a string array — full control.
     * @param popupMaxHeightInItems max visible items (-1 = default ~8)
     */
    public static boolean combo(String label, IntArg currentItem, String[] items, int popupMaxHeightInItems) {
        var arena = frameArena();
        var pCurrent = arena.allocate(cimgui_h.C_INT);
        pCurrent.set(cimgui_h.C_INT, 0, currentItem.get());

        var pItems = arena.allocate(cimgui_h.C_POINTER, items.length);
        for (int i = 0; i < items.length; i++) {
            pItems.setAtIndex(cimgui_h.C_POINTER, i, arena.allocateFrom(items[i]));
        }

        var changed = cimgui_h.igCombo_Str_arr(arena.allocateFrom(label), pCurrent, pItems, items.length, popupMaxHeightInItems);
        currentItem.set(pCurrent.get(cimgui_h.C_INT, 0));
        return changed;
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Drag Sliders
    // - Ctrl+Click on any drag box to turn them into an input box.
    // - v_speed=1.0f: mouse needs to move 1 pixel per unit. v_min >= v_max means no bounds.
    // - For Float2/3/4 and Int2/3/4: FloatArg/IntArg.value[] must have sufficient length.
    // =================================================================================================================

    /** Drag float. Returns true when value changed. If v_min >= v_max there is no bound. */
    public static boolean dragFloat(String label, FloatArg v) {
        return dragFloat(label, v, 1f, 0f, 0f, "%.3f", 0);
    }

    /** Drag float with speed and range. */
    public static boolean dragFloat(String label, FloatArg v, float speed, float min, float max) {
        return dragFloat(label, v, speed, min, max, "%.3f", 0);
    }

    /**
     * Drag float — full control.
     * @param flags {@code ImGuiSliderFlags}
     */
    public static boolean dragFloat(String label, FloatArg v, float speed, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT);
        pV.set(cimgui_h.C_FLOAT, 0, v.get());
        var changed = cimgui_h.igDragFloat(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_FLOAT, 0));
        return changed;
    }

    /** Drag 2-component float. {@code v.value} must have length >= 2. */
    public static boolean dragFloat2(String label, FloatArg v) {
        return dragFloat2(label, v, 1f, 0f, 0f, "%.3f", 0);
    }

    /** Drag 2-component float — full control. */
    public static boolean dragFloat2(String label, FloatArg v, float speed, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 2);
        pV.setAtIndex(cimgui_h.C_FLOAT, 0, v.value[0]);
        pV.setAtIndex(cimgui_h.C_FLOAT, 1, v.value[1]);
        var changed = cimgui_h.igDragFloat2(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        v.value[0] = pV.getAtIndex(cimgui_h.C_FLOAT, 0);
        v.value[1] = pV.getAtIndex(cimgui_h.C_FLOAT, 1);
        return changed;
    }

    /** Drag 3-component float. {@code v.value} must have length >= 3. */
    public static boolean dragFloat3(String label, FloatArg v) {
        return dragFloat3(label, v, 1f, 0f, 0f, "%.3f", 0);
    }

    /** Drag 3-component float — full control. */
    public static boolean dragFloat3(String label, FloatArg v, float speed, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 3);
        for (int i = 0; i < 3; i++) pV.setAtIndex(cimgui_h.C_FLOAT, i, v.value[i]);
        var changed = cimgui_h.igDragFloat3(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 3; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /** Drag 4-component float. {@code v.value} must have length >= 4. */
    public static boolean dragFloat4(String label, FloatArg v) {
        return dragFloat4(label, v, 1f, 0f, 0f, "%.3f", 0);
    }

    /** Drag 4-component float — full control. */
    public static boolean dragFloat4(String label, FloatArg v, float speed, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 4);
        for (int i = 0; i < 4; i++) pV.setAtIndex(cimgui_h.C_FLOAT, i, v.value[i]);
        var changed = cimgui_h.igDragFloat4(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 4; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /** Drag float range with separate min/max outputs. */
    public static boolean dragFloatRange2(String label, FloatArg vCurrentMin, FloatArg vCurrentMax) {
        return dragFloatRange2(label, vCurrentMin, vCurrentMax, 1f, 0f, 0f, "%.3f", null, 0);
    }

    /** Drag float range — full control. */
    public static boolean dragFloatRange2(String label, FloatArg vCurrentMin, FloatArg vCurrentMax, float speed, float vMin, float vMax, String format, String formatMax, int flags) {
        var arena = frameArena();
        var pMin = arena.allocate(cimgui_h.C_FLOAT); pMin.set(cimgui_h.C_FLOAT, 0, vCurrentMin.get());
        var pMax = arena.allocate(cimgui_h.C_FLOAT); pMax.set(cimgui_h.C_FLOAT, 0, vCurrentMax.get());
        var pFmtMax = formatMax != null ? arena.allocateFrom(formatMax) : MemorySegment.NULL;
        var changed = cimgui_h.igDragFloatRange2(arena.allocateFrom(label), pMin, pMax, speed, vMin, vMax, arena.allocateFrom(format), pFmtMax, flags);
        vCurrentMin.set(pMin.get(cimgui_h.C_FLOAT, 0));
        vCurrentMax.set(pMax.get(cimgui_h.C_FLOAT, 0));
        return changed;
    }

    /** Drag int. If v_min >= v_max there is no bound. */
    public static boolean dragInt(String label, IntArg v) {
        return dragInt(label, v, 1f, 0, 0, "%d", 0);
    }

    /** Drag int with speed and range. */
    public static boolean dragInt(String label, IntArg v, float speed, int min, int max) {
        return dragInt(label, v, speed, min, max, "%d", 0);
    }

    /**
     * Drag int — full control.
     * @param flags {@code ImGuiSliderFlags}
     */
    public static boolean dragInt(String label, IntArg v, float speed, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT);
        pV.set(cimgui_h.C_INT, 0, v.get());
        var changed = cimgui_h.igDragInt(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_INT, 0));
        return changed;
    }

    /** Drag 2-component int. {@code v.value} must have length >= 2. */
    public static boolean dragInt2(String label, IntArg v) {
        return dragInt2(label, v, 1f, 0, 0, "%d", 0);
    }

    /** Drag 2-component int — full control. */
    public static boolean dragInt2(String label, IntArg v, float speed, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 2);
        pV.setAtIndex(cimgui_h.C_INT, 0, v.value[0]);
        pV.setAtIndex(cimgui_h.C_INT, 1, v.value[1]);
        var changed = cimgui_h.igDragInt2(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        v.value[0] = pV.getAtIndex(cimgui_h.C_INT, 0);
        v.value[1] = pV.getAtIndex(cimgui_h.C_INT, 1);
        return changed;
    }

    /** Drag 3-component int. {@code v.value} must have length >= 3. */
    public static boolean dragInt3(String label, IntArg v) {
        return dragInt3(label, v, 1f, 0, 0, "%d", 0);
    }

    /** Drag 3-component int — full control. */
    public static boolean dragInt3(String label, IntArg v, float speed, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 3);
        for (int i = 0; i < 3; i++) pV.setAtIndex(cimgui_h.C_INT, i, v.value[i]);
        var changed = cimgui_h.igDragInt3(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 3; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_INT, i);
        return changed;
    }

    /** Drag 4-component int. {@code v.value} must have length >= 4. */
    public static boolean dragInt4(String label, IntArg v) {
        return dragInt4(label, v, 1f, 0, 0, "%d", 0);
    }

    /** Drag 4-component int — full control. */
    public static boolean dragInt4(String label, IntArg v, float speed, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 4);
        for (int i = 0; i < 4; i++) pV.setAtIndex(cimgui_h.C_INT, i, v.value[i]);
        var changed = cimgui_h.igDragInt4(arena.allocateFrom(label), pV, speed, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 4; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_INT, i);
        return changed;
    }

    /** Drag int range with separate min/max outputs. */
    public static boolean dragIntRange2(String label, IntArg vCurrentMin, IntArg vCurrentMax) {
        return dragIntRange2(label, vCurrentMin, vCurrentMax, 1f, 0, 0, "%d", null, 0);
    }

    /** Drag int range — full control. */
    public static boolean dragIntRange2(String label, IntArg vCurrentMin, IntArg vCurrentMax, float speed, int vMin, int vMax, String format, String formatMax, int flags) {
        var arena = frameArena();
        var pMin = arena.allocate(cimgui_h.C_INT); pMin.set(cimgui_h.C_INT, 0, vCurrentMin.get());
        var pMax = arena.allocate(cimgui_h.C_INT); pMax.set(cimgui_h.C_INT, 0, vCurrentMax.get());
        var pFmtMax = formatMax != null ? arena.allocateFrom(formatMax) : MemorySegment.NULL;
        var changed = cimgui_h.igDragIntRange2(arena.allocateFrom(label), pMin, pMax, speed, vMin, vMax, arena.allocateFrom(format), pFmtMax, flags);
        vCurrentMin.set(pMin.get(cimgui_h.C_INT, 0));
        vCurrentMax.set(pMax.get(cimgui_h.C_INT, 0));
        return changed;
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Regular Sliders
    // - Ctrl+Click on any slider to turn it into an input box.
    // - For Float2/3/4 and Int2/3/4: FloatArg/IntArg.value[] must have sufficient length.
    // =================================================================================================================

    /** Float slider. */
    public static boolean sliderFloat(String label, FloatArg v, float min, float max) {
        return sliderFloat(label, v, min, max, "%.3f", 0);
    }

    /**
     * Float slider — full control.
     * @param flags {@code ImGuiSliderFlags}
     */
    public static boolean sliderFloat(String label, FloatArg v, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT);
        pV.set(cimgui_h.C_FLOAT, 0, v.get());
        var changed = cimgui_h.igSliderFloat(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_FLOAT, 0));
        return changed;
    }

    /** 2-component float slider. {@code v.value} must have length >= 2. */
    public static boolean sliderFloat2(String label, FloatArg v, float min, float max) {
        return sliderFloat2(label, v, min, max, "%.3f", 0);
    }

    /** 2-component float slider — full control. */
    public static boolean sliderFloat2(String label, FloatArg v, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 2);
        pV.setAtIndex(cimgui_h.C_FLOAT, 0, v.value[0]);
        pV.setAtIndex(cimgui_h.C_FLOAT, 1, v.value[1]);
        var changed = cimgui_h.igSliderFloat2(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        v.value[0] = pV.getAtIndex(cimgui_h.C_FLOAT, 0);
        v.value[1] = pV.getAtIndex(cimgui_h.C_FLOAT, 1);
        return changed;
    }

    /** 3-component float slider. {@code v.value} must have length >= 3. */
    public static boolean sliderFloat3(String label, FloatArg v, float min, float max) {
        return sliderFloat3(label, v, min, max, "%.3f", 0);
    }

    /** 3-component float slider — full control. */
    public static boolean sliderFloat3(String label, FloatArg v, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 3);
        for (int i = 0; i < 3; i++) pV.setAtIndex(cimgui_h.C_FLOAT, i, v.value[i]);
        var changed = cimgui_h.igSliderFloat3(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 3; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /** 4-component float slider. {@code v.value} must have length >= 4. */
    public static boolean sliderFloat4(String label, FloatArg v, float min, float max) {
        return sliderFloat4(label, v, min, max, "%.3f", 0);
    }

    /** 4-component float slider — full control. */
    public static boolean sliderFloat4(String label, FloatArg v, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 4);
        for (int i = 0; i < 4; i++) pV.setAtIndex(cimgui_h.C_FLOAT, i, v.value[i]);
        var changed = cimgui_h.igSliderFloat4(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 4; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /** Angle slider. Input/output in radians, displayed in degrees. */
    public static boolean sliderAngle(String label, FloatArg vRad) {
        return sliderAngle(label, vRad, -360f, 360f, "%.0f deg", 0);
    }

    /** Angle slider — full control. */
    public static boolean sliderAngle(String label, FloatArg vRad, float vDegreesMin, float vDegreesMax, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT);
        pV.set(cimgui_h.C_FLOAT, 0, vRad.get());
        var changed = cimgui_h.igSliderAngle(arena.allocateFrom(label), pV, vDegreesMin, vDegreesMax, arena.allocateFrom(format), flags);
        vRad.set(pV.get(cimgui_h.C_FLOAT, 0));
        return changed;
    }

    /** Int slider. */
    public static boolean sliderInt(String label, IntArg v, int min, int max) {
        return sliderInt(label, v, min, max, "%d", 0);
    }

    /**
     * Int slider — full control.
     * @param flags {@code ImGuiSliderFlags}
     */
    public static boolean sliderInt(String label, IntArg v, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT);
        pV.set(cimgui_h.C_INT, 0, v.get());
        var changed = cimgui_h.igSliderInt(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_INT, 0));
        return changed;
    }

    /** 2-component int slider. {@code v.value} must have length >= 2. */
    public static boolean sliderInt2(String label, IntArg v, int min, int max) {
        return sliderInt2(label, v, min, max, "%d", 0);
    }

    /** 2-component int slider — full control. */
    public static boolean sliderInt2(String label, IntArg v, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 2);
        pV.setAtIndex(cimgui_h.C_INT, 0, v.value[0]);
        pV.setAtIndex(cimgui_h.C_INT, 1, v.value[1]);
        var changed = cimgui_h.igSliderInt2(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        v.value[0] = pV.getAtIndex(cimgui_h.C_INT, 0);
        v.value[1] = pV.getAtIndex(cimgui_h.C_INT, 1);
        return changed;
    }

    /** 3-component int slider. {@code v.value} must have length >= 3. */
    public static boolean sliderInt3(String label, IntArg v, int min, int max) {
        return sliderInt3(label, v, min, max, "%d", 0);
    }

    /** 3-component int slider — full control. */
    public static boolean sliderInt3(String label, IntArg v, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 3);
        for (int i = 0; i < 3; i++) pV.setAtIndex(cimgui_h.C_INT, i, v.value[i]);
        var changed = cimgui_h.igSliderInt3(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 3; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_INT, i);
        return changed;
    }

    /** 4-component int slider. {@code v.value} must have length >= 4. */
    public static boolean sliderInt4(String label, IntArg v, int min, int max) {
        return sliderInt4(label, v, min, max, "%d", 0);
    }

    /** 4-component int slider — full control. */
    public static boolean sliderInt4(String label, IntArg v, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 4);
        for (int i = 0; i < 4; i++) pV.setAtIndex(cimgui_h.C_INT, i, v.value[i]);
        var changed = cimgui_h.igSliderInt4(arena.allocateFrom(label), pV, min, max, arena.allocateFrom(format), flags);
        for (int i = 0; i < 4; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_INT, i);
        return changed;
    }

    /** Vertical float slider. */
    public static boolean vSliderFloat(String label, float width, float height, FloatArg v, float min, float max) {
        return vSliderFloat(label, width, height, v, min, max, "%.3f", 0);
    }

    /** Vertical float slider — full control. */
    public static boolean vSliderFloat(String label, float width, float height, FloatArg v, float min, float max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT);
        pV.set(cimgui_h.C_FLOAT, 0, v.get());
        var changed = cimgui_h.igVSliderFloat(arena.allocateFrom(label), imVec2(width, height), pV, min, max, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_FLOAT, 0));
        return changed;
    }

    /** Vertical int slider. */
    public static boolean vSliderInt(String label, float width, float height, IntArg v, int min, int max) {
        return vSliderInt(label, width, height, v, min, max, "%d", 0);
    }

    /** Vertical int slider — full control. */
    public static boolean vSliderInt(String label, float width, float height, IntArg v, int min, int max, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT);
        pV.set(cimgui_h.C_INT, 0, v.get());
        var changed = cimgui_h.igVSliderInt(arena.allocateFrom(label), imVec2(width, height), pV, min, max, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_INT, 0));
        return changed;
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Input with Keyboard
    // - StringArg is used for InputText to manage the mutable string buffer.
    // - bufSize controls the maximum character capacity of the input field.
    // =================================================================================================================

    /**
     * Single-line text input.
     * @param buf     in/out string value
     * @param bufSize maximum number of characters the input field can hold
     */
    public static boolean inputText(String label, StringArg buf, int bufSize) {
        return inputText(label, buf, bufSize, 0);
    }

    /**
     * Single-line text input — full control.
     * @param flags {@code ImGuiInputTextFlags}
     */
    public static boolean inputText(String label, StringArg buf, int bufSize, int flags) {
        var arena = frameArena();
        var nativeBuf = arena.allocate(cimgui_h.C_CHAR, bufSize);
        var bytes = buf.get().getBytes();
        int copyLen = Math.min(bytes.length, bufSize - 1);
        for (int i = 0; i < copyLen; i++) nativeBuf.setAtIndex(cimgui_h.C_CHAR, i, bytes[i]);
        nativeBuf.setAtIndex(cimgui_h.C_CHAR, copyLen, (byte) 0);
        var changed = cimgui_h.igInputText(arena.allocateFrom(label), nativeBuf, bufSize, flags, MemorySegment.NULL, MemorySegment.NULL);
        if (changed) buf.set(nativeBuf.getString(0));
        return changed;
    }

    /**
     * Multi-line text input.
     * @param buf     in/out string value
     * @param bufSize maximum number of characters
     */
    public static boolean inputTextMultiline(String label, StringArg buf, int bufSize) {
        return inputTextMultiline(label, buf, bufSize, 0f, 0f, 0);
    }

    /**
     * Multi-line text input — full control.
     * @param flags {@code ImGuiInputTextFlags}
     */
    public static boolean inputTextMultiline(String label, StringArg buf, int bufSize, float width, float height, int flags) {
        var arena = frameArena();
        var nativeBuf = arena.allocate(cimgui_h.C_CHAR, bufSize);
        var bytes = buf.get().getBytes();
        int copyLen = Math.min(bytes.length, bufSize - 1);
        for (int i = 0; i < copyLen; i++) nativeBuf.setAtIndex(cimgui_h.C_CHAR, i, bytes[i]);
        nativeBuf.setAtIndex(cimgui_h.C_CHAR, copyLen, (byte) 0);
        var changed = cimgui_h.igInputTextMultiline(arena.allocateFrom(label), nativeBuf, bufSize, imVec2(width, height), flags, MemorySegment.NULL, MemorySegment.NULL);
        if (changed) buf.set(nativeBuf.getString(0));
        return changed;
    }

    /**
     * Single-line text input with hint shown when empty.
     */
    public static boolean inputTextWithHint(String label, String hint, StringArg buf, int bufSize) {
        return inputTextWithHint(label, hint, buf, bufSize, 0);
    }

    /**
     * Single-line text input with hint — full control.
     * @param flags {@code ImGuiInputTextFlags}
     */
    public static boolean inputTextWithHint(String label, String hint, StringArg buf, int bufSize, int flags) {
        var arena = frameArena();
        var nativeBuf = arena.allocate(cimgui_h.C_CHAR, bufSize);
        var bytes = buf.get().getBytes();
        int copyLen = Math.min(bytes.length, bufSize - 1);
        for (int i = 0; i < copyLen; i++) nativeBuf.setAtIndex(cimgui_h.C_CHAR, i, bytes[i]);
        nativeBuf.setAtIndex(cimgui_h.C_CHAR, copyLen, (byte) 0);
        var changed = cimgui_h.igInputTextWithHint(arena.allocateFrom(label), arena.allocateFrom(hint), nativeBuf, bufSize, flags, MemorySegment.NULL, MemorySegment.NULL);
        if (changed) buf.set(nativeBuf.getString(0));
        return changed;
    }

    /** Float input field. */
    public static boolean inputFloat(String label, FloatArg v) {
        return inputFloat(label, v, 0f, 0f, "%.3f", 0);
    }

    /** Float input field — full control. @param flags {@code ImGuiInputTextFlags} */
    public static boolean inputFloat(String label, FloatArg v, float step, float stepFast, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT);
        pV.set(cimgui_h.C_FLOAT, 0, v.get());
        var changed = cimgui_h.igInputFloat(arena.allocateFrom(label), pV, step, stepFast, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_FLOAT, 0));
        return changed;
    }

    /** 2-component float input. {@code v.value} must have length >= 2. */
    public static boolean inputFloat2(String label, FloatArg v) {
        return inputFloat2(label, v, "%.3f", 0);
    }

    /** 2-component float input — full control. */
    public static boolean inputFloat2(String label, FloatArg v, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 2);
        pV.setAtIndex(cimgui_h.C_FLOAT, 0, v.value[0]);
        pV.setAtIndex(cimgui_h.C_FLOAT, 1, v.value[1]);
        var changed = cimgui_h.igInputFloat2(arena.allocateFrom(label), pV, arena.allocateFrom(format), flags);
        v.value[0] = pV.getAtIndex(cimgui_h.C_FLOAT, 0);
        v.value[1] = pV.getAtIndex(cimgui_h.C_FLOAT, 1);
        return changed;
    }

    /** 3-component float input. {@code v.value} must have length >= 3. */
    public static boolean inputFloat3(String label, FloatArg v) {
        return inputFloat3(label, v, "%.3f", 0);
    }

    /** 3-component float input — full control. */
    public static boolean inputFloat3(String label, FloatArg v, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 3);
        for (int i = 0; i < 3; i++) pV.setAtIndex(cimgui_h.C_FLOAT, i, v.value[i]);
        var changed = cimgui_h.igInputFloat3(arena.allocateFrom(label), pV, arena.allocateFrom(format), flags);
        for (int i = 0; i < 3; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /** 4-component float input. {@code v.value} must have length >= 4. */
    public static boolean inputFloat4(String label, FloatArg v) {
        return inputFloat4(label, v, "%.3f", 0);
    }

    /** 4-component float input — full control. */
    public static boolean inputFloat4(String label, FloatArg v, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_FLOAT, 4);
        for (int i = 0; i < 4; i++) pV.setAtIndex(cimgui_h.C_FLOAT, i, v.value[i]);
        var changed = cimgui_h.igInputFloat4(arena.allocateFrom(label), pV, arena.allocateFrom(format), flags);
        for (int i = 0; i < 4; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /** Int input field. */
    public static boolean inputInt(String label, IntArg v) {
        return inputInt(label, v, 1, 100, 0);
    }

    /** Int input field — full control. @param flags {@code ImGuiInputTextFlags} */
    public static boolean inputInt(String label, IntArg v, int step, int stepFast, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT);
        pV.set(cimgui_h.C_INT, 0, v.get());
        var changed = cimgui_h.igInputInt(arena.allocateFrom(label), pV, step, stepFast, flags);
        v.set(pV.get(cimgui_h.C_INT, 0));
        return changed;
    }

    /** 2-component int input. {@code v.value} must have length >= 2. */
    public static boolean inputInt2(String label, IntArg v) {
        return inputInt2(label, v, 0);
    }

    /** 2-component int input — full control. @param flags {@code ImGuiInputTextFlags} */
    public static boolean inputInt2(String label, IntArg v, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 2);
        pV.setAtIndex(cimgui_h.C_INT, 0, v.value[0]);
        pV.setAtIndex(cimgui_h.C_INT, 1, v.value[1]);
        var changed = cimgui_h.igInputInt2(arena.allocateFrom(label), pV, flags);
        v.value[0] = pV.getAtIndex(cimgui_h.C_INT, 0);
        v.value[1] = pV.getAtIndex(cimgui_h.C_INT, 1);
        return changed;
    }

    /** 3-component int input. {@code v.value} must have length >= 3. */
    public static boolean inputInt3(String label, IntArg v) {
        return inputInt3(label, v, 0);
    }

    /** 3-component int input — full control. @param flags {@code ImGuiInputTextFlags} */
    public static boolean inputInt3(String label, IntArg v, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 3);
        for (int i = 0; i < 3; i++) pV.setAtIndex(cimgui_h.C_INT, i, v.value[i]);
        var changed = cimgui_h.igInputInt3(arena.allocateFrom(label), pV, flags);
        for (int i = 0; i < 3; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_INT, i);
        return changed;
    }

    /** 4-component int input. {@code v.value} must have length >= 4. */
    public static boolean inputInt4(String label, IntArg v) {
        return inputInt4(label, v, 0);
    }

    /** 4-component int input — full control. @param flags {@code ImGuiInputTextFlags} */
    public static boolean inputInt4(String label, IntArg v, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_INT, 4);
        for (int i = 0; i < 4; i++) pV.setAtIndex(cimgui_h.C_INT, i, v.value[i]);
        var changed = cimgui_h.igInputInt4(arena.allocateFrom(label), pV, flags);
        for (int i = 0; i < 4; i++) v.value[i] = pV.getAtIndex(cimgui_h.C_INT, i);
        return changed;
    }

    /** Double input field. */
    public static boolean inputDouble(String label, DoubleArg v) {
        return inputDouble(label, v, 0.0, 0.0, "%.6f", 0);
    }

    /** Double input field — full control. @param flags {@code ImGuiInputTextFlags} */
    public static boolean inputDouble(String label, DoubleArg v, double step, double stepFast, String format, int flags) {
        var arena = frameArena();
        var pV = arena.allocate(cimgui_h.C_DOUBLE);
        pV.set(cimgui_h.C_DOUBLE, 0, v.get());
        var changed = cimgui_h.igInputDouble(arena.allocateFrom(label), pV, step, stepFast, arena.allocateFrom(format), flags);
        v.set(pV.get(cimgui_h.C_DOUBLE, 0));
        return changed;
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Color Editor/Picker
    // - col[3] / col[4]: FloatArg.value[] must have length >= 3 or 4 respectively.
    // =================================================================================================================

    /**
     * RGB color editor. {@code col.value} must have length >= 3.
     */
    public static boolean colorEdit3(String label, FloatArg col) {
        return colorEdit3(label, col, 0);
    }

    /** RGB color editor — full control. */
    public static boolean colorEdit3(String label, FloatArg col, int flags) {
        var arena = frameArena();
        var pCol = arena.allocate(cimgui_h.C_FLOAT, 3);
        for (int i = 0; i < 3; i++) pCol.setAtIndex(cimgui_h.C_FLOAT, i, col.value[i]);
        var changed = cimgui_h.igColorEdit3(arena.allocateFrom(label), pCol, flags);
        for (int i = 0; i < 3; i++) col.value[i] = pCol.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /**
     * RGBA color editor. {@code col.value} must have length >= 4.
     */
    public static boolean colorEdit4(String label, FloatArg col) {
        return colorEdit4(label, col, 0);
    }

    /** RGBA color editor — full control. */
    public static boolean colorEdit4(String label, FloatArg col, int flags) {
        var arena = frameArena();
        var pCol = arena.allocate(cimgui_h.C_FLOAT, 4);
        for (int i = 0; i < 4; i++) pCol.setAtIndex(cimgui_h.C_FLOAT, i, col.value[i]);
        var changed = cimgui_h.igColorEdit4(arena.allocateFrom(label), pCol, flags);
        for (int i = 0; i < 4; i++) col.value[i] = pCol.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /**
     * RGB color picker. {@code col.value} must have length >= 3.
     */
    public static boolean colorPicker3(String label, FloatArg col) {
        return colorPicker3(label, col, 0);
    }

    /** RGB color picker — full control. */
    public static boolean colorPicker3(String label, FloatArg col, int flags) {
        var arena = frameArena();
        var pCol = arena.allocate(cimgui_h.C_FLOAT, 3);
        for (int i = 0; i < 3; i++) pCol.setAtIndex(cimgui_h.C_FLOAT, i, col.value[i]);
        var changed = cimgui_h.igColorPicker3(arena.allocateFrom(label), pCol, flags);
        for (int i = 0; i < 3; i++) col.value[i] = pCol.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /**
     * RGBA color picker. {@code col.value} must have length >= 4.
     */
    public static boolean colorPicker4(String label, FloatArg col) {
        return colorPicker4(label, col, 0);
    }

    /** RGBA color picker — full control. */
    public static boolean colorPicker4(String label, FloatArg col, int flags) {
        var arena = frameArena();
        var pCol = arena.allocate(cimgui_h.C_FLOAT, 4);
        for (int i = 0; i < 4; i++) pCol.setAtIndex(cimgui_h.C_FLOAT, i, col.value[i]);
        var changed = cimgui_h.igColorPicker4(arena.allocateFrom(label), pCol, flags, MemorySegment.NULL);
        for (int i = 0; i < 4; i++) col.value[i] = pCol.getAtIndex(cimgui_h.C_FLOAT, i);
        return changed;
    }

    /**
     * Display a color square/button. Returns true when pressed.
     */
    public static boolean colorButton(String descId, float r, float g, float b, float a) {
        return colorButton(descId, r, g, b, a, 0, 0f, 0f);
    }

    /** Color button — full control. */
    public static boolean colorButton(String descId, float r, float g, float b, float a, int flags, float sizeX, float sizeY) {
        return cimgui_h.igColorButton(frameArena().allocateFrom(descId), imVec4(r, g, b, a), flags, imVec2(sizeX, sizeY));
    }

    /**
     * Initialize color editor options (e.g. default format, picker type).
     * @param flags {@code ImGuiColorEditFlags}
     */
    public static void setColorEditOptions(int flags) {
        cimgui_h.igSetColorEditOptions(flags);
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Trees
    // - TreeNode functions return true when open; call treePop() when done displaying contents.
    // =================================================================================================================

    /**
     * Tree node with label as both ID and display text. Returns true when open.
     * Call {@link #treePop()} when done.
     */
    public static boolean treeNode(String label) {
        return cimgui_h.igTreeNode_Str(frameArena().allocateFrom(label));
    }

    /**
     * Tree node with flags.
     * @param flags {@code ImGuiTreeNodeFlags}
     */
    public static boolean treeNodeEx(String label, int flags) {
        return cimgui_h.igTreeNodeEx_Str(frameArena().allocateFrom(label), flags);
    }

    /**
     * ~ Indent() + PushID(). Already called by treeNode() when it returns true.
     * Call treePop() to match.
     */
    public static void treePush(String strId) {
        cimgui_h.igTreePush_Str(frameArena().allocateFrom(strId));
    }

    /**
     * Use Java object identity as tree push ID (analogous to the {@code void*} overload).
     */
    public static void treePush(Object obj) {
        cimgui_h.igTreePush_Ptr(MemorySegment.ofAddress(System.identityHashCode(obj)));
    }

    /** ~ Unindent() + PopID(). */
    public static void treePop() {
        cimgui_h.igTreePop();
    }

    /** Horizontal distance preceding label when using TreeNode* or Bullet(). */
    public static float getTreeNodeToLabelSpacing() {
        return cimgui_h.igGetTreeNodeToLabelSpacing();
    }

    /**
     * Collapsing section header. Returns true when open. Does not indent or push on ID stack.
     */
    public static boolean collapsingHeader(String label) {
        return collapsingHeader(label, 0);
    }

    /** Collapsing header with flags. */
    public static boolean collapsingHeader(String label, int flags) {
        return cimgui_h.igCollapsingHeader_TreeNodeFlags(frameArena().allocateFrom(label), flags);
    }

    /**
     * Collapsing header with a close button. When {@code visible} is false the header is hidden.
     */
    public static boolean collapsingHeader(String label, BoolArg visible) {
        return collapsingHeader(label, visible, 0);
    }

    /** Collapsing header with close button and flags. */
    public static boolean collapsingHeader(String label, BoolArg visible, int flags) {
        var arena = frameArena();
        var pVisible = arena.allocate(cimgui_h.C_BOOL);
        pVisible.set(cimgui_h.C_BOOL, 0, visible.get());
        var result = cimgui_h.igCollapsingHeader_BoolPtr(arena.allocateFrom(label), pVisible, flags);
        visible.set(pVisible.get(cimgui_h.C_BOOL, 0));
        return result;
    }

    /**
     * Set the next TreeNode/CollapsingHeader open state.
     */
    public static void setNextItemOpen(boolean isOpen) {
        setNextItemOpen(isOpen, 0);
    }

    /** Set next item open state with condition. */
    public static void setNextItemOpen(boolean isOpen, int cond) {
        cimgui_h.igSetNextItemOpen(isOpen, cond);
    }

    /** Set ID to use for open/close storage (default: same as item ID). */
    public static void setNextItemStorageID(int storageId) {
        cimgui_h.igSetNextItemStorageID(storageId);
    }

    /** Retrieve tree node open/close state by storage ID. */
    public static boolean treeNodeGetOpen(int storageId) {
        return cimgui_h.igTreeNodeGetOpen(storageId);
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Selectables
    // - A selectable highlights when hovered and can display another color when selected.
    // =================================================================================================================

    /** Selectable item. Returns true when clicked. */
    public static boolean selectable(String label) {
        return selectable(label, false, 0, 0f, 0f);
    }

    /** Selectable with read-only selection state. */
    public static boolean selectable(String label, boolean selected) {
        return selectable(label, selected, 0, 0f, 0f);
    }

    /**
     * Selectable — full control.
     * @param flags {@code ImGuiSelectableFlags}
     * @param sizeX width (0 = use remaining width)
     * @param sizeY height (0 = use label height)
     */
    public static boolean selectable(String label, boolean selected, int flags, float sizeX, float sizeY) {
        return cimgui_h.igSelectable_Bool(frameArena().allocateFrom(label), selected, flags, imVec2(sizeX, sizeY));
    }

    /** Selectable with read-write selection state (auto-toggles). */
    public static boolean selectable(String label, BoolArg selected) {
        return selectable(label, selected, 0, 0f, 0f);
    }

    /** Selectable with read-write selection state — full control. */
    public static boolean selectable(String label, BoolArg selected, int flags, float sizeX, float sizeY) {
        var arena = frameArena();
        var pSelected = arena.allocate(cimgui_h.C_BOOL);
        pSelected.set(cimgui_h.C_BOOL, 0, selected.get());
        var result = cimgui_h.igSelectable_BoolPtr(arena.allocateFrom(label), pSelected, flags, imVec2(sizeX, sizeY));
        selected.set(pSelected.get(cimgui_h.C_BOOL, 0));
        return result;
    }
    // endregion

    // =================================================================================================================
    // region TODO: Multi-selection (BeginMultiSelect / EndMultiSelect)
    // =================================================================================================================
    // endregion

    // =================================================================================================================
    // region Widgets: List Boxes
    // =================================================================================================================

    /**
     * Open a framed scrolling list region. Call {@link #endListBox()} only if this returns true.
     */
    public static boolean beginListBox(String label) {
        return beginListBox(label, 0f, 0f);
    }

    /** Open a framed scrolling list region with explicit size. */
    public static boolean beginListBox(String label, float width, float height) {
        return cimgui_h.igBeginListBox(frameArena().allocateFrom(label), imVec2(width, height));
    }

    /** Only call endListBox() if beginListBox() returned true. */
    public static void endListBox() {
        cimgui_h.igEndListBox();
    }

    /**
     * Simple list box from a string array.
     * @param currentItem in/out index of current selection
     */
    public static boolean listBox(String label, IntArg currentItem, String[] items) {
        return listBox(label, currentItem, items, -1);
    }

    /**
     * Simple list box from a string array.
     * @param heightInItems visible item count (-1 = default ~7)
     */
    public static boolean listBox(String label, IntArg currentItem, String[] items, int heightInItems) {
        var arena = frameArena();
        var pCurrent = arena.allocate(cimgui_h.C_INT);
        pCurrent.set(cimgui_h.C_INT, 0, currentItem.get());
        var pItems = arena.allocate(cimgui_h.C_POINTER, items.length);
        for (int i = 0; i < items.length; i++) pItems.setAtIndex(cimgui_h.C_POINTER, i, arena.allocateFrom(items[i]));
        var changed = cimgui_h.igListBox_Str_arr(arena.allocateFrom(label), pCurrent, pItems, items.length, heightInItems);
        currentItem.set(pCurrent.get(cimgui_h.C_INT, 0));
        return changed;
    }
    // endregion

    // =================================================================================================================
    // region TODO: Widgets: Data Plotting
    // - Consider using ImPlot (https://github.com/epezent/implot) which is much better!
    // =================================================================================================================
    // endregion

    // =================================================================================================================
    // region Widgets: Value Helpers
    // - Shortcut for calling text() with a "name: value" format string.
    // =================================================================================================================

    /** Display "prefix: true/false". */
    public static void value(String prefix, boolean b) {
        cimgui_h.igValue_Bool(frameArena().allocateFrom(prefix), b);
    }

    /** Display "prefix: value". */
    public static void value(String prefix, int v) {
        cimgui_h.igValue_Int(frameArena().allocateFrom(prefix), v);
    }

    /** Display "prefix: value" for unsigned int. */
    public static void valueUnsigned(String prefix, int v) {
        cimgui_h.igValue_Uint(frameArena().allocateFrom(prefix), v);
    }

    /** Display "prefix: value". */
    public static void value(String prefix, float v) {
        value(prefix, v, null);
    }

    /** Display "prefix: value" with optional printf format string. */
    public static void value(String prefix, float v, String floatFormat) {
        var pFmt = floatFormat != null ? frameArena().allocateFrom(floatFormat) : MemorySegment.NULL;
        cimgui_h.igValue_Float(frameArena().allocateFrom(prefix), v, pFmt);
    }
    // endregion

    // =================================================================================================================
    // region Widgets: Menus
    // - Use beginMenuBar() on a window with ImGuiWindowFlags_MenuBar to append to its menu bar.
    // - Use beginMainMenuBar() to create a full-screen menu bar at the top.
    // =================================================================================================================

    /**
     * Append to the menu bar of the current window.
     * Requires {@code ImGuiWindowFlags_MenuBar} on the parent window.
     * Only call {@link #endMenuBar()} if this returns true.
     */
    public static boolean beginMenuBar() {
        return cimgui_h.igBeginMenuBar();
    }

    /** Only call endMenuBar() if beginMenuBar() returned true. */
    public static void endMenuBar() {
        cimgui_h.igEndMenuBar();
    }

    /**
     * Create and append to a full-screen menu bar at the top of the screen.
     * Only call {@link #endMainMenuBar()} if this returns true.
     */
    public static boolean beginMainMenuBar() {
        return cimgui_h.igBeginMainMenuBar();
    }

    /** Only call endMainMenuBar() if beginMainMenuBar() returned true. */
    public static void endMainMenuBar() {
        cimgui_h.igEndMainMenuBar();
    }

    /**
     * Create a sub-menu entry. Only call {@link #endMenu()} if this returns true.
     */
    public static boolean beginMenu(String label) {
        return beginMenu(label, true);
    }

    /** Create a sub-menu entry, optionally disabled. */
    public static boolean beginMenu(String label, boolean enabled) {
        return cimgui_h.igBeginMenu(frameArena().allocateFrom(label), enabled);
    }

    /** Only call endMenu() if beginMenu() returned true. */
    public static void endMenu() {
        cimgui_h.igEndMenu();
    }

    /** Menu item. Returns true when activated. */
    public static boolean menuItem(String label) {
        return menuItem(label, null, false, true);
    }

    /** Menu item with keyboard shortcut hint. */
    public static boolean menuItem(String label, String shortcut) {
        return menuItem(label, shortcut, false, true);
    }

    /**
     * Menu item — full control.
     * @param selected visual checkmark (read-only)
     * @param enabled  greyed out when false
     */
    public static boolean menuItem(String label, String shortcut, boolean selected, boolean enabled) {
        var arena = frameArena();
        var pShortcut = shortcut != null ? arena.allocateFrom(shortcut) : MemorySegment.NULL;
        return cimgui_h.igMenuItem_Bool(arena.allocateFrom(label), pShortcut, selected, enabled);
    }

    /** Menu item with read-write selected state (auto-toggles on click). */
    public static boolean menuItem(String label, String shortcut, BoolArg selected) {
        return menuItem(label, shortcut, selected, true);
    }

    /** Menu item with read-write selected state — full control. */
    public static boolean menuItem(String label, String shortcut, BoolArg selected, boolean enabled) {
        var arena = frameArena();
        var pShortcut = shortcut != null ? arena.allocateFrom(shortcut) : MemorySegment.NULL;
        var pSelected = arena.allocate(cimgui_h.C_BOOL);
        pSelected.set(cimgui_h.C_BOOL, 0, selected.get());
        var result = cimgui_h.igMenuItem_BoolPtr(arena.allocateFrom(label), pShortcut, pSelected, enabled);
        selected.set(pSelected.get(cimgui_h.C_BOOL, 0));
        return result;
    }
    // endregion

    // =================================================================================================================
    // region Tooltips
    // - Tooltips are windows following the mouse. They do not take focus.
    // - SetTooltip() is a shortcut for: if (BeginTooltip()) { Text(...); EndTooltip(); }
    // =================================================================================================================

    /**
     * Begin a tooltip window. Only call {@link #endTooltip()} if this returns true.
     */
    public static boolean beginTooltip() {
        return cimgui_h.igBeginTooltip();
    }

    /** Only call endTooltip() if beginTooltip() or beginItemTooltip() returned true. */
    public static void endTooltip() {
        cimgui_h.igEndTooltip();
    }

    /**
     * Set a text-only tooltip. Overrides any previous tooltip set this frame.
     * Often used after {@link #isItemHovered()}.
     */
    public static void setTooltip(String text) {
        SET_TOOLTIP.apply(frameArena().allocateFrom(text.replaceAll("%", "%%")));
    }

    /**
     * Begin a tooltip window if the preceding item was hovered.
     * Shortcut for the {@code IsItemHovered(ForTooltip) && BeginTooltip()} idiom.
     */
    public static boolean beginItemTooltip() {
        return cimgui_h.igBeginItemTooltip();
    }

    /** Set a text-only tooltip if the preceding item was hovered. */
    public static void setItemTooltip(String text) {
        SET_ITEM_TOOLTIP.apply(frameArena().allocateFrom(text.replaceAll("%", "%%")));
    }
    // endregion

    // =================================================================================================================
    // region Popups and Modals
    // - Popups block normal mouse hovering detection behind them.
    // - If not modal: closed by clicking outside or pressing ESCAPE.
    // - Popup IDs are relative to the current ID stack.
    // =================================================================================================================

    /**
     * Return true if the popup is open, and start appending into it.
     * Call {@link #endPopup()} when done.
     */
    public static boolean beginPopup(String strId) {
        return beginPopup(strId, 0);
    }

    /**
     * Begin popup with window flags.
     * @param flags {@code ImGuiWindowFlags}
     */
    public static boolean beginPopup(String strId, int flags) {
        return cimgui_h.igBeginPopup(frameArena().allocateFrom(strId), flags);
    }

    /**
     * Begin a modal popup. Blocks all interaction behind the window.
     * Call {@link #endPopup()} when done.
     */
    public static boolean beginPopupModal(String name) {
        return beginPopupModal(name, null, 0);
    }

    /** Begin a modal popup with optional close button and window flags. */
    public static boolean beginPopupModal(String name, BoolArg open, int flags) {
        var arena = frameArena();
        var pOpen = MemorySegment.NULL;
        if (open != null) {
            pOpen = arena.allocate(cimgui_h.C_BOOL);
            pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        }
        var result = cimgui_h.igBeginPopupModal(arena.allocateFrom(name), pOpen, flags);
        if (open != null) open.set(pOpen.get(cimgui_h.C_BOOL, 0));
        return result;
    }

    /** Only call endPopup() if beginPopup*()/beginPopupModal() returned true. */
    public static void endPopup() {
        cimgui_h.igEndPopup();
    }

    /**
     * Mark a popup as open. Call once — not every frame.
     */
    public static void openPopup(String strId) {
        openPopup(strId, 0);
    }

    /**
     * Open popup by string ID with flags.
     * @param popupFlags {@code ImGuiPopupFlags}
     */
    public static void openPopup(String strId, int popupFlags) {
        cimgui_h.igOpenPopup_Str(frameArena().allocateFrom(strId), popupFlags);
    }

    /** Open popup by integer ID. Useful from nested ID stacks. */
    public static void openPopup(int id, int popupFlags) {
        cimgui_h.igOpenPopup_ID(id, popupFlags);
    }

    /**
     * Open a popup when the last item is clicked (right-click by default).
     */
    public static void openPopupOnItemClick() {
        openPopupOnItemClick(null, 0);
    }

    /** Open popup on item click with optional ID override and flags. */
    public static void openPopupOnItemClick(String strId, int popupFlags) {
        var pId = strId != null ? frameArena().allocateFrom(strId) : MemorySegment.NULL;
        cimgui_h.igOpenPopupOnItemClick(pId, popupFlags);
    }

    /** Manually close the popup currently being built into. */
    public static void closeCurrentPopup() {
        cimgui_h.igCloseCurrentPopup();
    }

    /** Open+begin context popup when the last item is right-clicked. */
    public static boolean beginPopupContextItem() {
        return beginPopupContextItem(null, 0);
    }

    /** Context popup on last item with optional ID override and flags. */
    public static boolean beginPopupContextItem(String strId, int popupFlags) {
        var pId = strId != null ? frameArena().allocateFrom(strId) : MemorySegment.NULL;
        return cimgui_h.igBeginPopupContextItem(pId, popupFlags);
    }

    /** Open+begin context popup when the current window is right-clicked. */
    public static boolean beginPopupContextWindow() {
        return beginPopupContextWindow(null, 0);
    }

    /** Context popup on current window with optional ID override and flags. */
    public static boolean beginPopupContextWindow(String strId, int popupFlags) {
        var pId = strId != null ? frameArena().allocateFrom(strId) : MemorySegment.NULL;
        return cimgui_h.igBeginPopupContextWindow(pId, popupFlags);
    }

    /** Open+begin context popup when clicking in void (no windows). */
    public static boolean beginPopupContextVoid() {
        return beginPopupContextVoid(null, 0);
    }

    /** Context popup in void with optional ID override and flags. */
    public static boolean beginPopupContextVoid(String strId, int popupFlags) {
        var pId = strId != null ? frameArena().allocateFrom(strId) : MemorySegment.NULL;
        return cimgui_h.igBeginPopupContextVoid(pId, popupFlags);
    }

    /** Return true if the named popup is open at the current ID stack level. */
    public static boolean isPopupOpen(String strId) {
        return isPopupOpen(strId, 0);
    }

    /** isPopupOpen with flags (e.g. {@code ImGuiPopupFlags_AnyPopupId}). */
    public static boolean isPopupOpen(String strId, int flags) {
        return cimgui_h.igIsPopupOpen_Str(frameArena().allocateFrom(strId), flags);
    }
    // endregion

    // =================================================================================================================
    // region Tables
    // - Full-featured replacement for the legacy Columns API.
    // - Call BeginTable() -> [TableSetupColumn()] -> [TableHeadersRow()] -> TableNextRow()/TableNextColumn() -> EndTable()
    // =================================================================================================================

    /**
     * Begin a table. Returns false when not visible — always call {@link #endTable()} regardless.
     */
    public static boolean beginTable(String strId, int columns) {
        return beginTable(strId, columns, 0, 0f, 0f, 0f);
    }

    /**
     * Begin a table with flags.
     * @param flags {@code ImGuiTableFlags}
     */
    public static boolean beginTable(String strId, int columns, int flags) {
        return beginTable(strId, columns, flags, 0f, 0f, 0f);
    }

    /** Begin a table — full control. */
    public static boolean beginTable(String strId, int columns, int flags, float outerSizeX, float outerSizeY, float innerWidth) {
        return cimgui_h.igBeginTable(frameArena().allocateFrom(strId), columns, flags, imVec2(outerSizeX, outerSizeY), innerWidth);
    }

    /** Only call endTable() if beginTable() returned true. */
    public static void endTable() {
        cimgui_h.igEndTable();
    }

    /** Append into the first cell of a new row. */
    public static void tableNextRow() {
        tableNextRow(0, 0f);
    }

    /**
     * Append into the first cell of a new row.
     * @param rowFlags {@code ImGuiTableRowFlags}
     */
    public static void tableNextRow(int rowFlags, float minRowHeight) {
        cimgui_h.igTableNextRow(rowFlags, minRowHeight);
    }

    /** Append into the next column. Returns true when the column is visible. */
    public static boolean tableNextColumn() {
        return cimgui_h.igTableNextColumn();
    }

    /** Append into the specified column. Returns true when the column is visible. */
    public static boolean tableSetColumnIndex(int columnN) {
        return cimgui_h.igTableSetColumnIndex(columnN);
    }

    /** Declare a column with default options. */
    public static void tableSetupColumn(String label) {
        tableSetupColumn(label, 0, 0f, 0);
    }

    /** Declare a column with flags. */
    public static void tableSetupColumn(String label, int flags) {
        tableSetupColumn(label, flags, 0f, 0);
    }

    /**
     * Declare a column with label, resize flags, default width/weight, and user ID.
     * @param flags {@code ImGuiTableColumnFlags}
     */
    public static void tableSetupColumn(String label, int flags, float initWidthOrWeight, int userId) {
        cimgui_h.igTableSetupColumn(frameArena().allocateFrom(label), flags, initWidthOrWeight, userId);
    }

    /** Lock columns/rows so they remain visible when scrolled. */
    public static void tableSetupScrollFreeze(int cols, int rows) {
        cimgui_h.igTableSetupScrollFreeze(cols, rows);
    }

    /** Submit one header cell manually. */
    public static void tableHeader(String label) {
        cimgui_h.igTableHeader(frameArena().allocateFrom(label));
    }

    /** Submit a header row based on TableSetupColumn() data, and open the context menu. */
    public static void tableHeadersRow() {
        cimgui_h.igTableHeadersRow();
    }

    /**
     * Submit a row of angled headers for columns flagged with {@code ImGuiTableColumnFlags_AngledHeader}.
     * Must be the first row.
     */
    public static void tableAngledHeadersRow() {
        cimgui_h.igTableAngledHeadersRow();
    }

    /**
     * Get latest sort specs for the table. Returns NULL when not sorting.
     * Do not hold this pointer past the next {@link #beginTable} call.
     */
    public static MemorySegment tableGetSortSpecs() {
        return cimgui_h.igTableGetSortSpecs();
    }

    /** Return number of columns (value passed to beginTable). */
    public static int tableGetColumnCount() {
        return cimgui_h.igTableGetColumnCount();
    }

    /** Return current column index. */
    public static int tableGetColumnIndex() {
        return cimgui_h.igTableGetColumnIndex();
    }

    /** Return current row index (header rows are counted). */
    public static int tableGetRowIndex() {
        return cimgui_h.igTableGetRowIndex();
    }

    /** Return current column name. */
    public static String tableGetColumnName() {
        return tableGetColumnName(-1);
    }

    /** Return column name. Pass -1 for current column. */
    public static String tableGetColumnName(int columnN) {
        var ptr = cimgui_h.igTableGetColumnName_Int(columnN);
        return ptr.equals(MemorySegment.NULL) ? "" : ptr.getString(0);
    }

    /**
     * Return column flags. Pass -1 for current column.
     * @return {@code ImGuiTableColumnFlags}
     */
    public static int tableGetColumnFlags() {
        return tableGetColumnFlags(-1);
    }

    /** Return flags for the specified column. */
    public static int tableGetColumnFlags(int columnN) {
        return cimgui_h.igTableGetColumnFlags(columnN);
    }

    /** Enable or disable user access to a column. Set false to hide it. */
    public static void tableSetColumnEnabled(int columnN, boolean enabled) {
        cimgui_h.igTableSetColumnEnabled(columnN, enabled);
    }

    /**
     * Return hovered column index, -1 when not hovered, or columnsCount for the unused right space.
     */
    public static int tableGetHoveredColumn() {
        return cimgui_h.igTableGetHoveredColumn();
    }

    /**
     * Change the background color of a cell, row, or column.
     * @param target {@code ImGuiTableBgTarget}
     */
    public static void tableSetBgColor(int target, int color) {
        tableSetBgColor(target, color, -1);
    }

    /** tableSetBgColor targeting a specific column. */
    public static void tableSetBgColor(int target, int color, int columnN) {
        cimgui_h.igTableSetBgColor(target, color, columnN);
    }
    // endregion

    // =================================================================================================================
    // region Legacy Columns API (prefer Tables!)
    // =================================================================================================================

    /** Begin single-column layout (resets to default). */
    public static void columns() {
        columns(1, null, true);
    }

    /** Begin multi-column layout. */
    public static void columns(int count) {
        columns(count, null, true);
    }

    /** Begin multi-column layout with optional ID and border toggle. */
    public static void columns(int count, String id, boolean borders) {
        var pId = id != null ? frameArena().allocateFrom(id) : MemorySegment.NULL;
        cimgui_h.igColumns(count, pId, borders);
    }

    /** Move to the next column. */
    public static void nextColumn() {
        cimgui_h.igNextColumn();
    }

    /** Return current column index. */
    public static int getColumnIndex() {
        return cimgui_h.igGetColumnIndex();
    }

    /** Get current column width in pixels. */
    public static float getColumnWidth() {
        return getColumnWidth(-1);
    }

    /** Get column width in pixels. Pass -1 for current column. */
    public static float getColumnWidth(int columnIndex) {
        return cimgui_h.igGetColumnWidth(columnIndex);
    }

    /** Set column width in pixels. Pass -1 for current column. */
    public static void setColumnWidth(int columnIndex, float width) {
        cimgui_h.igSetColumnWidth(columnIndex, width);
    }

    /** Get current column line position in pixels from the left side of the content region. */
    public static float getColumnOffset() {
        return getColumnOffset(-1);
    }

    /** Get column offset in pixels. Pass -1 for current column. */
    public static float getColumnOffset(int columnIndex) {
        return cimgui_h.igGetColumnOffset(columnIndex);
    }

    /** Set column offset in pixels. Pass -1 for current column. */
    public static void setColumnOffset(int columnIndex, float offsetX) {
        cimgui_h.igSetColumnOffset(columnIndex, offsetX);
    }

    /** Return the total number of columns. */
    public static int getColumnsCount() {
        return cimgui_h.igGetColumnsCount();
    }
    // endregion

    // =================================================================================================================
    // region Tab Bars and Tabs
    // =================================================================================================================

    /**
     * Create and append into a tab bar. Only call {@link #endTabBar()} if this returns true.
     */
    public static boolean beginTabBar(String strId) {
        return beginTabBar(strId, 0);
    }

    /**
     * Begin a tab bar with flags.
     * @param flags {@code ImGuiTabBarFlags}
     */
    public static boolean beginTabBar(String strId, int flags) {
        return cimgui_h.igBeginTabBar(frameArena().allocateFrom(strId), flags);
    }

    /** Only call endTabBar() if beginTabBar() returned true. */
    public static void endTabBar() {
        cimgui_h.igEndTabBar();
    }

    /**
     * Create a tab item. Returns true when selected.
     * Only call {@link #endTabItem()} if this returns true.
     */
    public static boolean beginTabItem(String label) {
        return beginTabItem(label, null, 0);
    }

    /**
     * Create a closable tab item.
     * @param open  in/out; set to false when the close button is clicked
     * @param flags {@code ImGuiTabItemFlags}
     */
    public static boolean beginTabItem(String label, BoolArg open, int flags) {
        var arena = frameArena();
        var pOpen = MemorySegment.NULL;
        if (open != null) {
            pOpen = arena.allocate(cimgui_h.C_BOOL);
            pOpen.set(cimgui_h.C_BOOL, 0, open.get());
        }
        var result = cimgui_h.igBeginTabItem(arena.allocateFrom(label), pOpen, flags);
        if (open != null) open.set(pOpen.get(cimgui_h.C_BOOL, 0));
        return result;
    }

    /** Only call endTabItem() if beginTabItem() returned true. */
    public static void endTabItem() {
        cimgui_h.igEndTabItem();
    }

    /**
     * Tab that behaves like a button. Returns true when clicked. Cannot be selected.
     */
    public static boolean tabItemButton(String label) {
        return tabItemButton(label, 0);
    }

    /**
     * Tab item button with flags.
     * @param flags {@code ImGuiTabItemFlags}
     */
    public static boolean tabItemButton(String label, int flags) {
        return cimgui_h.igTabItemButton(frameArena().allocateFrom(label), flags);
    }

    /** Notify the tab bar of a closed tab to reduce visual flicker on reorderable tab bars. */
    public static void setTabItemClosed(String tabOrDockedWindowLabel) {
        cimgui_h.igSetTabItemClosed(frameArena().allocateFrom(tabOrDockedWindowLabel));
    }
    // endregion

    // =================================================================================================================
    // region Logging / Capture
    // - All text output from the interface can be captured into tty/file/clipboard.
    // =================================================================================================================

    /** Start logging to stdout. @param autoOpenDepth tree nodes to auto-expand (-1 = default) */
    public static void logToTTY() {
        logToTTY(-1);
    }

    /** Start logging to stdout with explicit auto-open depth. */
    public static void logToTTY(int autoOpenDepth) {
        cimgui_h.igLogToTTY(autoOpenDepth);
    }

    /** Start logging to file (imgui_log.txt by default). */
    public static void logToFile() {
        logToFile(-1, null);
    }

    /** Start logging to a named file. Pass null for the default filename. */
    public static void logToFile(int autoOpenDepth, String filename) {
        var pFile = filename != null ? frameArena().allocateFrom(filename) : MemorySegment.NULL;
        cimgui_h.igLogToFile(autoOpenDepth, pFile);
    }

    /** Start logging to the OS clipboard. */
    public static void logToClipboard() {
        logToClipboard(-1);
    }

    /** Start logging to clipboard with explicit auto-open depth. */
    public static void logToClipboard(int autoOpenDepth) {
        cimgui_h.igLogToClipboard(autoOpenDepth);
    }

    /** Stop logging (close file, etc.). */
    public static void logFinish() {
        cimgui_h.igLogFinish();
    }

    /** Display helper buttons for logging to tty/file/clipboard. */
    public static void logButtons() {
        cimgui_h.igLogButtons();
    }

    /** Pass text straight to the log without displaying it in the UI. */
    public static void logText(String text) {
        LOG_TEXT.apply(frameArena().allocateFrom(text.replaceAll("%", "%%")));
    }
    // endregion

    // =================================================================================================================
    // region Drag and Drop
    // - Source: beginDragDropSource() -> setDragDropPayload() -> endDragDropSource()
    // - Target: beginDragDropTarget() -> acceptDragDropPayload() -> endDragDropTarget()
    // =================================================================================================================

    /**
     * Call after submitting an item that may be dragged. When true, call
     * {@link #setDragDropPayload} and {@link #endDragDropSource()}.
     */
    public static boolean beginDragDropSource() {
        return beginDragDropSource(0);
    }

    /**
     * Begin drag drop source with flags.
     * @param flags {@code ImGuiDragDropFlags}
     */
    public static boolean beginDragDropSource(int flags) {
        return cimgui_h.igBeginDragDropSource(flags);
    }

    /**
     * Set the payload data. {@code data} is copied by ImGui.
     * @param type user-defined string, max 32 characters
     */
    public static boolean setDragDropPayload(String type, MemorySegment data, long size) {
        return setDragDropPayload(type, data, size, 0);
    }

    /**
     * Set drag-drop payload with condition.
     * @param cond {@code ImGuiCond} (0 = always)
     */
    public static boolean setDragDropPayload(String type, MemorySegment data, long size, int cond) {
        return cimgui_h.igSetDragDropPayload(frameArena().allocateFrom(type), data, size, cond);
    }

    /** Only call endDragDropSource() if beginDragDropSource() returned true. */
    public static void endDragDropSource() {
        cimgui_h.igEndDragDropSource();
    }

    /**
     * Call after submitting an item that may receive a payload. When true, call
     * {@link #acceptDragDropPayload} and {@link #endDragDropTarget()}.
     */
    public static boolean beginDragDropTarget() {
        return cimgui_h.igBeginDragDropTarget();
    }

    /**
     * Accept payload of the given type. Returns an ImGuiPayload* as MemorySegment, or NULL.
     */
    public static MemorySegment acceptDragDropPayload(String type) {
        return acceptDragDropPayload(type, 0);
    }

    /**
     * Accept drag-drop payload with flags.
     * @param flags {@code ImGuiDragDropFlags}
     */
    public static MemorySegment acceptDragDropPayload(String type, int flags) {
        return cimgui_h.igAcceptDragDropPayload(frameArena().allocateFrom(type), flags);
    }

    /** Only call endDragDropTarget() if beginDragDropTarget() returned true. */
    public static void endDragDropTarget() {
        cimgui_h.igEndDragDropTarget();
    }

    /** Peek at the current drag payload from anywhere. Returns NULL when drag-and-drop is inactive. */
    public static MemorySegment getDragDropPayload() {
        return cimgui_h.igGetDragDropPayload();
    }
    // endregion

    // =================================================================================================================
    // region Disabling
    // - Disables all user interactions and dims items visually (applies style.DisabledAlpha).
    // - A single BeginDisabled(true) in the stack keeps everything disabled; nesting is supported.
    // =================================================================================================================

    /** Begin a disabled section. All items inside will be non-interactive and dimmed. */
    public static void beginDisabled() {
        beginDisabled(true);
    }

    /** Begin a disabled section. Passing false is a no-op (useful for conditional expressions). */
    public static void beginDisabled(boolean disabled) {
        cimgui_h.igBeginDisabled(disabled);
    }

    /** End a disabled section started with {@link #beginDisabled()}. */
    public static void endDisabled() {
        cimgui_h.igEndDisabled();
    }
    // endregion

    // =================================================================================================================
    // region Clipping
    // - Mouse hovering is affected by pushClipRect(), unlike direct ImDrawList calls which are render-only.
    // =================================================================================================================

    /**
     * Push a clip rectangle affecting mouse hovering and rendering.
     * @param intersectWithCurrentClipRect if true, intersects with the current clip rect
     */
    public static void pushClipRect(float clipRectMinX, float clipRectMinY, float clipRectMaxX, float clipRectMaxY, boolean intersectWithCurrentClipRect) {
        cimgui_h.igPushClipRect(imVec2(clipRectMinX, clipRectMinY), imVec2(clipRectMaxX, clipRectMaxY), intersectWithCurrentClipRect);
    }

    /** Pop the last clip rectangle. */
    public static void popClipRect() {
        cimgui_h.igPopClipRect();
    }
    // endregion

    // =================================================================================================================
    // region Focus and Activation
    // =================================================================================================================

    /** Make the last item the default focused item of a newly appearing window. */
    public static void setItemDefaultFocus() {
        cimgui_h.igSetItemDefaultFocus();
    }

    /**
     * Focus keyboard on the next widget. Use positive offset to access sub-components of a
     * multi-component widget. Use -1 to access the previous widget.
     */
    public static void setKeyboardFocusHere() {
        setKeyboardFocusHere(0);
    }

    /** Set keyboard focus with offset. */
    public static void setKeyboardFocusHere(int offset) {
        cimgui_h.igSetKeyboardFocusHere(offset);
    }
    // endregion

    // =================================================================================================================
    // region Keyboard/Gamepad Navigation
    // =================================================================================================================

    /**
     * Alter the visibility of the keyboard/gamepad cursor.
     * Default: shown when using an arrow key, hidden when clicking with the mouse.
     */
    public static void setNavCursorVisible(boolean visible) {
        cimgui_h.igSetNavCursorVisible(visible);
    }

    /**
     * Allow the next item to be overlapped by a subsequent item.
     * Useful with invisible buttons, selectables, and tree nodes.
     */
    public static void setNextItemAllowOverlap() {
        cimgui_h.igSetNextItemAllowOverlap();
    }
    // endregion

    // =================================================================================================================
    // region Item / Widget Utilities and Query Functions
    // - Most functions refer to the previously submitted item.
    // =================================================================================================================

    /**
     * Is the last item hovered and usable (not blocked by a popup)?
     */
    public static boolean isItemHovered() {
        return isItemHovered(0);
    }

    /**
     * Is the last item hovered, with flags.
     * @param flags {@code ImGuiHoveredFlags}
     */
    public static boolean isItemHovered(int flags) {
        return cimgui_h.igIsItemHovered(flags);
    }

    /** Is the last item active (e.g. button held, text field being edited)? */
    public static boolean isItemActive() {
        return cimgui_h.igIsItemActive();
    }

    /** Is the last item focused for keyboard/gamepad navigation? */
    public static boolean isItemFocused() {
        return cimgui_h.igIsItemFocused();
    }

    /**
     * Is the last item hovered and the given mouse button was clicked?
     */
    public static boolean isItemClicked() {
        return isItemClicked(0);
    }

    /**
     * Is the last item clicked with the given mouse button?
     * @param mouseButton {@code ImGuiMouseButton} (default 0 = left)
     */
    public static boolean isItemClicked(int mouseButton) {
        return cimgui_h.igIsItemClicked(mouseButton);
    }

    /** Is the last item visible (not clipped or scrolled out)? */
    public static boolean isItemVisible() {
        return cimgui_h.igIsItemVisible();
    }

    /** Did the last item modify its value this frame, or was it pressed? */
    public static boolean isItemEdited() {
        return cimgui_h.igIsItemEdited();
    }

    /** Was the last item just made active (previously inactive)? */
    public static boolean isItemActivated() {
        return cimgui_h.igIsItemActivated();
    }

    /** Was the last item just made inactive (previously active)? */
    public static boolean isItemDeactivated() {
        return cimgui_h.igIsItemDeactivated();
    }

    /** Was the last item deactivated AND did it change value while active? */
    public static boolean isItemDeactivatedAfterEdit() {
        return cimgui_h.igIsItemDeactivatedAfterEdit();
    }

    /** Was the last item's open state toggled? Set by treeNode*(). */
    public static boolean isItemToggledOpen() {
        return cimgui_h.igIsItemToggledOpen();
    }

    /** Is any item hovered? */
    public static boolean isAnyItemHovered() {
        return cimgui_h.igIsAnyItemHovered();
    }

    /** Is any item active? */
    public static boolean isAnyItemActive() {
        return cimgui_h.igIsAnyItemActive();
    }

    /** Is any item focused? */
    public static boolean isAnyItemFocused() {
        return cimgui_h.igIsAnyItemFocused();
    }

    /** Get the ID of the last item. */
    public static int getItemID() {
        return cimgui_h.igGetItemID();
    }

    /** Get the upper-left bounding rectangle of the last item (screen space). */
    public static Vec2f getItemRectMin() {
        var result = cimgui_h.igGetItemRectMin(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /** Get the lower-right bounding rectangle of the last item (screen space). */
    public static Vec2f getItemRectMax() {
        var result = cimgui_h.igGetItemRectMax(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /** Get the size of the last item. */
    public static Vec2f getItemRectSize() {
        var result = cimgui_h.igGetItemRectSize(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /**
     * Get generic flags of the last item.
     * @return {@code ImGuiItemFlags}
     */
    public static int getItemFlags() {
        return cimgui_h.igGetItemFlags();
    }
    // endregion

    // =================================================================================================================
    // region TODO: Viewports
    // - GetMainViewport() returns an ImGuiViewport* — deferred until a viewport wrapper is implemented.
    // =================================================================================================================
    // endregion

    // =================================================================================================================
    // region TODO: Background / Foreground Draw Lists
    // - Requires DrawList wrapper — deferred until that is implemented.
    // =================================================================================================================
    // endregion

    // =================================================================================================================
    // region Miscellaneous Utilities
    // =================================================================================================================

    /** Test if a rectangle of the given size (starting from cursor position) is visible / not clipped. */
    public static boolean isRectVisible(float sizeX, float sizeY) {
        return cimgui_h.igIsRectVisible_Nil(imVec2(sizeX, sizeY));
    }

    /** Test if a rectangle in screen space is visible / not clipped. */
    public static boolean isRectVisible(float rectMinX, float rectMinY, float rectMaxX, float rectMaxY) {
        return cimgui_h.igIsRectVisible_Vec2(imVec2(rectMinX, rectMinY), imVec2(rectMaxX, rectMaxY));
    }

    /** Get global ImGui time, incremented by {@code io.DeltaTime} every frame. */
    public static double getTime() {
        return cimgui_h.igGetTime();
    }

    /** Get the global ImGui frame count, incremented by 1 every frame. */
    public static int getFrameCount() {
        return cimgui_h.igGetFrameCount();
    }

    /** Get a string corresponding to an {@code ImGuiCol} enum value (for display/saving). */
    public static String getStyleColorName(int imGuiColIdx) {
        return cimgui_h.igGetStyleColorName(imGuiColIdx).getString(0);
    }

    /** Calculate the size of a text string. */
    public static Vec2f calcTextSize(String text) {
        return calcTextSize(text, false, -1f);
    }

    /**
     * Calculate the size of a text string.
     * @param hideTextAfterDoubleHash stop measuring at {@code ##} (the label trick)
     * @param wrapWidth               wrapping width (-1 = no wrap)
     */
    public static Vec2f calcTextSize(String text, boolean hideTextAfterDoubleHash, float wrapWidth) {
        var arena = frameArena();
        var result = cimgui_h.igCalcTextSize(arena, arena.allocateFrom(text), MemorySegment.NULL, hideTextAfterDoubleHash, wrapWidth);
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }
    // endregion

    // =================================================================================================================
    // region Color Utilities
    // =================================================================================================================

    /** Convert a packed 32-bit color to Vec4f (r, g, b, a) with components in [0, 1]. */
    public static Vec4f colorConvertU32ToFloat4(int color) {
        var result = cimgui_h.igColorConvertU32ToFloat4(frameArena(), color);
        return new Vec4f(ImVec4_c.x(result), ImVec4_c.y(result), ImVec4_c.z(result), ImVec4_c.w(result));
    }

    /** Convert Vec4f (r, g, b, a) to a packed 32-bit color. */
    public static int colorConvertFloat4ToU32(float r, float g, float b, float a) {
        return cimgui_h.igColorConvertFloat4ToU32(imVec4(r, g, b, a));
    }

    /** Convert RGB to HSV. Output values are written into the provided {@link FloatArg} objects. */
    public static void colorConvertRGBtoHSV(float r, float g, float b, FloatArg outH, FloatArg outS, FloatArg outV) {
        var arena = frameArena();
        var pH = arena.allocate(cimgui_h.C_FLOAT);
        var pS = arena.allocate(cimgui_h.C_FLOAT);
        var pV = arena.allocate(cimgui_h.C_FLOAT);
        cimgui_h.igColorConvertRGBtoHSV(r, g, b, pH, pS, pV);
        outH.set(pH.get(cimgui_h.C_FLOAT, 0));
        outS.set(pS.get(cimgui_h.C_FLOAT, 0));
        outV.set(pV.get(cimgui_h.C_FLOAT, 0));
    }

    /** Convert HSV to RGB. Output values are written into the provided {@link FloatArg} objects. */
    public static void colorConvertHSVtoRGB(float h, float s, float v, FloatArg outR, FloatArg outG, FloatArg outB) {
        var arena = frameArena();
        var pR = arena.allocate(cimgui_h.C_FLOAT);
        var pG = arena.allocate(cimgui_h.C_FLOAT);
        var pB = arena.allocate(cimgui_h.C_FLOAT);
        cimgui_h.igColorConvertHSVtoRGB(h, s, v, pR, pG, pB);
        outR.set(pR.get(cimgui_h.C_FLOAT, 0));
        outG.set(pG.get(cimgui_h.C_FLOAT, 0));
        outB.set(pB.get(cimgui_h.C_FLOAT, 0));
    }
    // endregion

    // =================================================================================================================
    // region Inputs: Keyboard
    // =================================================================================================================

    /** Is the given key currently being held? */
    public static boolean isKeyDown(int imGuiKey) {
        return cimgui_h.igIsKeyDown_Nil(imGuiKey);
    }

    /** Was the given key pressed this frame? Repeats using {@code io.KeyRepeatDelay / KeyRepeatRate}. */
    public static boolean isKeyPressed(int imGuiKey) {
        return isKeyPressed(imGuiKey, true);
    }

    /** Was the given key pressed? Control whether repeat is enabled. */
    public static boolean isKeyPressed(int imGuiKey, boolean repeat) {
        return cimgui_h.igIsKeyPressed_Bool(imGuiKey, repeat);
    }

    /** Was the given key released this frame? */
    public static boolean isKeyReleased(int imGuiKey) {
        return cimgui_h.igIsKeyReleased_Nil(imGuiKey);
    }

    /**
     * Was the given key chord (mods + key) pressed this frame?
     * E.g. {@code ImGuiMod_Ctrl | ImGuiKey_S}
     */
    public static boolean isKeyChordPressed(int imGuiKeyChord) {
        return cimgui_h.igIsKeyChordPressed_Nil(imGuiKeyChord);
    }

    /** Return how many times the key was pressed, factoring in repeat rate and delay. */
    public static int getKeyPressedAmount(int imGuiKey, float repeatDelay, float rate) {
        return cimgui_h.igGetKeyPressedAmount(imGuiKey, repeatDelay, rate);
    }

    /** Return the English name of the key (for debugging). */
    public static String getKeyName(int imGuiKey) {
        return cimgui_h.igGetKeyName(imGuiKey).getString(0);
    }

    /**
     * Override {@code io.WantCaptureKeyboard} for the next frame.
     * When true, instructs your app to ignore keyboard input.
     */
    public static void setNextFrameWantCaptureKeyboard(boolean wantCaptureKeyboard) {
        cimgui_h.igSetNextFrameWantCaptureKeyboard(wantCaptureKeyboard);
    }
    // endregion

    // =================================================================================================================
    // region Inputs: Shortcuts and Routing
    // =================================================================================================================

    /**
     * Test a key chord with focus routing. Prefer over {@link #isKeyChordPressed} for UI shortcuts.
     */
    public static boolean shortcut(int imGuiKeyChord) {
        return shortcut(imGuiKeyChord, 0);
    }

    /**
     * Shortcut test with flags.
     * @param flags {@code ImGuiInputFlags}
     */
    public static boolean shortcut(int imGuiKeyChord, int flags) {
        return cimgui_h.igShortcut_Nil(imGuiKeyChord, flags);
    }

    /** Assign a shortcut to the next item. */
    public static void setNextItemShortcut(int imGuiKeyChord) {
        setNextItemShortcut(imGuiKeyChord, 0);
    }

    /** Assign a shortcut to the next item with flags. */
    public static void setNextItemShortcut(int imGuiKeyChord, int flags) {
        cimgui_h.igSetNextItemShortcut(imGuiKeyChord, flags);
    }

    /**
     * Set key owner to last item ID if it is hovered or active.
     * Equivalent to: {@code if (isItemHovered() || isItemActive()) SetKeyOwner(key, GetItemID())}
     */
    public static void setItemKeyOwner(int imGuiKey) {
        cimgui_h.igSetItemKeyOwner_Nil(imGuiKey);
    }
    // endregion

    // =================================================================================================================
    // region Inputs: Mouse
    // =================================================================================================================

    /** Is the given mouse button currently held? */
    public static boolean isMouseDown(int imGuiMouseButton) {
        return cimgui_h.igIsMouseDown_Nil(imGuiMouseButton);
    }

    /** Did the mouse button go from not-pressed to pressed this frame? */
    public static boolean isMouseClicked(int imGuiMouseButton) {
        return isMouseClicked(imGuiMouseButton, false);
    }

    /** isMouseClicked with repeat support. */
    public static boolean isMouseClicked(int imGuiMouseButton, boolean repeat) {
        return cimgui_h.igIsMouseClicked_Bool(imGuiMouseButton, repeat);
    }

    /** Did the mouse button go from pressed to not-pressed this frame? */
    public static boolean isMouseReleased(int imGuiMouseButton) {
        return cimgui_h.igIsMouseReleased_Nil(imGuiMouseButton);
    }

    /** Did the mouse button double-click this frame? */
    public static boolean isMouseDoubleClicked(int imGuiMouseButton) {
        return cimgui_h.igIsMouseDoubleClicked_Nil(imGuiMouseButton);
    }

    /** Delayed mouse release — use sparingly for advanced interactions. */
    public static boolean isMouseReleasedWithDelay(int imGuiMouseButton, float delay) {
        return cimgui_h.igIsMouseReleasedWithDelay(imGuiMouseButton, delay);
    }

    /** Return the number of successive mouse clicks at the time of the last click event. */
    public static int getMouseClickedCount(int imGuiMouseButton) {
        return cimgui_h.igGetMouseClickedCount(imGuiMouseButton);
    }

    /**
     * Is the mouse hovering the given bounding rectangle (screen space)?
     * Clipped by current clip rect; ignores focus/window ordering.
     */
    public static boolean isMouseHoveringRect(float rMinX, float rMinY, float rMaxX, float rMaxY) {
        return isMouseHoveringRect(rMinX, rMinY, rMaxX, rMaxY, true);
    }

    /** isMouseHoveringRect with clip control. */
    public static boolean isMouseHoveringRect(float rMinX, float rMinY, float rMaxX, float rMaxY, boolean clip) {
        return cimgui_h.igIsMouseHoveringRect(imVec2(rMinX, rMinY), imVec2(rMaxX, rMaxY), clip);
    }

    /** Is the mouse position valid? (Convention: (-FLT_MAX, -FLT_MAX) means no mouse.) */
    public static boolean isMousePosValid() {
        return cimgui_h.igIsMousePosValid(MemorySegment.NULL);
    }

    /** Is any mouse button currently held? */
    public static boolean isAnyMouseDown() {
        return cimgui_h.igIsAnyMouseDown();
    }

    /** Get current mouse position (shortcut to {@code io.MousePos}). */
    public static Vec2f getMousePos() {
        var result = cimgui_h.igGetMousePos(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /** Get mouse position at the time the current popup was opened. */
    public static Vec2f getMousePosOnOpeningCurrentPopup() {
        var result = cimgui_h.igGetMousePosOnOpeningCurrentPopup(frameArena());
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /** Is the mouse dragging? Uses {@code io.MouseDraggingThreshold} if lock_threshold < 0. */
    public static boolean isMouseDragging(int imGuiMouseButton) {
        return isMouseDragging(imGuiMouseButton, -1f);
    }

    /** isMouseDragging with explicit lock threshold. */
    public static boolean isMouseDragging(int imGuiMouseButton, float lockThreshold) {
        return cimgui_h.igIsMouseDragging(imGuiMouseButton, lockThreshold);
    }

    /**
     * Return the delta from the initial click position while the mouse button is held or was just released.
     * Locked until the mouse moves past threshold.
     */
    public static Vec2f getMouseDragDelta() {
        return getMouseDragDelta(0, -1f);
    }

    /** getMouseDragDelta for the given button. */
    public static Vec2f getMouseDragDelta(int imGuiMouseButton) {
        return getMouseDragDelta(imGuiMouseButton, -1f);
    }

    /** getMouseDragDelta with explicit lock threshold. */
    public static Vec2f getMouseDragDelta(int imGuiMouseButton, float lockThreshold) {
        var result = cimgui_h.igGetMouseDragDelta(frameArena(), imGuiMouseButton, lockThreshold);
        return new Vec2f(ImVec2_c.x(result), ImVec2_c.y(result));
    }

    /** Reset the mouse drag delta for the left button. */
    public static void resetMouseDragDelta() {
        resetMouseDragDelta(0);
    }

    /** Reset the mouse drag delta for the given button. */
    public static void resetMouseDragDelta(int imGuiMouseButton) {
        cimgui_h.igResetMouseDragDelta(imGuiMouseButton);
    }

    /**
     * Get the desired mouse cursor shape.
     * @return {@code ImGuiMouseCursor}
     */
    public static int getMouseCursor() {
        return cimgui_h.igGetMouseCursor();
    }

    /**
     * Set the desired mouse cursor shape.
     * @param cursorType {@code ImGuiMouseCursor}
     */
    public static void setMouseCursor(int cursorType) {
        cimgui_h.igSetMouseCursor(cursorType);
    }

    /**
     * Override {@code io.WantCaptureMouse} for the next frame.
     * When true, instructs your app to ignore mouse input.
     */
    public static void setNextFrameWantCaptureMouse(boolean wantCaptureMouse) {
        cimgui_h.igSetNextFrameWantCaptureMouse(wantCaptureMouse);
    }
    // endregion

    // =================================================================================================================
    // region Clipboard Utilities
    // =================================================================================================================

    /** Get clipboard text. */
    public static String getClipboardText() {
        var ptr = cimgui_h.igGetClipboardText();
        return ptr.equals(MemorySegment.NULL) ? "" : ptr.getString(0);
    }

    /** Set clipboard text. */
    public static void setClipboardText(String text) {
        cimgui_h.igSetClipboardText(frameArena().allocateFrom(text));
    }
    // endregion

    // =================================================================================================================
    // region Settings / .ini Utilities
    // =================================================================================================================

    /**
     * Load settings from disk. Call after {@code CreateContext()} and before the first {@code NewFrame()}.
     */
    public static void loadIniSettingsFromDisk(String iniFilename) {
        cimgui_h.igLoadIniSettingsFromDisk(frameArena().allocateFrom(iniFilename));
    }

    /**
     * Load settings from a string. Call after {@code CreateContext()} and before the first {@code NewFrame()}.
     */
    public static void loadIniSettingsFromMemory(String iniData) {
        var arena = frameArena();
        cimgui_h.igLoadIniSettingsFromMemory(arena.allocateFrom(iniData), iniData.length());
    }

    /** Save settings to disk. Called automatically if {@code io.IniFilename} is set. */
    public static void saveIniSettingsToDisk(String iniFilename) {
        cimgui_h.igSaveIniSettingsToDisk(frameArena().allocateFrom(iniFilename));
    }

    /**
     * Return the current settings as a zero-terminated string.
     * Call when {@code io.WantSaveIniSettings} is set, then save and clear the flag.
     */
    public static String saveIniSettingsToMemory() {
        var ptr = cimgui_h.igSaveIniSettingsToMemory(MemorySegment.NULL);
        return ptr.equals(MemorySegment.NULL) ? "" : ptr.getString(0);
    }
    // endregion

    // =================================================================================================================
    // region Debug Utilities
    // =================================================================================================================

    /** Debug: show encoding tooltip for the given text. */
    public static void debugTextEncoding(String text) {
        cimgui_h.igDebugTextEncoding(frameArena().allocateFrom(text));
    }

    /** Debug: flash/highlight the given style color. */
    public static void debugFlashStyleColor(int imGuiColIdx) {
        cimgui_h.igDebugFlashStyleColor(imGuiColIdx);
    }

    /** Debug: activate the item picker tool (click an item to break into the debugger). */
    public static void debugStartItemPicker() {
        cimgui_h.igDebugStartItemPicker();
    }
    // endregion

    // -----------------------------------------------------------------------------------------------------------------
    // region Utility methods
    // -----------------------------------------------------------------------------------------------------------------

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
    // endregion
}
