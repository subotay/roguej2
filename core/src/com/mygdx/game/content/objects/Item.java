package com.mygdx.game.content.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.mygdx.game.content.Level;

public class Item extends Obiect {

    public Item(String id){this.id=id;}



    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta, SpriteBatch batch) {

    }
}

/*public Item(Level level) {
        super(level);
        id="it1";
    }

    public Item(Level level, MapProperties props) {
        super(level, props);
        id="it1";
    }*/