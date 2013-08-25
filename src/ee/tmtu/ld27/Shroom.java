package ee.tmtu.ld27;

import aurelienribon.tweenengine.*;

import ee.tmtu.ld27.entity.TextEntity;
import ee.tmtu.ld27.world.World;
import ee.tmtu.libludum.assets.AssetManager;
import ee.tmtu.libludum.core.ColorAccessor;
import ee.tmtu.libludum.core.Game;
import ee.tmtu.libludum.core.GameSettings;
import ee.tmtu.libludum.graphics.Font;
import ee.tmtu.libludum.graphics.SpriteBatch;
import ee.tmtu.libludum.graphics.Texture;
import ee.tmtu.libludum.sound.Audio;
import ee.tmtu.libludum.sound.Sound;
import ee.tmtu.libludum.ui.*;
import ee.tmtu.libludum.ui.event.KeyEvent;
import ee.tmtu.libludum.ui.event.KeyListener;
import ee.tmtu.libludum.ui.event.MouseEvent;
import ee.tmtu.libludum.ui.event.MouseListener;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.opengl.GL11.*;

public class Shroom extends Game {

    public String currentTitle;
    public String title = "Word\247io";
    public String hiscores = "Hi\247Scores";
    public String chooseRound = "Choose \247Round";
    public String roundx = "Round \247";
    public static final boolean[] keys = new boolean[256];
    public static final TweenManager TWEEN = new TweenManager();
    public HiScoreList hiScoreList;
    public Button toaster;
    public boolean inGame;
    public boolean enteredName;
    public String name;
    public Texture silhouette;
    public Texture blank;
    public Button mute;
    public SpriteBatch batch;
    public Sound purple_retort;
    public Sound win;
    public Sound fail;
    public Root root;
    public Font bigFont;
    public Font font;
    public World world;
    public Color overlay;
    public int score;
    public int currentRound;

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

        Tween.registerAccessor(Color.class, new ColorAccessor());

