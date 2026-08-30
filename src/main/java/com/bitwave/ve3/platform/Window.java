package com.bitwave.ve3.platform;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Window and render space handler - creates a configurable window using GLFW.
 */
public class Window {
    private long handle;
    private int width;
    private int height;
    private final Input input = new Input();

    public void create(WindowConfig config) {
        glfwSetErrorCallback((error, desc) -> {
            System.err.println("GLFW Error: [" + error + "]: " + MemoryUtil.memUTF8(desc));
        });

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, config.resizeable() ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // a macOS-required glfwWindowHint
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        handle = glfwCreateWindow(config.width(), config.height(), config.title(), 0, 0);
        if (handle == 0) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        this.width = config.width();
        this.height = config.height();

        glfwSetFramebufferSizeCallback(handle, (win, w, h) -> {
            this.width = w;
            this.height = h;
            glViewport(0, 0, w, h);
        });

        input.attachCallbacks(handle);

        glfwMakeContextCurrent(handle);
        GL.createCapabilities();

        glfwSwapInterval(config.vsync() ? 1 : 0);
        glfwShowWindow(handle);
    }

    public void pollEvents() {
        input.beginFrame();
        glfwPollEvents();
    }

    public void swapBuffers() {
        glfwSwapBuffers(handle);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void destroy() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        var callback = glfwSetErrorCallback(null);
        if (callback != null) callback.free();
    }

    public long handle() { return handle; }
    public int width() { return width; }
    public int height() { return height; }
    public Input input() { return input; }
}
