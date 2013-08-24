package ee.tmtu.ld27.world;

import ee.tmtu.libludum.graphics.SpriteBatch;
import ee.tmtu.libludum.graphics.Texture;
import org.lwjgl.util.vector.Vector4f;

public class Tile {

    public Texture texture;
    public int[] coords;
    public Room room;

    public Tile(Texture texture, int[] coords) {
        this.texture = texture;
        this.coords = coords;
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        batch.drawRegion(this.texture, (float)this.coords[0]/this.texture.width, (float)this.coords[1]/this.texture.height, (float)this.coords[2]/this.texture.width, (float)this.coords[3]/this.texture.height, x, y, width, height);
    }

}
