package ee.tmtu.ld27.entity;

import ee.tmtu.ld27.world.Room;
import ee.tmtu.libludum.graphics.Sprite;
import ee.tmtu.libludum.graphics.SpriteBatch;

public class Player extends Entity {

    public Sprite playerSprite;

    public Player() {

    }

    @Override
    public void draw(SpriteBatch batch, double lerp) {
        batch.draw(this.playerSprite);
    }

    public Room getCurrentRoom() {
        return null;
    }

}
