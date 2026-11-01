package org.bitwave.ve3.core.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

/**
 * Creates a GLFW window with express helpers for input and timing.
 */
public class Window {
    private final int width;
    private final int height;
    private final String title;
    private long windowHandle;
    private double lastTime;
    private double deltaTime = 0.0;

    private int fbWidth;
    private int fbHeight;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        windowHandle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (windowHandle == 0) throw new RuntimeException("Failed to create the GLFW window");

        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowHandle);

        GL.createCapabilities();
        System.out.println("OpenGL version: " + org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_VERSION));

        int[] fbW = new int[1], fbH = new int[1];
        GLFW.glfwGetFramebufferSize(windowHandle, fbW, fbH);
        fbWidth = fbW[0];
        fbHeight = fbH[0];

        glViewport(0, 0, fbWidth, fbHeight);

        GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, newFbWidth, newFbHeight) -> {
            fbWidth = newFbWidth;
            fbHeight = newFbHeight;
            glViewport(0, 0, fbWidth, fbHeight);
        });

        GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        lastTime = GLFW.glfwGetTime();
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
        double now = GLFW.glfwGetTime();
        deltaTime = now - lastTime;
        lastTime = now;
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(windowHandle);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFramebufferWidth() {
        return fbWidth;
    }

    public int getFramebufferHeight() {
        return fbHeight;
    }
}
