cmake_minimum_required(VERSION 3.20)
project(cimgui CXX)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# CIMGUI_SRC_DIR must point to the cloned cimgui repository root.
if(NOT DEFINED CIMGUI_SRC_DIR)
    message(FATAL_ERROR "Pass -DCIMGUI_SRC_DIR=<path-to-cimgui-repo>")
endif()

set(SOURCES
    ${CIMGUI_SRC_DIR}/cimgui.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/jimgui_opengl3.cpp
    ${CIMGUI_SRC_DIR}/imgui/imgui.cpp
    ${CIMGUI_SRC_DIR}/imgui/imgui_draw.cpp
    ${CIMGUI_SRC_DIR}/imgui/imgui_tables.cpp
    ${CIMGUI_SRC_DIR}/imgui/imgui_widgets.cpp
    ${CIMGUI_SRC_DIR}/imgui/imgui_demo.cpp
    ${CIMGUI_SRC_DIR}/imgui/backends/imgui_impl_opengl3.cpp
)

add_library(cimgui SHARED ${SOURCES})

target_include_directories(cimgui PRIVATE
    ${CIMGUI_SRC_DIR}
    ${CIMGUI_SRC_DIR}/imgui
    ${CIMGUI_SRC_DIR}/imgui/backends
)

# Only the OpenGL3 backend — no GLFW.
# Input is handled by the LibGDX InputProcessor on the Java side.
#
# Note: do NOT define CIMGUI_DEFINE_ENUMS_AND_STRUCTS here.
# That macro is only used by jextract (C parser mode) to read the header.
# The C++ source already gets type definitions via #include "imgui/imgui.h".
target_compile_definitions(cimgui PRIVATE
    CIMGUI_USE_OPENGL3
)

if(WIN32)
    target_link_libraries(cimgui PRIVATE opengl32)
    # Use a .def file to export the OpenGL3 wrapper functions under the canonical
    # ImGui_ImplOpenGL3_* names expected by the Java bindings.
    target_link_options(cimgui PRIVATE
        "/DEF:${CMAKE_CURRENT_SOURCE_DIR}/cimgui.def")
elseif(APPLE)
    find_library(OPENGL_LIB OpenGL REQUIRED)
    target_link_libraries(cimgui PRIVATE ${OPENGL_LIB})
elseif(UNIX)
    target_link_libraries(cimgui PRIVATE GL)
endif()

# Output the DLL/SO/dylib directly — the Gradle task copies it into resources.
set_target_properties(cimgui PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}
    LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}
)
