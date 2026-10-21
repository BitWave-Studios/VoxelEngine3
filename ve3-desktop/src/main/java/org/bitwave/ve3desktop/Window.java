package org.bitwave.ve3desktop;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A GLFW window wrapper.
 *
 * <p>Manages the lifecycle of a GLFW window and its associated OpenGL context.</p>
 */
public final class Window implements AutoCloseable {
    private final long handle;
    private GLFWErrorCallback errorCallback;

    public Window(String title, int width, int height) {
        GLFWErrorCallback previous = glfwSetErrorCallback(
                GLFWErrorCallback.createPrint(System.err)
        );

        if (previous != null) {
            previous.free();
        }

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        handle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (handle == NULL) {
            glfwTerminate();
            throw new IllegalStateException("Unable to create GLFW window");
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer windowWidth = stack.mallocInt(1);
            IntBuffer windowHeight = stack.mallocInt(1);

            glfwGetWindowSize(handle, windowWidth, windowHeight);

            glfwSetWindowPos(
                    handle,
                    centre(windowWidth.get(0), primaryMonitorWidth()),
                    centre(windowHeight.get(0), primaryMonitorHeight())
            );
        }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        glfwShowWindow(handle);
        GL.createCapabilities();
    }

    public long handle() {
        return handle;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void swapBuffers() {
        glfwSwapBuffers(handle);
    }

    public void requestClose() {
        glfwSetWindowShouldClose(handle, true);
    }

    @Override
    public void close() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private long window() {
        return handle;
    }

    private int primaryMonitorWidth() {
        var mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return mode == null ? 0 : mode.width();
    }

    private int primaryMonitorHeight() {
        var mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return mode == null ? 0 : mode.height();
    }

    private static int centre(int size, int available) {
        return Math.max(0, (available - size) / 2);
    }
}
