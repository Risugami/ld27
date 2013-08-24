package ee.tmtu.ld27.entity;

import ee.tmtu.ld27.Shroom;
import ee.tmtu.ld27.world.World;
import ee.tmtu.libludum.graphics.Font;
import ee.tmtu.libludum.graphics.SpriteBatch;

public class TextEntity extends Entity {

    public float prevRotation, rotation;
    public float rotationSpeed;
    public float weight;
    public Font font;
    public String word;

    public TextEntity(World world, Font font, String word) {
        super(world);
        this.font = font;
        this.word = word;
        this.weight = font.getWidth(word) / word.length() / 2;
        this.rotation = (float) (Math.random() * 360.);
        this.rotationSpeed = this.weight * .0005f;
    }

    @Override
    public void update() {
        this.xPrev = x;
        this.yPrev = y;
        this.prevRotation = this.rotation;
        this.rotation += this.rotationSpeed;
        this.y += this.weight;
    }

    @Override
    public void draw(SpriteBatch batch, double lerp) {
        this.font.draw(batch, Shroom.lerp(this.xPrev, this.x, lerp), Shroom.lerp(this.yPrev, this.y, lerp), Shroom.lerp(this.prevRotation, this.rotation, lerp), this.word);
    }

}
