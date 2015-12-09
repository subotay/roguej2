package com.mygdx.game.content.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.content.Level;


public class Container extends Obiect {
    public final Array<Item> items;

    public Container(){  this.items=new Array<Item>(); }

    /**   spawn on death, drops*/
    public Container(Level level, Item... items){
        super(level);
        this.items=new Array<Item>();
        for (Item it:items)
            this.items.add(it);
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

}

/*   *//** constructor apela numai din map parser, deci e chest; rendered in background, nu are nevoie de sprite *//*
    public Container(Level level, MapProperties props) {
        super(level,props);
        this.items=new Array<Item>();
//        Assets.INST.loadSprite(Assets.LOOT1);
//        sprite= Assets.INST.getSprite(Assets.LOOT1);
//        sprite.setBounds(poz.x, poz.y,1,1);
    }*/