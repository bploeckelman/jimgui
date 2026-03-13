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
    // region TODO: Widgets: Combo Box (Dropdown)
    // - The BeginCombo()/EndCombo() api allows you to manage your contents and selection state however you want it, by creating e.g. Selectable() items.
    // - The old Combo() api are helpers over BeginCombo()/EndCombo() which are kept available for convenience purpose. This is analogous to how ListBox are created.
    // =================================================================================================================

//    IMGUI_API bool          BeginCombo(const char* label, const char* preview_value, ImGuiComboFlags flags = 0);
//    IMGUI_API void          EndCombo(); // only call EndCombo() if BeginCombo() returns true!
//    IMGUI_API bool          Combo(const char* label, int* current_item, const char* const items[], int items_count, int popup_max_height_in_items = -1);
//    IMGUI_API bool          Combo(const char* label, int* current_item, const char* items_separated_by_zeros, int popup_max_height_in_items = -1);      // Separate items with \0 within a string, end item-list with \0\0. e.g. "One\0Two\0Three\0"
//    IMGUI_API bool          Combo(const char* label, int* current_item, const char* (*getter)(void* user_data, int idx), void* user_data, int items_count, int popup_max_height_in_items = -1);
    // endregion

    // =================================================================================================================
    // region TODO: Widgets: Drag Sliders
    // - Ctrl+Click on any drag box to turn them into an input box. Manually input values aren't clamped by default and can go off-bounds. Use ImGuiSliderFlags_AlwaysClamp to always clamp.
    // - For all the Float2/Float3/Float4/Int2/Int3/Int4 versions of every function, note that a 'float v[X]' function argument is the same as 'float* v',
    //   the array syntax is just a way to document the number of elements that are expected to be accessible. You can pass address of your first element out of a contiguous set, e.g. &myvector.x
    // - Adjust format string to decorate the value with a prefix, a suffix, or adapt the editing and display precision e.g. "%.3f" -> 1.234; "%5.2f secs" -> 01.23 secs; "Biscuit: %.0f" -> Biscuit: 1; etc.
    // - Format string may also be set to NULL or use the default format ("%f" or "%d").
    // - Speed are per-pixel of mouse movement (v_speed=0.2f: mouse needs to move by 5 pixels to increase value by 1). For keyboard/gamepad navigation, minimum speed is Max(v_speed, minimum_step_at_given_precision).
    // - Use v_min < v_max to clamp edits to given limits. Note that Ctrl+Click manual input can override those limits if ImGuiSliderFlags_AlwaysClamp is not used.
    // - Use v_max = FLT_MAX / INT_MAX etc to avoid clamping to a maximum, same with v_min = -FLT_MAX / INT_MIN to avoid clamping to a minimum.
    // - We use the same sets of flags for DragXXX() and SliderXXX() functions as the features are the same and it makes it easier to swap them.
    // - Legacy: Pre-1.78 there are DragXXX() function signatures that take a final `float power=1.0f' argument instead of the `ImGuiSliderFlags flags=0' argument.
    //   If you get a warning converting a float to ImGuiSliderFlags, read https://github.com/ocornut/imgui/issues/3361
    // =================================================================================================================

//    IMGUI_API bool          DragFloat(const char* label, float* v, float v_speed = 1.0f, float v_min = 0.0f, float v_max = 0.0f, const char* format = "%.3f", ImGuiSliderFlags flags = 0);     // If v_min >= v_max we have no bound
//    IMGUI_API bool          DragFloat2(const char* label, float v[2], float v_speed = 1.0f, float v_min = 0.0f, float v_max = 0.0f, const char* format = "%.3f", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragFloat3(const char* label, float v[3], float v_speed = 1.0f, float v_min = 0.0f, float v_max = 0.0f, const char* format = "%.3f", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragFloat4(const char* label, float v[4], float v_speed = 1.0f, float v_min = 0.0f, float v_max = 0.0f, const char* format = "%.3f", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragFloatRange2(const char* label, float* v_current_min, float* v_current_max, float v_speed = 1.0f, float v_min = 0.0f, float v_max = 0.0f, const char* format = "%.3f", const char* format_max = NULL, ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragInt(const char* label, int* v, float v_speed = 1.0f, int v_min = 0, int v_max = 0, const char* format = "%d", ImGuiSliderFlags flags = 0);  // If v_min >= v_max we have no bound
//    IMGUI_API bool          DragInt2(const char* label, int v[2], float v_speed = 1.0f, int v_min = 0, int v_max = 0, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragInt3(const char* label, int v[3], float v_speed = 1.0f, int v_min = 0, int v_max = 0, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragInt4(const char* label, int v[4], float v_speed = 1.0f, int v_min = 0, int v_max = 0, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragIntRange2(const char* label, int* v_current_min, int* v_current_max, float v_speed = 1.0f, int v_min = 0, int v_max = 0, const char* format = "%d", const char* format_max = NULL, ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragScalar(const char* label, ImGuiDataType data_type, void* p_data, float v_speed = 1.0f, const void* p_min = NULL, const void* p_max = NULL, const char* format = NULL, ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          DragScalarN(const char* label, ImGuiDataType data_type, void* p_data, int components, float v_speed = 1.0f, const void* p_min = NULL, const void* p_max = NULL, const char* format = NULL, ImGuiSliderFlags flags = 0);
    // endregion

    // =================================================================================================================
    // region TODO: Widgets: Regular Sliders
    // - Ctrl+Click on any slider to turn them into an input box. Manually input values aren't clamped by default and can go off-bounds. Use ImGuiSliderFlags_AlwaysClamp to always clamp.
    // - Adjust format string to decorate the value with a prefix, a suffix, or adapt the editing and display precision e.g. "%.3f" -> 1.234; "%5.2f secs" -> 01.23 secs; "Biscuit: %.0f" -> Biscuit: 1; etc.
    // - Format string may also be set to NULL or use the default format ("%f" or "%d").
    // - Legacy: Pre-1.78 there are SliderXXX() function signatures that take a final `float power=1.0f' argument instead of the `ImGuiSliderFlags flags=0' argument.
    //   If you get a warning converting a float to ImGuiSliderFlags, read https://github.com/ocornut/imgui/issues/3361
    // =================================================================================================================

//    IMGUI_API bool          SliderFloat(const char* label, float* v, float v_min, float v_max, const char* format = "%.3f", ImGuiSliderFlags flags = 0);     // adjust format to decorate the value with a prefix or a suffix for in-slider labels or unit display.
//    IMGUI_API bool          SliderFloat2(const char* label, float v[2], float v_min, float v_max, const char* format = "%.3f", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderFloat3(const char* label, float v[3], float v_min, float v_max, const char* format = "%.3f", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderFloat4(const char* label, float v[4], float v_min, float v_max, const char* format = "%.3f", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderAngle(const char* label, float* v_rad, float v_degrees_min = -360.0f, float v_degrees_max = +360.0f, const char* format = "%.0f deg", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderInt(const char* label, int* v, int v_min, int v_max, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderInt2(const char* label, int v[2], int v_min, int v_max, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderInt3(const char* label, int v[3], int v_min, int v_max, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderInt4(const char* label, int v[4], int v_min, int v_max, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderScalar(const char* label, ImGuiDataType data_type, void* p_data, const void* p_min, const void* p_max, const char* format = NULL, ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          SliderScalarN(const char* label, ImGuiDataType data_type, void* p_data, int components, const void* p_min, const void* p_max, const char* format = NULL, ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          VSliderFloat(const char* label, const ImVec2& size, float* v, float v_min, float v_max, const char* format = "%.3f", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          VSliderInt(const char* label, const ImVec2& size, int* v, int v_min, int v_max, const char* format = "%d", ImGuiSliderFlags flags = 0);
//    IMGUI_API bool          VSliderScalar(const char* label, const ImVec2& size, ImGuiDataType data_type, void* p_data, const void* p_min, const void* p_max, const char* format = NULL, ImGuiSliderFlags flags = 0);
    // endregion

    // Widgets: Input with Keyboard
    // - If you want to use InputText() with std::string or any custom dynamic string type, use the wrapper in misc/cpp/imgui_stdlib.h/.cpp!
    // - Most of the ImGuiInputTextFlags flags are only useful for InputText() and not for InputFloatX, InputIntX, InputDouble etc.
