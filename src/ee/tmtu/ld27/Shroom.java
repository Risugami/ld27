package ee.tmtu.ld27;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import ee.tmtu.libludum.assets.AssetManager;
import ee.tmtu.libludum.core.Game;
import ee.tmtu.libludum.core.GameSettings;
import ee.tmtu.libludum.graphics.Font;
import ee.tmtu.libludum.graphics.SpriteBatch;
import ee.tmtu.libludum.ui.Margin;
import ee.tmtu.libludum.ui.Padding;
import ee.tmtu.libludum.ui.Root;
import ee.tmtu.libludum.ui.event.MouseEvent;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

public class Shroom extends Game {

    public static final TweenManager TWEEN = new TweenManager();
    public SpriteBatch batch;
    public Root root;
    public Font font;

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

        this.batch = new SpriteBatch(500);
        this.root = new Root(new Margin(0), new Padding(0));
        this.font = AssetManager.load("./assets/fairfax.fnt", Font.class);
    }

    double tenseconds = 10.f;

    @Override
    public void update() {
        Shroom.TWEEN.update((float) (1. / 20.));
        this.tenseconds -= 1. / 20.;

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
    }

    @Override
    public void draw(double lerp) {
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0.f, 0.f, 0.f, 1.f);

        this.batch.start();
        this.font.draw(this.batch, 400, 400, "" + tenseconds);
        this.batch.end();
    }

    public static void main(String[] args) {
        GameSettings settings = GameSettings.from("./assets/settings.cfg");
        new Thread(new Shroom(settings)).start();
    }

}
