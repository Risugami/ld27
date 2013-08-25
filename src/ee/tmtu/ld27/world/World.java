package ee.tmtu.ld27.world;

import ee.tmtu.ld27.entity.Entity;
import ee.tmtu.ld27.entity.TextEntity;
import ee.tmtu.libludum.graphics.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;

public class World {

    public ArrayList<Entity> entities;

    public World() {
        this.entities = new ArrayList<>(50);
    }

    public boolean submit(String str) {
        for(Entity entity : this.entities) {
            if(entity instanceof TextEntity) {
                TextEntity textEntity = (TextEntity)entity;
                System.out.println(textEntity.word);
                if(textEntity.word.equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void update() {
        Iterator<Entity> iterator = this.entities.iterator();
        Entity entity;
        while(iterator.hasNext()) {
            entity = iterator.next();
            if(entity.dead) {
                iterator.remove();
                continue;
            }
            entity.update();
        }
    }

    public void draw(SpriteBatch batch, double lerp) {
        for(Entity entity : this.entities) {
            entity.draw(batch, lerp);
        }
    }

}