//    IMGUI_API bool          InputText(const char* label, char* buf, size_t buf_size, ImGuiInputTextFlags flags = 0, ImGuiInputTextCallback callback = NULL, void* user_data = NULL);
//    IMGUI_API bool          InputTextMultiline(const char* label, char* buf, size_t buf_size, const ImVec2& size = ImVec2(0, 0), ImGuiInputTextFlags flags = 0, ImGuiInputTextCallback callback = NULL, void* user_data = NULL);
//    IMGUI_API bool          InputTextWithHint(const char* label, const char* hint, char* buf, size_t buf_size, ImGuiInputTextFlags flags = 0, ImGuiInputTextCallback callback = NULL, void* user_data = NULL);
//    IMGUI_API bool          InputFloat(const char* label, float* v, float step = 0.0f, float step_fast = 0.0f, const char* format = "%.3f", ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputFloat2(const char* label, float v[2], const char* format = "%.3f", ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputFloat3(const char* label, float v[3], const char* format = "%.3f", ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputFloat4(const char* label, float v[4], const char* format = "%.3f", ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputInt(const char* label, int* v, int step = 1, int step_fast = 100, ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputInt2(const char* label, int v[2], ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputInt3(const char* label, int v[3], ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputInt4(const char* label, int v[4], ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputDouble(const char* label, double* v, double step = 0.0, double step_fast = 0.0, const char* format = "%.6f", ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputScalar(const char* label, ImGuiDataType data_type, void* p_data, const void* p_step = NULL, const void* p_step_fast = NULL, const char* format = NULL, ImGuiInputTextFlags flags = 0);
//    IMGUI_API bool          InputScalarN(const char* label, ImGuiDataType data_type, void* p_data, int components, const void* p_step = NULL, const void* p_step_fast = NULL, const char* format = NULL, ImGuiInputTextFlags flags = 0);

    // Widgets: Color Editor/Picker (tip: the ColorEdit* functions have a little color square that can be left-clicked to open a picker, and right-clicked to open an option menu.)
    // - Note that in C++ a 'float v[X]' function argument is the _same_ as 'float* v', the array syntax is just a way to document the number of elements that are expected to be accessible.
    // - You can pass the address of a first float element out of a contiguous structure, e.g. &myvector.x
//    IMGUI_API bool          ColorEdit3(const char* label, float col[3], ImGuiColorEditFlags flags = 0);
//    IMGUI_API bool          ColorEdit4(const char* label, float col[4], ImGuiColorEditFlags flags = 0);
//    IMGUI_API bool          ColorPicker3(const char* label, float col[3], ImGuiColorEditFlags flags = 0);
//    IMGUI_API bool          ColorPicker4(const char* label, float col[4], ImGuiColorEditFlags flags = 0, const float* ref_col = NULL);
//    IMGUI_API bool          ColorButton(const char* desc_id, const ImVec4& col, ImGuiColorEditFlags flags = 0, const ImVec2& size = ImVec2(0, 0)); // display a color square/button, hover for details, return true when pressed.
//    IMGUI_API void          SetColorEditOptions(ImGuiColorEditFlags flags);                     // initialize current options (generally on application startup) if you want to select a default format, picker type, etc. User will be able to change many settings, unless you pass the _NoOptions flag to your calls.

    // Widgets: Trees
    // - TreeNode functions return true when the node is open, in which case you need to also call TreePop() when you are finished displaying the tree node contents.
//    IMGUI_API bool          TreeNode(const char* label);
//    IMGUI_API bool          TreeNode(const char* str_id, const char* fmt, ...) IM_FMTARGS(2);   // helper variation to easily decorrelate the id from the displayed string. Read the FAQ about why and how to use ID. to align arbitrary text at the same level as a TreeNode() you can use Bullet().
//    IMGUI_API bool          TreeNode(const void* ptr_id, const char* fmt, ...) IM_FMTARGS(2);   // "
//    IMGUI_API bool          TreeNodeV(const char* str_id, const char* fmt, va_list args) IM_FMTLIST(2);
//    IMGUI_API bool          TreeNodeV(const void* ptr_id, const char* fmt, va_list args) IM_FMTLIST(2);
//    IMGUI_API bool          TreeNodeEx(const char* label, ImGuiTreeNodeFlags flags = 0);
//    IMGUI_API bool          TreeNodeEx(const char* str_id, ImGuiTreeNodeFlags flags, const char* fmt, ...) IM_FMTARGS(3);
//    IMGUI_API bool          TreeNodeEx(const void* ptr_id, ImGuiTreeNodeFlags flags, const char* fmt, ...) IM_FMTARGS(3);
//    IMGUI_API bool          TreeNodeExV(const char* str_id, ImGuiTreeNodeFlags flags, const char* fmt, va_list args) IM_FMTLIST(3);
//    IMGUI_API bool          TreeNodeExV(const void* ptr_id, ImGuiTreeNodeFlags flags, const char* fmt, va_list args) IM_FMTLIST(3);
//    IMGUI_API void          TreePush(const char* str_id);                                       // ~ Indent()+PushID(). Already called by TreeNode() when returning true, but you can call TreePush/TreePop yourself if desired.
//    IMGUI_API void          TreePush(const void* ptr_id);                                       // "
//    IMGUI_API void          TreePop();                                                          // ~ Unindent()+PopID()
//    IMGUI_API float         GetTreeNodeToLabelSpacing();                                        // horizontal distance preceding label when using TreeNode*() or Bullet() == (g.FontSize + style.FramePadding.x*2) for a regular unframed TreeNode
//    IMGUI_API bool          CollapsingHeader(const char* label, ImGuiTreeNodeFlags flags = 0);  // if returning 'true' the header is open. doesn't indent nor push on ID stack. user doesn't have to call TreePop().
//    IMGUI_API bool          CollapsingHeader(const char* label, bool* p_visible, ImGuiTreeNodeFlags flags = 0); // when 'p_visible != NULL': if '*p_visible==true' display an additional small close button on upper right of the header which will set the bool to false when clicked, if '*p_visible==false' don't display the header.
//    IMGUI_API void          SetNextItemOpen(bool is_open, ImGuiCond cond = 0);                  // set next TreeNode/CollapsingHeader open state.
//    IMGUI_API void          SetNextItemStorageID(ImGuiID storage_id);                           // set id to use for open/close storage (default to same as item id).
//    IMGUI_API bool          TreeNodeGetOpen(ImGuiID storage_id);                                // retrieve tree node open/close state.

    // Widgets: Selectables
    // - A selectable highlights when hovered, and can display another color when selected.
    // - Neighbors selectable extend their highlight bounds in order to leave no gap between them. This is so a series of selected Selectable appear contiguous.
