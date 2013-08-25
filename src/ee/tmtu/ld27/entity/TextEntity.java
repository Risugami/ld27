package ee.tmtu.ld27.entity;

import ee.tmtu.ld27.Shroom;
import ee.tmtu.ld27.world.World;
import ee.tmtu.libludum.graphics.Font;
import ee.tmtu.libludum.graphics.SpriteBatch;
import org.lwjgl.util.Color;

public class TextEntity extends Entity {

    public float prevRotation, rotation;
    public float rotationSpeed;
    public float weight;
    public Font font;
    public String word;
    public boolean score, xrazy;

    public TextEntity(World world, float x, float y, Font font, String word) {
        super(world);
        this.x = x;
        this.y = y;
        this.font = font;
        this.word = word;
        this.weight = font.getWidth(word) / word.length() / 2;
        this.rotation = (float) (Math.random() * 360.);
        this.rotationSpeed = this.weight * .0005f;
    }

    public TextEntity(World world, float x, float y, Font font, String word, boolean xrazy) {
        super(world);
        this.x = x;
        this.y = y;
        this.font = font;
        this.word = word;
        this.weight = font.getWidth(word) / word.length() / 4 * (float)(Math.random() * 4);
        this.rotation = (float) (Math.random() * 360.);
        this.rotationSpeed = this.weight * .0005f;
        this.xrazy = xrazy;
    }

    @Override
    public void update() {
        this.xPrev = x;
        this.yPrev = y;
        this.prevRotation = this.rotation;
        this.rotation += this.rotationSpeed;
        this.y += this.weight;
        if(this.xrazy) {
            if(this.y - this.font.getWidth(this.word) - this.font.lineheight > 800) {
                if(score) this.dead = true;
                this.y = this.yPrev = -(this.font.lineheight + this.font.getWidth(this.word));
            }
        } else {
            if(this.y - 20 - this.font.lineheight > 800) {
                if(score) this.dead = true;
                this.y = this.yPrev = -(this.font.lineheight + this.font.getWidth(this.word));
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch, double lerp) {
        this.font.draw(batch, Shroom.lerp(this.xPrev, this.x, lerp), Shroom.lerp(this.yPrev, this.y, lerp), xrazy ? Shroom.lerp(this.prevRotation, this.rotation, lerp) : (float)Math.sin(Shroom.lerp(this.prevRotation, this.rotation, lerp) * this.rotationSpeed) / 2, this.word, new Color(251, 201, 148, 128));
    }

}