        this.blank = AssetManager.load("./assets/img/blank.png", Texture.class);
        this.silhouette = AssetManager.load("./assets/img/silhouette.png", Texture.class);
        this.batch = new SpriteBatch(1000);
        this.root = new Root(new Margin(0), new Padding(15));
        this.root.x = this.settings.width / 2 - 125;
        this.root.y = this.settings.height / 2 - 100;
        this.root.width = 250;
        this.bigFont = AssetManager.load("./assets/font/advocut30.fnt", Font.class);
        this.font = AssetManager.load("./assets/font/advocut19.fnt", Font.class);
        this.purple_retort = AssetManager.load("./assets/sound/bu-a-purple-retort.wav", Sound.class);
        this.win = AssetManager.load("./assets/sound/win.wav", Sound.class);
        this.fail = AssetManager.load("./assets/sound/fail.wav", Sound.class);
        this.world = new World();
        this.toaster = new Button("Enter a name and press submit/enter to start the game.", this.font);
        this.toaster.padding = new Padding(10);
        this.toaster.layout();
        this.toaster.x = 400 - this.toaster.width / 2;
        this.toaster.y = 20;
        this.mute = new Button("Toggle Mute", this.font);
        this.mute.padding = new Padding(10);
        this.mute.layout();
        this.mute.x = 20;
        this.mute.y = this.settings.height - this.mute.height - 10;
        this.mute.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                Shroom.this.settings.mute = !Shroom.this.settings.mute;
                Shroom.this.settings.save("./assets/settings.cfg");
            }
        };
        this.root.indie = this.mute;
        this.currentTitle = this.title;
        this.overlay = new Color(0, 0, 0, 255);
        Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).ease(TweenEquations.easeInOutQuad).start(Shroom.TWEEN);
        this.purple_retort.setLooping(true);
        this.purple_retort.setGain(this.settings.mute ? 0.f : this.settings.volume);
        Audio.play(this.purple_retort);
        this.hiScoreList = HiScoreList.from("./assets/data/hiscores.json");

        this.initMenu();
        String[] themes = AssetManager.load("./assets/data/round3.txt", String[].class);
        for(int i = 0; i < themes.length; i++) {
            String str = themes[i];
            Shroom.this.world.entities.add(new TextEntity(Shroom.this.world, i * (800.f / themes.length), -20 * i, font, str, true));
        }
    }

    public void prune(ArrayList<HiScore> list) {
        Collections.sort(list);
        if(list.size() > 5) {
            for(int i = 5; i < list.size(); i++) {
                list.remove(5);
            }
        }
    }

    public void saveHiScore(HiScore hiScore) {
        switch (hiScore.round) {
            case 1:
                this.hiScoreList.round1.add(hiScore);
                prune(this.hiScoreList.round1);
                break;
            case 2:
                this.hiScoreList.round2.add(hiScore);
                prune(this.hiScoreList.round2);
                break;
            case 3:
                this.hiScoreList.round3.add(hiScore);
                prune(this.hiScoreList.round3);
                break;
            case 4:
                this.hiScoreList.round4.add(hiScore);
                prune(this.hiScoreList.round4);
                break;
            case 5:
                this.hiScoreList.round5.add(hiScore);
                prune(this.hiScoreList.round5);
                break;
        }
    }

    public ArrayList<HiScore> getRoundHiScore(int round) {
        switch (round) {
            case 1:
                return hiScoreList.round1;
            case 2:
                return hiScoreList.round2;
            case 3:
                return hiScoreList.round3;
            case 4:
                return hiScoreList.round4;
            case 5:
                return hiScoreList.round5;
        }
        return null;
    }

    public void initHiScoreRound(int round) {
        this.root.clear();
        //this.world.entities.clear();
        this.currentTitle = this.roundx + round;
        this.inGame = false;

        for(int i = 0; i < this.getRoundHiScore(round).size(); i++) {
            prune(this.getRoundHiScore(round));
            HiScoreButton btn = new HiScoreButton(this.getRoundHiScore(round).get(i), this.font);
            btn.padding = new Padding(10);
            this.root.add(btn);
        }
        for(int i = 0; i < 5 - this.getRoundHiScore(round).size(); i++) {
            Button btn = new Button("", this.font);
            btn.padding = new Padding(10);
            this.root.add(btn);
        }

        Button menu = new Button("Return", this.font);
        menu.padding = new Padding(10);
        menu.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).start(Shroom.TWEEN);
                Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(.5f).start(Shroom.TWEEN);
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Shroom.this.initHiScore();
                    }
                }).delay(.5f).start(Shroom.TWEEN);
            }
        };
        this.root.add(menu);
        this.root.layout();
    }

    public void initHiScore() {
        this.root.clear();
        this.currentTitle = this.hiscores;
        //this.world.entities.clear();
        this.inGame = false;

        for(int i = 5; i > 0; i--) {
            Button btn = new Button("Round " + (i), this.font);
            btn.padding = new Padding(10);
            final int finalI = i;
            btn.listener = new MouseListener() {
                @Override
                public void onMouseEvent(MouseEvent event) {
                    Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).start(Shroom.TWEEN);
                    Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(.5f).start(Shroom.TWEEN);
                    Tween.call(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> baseTween) {
                            Shroom.this.initHiScoreRound(finalI);
                        }
                    }).delay(.5f).start(Shroom.TWEEN);
                }
            };
            this.root.add(btn);
        }

        Button menu = new Button("Return", this.font);
        menu.padding = new Padding(10);
        menu.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).start(Shroom.TWEEN);
                Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(.5f).start(Shroom.TWEEN);
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Shroom.this.initMenu();
                    }
                }).delay(.5f).start(Shroom.TWEEN);
            }
        };
        this.root.add(menu);
        this.root.layout();
    }

    public void initGame() {
        this.root.clear();
        this.world.entities.clear();
        this.inGame = true;
        final Button button = new Button("Submit", this.font);
        button.padding = new Padding(10);
        final TextField tfield = new TextField(this.font);
        tfield.padding = new Padding(10);
        tfield.listener = new KeyListener() {
            @Override
            public boolean onKey(KeyEvent event) {
                if(!finishing) {
                    if(event.state == KeyEvent.KeyState.DOWN) {
                        if(event.key == Keyboard.KEY_RETURN) {
                            if(!enteredName) {
                                String[] themes = AssetManager.load("./assets/data/round" + Shroom.this.currentRound + ".txt", String[].class);
                                for(int i = 0; i < themes.length; i++) {
                                    String str = themes[i];
                                    Shroom.this.world.entities.add(new TextEntity(Shroom.this.world, i * (800.f / themes.length), -20 * i, font, str));
                                }
                                name = tfield.str;
                                enteredName = true;
                            }
                            if(Shroom.this.world.submit(Shroom.this, tfield.str)) {
                                Audio.play(Shroom.this.win);
                                Shroom.this.score += 100;
                            } else {
                                Audio.play(Shroom.this.fail);
                            }
                            tfield.str = "";
                            button.state = Component.ComponentState.DOWN;
                            button.drawable = button.click;
                            return true;
                        }
                    } else {
                        button.state = Component.ComponentState.IDLE;
                        button.drawable = button.idle;
                    }
                }
                return false;
            }
        };
        button.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                if(!finishing) {
                    if(!enteredName) {
                        String[] themes = AssetManager.load("./assets/data/round" + Shroom.this.currentRound + ".txt", String[].class);
                        for(int i = 0; i < themes.length; i++) {
                            String str = themes[i];
                            Shroom.this.world.entities.add(new TextEntity(Shroom.this.world, i * (800.f / themes.length), -20 * i, font, str));
                        }
                        name = tfield.str;
                        enteredName = true;
                    }
                    if(Shroom.this.world.submit(Shroom.this, tfield.str)) {
                        Audio.play(Shroom.this.win);
                        Shroom.this.score += 100;
                    } else {
                        Audio.play(Shroom.this.fail);
                    }
                    tfield.str = "";
                }
            }
        };
        this.root.add(tfield);
        this.root.requestFocus(tfield);
        this.root.add(button);
        this.root.layout();
    }

    public void initRoundSelection() {
        this.inGame = false;
        //this.world.entities.clear();
        this.currentTitle = this.chooseRound;
        this.root.clear();
        for(int i = 5; i > 0; i--) {
            Button btn = new Button("Round " + (i), this.font);
            btn.padding = new Padding(10);
            final int finalI = i;
            btn.listener = new MouseListener() {
                @Override
                public void onMouseEvent(MouseEvent event) {
                    Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).start(Shroom.TWEEN);
                    Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(.5f).start(Shroom.TWEEN);
                    Tween.call(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> baseTween) {
                            Shroom.this.time = 10.f;
                            Shroom.this.finishing = false;
                            Shroom.this.currentRound = finalI;
                            Shroom.this.score = 0;
                            Shroom.this.enteredName = false;
                            Shroom.this.initGame();
                        }
                    }).delay(.5f).start(Shroom.TWEEN);
                }
            };
            this.root.add(btn);
        }

        Button menu = new Button("Return", this.font);
        menu.padding = new Padding(10);
        menu.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).start(Shroom.TWEEN);
                Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(.5f).start(Shroom.TWEEN);
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Shroom.this.initMenu();
                    }
                }).delay(.5f).start(Shroom.TWEEN);
            }
        };
        this.root.add(menu);
        this.root.layout();
    }

    public void initMenu() {
        this.inGame = false;
        //this.world.entities.clear();
        this.currentTitle = this.title;
        this.root.clear();
        Button play = new Button("Play Game", this.font);
        play.padding = new Padding(10);
        play.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).start(Shroom.TWEEN);
                Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(.5f).start(Shroom.TWEEN);
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Shroom.this.initRoundSelection();
                    }
                }).delay(.5f).start(Shroom.TWEEN);
            }
        };
        Button hiscore = new Button("Hi-score", this.font);
        hiscore.padding = new Padding(10);
        hiscore.listener = new MouseListener() {
            @Override
            public void onMouseEvent(MouseEvent event) {
                Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).start(Shroom.TWEEN);
                Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(.5f).start(Shroom.TWEEN);
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Shroom.this.initHiScore();
                    }
                }).delay(.5f).start(Shroom.TWEEN);
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
        this.root.add(hiscore);
        this.root.add(quit);
        this.root.layout();
    }

    public void finishGame() {
        this.enteredName = false;
        HiScore hiScore = new HiScore(this.currentRound, this.name, this.score);
        this.saveHiScore(hiScore);

        this.hiScoreList.save("./assets/data/hiscores.json");
        Tween.to(overlay, ColorAccessor.A, .5f).target(1.f).delay(5.f).start(Shroom.TWEEN);
        Tween.to(overlay, ColorAccessor.A, .5f).target(0.f).delay(5.5f).start(Shroom.TWEEN);
        Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> baseTween) {
                Shroom.this.world.entities.clear();
                String[] themes = AssetManager.load("./assets/data/round3.txt", String[].class);
                for(int i = 0; i < themes.length; i++) {
                    String str = themes[i];
                    Shroom.this.world.entities.add(new TextEntity(Shroom.this.world, i * (800.f / themes.length), -20 * i, font, str, true));
                }
                Shroom.this.initMenu();
                Shroom.this.inGame = false;
            }
        }).delay(5.5f).start(Shroom.TWEEN);
    }

    boolean finishing;
    double time = 10.f;

    @Override
    public void update() {
        Shroom.TWEEN.update((float) (1. / 20.));
        if(this.inGame && this.enteredName) {
            this.time -= 1. / 40.;
            if(this.time < 0) {
                this.time = 0;
                if(!this.finishing) {
                    this.finishing = true;
                    this.finishGame();
                }
            }
        }

        if(this.settings.mute) {
            this.win.setGain(0);
            this.fail.setGain(0);
            this.purple_retort.setGain(0);
        } else {
            this.win.setGain(this.settings.volume);
            this.fail.setGain(this.settings.volume);
            this.purple_retort.setGain(this.settings.volume);
        }

        KeyEvent ke = new KeyEvent();
        while(Keyboard.next()) {
            ke.key = Keyboard.getEventKey();
            ke.meta = Keyboard.isKeyDown(Keyboard.KEY_LMETA);
            ke.shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
            ke.ch = Keyboard.getEventCharacter();
            if(Keyboard.getEventKeyState()) {
                ke.state = KeyEvent.KeyState.DOWN;
            } else {
                ke.state = KeyEvent.KeyState.UP;
            }
            root.fire(ke);
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
        this.root.update();
    }

    @Override
    public void draw(double lerp) {
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(129.f / 255.f, 109.f / 255.f, 80.f / 255.f, 1.f);

        this.batch.start();

        this.world.draw(this.batch, lerp);
        if(this.inGame) {
            if(this.enteredName) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                this.toaster.title = decimalFormat.format(this.time);
                this.toaster.layout();
            } else if(this.finishing) {
                this.toaster.title = "Score: " + this.score;
                this.toaster.layout();
            } else {
                this.toaster.title = "Enter a name and press submit/enter to start the game.";
                this.toaster.layout();
            }
            this.toaster.x = 400 - this.toaster.width / 2;
            this.toaster.draw(batch, lerp);
        }

        if(!this.inGame) {
            this.bigFont.draw(batch, this.settings.width / 2, this.settings.height / 2 - 135, this.currentTitle, Font.Orientation.CENTER);
        }

        this.root.draw(this.batch, lerp);
        this.mute.draw(this.batch, lerp);
        this.batch.draw(this.silhouette, 0, 0, 800, 600);

        this.batch.setColor(this.overlay);
        this.batch.draw(this.blank, 0, 0, 800, 600);
        this.batch.setColor(ReadableColor.WHITE);

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