//    IMGUI_API bool          Selectable(const char* label, bool selected = false, ImGuiSelectableFlags flags = 0, const ImVec2& size = ImVec2(0, 0)); // "bool selected" carry the selection state (read-only). Selectable() is clicked is returns true so you can modify your selection state. size.x==0.0: use remaining width, size.x>0.0: specify width. size.y==0.0: use label height, size.y>0.0: specify height
//    IMGUI_API bool          Selectable(const char* label, bool* p_selected, ImGuiSelectableFlags flags = 0, const ImVec2& size = ImVec2(0, 0));      // "bool* p_selected" point to the selection state (read-write), as a convenient helper.

    // Multi-selection system for Selectable(), Checkbox(), TreeNode() functions [BETA]
    // - This enables standard multi-selection/range-selection idioms (Ctrl+Mouse/Keyboard, Shift+Mouse/Keyboard, etc.) in a way that also allow a clipper to be used.
    // - ImGuiSelectionUserData is often used to store your item index within the current view (but may store something else).
    // - Read comments near ImGuiMultiSelectIO for instructions/details and see 'Demo->Widgets->Selection State & Multi-Select' for demo.
    // - TreeNode() is technically supported but... using this correctly is more complicated. You need some sort of linear/random access to your tree,
    //   which is suited to advanced trees setups already implementing filters and clipper. We will work simplifying the current demo.
    // - 'selection_size' and 'items_count' parameters are optional and used by a few features. If they are costly for you to compute, you may avoid them.
//    IMGUI_API ImGuiMultiSelectIO*   BeginMultiSelect(ImGuiMultiSelectFlags flags, int selection_size = -1, int items_count = -1);
//    IMGUI_API ImGuiMultiSelectIO*   EndMultiSelect();
//    IMGUI_API void                  SetNextItemSelectionUserData(ImGuiSelectionUserData selection_user_data);
//    IMGUI_API bool                  IsItemToggledSelection();                                   // Was the last item selection state toggled? Useful if you need the per-item information _before_ reaching EndMultiSelect(). We only returns toggle _event_ in order to handle clipping correctly.

    // Widgets: List Boxes
    // - This is essentially a thin wrapper to using BeginChild/EndChild with the ImGuiChildFlags_FrameStyle flag for stylistic changes + displaying a label.
    // - If you don't need a label you can probably simply use BeginChild() with the ImGuiChildFlags_FrameStyle flag for the same result.
    // - You can submit contents and manage your selection state however you want it, by creating e.g. Selectable() or any other items.
    // - The simplified/old ListBox() api are helpers over BeginListBox()/EndListBox() which are kept available for convenience purpose. This is analogous to how Combos are created.
    // - Choose frame width:   size.x > 0.0f: custom  /  size.x < 0.0f or -FLT_MIN: right-align   /  size.x = 0.0f (default): use current ItemWidth
    // - Choose frame height:  size.y > 0.0f: custom  /  size.y < 0.0f or -FLT_MIN: bottom-align  /  size.y = 0.0f (default): arbitrary default height which can fit ~7 items
//    IMGUI_API bool          BeginListBox(const char* label, const ImVec2& size = ImVec2(0, 0)); // open a framed scrolling region
//    IMGUI_API void          EndListBox();                                                       // only call EndListBox() if BeginListBox() returned true!
//    IMGUI_API bool          ListBox(const char* label, int* current_item, const char* const items[], int items_count, int height_in_items = -1);
//    IMGUI_API bool          ListBox(const char* label, int* current_item, const char* (*getter)(void* user_data, int idx), void* user_data, int items_count, int height_in_items = -1);

    // Widgets: Data Plotting
    // - Consider using ImPlot (https://github.com/epezent/implot) which is much better!
//    IMGUI_API void          PlotLines(const char* label, const float* values, int values_count, int values_offset = 0, const char* overlay_text = NULL, float scale_min = FLT_MAX, float scale_max = FLT_MAX, ImVec2 graph_size = ImVec2(0, 0), int stride = sizeof(float));
//    IMGUI_API void          PlotLines(const char* label, float(*values_getter)(void* data, int idx), void* data, int values_count, int values_offset = 0, const char* overlay_text = NULL, float scale_min = FLT_MAX, float scale_max = FLT_MAX, ImVec2 graph_size = ImVec2(0, 0));
//    IMGUI_API void          PlotHistogram(const char* label, const float* values, int values_count, int values_offset = 0, const char* overlay_text = NULL, float scale_min = FLT_MAX, float scale_max = FLT_MAX, ImVec2 graph_size = ImVec2(0, 0), int stride = sizeof(float));
//    IMGUI_API void          PlotHistogram(const char* label, float (*values_getter)(void* data, int idx), void* data, int values_count, int values_offset = 0, const char* overlay_text = NULL, float scale_min = FLT_MAX, float scale_max = FLT_MAX, ImVec2 graph_size = ImVec2(0, 0));

    // Widgets: Value() Helpers.
    // - Those are merely shortcut to calling Text() with a format string. Output single value in "name: value" format (tip: freely declare more in your code to handle your types. you can add functions to the ImGui namespace)
//    IMGUI_API void          Value(const char* prefix, bool b);
//    IMGUI_API void          Value(const char* prefix, int v);
//    IMGUI_API void          Value(const char* prefix, unsigned int v);
//    IMGUI_API void          Value(const char* prefix, float v, const char* float_format = NULL);

    // Widgets: Menus
    // - Use BeginMenuBar() on a window ImGuiWindowFlags_MenuBar to append to its menu bar.
    // - Use BeginMainMenuBar() to create a menu bar at the top of the screen and append to it.
    // - Use BeginMenu() to create a menu. You can call BeginMenu() multiple time with the same identifier to append more items to it.
    // - Not that MenuItem() keyboardshortcuts are displayed as a convenience but _not processed_ by Dear ImGui at the moment.
//    IMGUI_API bool          BeginMenuBar();                                                     // append to menu-bar of current window (requires ImGuiWindowFlags_MenuBar flag set on parent window).
//    IMGUI_API void          EndMenuBar();                                                       // only call EndMenuBar() if BeginMenuBar() returns true!
//    IMGUI_API bool          BeginMainMenuBar();                                                 // create and append to a full screen menu-bar.
//    IMGUI_API void          EndMainMenuBar();                                                   // only call EndMainMenuBar() if BeginMainMenuBar() returns true!
//    IMGUI_API bool          BeginMenu(const char* label, bool enabled = true);                  // create a sub-menu entry. only call EndMenu() if this returns true!
//    IMGUI_API void          EndMenu();                                                          // only call EndMenu() if BeginMenu() returns true!
//    IMGUI_API bool          MenuItem(const char* label, const char* shortcut = NULL, bool selected = false, bool enabled = true);  // return true when activated.
//    IMGUI_API bool          MenuItem(const char* label, const char* shortcut, bool* p_selected, bool enabled = true);              // return true when activated + toggle (*p_selected) if p_selected != NULL

    // Tooltips
    // - Tooltips are windows following the mouse. They do not take focus away.
    // - A tooltip window can contain items of any types.
    // - SetTooltip() is more or less a shortcut for the 'if (BeginTooltip()) { Text(...); EndTooltip(); }' idiom (with a subtlety that it discard any previously submitted tooltip)
//    IMGUI_API bool          BeginTooltip();                                                     // begin/append a tooltip window.
//    IMGUI_API void          EndTooltip();                                                       // only call EndTooltip() if BeginTooltip()/BeginItemTooltip() returns true!
//    IMGUI_API void          SetTooltip(const char* fmt, ...) IM_FMTARGS(1);                     // set a text-only tooltip. Often used after a ImGui::IsItemHovered() check. Override any previous call to SetTooltip().
//    IMGUI_API void          SetTooltipV(const char* fmt, va_list args) IM_FMTLIST(1);

    // Tooltips: helpers for showing a tooltip when hovering an item
    // - BeginItemTooltip() is a shortcut for the 'if (IsItemHovered(ImGuiHoveredFlags_ForTooltip) && BeginTooltip())' idiom.
    // - SetItemTooltip() is a shortcut for the 'if (IsItemHovered(ImGuiHoveredFlags_ForTooltip)) { SetTooltip(...); }' idiom.
    // - Where 'ImGuiHoveredFlags_ForTooltip' itself is a shortcut to use 'style.HoverFlagsForTooltipMouse' or 'style.HoverFlagsForTooltipNav' depending on active input type. For mouse it defaults to 'ImGuiHoveredFlags_Stationary | ImGuiHoveredFlags_DelayShort'.
