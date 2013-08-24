package ee.tmtu.ld27;

import ee.tmtu.libludum.core.Game;
import ee.tmtu.libludum.core.GameSettings;

public class Wordio extends Game {

    public Wordio(GameSettings settings) {
        super(settings);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(double lerp) {

    }

    public static void main(String[] args) {
        GameSettings settings = GameSettings.from("./assets/settings.cfg");
        new Thread(new Wordio(settings)).start();
    }

}
