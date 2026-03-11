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
    //endregion

    // =================================================================================================================
    // region Cached Invokers
    // =================================================================================================================

    private static final cimgui_h.igTextColored TEXT_COLORED = cimgui_h.igTextColored.makeInvoker();
    private static final cimgui_h.igTextDisabled TEXT_DISABLED = cimgui_h.igTextDisabled.makeInvoker();
    private static final cimgui_h.igTextWrapped TEXT_WRAPPED = cimgui_h.igTextWrapped.makeInvoker();
    private static final cimgui_h.igLabelText LABEL_TEXT = cimgui_h.igLabelText.makeInvoker();
    private static final cimgui_h.igBulletText BULLET_TEXT = cimgui_h.igBulletText.makeInvoker();
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
    // region TODO: Parameters stacks (shared)
    // =================================================================================================================

//    IMGUI_API void          PushStyleColor(ImGuiCol idx, ImU32 col);                        // modify a style color. always use this if you modify the style after NewFrame().
//    IMGUI_API void          PushStyleColor(ImGuiCol idx, const ImVec4& col);
//    IMGUI_API void          PopStyleColor(int count = 1);
//    IMGUI_API void          PushStyleVar(ImGuiStyleVar idx, float val);                     // modify a style float variable. always use this if you modify the style after NewFrame()!
//    IMGUI_API void          PushStyleVar(ImGuiStyleVar idx, const ImVec2& val);             // modify a style ImVec2 variable. "
//    IMGUI_API void          PushStyleVarX(ImGuiStyleVar idx, float val_x);                  // modify X component of a style ImVec2 variable. "
//    IMGUI_API void          PushStyleVarY(ImGuiStyleVar idx, float val_y);                  // modify Y component of a style ImVec2 variable. "
//    IMGUI_API void          PopStyleVar(int count = 1);
//    IMGUI_API void          PushItemFlag(ImGuiItemFlags option, bool enabled);              // modify specified shared item flag, e.g. PushItemFlag(ImGuiItemFlags_NoTabStop, true)
//    IMGUI_API void          PopItemFlag();
    // endregion

    // =================================================================================================================
    // region TODO: Parameters stacks (current window)
    // =================================================================================================================

//    IMGUI_API void          PushItemWidth(float item_width);                                // push width of items for common large "item+label" widgets. >0.0f: width in pixels, <0.0f align xx pixels to the right of window (so -FLT_MIN always align width to the right side).
//    IMGUI_API void          PopItemWidth();
//    IMGUI_API void          SetNextItemWidth(float item_width);                             // set width of the _next_ common large "item+label" widget. >0.0f: width in pixels, <0.0f align xx pixels to the right of window (so -FLT_MIN always align width to the right side)
//    IMGUI_API float         CalcItemWidth();                                                // width of item given pushed settings and current cursor position. NOT necessarily the width of last item unlike most 'Item' functions.
//    IMGUI_API void          PushTextWrapPos(float wrap_local_pos_x = 0.0f);                 // push word-wrapping position for Text*() commands. < 0.0f: no wrapping; 0.0f: wrap to end of window (or column); > 0.0f: wrap at 'wrap_pos_x' position in window local space
//    IMGUI_API void          PopTextWrapPos();
    // endregion

    // =================================================================================================================
    // region TODO: Style read access
    // - Use the ShowStyleEditor() function to interactively see/edit the colors.
    // =================================================================================================================

//    IMGUI_API ImVec2        GetFontTexUvWhitePixel();                                       // get UV coordinate for a white pixel, useful to draw custom shapes via the ImDrawList API
//    IMGUI_API ImU32         GetColorU32(ImGuiCol idx, float alpha_mul = 1.0f);              // retrieve given style color with style alpha applied and optional extra alpha multiplier, packed as a 32-bit value suitable for ImDrawList
//    IMGUI_API ImU32         GetColorU32(const ImVec4& col);                                 // retrieve given color with style alpha applied, packed as a 32-bit value suitable for ImDrawList
//    IMGUI_API ImU32         GetColorU32(ImU32 col, float alpha_mul = 1.0f);                 // retrieve given color with style alpha applied, packed as a 32-bit value suitable for ImDrawList
//    IMGUI_API const ImVec4& GetStyleColorVec4(ImGuiCol idx);                                // retrieve style color as stored in ImGuiStyle structure. use to feed back into PushStyleColor(), otherwise use GetColorU32() to get style color with style alpha baked in.
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
    // region TODO: Other layout functions
    // =================================================================================================================

//    IMGUI_API void          Separator();                                                    // separator, generally horizontal. inside a menu bar or in horizontal layout mode, this becomes a vertical separator.

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

//    IMGUI_API void          NewLine();                                                      // undo a SameLine() or force a new line when in a horizontal-layout context.
//    IMGUI_API void          Spacing();                                                      // add vertical spacing.
//    IMGUI_API void          Dummy(const ImVec2& size);                                      // add a dummy item of given size. unlike InvisibleButton(), Dummy() won't take the mouse click or be navigable into.
//    IMGUI_API void          Indent(float indent_w = 0.0f);                                  // move content position toward the right, by indent_w, or style.IndentSpacing if indent_w <= 0
//    IMGUI_API void          Unindent(float indent_w = 0.0f);                                // move content position back to the left, by indent_w, or style.IndentSpacing if indent_w <= 0
//    IMGUI_API void          BeginGroup();                                                   // lock horizontal starting position
//    IMGUI_API void          EndGroup();                                                     // unlock horizontal starting position + capture the whole group bounding box into one "item" (so you can use IsItemHovered() or layout primitives such as SameLine() on whole group, etc.)
//    IMGUI_API void          AlignTextToFramePadding();                                      // vertically align upcoming text baseline to FramePadding.y so that it will align properly to regularly framed items (call if you have text on a line before a framed item)
//    IMGUI_API float         GetTextLineHeight();                                            // ~ FontSize
//    IMGUI_API float         GetTextLineHeightWithSpacing();                                 // ~ FontSize + style.ItemSpacing.y (distance in pixels between 2 consecutive lines of text)
//    IMGUI_API float         GetFrameHeight();                                               // ~ FontSize + style.FramePadding.y * 2
//    IMGUI_API float         GetFrameHeightWithSpacing();                                    // ~ FontSize + style.FramePadding.y * 2 + style.ItemSpacing.y (distance in pixels between 2 consecutive lines of framed widgets)
    // endregion

    // =================================================================================================================
    // region TODO: ID stack/scopes
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

//    IMGUI_API void          PushID(const char* str_id);                                     // push string into the ID stack (will hash string).
//    IMGUI_API void          PushID(const char* str_id_begin, const char* str_id_end);       // push string into the ID stack (will hash string).
//    IMGUI_API void          PushID(const void* ptr_id);                                     // push pointer into the ID stack (will hash pointer).
//    IMGUI_API void          PushID(int int_id);                                             // push integer into the ID stack (will hash integer).
//    IMGUI_API void          PopID();                                                        // pop from the ID stack.
//    IMGUI_API ImGuiID       GetID(const char* str_id);                                      // calculate unique ID (hash of whole ID stack + given parameter). e.g. if you want to query into ImGuiStorage yourself
//    IMGUI_API ImGuiID       GetID(const char* str_id_begin, const char* str_id_end);
//    IMGUI_API ImGuiID       GetID(const void* ptr_id);
//    IMGUI_API ImGuiID       GetID(int int_id);
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
    // region TODO: Widgets: Main
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




    // TODO: continue from: https://github.com/ocornut/imgui/blob/master/imgui.h#L646
    // TODO: images, combo box / dropdown




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