//    IMGUI_API bool          BeginItemTooltip();                                                 // begin/append a tooltip window if preceding item was hovered.
//    IMGUI_API void          SetItemTooltip(const char* fmt, ...) IM_FMTARGS(1);                 // set a text-only tooltip if preceding item was hovered. override any previous call to SetTooltip().
//    IMGUI_API void          SetItemTooltipV(const char* fmt, va_list args) IM_FMTLIST(1);

    // Popups, Modals
    //  - They block normal mouse hovering detection (and therefore most mouse interactions) behind them.
    //  - If not modal: they can be closed by clicking anywhere outside them, or by pressing ESCAPE.
    //  - Their visibility state (~bool) is held internally instead of being held by the programmer as we are used to with regular Begin*() calls.
    //  - The 3 properties above are related: we need to retain popup visibility state in the library because popups may be closed as any time.
    //  - You can bypass the hovering restriction by using ImGuiHoveredFlags_AllowWhenBlockedByPopup when calling IsItemHovered() or IsWindowHovered().
    //  - IMPORTANT: Popup identifiers are relative to the current ID stack, so OpenPopup and BeginPopup generally needs to be at the same level of the stack.
    //    This is sometimes leading to confusing mistakes. May rework this in the future.
    //  - BeginPopup(): query popup state, if open start appending into the window. Call EndPopup() afterwards if returned true. ImGuiWindowFlags are forwarded to the window.
    //  - BeginPopupModal(): block every interaction behind the window, cannot be closed by user, add a dimming background, has a title bar.
//    IMGUI_API bool          BeginPopup(const char* str_id, ImGuiWindowFlags flags = 0);                         // return true if the popup is open, and you can start outputting to it.
//    IMGUI_API bool          BeginPopupModal(const char* name, bool* p_open = NULL, ImGuiWindowFlags flags = 0); // return true if the modal is open, and you can start outputting to it.
//    IMGUI_API void          EndPopup();                                                                         // only call EndPopup() if BeginPopupXXX() returns true!

    // Popups: open/close functions
    //  - OpenPopup(): set popup state to open. ImGuiPopupFlags are available for opening options.
    //  - If not modal: they can be closed by clicking anywhere outside them, or by pressing ESCAPE.
    //  - CloseCurrentPopup(): use inside the BeginPopup()/EndPopup() scope to close manually.
    //  - CloseCurrentPopup() is called by default by Selectable()/MenuItem() when activated (FIXME: need some options).
    //  - Use ImGuiPopupFlags_NoOpenOverExistingPopup to avoid opening a popup if there's already one at the same level. This is equivalent to e.g. testing for !IsAnyPopupOpen() prior to OpenPopup().
    //  - Use IsWindowAppearing() after BeginPopup() to tell if a window just opened.
//    IMGUI_API void          OpenPopup(const char* str_id, ImGuiPopupFlags popup_flags = 0);                     // call to mark popup as open (don't call every frame!).
//    IMGUI_API void          OpenPopup(ImGuiID id, ImGuiPopupFlags popup_flags = 0);                             // id overload to facilitate calling from nested stacks
//    IMGUI_API void          OpenPopupOnItemClick(const char* str_id = NULL, ImGuiPopupFlags popup_flags = 0);   // helper to open popup when clicked on last item. Default to ImGuiPopupFlags_MouseButtonRight == 1. (note: actually triggers on the mouse _released_ event to be consistent with popup behaviors)
//    IMGUI_API void          CloseCurrentPopup();                                                                // manually close the popup we have begin-ed into.

    // Popups: Open+Begin popup combined functions helpers to create context menus.
    //  - Helpers to do OpenPopup+BeginPopup where the Open action is triggered by e.g. hovering an item and right-clicking.
    //  - IMPORTANT: Notice that BeginPopupContextXXX takes ImGuiPopupFlags just like OpenPopup() and unlike BeginPopup(). For full consistency, we may add ImGuiWindowFlags to the BeginPopupContextXXX functions in the future.
    //  - IMPORTANT: If you ever used the left mouse button with BeginPopupContextXXX() helpers before 1.92.6:
    //    - Before this version, OpenPopupOnItemClick(), BeginPopupContextItem(), BeginPopupContextWindow(), BeginPopupContextVoid() had 'a ImGuiPopupFlags popup_flags = 1' default value in their function signature.
    //    - Before: Explicitly passing a literal 0 meant ImGuiPopupFlags_MouseButtonLeft. The default = 1 meant ImGuiPopupFlags_MouseButtonRight.
    //    - After: The default = 0 means ImGuiPopupFlags_MouseButtonRight. Explicitly passing a literal 1 also means ImGuiPopupFlags_MouseButtonRight (if legacy behavior are enabled) or will assert (if legacy behavior are disabled).
    //    - TL;DR: if you don't want to use right mouse button for popups, always specify it explicitly using a named ImGuiPopupFlags_MouseButtonXXXX value.
    //    - Read "API BREAKING CHANGES" 2026/01/07 (1.92.6) entry in imgui.cpp or GitHub topic #9157 for all details.
//    IMGUI_API bool          BeginPopupContextItem(const char* str_id = NULL, ImGuiPopupFlags popup_flags = 0);  // open+begin popup when clicked on last item. Use str_id==NULL to associate the popup to previous item. If you want to use that on a non-interactive item such as Text() you need to pass in an explicit ID here. read comments in .cpp!
//    IMGUI_API bool          BeginPopupContextWindow(const char* str_id = NULL, ImGuiPopupFlags popup_flags = 0);// open+begin popup when clicked on current window.
//    IMGUI_API bool          BeginPopupContextVoid(const char* str_id = NULL, ImGuiPopupFlags popup_flags = 0);  // open+begin popup when clicked in void (where there are no windows).

    // Popups: query functions
    //  - IsPopupOpen(): return true if the popup is open at the current BeginPopup() level of the popup stack.
    //  - IsPopupOpen() with ImGuiPopupFlags_AnyPopupId: return true if any popup is open at the current BeginPopup() level of the popup stack.
    //  - IsPopupOpen() with ImGuiPopupFlags_AnyPopupId + ImGuiPopupFlags_AnyPopupLevel: return true if any popup is open.
//    IMGUI_API bool          IsPopupOpen(const char* str_id, ImGuiPopupFlags flags = 0);                         // return true if the popup is open.

    // Tables
    // - Full-featured replacement for old Columns API.
    // - See Demo->Tables for demo code. See top of imgui_tables.cpp for general commentary.
    // - See ImGuiTableFlags_ and ImGuiTableColumnFlags_ enums for a description of available flags.
    // The typical call flow is:
    // - 1. Call BeginTable(), early out if returning false.
    // - 2. Optionally call TableSetupColumn() to submit column name/flags/defaults.
    // - 3. Optionally call TableSetupScrollFreeze() to request scroll freezing of columns/rows.
    // - 4. Optionally call TableHeadersRow() to submit a header row. Names are pulled from TableSetupColumn() data.
    // - 5. Populate contents:
    //    - In most situations you can use TableNextRow() + TableSetColumnIndex(N) to start appending into a column.
    //    - If you are using tables as a sort of grid, where every column is holding the same type of contents,
    //      you may prefer using TableNextColumn() instead of TableNextRow() + TableSetColumnIndex().
    //      TableNextColumn() will automatically wrap-around into the next row if needed.
    //    - IMPORTANT: Comparatively to the old Columns() API, we need to call TableNextColumn() for the first column!
    //    - Summary of possible call flow:
    //        - TableNextRow() -> TableSetColumnIndex(0) -> Text("Hello 0") -> TableSetColumnIndex(1) -> Text("Hello 1")  // OK
    //        - TableNextRow() -> TableNextColumn()      -> Text("Hello 0") -> TableNextColumn()      -> Text("Hello 1")  // OK
    //        -                   TableNextColumn()      -> Text("Hello 0") -> TableNextColumn()      -> Text("Hello 1")  // OK: TableNextColumn() automatically gets to next row!
    //        - TableNextRow()                           -> Text("Hello 0")                                               // Not OK! Missing TableSetColumnIndex() or TableNextColumn()! Text will not appear!
    // - 5. Call EndTable()
