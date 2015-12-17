package com.mygdx.game.content.objects.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.objects.Obiect;


public class ItemContainer extends Obiect {
    public final ObjectMap<Item, Integer> items;

    /** erou inv, eqp*/
    public ItemContainer(){  this.items= new ObjectMap<Item, Integer>(); }

    /**   spawn on death, drops*/
    public ItemContainer(Level level, Item... items){
        super(level);
        this.items= new ObjectMap<Item, Integer>();
        for (Item it:items)
            this.items.put(it, 1);
    }



    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        if (sprite!=null)
            sprite.draw(batch);
    }

    @Override
    public void dispose() {
    }

    @Override
    public String toString() {
        return "\n"+items;
    }
}

