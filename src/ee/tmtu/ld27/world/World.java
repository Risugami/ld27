package ee.tmtu.ld27.world;

import ee.tmtu.ld27.entity.Player;
import ee.tmtu.libludum.graphics.SpriteBatch;

public class World {

    public Player player;
    public Room[][] rooms;

    public World() {

    }

    public void update() {
        this.player.getCurrentRoom().update();
    }

    public void draw(SpriteBatch batch, double lerp) {
        this.player.getCurrentRoom().draw(batch, lerp);
    }

}