//    IMGUI_API bool          BeginTable(const char* str_id, int columns, ImGuiTableFlags flags = 0, const ImVec2& outer_size = ImVec2(0.0f, 0.0f), float inner_width = 0.0f);
//    IMGUI_API void          EndTable();                                         // only call EndTable() if BeginTable() returns true!
//    IMGUI_API void          TableNextRow(ImGuiTableRowFlags row_flags = 0, float min_row_height = 0.0f); // append into the first cell of a new row. 'min_row_height' include the minimum top and bottom padding aka CellPadding.y * 2.0f.
//    IMGUI_API bool          TableNextColumn();                                  // append into the next column (or first column of next row if currently in last column). Return true when column is visible.
//    IMGUI_API bool          TableSetColumnIndex(int column_n);                  // append into the specified column. Return true when column is visible.

    // Tables: Headers & Columns declaration
    // - Use TableSetupColumn() to specify label, resizing policy, default width/weight, id, various other flags etc.
    // - Use TableHeadersRow() to create a header row and automatically submit a TableHeader() for each column.
    //   Headers are required to perform: reordering, sorting, and opening the context menu.
    //   The context menu can also be made available in columns body using ImGuiTableFlags_ContextMenuInBody.
    // - You may manually submit headers using TableNextRow() + TableHeader() calls, but this is only useful in
    //   some advanced use cases (e.g. adding custom widgets in header row).
    // - Use TableSetupScrollFreeze() to lock columns/rows so they stay visible when scrolled.
//    IMGUI_API void          TableSetupColumn(const char* label, ImGuiTableColumnFlags flags = 0, float init_width_or_weight = 0.0f, ImGuiID user_id = 0);
//    IMGUI_API void          TableSetupScrollFreeze(int cols, int rows);         // lock columns/rows so they stay visible when scrolled.
//    IMGUI_API void          TableHeader(const char* label);                     // submit one header cell manually (rarely used)
//    IMGUI_API void          TableHeadersRow();                                  // submit a row with headers cells based on data provided to TableSetupColumn() + submit context menu
//    IMGUI_API void          TableAngledHeadersRow();                            // submit a row with angled headers for every column with the ImGuiTableColumnFlags_AngledHeader flag. MUST BE FIRST ROW.

    // Tables: Sorting & Miscellaneous functions
    // - Sorting: call TableGetSortSpecs() to retrieve latest sort specs for the table. NULL when not sorting.
    //   When 'sort_specs->SpecsDirty == true' you should sort your data. It will be true when sorting specs have
    //   changed since last call, or the first time. Make sure to set 'SpecsDirty = false' after sorting,
    //   else you may wastefully sort your data every frame!
    // - Functions args 'int column_n' treat the default value of -1 as the same as passing the current column index.
//    IMGUI_API ImGuiTableSortSpecs*  TableGetSortSpecs();                        // get latest sort specs for the table (NULL if not sorting).  Lifetime: don't hold on this pointer over multiple frames or past any subsequent call to BeginTable().
//    IMGUI_API int                   TableGetColumnCount();                      // return number of columns (value passed to BeginTable)
//    IMGUI_API int                   TableGetColumnIndex();                      // return current column index.
//    IMGUI_API int                   TableGetRowIndex();                         // return current row index (header rows are accounted for)
//    IMGUI_API const char*           TableGetColumnName(int column_n = -1);      // return "" if column didn't have a name declared by TableSetupColumn(). Pass -1 to use current column.
//    IMGUI_API ImGuiTableColumnFlags TableGetColumnFlags(int column_n = -1);     // return column flags so you can query their Enabled/Visible/Sorted/Hovered status flags. Pass -1 to use current column.
//    IMGUI_API void                  TableSetColumnEnabled(int column_n, bool v);// change user accessible enabled/disabled state of a column. Set to false to hide the column. User can use the context menu to change this themselves (right-click in headers, or right-click in columns body with ImGuiTableFlags_ContextMenuInBody)
//    IMGUI_API int                   TableGetHoveredColumn();                    // return hovered column. return -1 when table is not hovered. return columns_count if the unused space at the right of visible columns is hovered. Can also use (TableGetColumnFlags() & ImGuiTableColumnFlags_IsHovered) instead.
//    IMGUI_API void                  TableSetBgColor(ImGuiTableBgTarget target, ImU32 color, int column_n = -1);  // change the color of a cell, row, or column. See ImGuiTableBgTarget_ flags for details.

    // Legacy Columns API (prefer using Tables!)
    // - You can also use SameLine(pos_x) to mimic simplified columns.
//    IMGUI_API void          Columns(int count = 1, const char* id = NULL, bool borders = true);
//    IMGUI_API void          NextColumn();                                                       // next column, defaults to current row or next row if the current row is finished
//    IMGUI_API int           GetColumnIndex();                                                   // get current column index
//    IMGUI_API float         GetColumnWidth(int column_index = -1);                              // get column width (in pixels). pass -1 to use current column
//    IMGUI_API void          SetColumnWidth(int column_index, float width);                      // set column width (in pixels). pass -1 to use current column
//    IMGUI_API float         GetColumnOffset(int column_index = -1);                             // get position of column line (in pixels, from the left side of the contents region). pass -1 to use current column, otherwise 0..GetColumnsCount() inclusive. column 0 is typically 0.0f
//    IMGUI_API void          SetColumnOffset(int column_index, float offset_x);                  // set position of column line (in pixels, from the left side of the contents region). pass -1 to use current column
//    IMGUI_API int           GetColumnsCount();

    // Tab Bars, Tabs
    // - Note: Tabs are automatically created by the docking system (when in 'docking' branch). Use this to create tab bars/tabs yourself.
//    IMGUI_API bool          BeginTabBar(const char* str_id, ImGuiTabBarFlags flags = 0);        // create and append into a TabBar
//    IMGUI_API void          EndTabBar();                                                        // only call EndTabBar() if BeginTabBar() returns true!
//    IMGUI_API bool          BeginTabItem(const char* label, bool* p_open = NULL, ImGuiTabItemFlags flags = 0); // create a Tab. Returns true if the Tab is selected.
//    IMGUI_API void          EndTabItem();                                                       // only call EndTabItem() if BeginTabItem() returns true!
//    IMGUI_API bool          TabItemButton(const char* label, ImGuiTabItemFlags flags = 0);      // create a Tab behaving like a button. return true when clicked. cannot be selected in the tab bar.
//    IMGUI_API void          SetTabItemClosed(const char* tab_or_docked_window_label);           // notify TabBar or Docking system of a closed tab/window ahead (useful to reduce visual flicker on reorderable tab bars). For tab-bar: call after BeginTabBar() and before Tab submissions. Otherwise call with a window name.

    // Logging/Capture
    // - All text output from the interface can be captured into tty/file/clipboard. By default, tree nodes are automatically opened during logging.
