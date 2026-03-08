// jimgui_opengl3.cpp
//
// C-callable wrappers for the ImGui OpenGL3 backend.
//
// We DON'T include imgui_impl_opengl3.h here because that header declares the
// functions with plain C++ linkage, and if we then define them with extern "C"
// MSVC raises C2375 (redefinition; different linkage). Instead, we forward-
// declare the C++ functions ourselves, wrap them with unique internal names
// under extern "C", and export them under the expected names via a .def file.

#include "imgui.h"  // ImDrawData, ImTextureData

// Forward-declare the C++ backend functions (defined in imgui_impl_opengl3.cpp).
// Plain C++ linkage — no extern "C", matches imgui_impl_opengl3.h.
bool ImGui_ImplOpenGL3_Init(const char* glsl_version);
void ImGui_ImplOpenGL3_Shutdown();
void ImGui_ImplOpenGL3_NewFrame();
void ImGui_ImplOpenGL3_RenderDrawData(ImDrawData* draw_data);
bool ImGui_ImplOpenGL3_CreateDeviceObjects();
void ImGui_ImplOpenGL3_DestroyDeviceObjects();
void ImGui_ImplOpenGL3_UpdateTexture(ImTextureData* tex);

// Export with C linkage under internal names.
// The .def file re-exports these as ImGui_ImplOpenGL3_* (no prefix).
extern "C" {
    bool  w_ImplOpenGL3_Init(const char* v)        { return ImGui_ImplOpenGL3_Init(v); }
    void  w_ImplOpenGL3_Shutdown()                  { ImGui_ImplOpenGL3_Shutdown(); }
    void  w_ImplOpenGL3_NewFrame()                  { ImGui_ImplOpenGL3_NewFrame(); }
    void  w_ImplOpenGL3_RenderDrawData(ImDrawData* d){ ImGui_ImplOpenGL3_RenderDrawData(d); }
    bool  w_ImplOpenGL3_CreateDeviceObjects()       { return ImGui_ImplOpenGL3_CreateDeviceObjects(); }
    void  w_ImplOpenGL3_DestroyDeviceObjects()      { ImGui_ImplOpenGL3_DestroyDeviceObjects(); }
    void  w_ImplOpenGL3_UpdateTexture(ImTextureData* t){ ImGui_ImplOpenGL3_UpdateTexture(t); }
}
