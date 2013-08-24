package ee.tmtu.ld27.world;

import ee.tmtu.ld27.entity.Entity;
import ee.tmtu.libludum.graphics.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;

public class Room {

    public ArrayList<Entity> entities;
    public Tile[][] tiles;

    public Room() {
        this.entities = new ArrayList<>();
        this.tiles = new Tile[8][6];
    }

    public void update() {
        Iterator<Entity> entityIterator = this.entities.iterator();
        Entity entity;
        while(entityIterator.hasNext()) {
            entity = entityIterator.next();
            if(entity.dead) {
                entityIterator.remove();
                continue;
            }
            entity.update();
        }
    }

    public void draw(SpriteBatch batch, double lerp) {
        for(int x = 0; x < this.tiles.length; x++) {
            for(int y = 0; y < this.tiles[x].length; y++) {
                Tile tile = this.tiles[x][y];
                if(tile != null) {
                    tile.draw(batch, (400.f / 8.f) * x, (300.f / 6.f) * y, 400.f / 8.f, 300.f / 6.f);
                }
            }
        }
        for(Entity entity : this.entities) {
            entity.draw(batch, lerp);
        }
    }


}
