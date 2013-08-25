package ee.tmtu.ld27;

import ee.tmtu.libludum.core.GameSettings;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.applet.Applet;
import java.awt.*;
import java.io.File;

public class AppletMain extends Applet {

    Canvas display_parent;

    Thread gameThread;

    boolean running = false;


    public void startLWJGL() {
        File file = new File("./");
        System.out.println(file.getAbsolutePath());
        gameThread = new Thread(new Shroom(GameSettings.from("./assets/settings.cfg")));
        gameThread.start();
    }


    /**
     * Tell game loop to stop running, after which the LWJGL Display will
     * be destoryed. The main thread will wait for the Display.destroy().
     */
    private void stopLWJGL() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {

    }

    public void stop() {

    }

    /**
     * Applet Destroy method will remove the canvas,
     * before canvas is destroyed it will notify stopLWJGL()
     * to stop the main game loop and to destroy the Display
     */
    public void destroy() {
        remove(display_parent);
        super.destroy();
    }

    public void init() {
        setLayout(new BorderLayout());
        try {
            display_parent = new Canvas() {
                public final void addNotify() {
                    super.addNotify();
                    startLWJGL();
                }
                public final void removeNotify() {
                    stopLWJGL();
                    super.removeNotify();
                }
            };
            display_parent.setSize(getWidth(),getHeight());
            add(display_parent);
            display_parent.setFocusable(true);
            display_parent.requestFocus();
            display_parent.setIgnoreRepaint(true);
            setVisible(true);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("Unable to create display");
        }
    }

    protected void initGL() {

    }

    public void gameLoop() {
        while(running) {
            Display.sync(60);
            Display.update();
        }

        Display.destroy();
    }

}
