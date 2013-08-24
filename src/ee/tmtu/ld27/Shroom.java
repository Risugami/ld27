package ee.tmtu.ld27;

import aurelienribon.tweenengine.TweenManager;
import ee.tmtu.ld27.entity.TextEntity;
import ee.tmtu.ld27.world.World;
import ee.tmtu.libludum.assets.AssetManager;
import ee.tmtu.libludum.core.Game;
import ee.tmtu.libludum.core.GameSettings;
import ee.tmtu.libludum.graphics.Font;
import ee.tmtu.libludum.graphics.SpriteBatch;
import ee.tmtu.libludum.graphics.Texture;
import ee.tmtu.libludum.ui.Button;
import ee.tmtu.libludum.ui.Margin;
import ee.tmtu.libludum.ui.Padding;
import ee.tmtu.libludum.ui.Root;
import ee.tmtu.libludum.ui.event.MouseEvent;
import ee.tmtu.libludum.ui.event.MouseListener;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

public class Shroom extends Game {

    public static final boolean[] keys = new boolean[256];
    public static final TweenManager TWEEN = new TweenManager();
    public Texture silhouette;
    public SpriteBatch batch;
    public Root root;
    public Font font;
    public World world;

    public Shroom(GameSettings settings) {
        super(settings);
    }

    @Override
    public void init() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.f, this.settings.width, this.settings.height, 0.f, 0.f, 1.f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_ALPHA_TEST);

        this.silhouette = AssetManager.load("./assets/silhouette.png", Texture.class);
        this.batch = new SpriteBatch(500);
        this.root = new Root(new Margin(0), new Padding(15));
        this.root.x = this.settings.width / 2 - 125;
        this.root.y = this.settings.height / 2 - 100;
        this.root.width = 250;
        this.font = AssetManager.load("./assets/fairfax.fnt", Font.class);

        Button play = new Button("Play Game", this.font);
        play.padding = new Padding(10);
        play.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
            }
        };
        Button settings = new Button("Settings", this.font);
        settings.padding = new Padding(10);
        settings.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
            }
        };
        Button quit = new Button("Quit", this.font);
        quit.padding = new Padding(10);
        quit.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                Shroom.this.shutdown();
            }
        };
        this.root.add(play);
        this.root.add(settings);
        this.root.add(quit);
        this.root.layout();
        this.world = new World();
        this.world.entities.add(new TextEntity(this.world, this.font, "Ludum Dare!"));
    }

    double tenseconds = 10.f;

    @Override
    public void update() {
        Shroom.TWEEN.update((float) (1. / 20.));
        this.tenseconds -= 1. / 20.;

        for(int i = 0; i < 256; i++) {
            Shroom.keys[i] = Keyboard.isKeyDown(i);
        }
        MouseEvent me = new MouseEvent();
        while(Mouse.next()) {
            me.btn = Mouse.getEventButton();
            me.x = Mouse.getEventX();
            me.y = this.settings.height - Mouse.getEventY();
            me.dx = Mouse.getEventDX();
            me.dy = Mouse.getEventDY();
            me.state = MouseEvent.MouseState.NULL;
            if(me.dx != 0 || me.dy != 0) {
                me.state = MouseEvent.MouseState.MOVE;
            }
            if(me.btn > -1) {
                if(Mouse.getEventButtonState()) {
                    me.state = MouseEvent.MouseState.DOWN;
                } else {
                    me.state = MouseEvent.MouseState.UP;
                }
            }
            root.fire(me);
        }
        this.world.update();
    }

    @Override
    public void draw(double lerp) {
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(129.f / 255.f, 109.f / 255.f, 80.f / 255.f, 1.f);

        this.batch.start();
        this.world.draw(this.batch, lerp);
        this.root.draw(this.batch, lerp);
        this.batch.draw(this.silhouette, 0, 0, 800, 600);
        this.batch.end();
    }

    public static float lerp(float prev, float now, double lerp) {
        return (float) (prev + (now - prev) * lerp);
    }

    public static void main(String[] args) {
        GameSettings settings = GameSettings.from("./assets/settings.cfg");
        new Thread(new Shroom(settings)).start();
    }

}