//    IMGUI_API void          LogToTTY(int auto_open_depth = -1);                                 // start logging to tty (stdout)
//    IMGUI_API void          LogToFile(int auto_open_depth = -1, const char* filename = NULL);   // start logging to file
//    IMGUI_API void          LogToClipboard(int auto_open_depth = -1);                           // start logging to OS clipboard
//    IMGUI_API void          LogFinish();                                                        // stop logging (close file, etc.)
//    IMGUI_API void          LogButtons();                                                       // helper to display buttons for logging to tty/file/clipboard
//    IMGUI_API void          LogText(const char* fmt, ...) IM_FMTARGS(1);                        // pass text data straight to log (without being displayed)
//    IMGUI_API void          LogTextV(const char* fmt, va_list args) IM_FMTLIST(1);

    // Drag and Drop
    // - On source items, call BeginDragDropSource(), if it returns true also call SetDragDropPayload() + EndDragDropSource().
    // - On target candidates, call BeginDragDropTarget(), if it returns true also call AcceptDragDropPayload() + EndDragDropTarget().
    // - If you stop calling BeginDragDropSource() the payload is preserved however it won't have a preview tooltip (we currently display a fallback "..." tooltip, see #1725)
    // - An item can be both drag source and drop target.
//    IMGUI_API bool          BeginDragDropSource(ImGuiDragDropFlags flags = 0);                                      // call after submitting an item which may be dragged. when this return true, you can call SetDragDropPayload() + EndDragDropSource()
//    IMGUI_API bool          SetDragDropPayload(const char* type, const void* data, size_t sz, ImGuiCond cond = 0);  // type is a user defined string of maximum 32 characters. Strings starting with '_' are reserved for dear imgui internal types. Data is copied and held by imgui. Return true when payload has been accepted.
//    IMGUI_API void          EndDragDropSource();                                                                    // only call EndDragDropSource() if BeginDragDropSource() returns true!
//    IMGUI_API bool                  BeginDragDropTarget();                                                          // call after submitting an item that may receive a payload. If this returns true, you can call AcceptDragDropPayload() + EndDragDropTarget()
//    IMGUI_API const ImGuiPayload*   AcceptDragDropPayload(const char* type, ImGuiDragDropFlags flags = 0);          // accept contents of a given type. If ImGuiDragDropFlags_AcceptBeforeDelivery is set you can peek into the payload before the mouse button is released.
//    IMGUI_API void                  EndDragDropTarget();                                                            // only call EndDragDropTarget() if BeginDragDropTarget() returns true!
//    IMGUI_API const ImGuiPayload*   GetDragDropPayload();                                                           // peek directly into the current payload from anywhere. returns NULL when drag and drop is finished or inactive. use ImGuiPayload::IsDataType() to test for the payload type.

    // Disabling [BETA API]
    // - Disable all user interactions and dim items visuals (applying style.DisabledAlpha over current colors)
    // - Those can be nested but it cannot be used to enable an already disabled section (a single BeginDisabled(true) in the stack is enough to keep everything disabled)
    // - Tooltips windows are automatically opted out of disabling. Note that IsItemHovered() by default returns false on disabled items, unless using ImGuiHoveredFlags_AllowWhenDisabled.
    // - BeginDisabled(false)/EndDisabled() essentially does nothing but is provided to facilitate use of boolean expressions (as a micro-optimization: if you have tens of thousands of BeginDisabled(false)/EndDisabled() pairs, you might want to reformulate your code to avoid making those calls)
//    IMGUI_API void          BeginDisabled(bool disabled = true);
//    IMGUI_API void          EndDisabled();

    // Clipping
    // - Mouse hovering is affected by ImGui::PushClipRect() calls, unlike direct calls to ImDrawList::PushClipRect() which are render only.
//    IMGUI_API void          PushClipRect(const ImVec2& clip_rect_min, const ImVec2& clip_rect_max, bool intersect_with_current_clip_rect);
//    IMGUI_API void          PopClipRect();

    // Focus, Activation
//    IMGUI_API void          SetItemDefaultFocus();                                              // make last item the default focused item of a newly appearing window.
//    IMGUI_API void          SetKeyboardFocusHere(int offset = 0);                               // focus keyboard on the next widget. Use positive 'offset' to access sub components of a multiple component widget. Use -1 to access previous widget.

    // Keyboard/Gamepad Navigation
//    IMGUI_API void          SetNavCursorVisible(bool visible);                                  // alter visibility of keyboard/gamepad cursor. by default: show when using an arrow key, hide when clicking with mouse.

    // Overlapping mode
//    IMGUI_API void          SetNextItemAllowOverlap();                                          // allow next item to be overlapped by a subsequent item. Useful with invisible buttons, selectable, treenode covering an area where subsequent items may need to be added. Note that both Selectable() and TreeNode() have dedicated flags doing this.

    // Item/Widgets Utilities and Query Functions
    // - Most of the functions are referring to the previous Item that has been submitted.
    // - See Demo Window under "Widgets->Querying Status" for an interactive visualization of most of those functions.
//    IMGUI_API bool          IsItemHovered(ImGuiHoveredFlags flags = 0);                         // is the last item hovered? (and usable, aka not blocked by a popup, etc.). See ImGuiHoveredFlags for more options.
//    IMGUI_API bool          IsItemActive();                                                     // is the last item active? (e.g. button being held, text field being edited. This will continuously return true while holding mouse button on an item. Items that don't interact will always return false)
//    IMGUI_API bool          IsItemFocused();                                                    // is the last item focused for keyboard/gamepad navigation?
//    IMGUI_API bool          IsItemClicked(ImGuiMouseButton mouse_button = 0);                   // is the last item hovered and mouse clicked on? (**)  == IsMouseClicked(mouse_button) && IsItemHovered()Important. (**) this is NOT equivalent to the behavior of e.g. Button(). Read comments in function definition.
//    IMGUI_API bool          IsItemVisible();                                                    // is the last item visible? (items may be out of sight because of clipping/scrolling)
//    IMGUI_API bool          IsItemEdited();                                                     // did the last item modify its underlying value this frame? or was pressed? This is generally the same as the "bool" return value of many widgets.
//    IMGUI_API bool          IsItemActivated();                                                  // was the last item just made active (item was previously inactive).
//    IMGUI_API bool          IsItemDeactivated();                                                // was the last item just made inactive (item was previously active). Useful for Undo/Redo patterns with widgets that require continuous editing.
//    IMGUI_API bool          IsItemDeactivatedAfterEdit();                                       // was the last item just made inactive and made a value change when it was active? (e.g. Slider/Drag moved). Useful for Undo/Redo patterns with widgets that require continuous editing. Note that you may get false positives (some widgets such as Combo()/ListBox()/Selectable() will return true even when clicking an already selected item).
//    IMGUI_API bool          IsItemToggledOpen();                                                // was the last item open state toggled? set by TreeNode().
//    IMGUI_API bool          IsAnyItemHovered();                                                 // is any item hovered?
//    IMGUI_API bool          IsAnyItemActive();                                                  // is any item active?
//    IMGUI_API bool          IsAnyItemFocused();                                                 // is any item focused?
//    IMGUI_API ImGuiID       GetItemID();                                                        // get ID of last item (~~ often same ImGui::GetID(label) beforehand)
//    IMGUI_API ImVec2        GetItemRectMin();                                                   // get upper-left bounding rectangle of the last item (screen space)
//    IMGUI_API ImVec2        GetItemRectMax();                                                   // get lower-right bounding rectangle of the last item (screen space)
//    IMGUI_API ImVec2        GetItemRectSize();                                                  // get size of last item
//    IMGUI_API ImGuiItemFlags GetItemFlags();                                                    // get generic flags of last item

    // Viewports
    // - Currently represents the Platform Window created by the application which is hosting our Dear ImGui windows.
    // - In 'docking' branch with multi-viewport enabled, we extend this concept to have multiple active viewports.
    // - In the future we will extend this concept further to also represent Platform Monitor and support a "no main platform window" operation mode.
