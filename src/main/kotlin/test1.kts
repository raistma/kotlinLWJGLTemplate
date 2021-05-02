package cz.unknown.kotlinTest

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

fun main() {
    println("ahoj")
    val app = TestApp()
    app.run()
}

main()

class TestApp {

    companion object {

        val WINDOW_SIZE = Pair(800, 600)

    }

    private var errorCallback : GLFWErrorCallback? = null
    private var keyCallback : GLFWKeyCallback? = null
    private var window : Long? = null

    fun run() {

        println("ahoj2")

        try {

            init()
            loop()
            // Destroy window
            glfwDestroyWindow(window!!);
            keyCallback?.free()

        } finally {

            // Terminate GLFW
            glfwTerminate()
            errorCallback?.free()

        }
    }

    fun init() {
        println("ahoj3")
        errorCallback = GLFWErrorCallback.createPrint(System.err).set()

        if(!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        window = glfwCreateWindow(WINDOW_SIZE.first, WINDOW_SIZE.second, "Hello", NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        keyCallback = glfwSetKeyCallback(window!!, object : GLFWKeyCallback() {
            override fun invoke(window: Long,
                                key: Int,
                                scancode: Int,
                                action: Int,
                                mods: Int) {

                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                    glfwSetWindowShouldClose(window, true)
                }

            }
        })

        // Get the resolution of the primary monitor
        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor()) ?: throw IllegalStateException("Unknown video mode")

        // Center our window
        glfwSetWindowPos(
                window!!,
                (videoMode.width() - WINDOW_SIZE.first) / 2,
                (videoMode.height() - WINDOW_SIZE.second) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window!!)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(window!!)
    }

    fun loop() {
        GL.createCapabilities()

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (glfwWindowShouldClose(window!!) == false ) {

            // Clear the framebuffer
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            // Swap the color buffers
            glfwSwapBuffers(window!!)

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }

}