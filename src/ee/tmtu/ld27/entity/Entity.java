package ee.tmtu.ld27.entity;

import ee.tmtu.ld27.world.World;
import ee.tmtu.libludum.graphics.SpriteBatch;

public abstract class Entity {

    public float x, y, xPrev, yPrev, width, height;
    public boolean dead;
    public World world;

    public Entity(World world) {
        this.world = world;
    }

    public void update() {

    }

    public void draw(SpriteBatch batch, double lerp) {

    }

}