//    IMGUI_API ImGuiViewport* GetMainViewport();                                                 // return primary/default viewport. This can never be NULL.

    // Background/Foreground Draw Lists
//    IMGUI_API ImDrawList*   GetBackgroundDrawList();                                            // this draw list will be the first rendered one. Useful to quickly draw shapes/text behind dear imgui contents.
//    IMGUI_API ImDrawList*   GetForegroundDrawList();                                            // this draw list will be the last rendered one. Useful to quickly draw shapes/text over dear imgui contents.

    // Miscellaneous Utilities
//    IMGUI_API bool          IsRectVisible(const ImVec2& size);                                  // test if rectangle (of given size, starting from cursor position) is visible / not clipped.
//    IMGUI_API bool          IsRectVisible(const ImVec2& rect_min, const ImVec2& rect_max);      // test if rectangle (in screen space) is visible / not clipped. to perform coarse clipping on user's side.
//    IMGUI_API double        GetTime();                                                          // get global imgui time. incremented by io.DeltaTime every frame.
//    IMGUI_API int           GetFrameCount();                                                    // get global imgui frame count. incremented by 1 every frame.
//    IMGUI_API ImDrawListSharedData* GetDrawListSharedData();                                    // you may use this when creating your own ImDrawList instances.
//    IMGUI_API const char*   GetStyleColorName(ImGuiCol idx);                                    // get a string corresponding to the enum value (for display, saving, etc.).
//    IMGUI_API void          SetStateStorage(ImGuiStorage* storage);                             // replace current window storage with our own (if you want to manipulate it yourself, typically clear subsection of it)
//    IMGUI_API ImGuiStorage* GetStateStorage();

    // Text Utilities
//    IMGUI_API ImVec2        CalcTextSize(const char* text, const char* text_end = NULL, bool hide_text_after_double_hash = false, float wrap_width = -1.0f);

    // Color Utilities
//    IMGUI_API ImVec4        ColorConvertU32ToFloat4(ImU32 in);
//    IMGUI_API ImU32         ColorConvertFloat4ToU32(const ImVec4& in);
//    IMGUI_API void          ColorConvertRGBtoHSV(float r, float g, float b, float& out_h, float& out_s, float& out_v);
//    IMGUI_API void          ColorConvertHSVtoRGB(float h, float s, float v, float& out_r, float& out_g, float& out_b);

    // Inputs Utilities: Raw Keyboard/Mouse/Gamepad Access
    // - Consider using the Shortcut() function instead of IsKeyPressed()/IsKeyChordPressed()! Shortcut() is easier to use and better featured (can do focus routing check).
    // - the ImGuiKey enum contains all possible keyboard, mouse and gamepad inputs (e.g. ImGuiKey_A, ImGuiKey_MouseLeft, ImGuiKey_GamepadDpadUp...).
    // - (legacy: before v1.87 (2022-02), we used ImGuiKey < 512 values to carry native/user indices as defined by each backends. This was obsoleted in 1.87 (2022-02) and completely removed in 1.91.5 (2024-11). See https://github.com/ocornut/imgui/issues/4921)
//    IMGUI_API bool          IsKeyDown(ImGuiKey key);                                            // is key being held.
//    IMGUI_API bool          IsKeyPressed(ImGuiKey key, bool repeat = true);                     // was key pressed (went from !Down to Down)? Repeat rate uses io.KeyRepeatDelay / KeyRepeatRate.
//    IMGUI_API bool          IsKeyReleased(ImGuiKey key);                                        // was key released (went from Down to !Down)?
//    IMGUI_API bool          IsKeyChordPressed(ImGuiKeyChord key_chord);                         // was key chord (mods + key) pressed, e.g. you can pass 'ImGuiMod_Ctrl | ImGuiKey_S' as a key-chord. This doesn't do any routing or focus check, please consider using Shortcut() function instead.
//    IMGUI_API int           GetKeyPressedAmount(ImGuiKey key, float repeat_delay, float rate);  // uses provided repeat rate/delay. return a count, most often 0 or 1 but might be >1 if RepeatRate is small enough that DeltaTime > RepeatRate
//    IMGUI_API const char*   GetKeyName(ImGuiKey key);                                           // [DEBUG] returns English name of the key. Those names are provided for debugging purpose and are not meant to be saved persistently nor compared.
//    IMGUI_API void          SetNextFrameWantCaptureKeyboard(bool want_capture_keyboard);        // Override io.WantCaptureKeyboard flag next frame (said flag is left for your application to handle, typically when true it instructs your app to ignore inputs). e.g. force capture keyboard when your widget is being hovered. This is equivalent to setting "io.WantCaptureKeyboard = want_capture_keyboard"; after the next NewFrame() call.

    // Inputs Utilities: Shortcut Testing & Routing
    // - Typical use is e.g.: 'if (ImGui::Shortcut(ImGuiMod_Ctrl | ImGuiKey_S)) { ... }'.
    // - Flags: Default route use ImGuiInputFlags_RouteFocused, but see ImGuiInputFlags_RouteGlobal and other options in ImGuiInputFlags_!
    // - Flags: Use ImGuiInputFlags_Repeat to support repeat.
    // - ImGuiKeyChord = a ImGuiKey + optional ImGuiMod_Alt/ImGuiMod_Ctrl/ImGuiMod_Shift/ImGuiMod_Super.
    //       ImGuiKey_C                          // Accepted by functions taking ImGuiKey or ImGuiKeyChord arguments
    //       ImGuiMod_Ctrl | ImGuiKey_C          // Accepted by functions taking ImGuiKeyChord arguments
    //   only ImGuiMod_XXX values are legal to combine with an ImGuiKey. You CANNOT combine two ImGuiKey values.
    // - The general idea is that several callers may register interest in a shortcut, and only one owner gets it.
    //      Parent   -> call Shortcut(Ctrl+S)    // When Parent is focused, Parent gets the shortcut.
    //        Child1 -> call Shortcut(Ctrl+S)    // When Child1 is focused, Child1 gets the shortcut (Child1 overrides Parent shortcuts)
    //        Child2 -> no call                  // When Child2 is focused, Parent gets the shortcut.
    //   The whole system is order independent, so if Child1 makes its calls before Parent, results will be identical.
    //   This is an important property as it facilitate working with foreign code or larger codebase.
    // - To understand the difference:
    //   - IsKeyChordPressed() compares mods and call IsKeyPressed()
    //     -> the function has no side-effect.
    //   - Shortcut() submits a route, routes are resolved, if it currently can be routed it calls IsKeyChordPressed()
    //     -> the function has (desirable) side-effects as it can prevents another call from getting the route.
    // - Visualize registered routes in 'Metrics/Debugger->Inputs'.
//    IMGUI_API bool          Shortcut(ImGuiKeyChord key_chord, ImGuiInputFlags flags = 0);
//    IMGUI_API void          SetNextItemShortcut(ImGuiKeyChord key_chord, ImGuiInputFlags flags = 0);

    // Inputs Utilities: Key/Input Ownership [BETA]
    // - One common use case would be to allow your items to disable standard inputs behaviors such
    //   as Tab or Alt key handling, Mouse Wheel scrolling, etc.
    //   e.g. Button(...); SetItemKeyOwner(ImGuiKey_MouseWheelY); to make hovering/activating a button disable wheel for scrolling.
    // - Reminder ImGuiKey enum include access to mouse buttons and gamepad, so key ownership can apply to them.
    // - Many related features are still in imgui_internal.h. For instance, most IsKeyXXX()/IsMouseXXX() functions have an owner-id-aware version.
//    IMGUI_API void          SetItemKeyOwner(ImGuiKey key);                                      // Set key owner to last item ID if it is hovered or active. Equivalent to 'if (IsItemHovered() || IsItemActive()) { SetKeyOwner(key, GetItemID());'.

    // Inputs Utilities: Mouse
    // - To refer to a mouse button, you may use named enums in your code e.g. ImGuiMouseButton_Left, ImGuiMouseButton_Right.
    // - You can also use regular integer: it is forever guaranteed that 0=Left, 1=Right, 2=Middle.
    // - Dragging operations are only reported after mouse has moved a certain distance away from the initial clicking position (see 'lock_threshold' and 'io.MouseDraggingThreshold')
//    IMGUI_API bool          IsMouseDown(ImGuiMouseButton button);                               // is mouse button held?
//    IMGUI_API bool          IsMouseClicked(ImGuiMouseButton button, bool repeat = false);       // did mouse button clicked? (went from !Down to Down). Same as GetMouseClickedCount() == 1.
//    IMGUI_API bool          IsMouseReleased(ImGuiMouseButton button);                           // did mouse button released? (went from Down to !Down)
//    IMGUI_API bool          IsMouseDoubleClicked(ImGuiMouseButton button);                      // did mouse button double-clicked? Same as GetMouseClickedCount() == 2. (note that a double-click will also report IsMouseClicked() == true)
//    IMGUI_API bool          IsMouseReleasedWithDelay(ImGuiMouseButton button, float delay);     // delayed mouse release (use very sparingly!). Generally used with 'delay >= io.MouseDoubleClickTime' + combined with a 'io.MouseClickedLastCount==1' test. This is a very rarely used UI idiom, but some apps use this: e.g. MS Explorer single click on an icon to rename.
//    IMGUI_API int           GetMouseClickedCount(ImGuiMouseButton button);                      // return the number of successive mouse-clicks at the time where a click happen (otherwise 0).
//    IMGUI_API bool          IsMouseHoveringRect(const ImVec2& r_min, const ImVec2& r_max, bool clip = true);// is mouse hovering given bounding rect (in screen space). clipped by current clipping settings, but disregarding of other consideration of focus/window ordering/popup-block.
//    IMGUI_API bool          IsMousePosValid(const ImVec2* mouse_pos = NULL);                    // by convention we use (-FLT_MAX,-FLT_MAX) to denote that there is no mouse available
//    IMGUI_API bool          IsAnyMouseDown();                                                   // [WILL OBSOLETE] is any mouse button held? This was designed for backends, but prefer having backend maintain a mask of held mouse buttons, because upcoming input queue system will make this invalid.
//    IMGUI_API ImVec2        GetMousePos();                                                      // shortcut to ImGui::GetIO().MousePos provided by user, to be consistent with other calls
//    IMGUI_API ImVec2        GetMousePosOnOpeningCurrentPopup();                                 // retrieve mouse position at the time of opening popup we have BeginPopup() into (helper to avoid user backing that value themselves)
//    IMGUI_API bool          IsMouseDragging(ImGuiMouseButton button, float lock_threshold = -1.0f);         // is mouse dragging? (uses io.MouseDraggingThreshold if lock_threshold < 0.0f)
//    IMGUI_API ImVec2        GetMouseDragDelta(ImGuiMouseButton button = 0, float lock_threshold = -1.0f);   // return the delta from the initial clicking position while the mouse button is pressed or was just released. This is locked and return 0.0f until the mouse moves past a distance threshold at least once (uses io.MouseDraggingThreshold if lock_threshold < 0.0f)
//    IMGUI_API void          ResetMouseDragDelta(ImGuiMouseButton button = 0);                   //
//    IMGUI_API ImGuiMouseCursor GetMouseCursor();                                                // get desired mouse cursor shape. Important: reset in ImGui::NewFrame(), this is updated during the frame. valid before Render(). If you use software rendering by setting io.MouseDrawCursor ImGui will render those for you
//    IMGUI_API void          SetMouseCursor(ImGuiMouseCursor cursor_type);                       // set desired mouse cursor shape
//    IMGUI_API void          SetNextFrameWantCaptureMouse(bool want_capture_mouse);              // Override io.WantCaptureMouse flag next frame (said flag is left for your application to handle, typical when true it instructs your app to ignore inputs). This is equivalent to setting "io.WantCaptureMouse = want_capture_mouse;" after the next NewFrame() call.

    // Clipboard Utilities
    // - Also see the LogToClipboard() function to capture GUI into clipboard, or easily output text data to the clipboard.
//    IMGUI_API const char*   GetClipboardText();
//    IMGUI_API void          SetClipboardText(const char* text);

    // Settings/.Ini Utilities
    // - The disk functions are automatically called if io.IniFilename != NULL (default is "imgui.ini").
    // - Set io.IniFilename to NULL to load/save manually. Read io.WantSaveIniSettings description about handling .ini saving manually.
    // - Important: default value "imgui.ini" is relative to current working dir! Most apps will want to lock this to an absolute path (e.g. same path as executables).
//    IMGUI_API void          LoadIniSettingsFromDisk(const char* ini_filename);                  // call after CreateContext() and before the first call to NewFrame(). NewFrame() automatically calls LoadIniSettingsFromDisk(io.IniFilename).
//    IMGUI_API void          LoadIniSettingsFromMemory(const char* ini_data, size_t ini_size=0); // call after CreateContext() and before the first call to NewFrame() to provide .ini data from your own data source.
//    IMGUI_API void          SaveIniSettingsToDisk(const char* ini_filename);                    // this is automatically called (if io.IniFilename is not empty) a few seconds after any modification that should be reflected in the .ini file (and also by DestroyContext).
//    IMGUI_API const char*   SaveIniSettingsToMemory(size_t* out_ini_size = NULL);               // return a zero-terminated string with the .ini data which you can save by your own mean. call when io.WantSaveIniSettings is set, then save data by your own mean and clear io.WantSaveIniSettings.

    // Debug Utilities
    // - Your main debugging friend is the ShowMetricsWindow() function.
    // - Interactive tools are all accessible from the 'Dear ImGui Demo->Tools' menu.
    // - Read https://github.com/ocornut/imgui/wiki/Debug-Tools for a description of all available debug tools.
//    IMGUI_API void          DebugTextEncoding(const char* text);
//    IMGUI_API void          DebugFlashStyleColor(ImGuiCol idx);
//    IMGUI_API void          DebugStartItemPicker();
//    IMGUI_API bool          DebugCheckVersionAndDataLayout(const char* version_str, size_t sz_io, size_t sz_style, size_t sz_vec2, size_t sz_vec4, size_t sz_drawvert, size_t sz_drawidx); // This is called by IMGUI_CHECKVERSION() macro.
//#ifndef IMGUI_DISABLE_DEBUG_TOOLS
//    IMGUI_API void          DebugLog(const char* fmt, ...)           IM_FMTARGS(1); // Call via IMGUI_DEBUG_LOG() for maximum stripping in caller code!
//    IMGUI_API void          DebugLogV(const char* fmt, va_list args) IM_FMTLIST(1);
//#endif

    // Memory Allocators
    // - Those functions are not reliant on the current context.
    // - DLL users: heaps and globals are not shared across DLL boundaries! You will need to call SetCurrentContext() + SetAllocatorFunctions()
    //   for each static/DLL boundary you are calling from. Read "Context and Memory Allocators" section of imgui.cpp for more details.
//    IMGUI_API void          SetAllocatorFunctions(ImGuiMemAllocFunc alloc_func, ImGuiMemFreeFunc free_func, void* user_data = NULL);
//    IMGUI_API void          GetAllocatorFunctions(ImGuiMemAllocFunc* p_alloc_func, ImGuiMemFreeFunc* p_free_func, void** p_user_data);
//    IMGUI_API void*         MemAlloc(size_t size);
//    IMGUI_API void          MemFree(void* ptr);

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
