package com.mygdx.game.content.objects.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.objects.Obiect;

import java.util.EnumMap;

public abstract class Item extends Obiect {
    // -------  not used:
        //Level level;   //Obiect
        //Vector2 poz;   //Entitate

    //String id;      //Entitate
    //Sprite sprite;  //Entitate
    public String spriteN;
    public boolean stacks;
    public String description;
    public String name;
    public ItemTip tip;

//---------------------------------
    public enum ItemTip{
        WEAPON,
        HELM, ARMOR, BOOTS, SHIELD, AMULET, RING, TOOL,
        CONS,
        MISC
    }
//---------------------------------

    public Item(){ }

    @Override
    public void update(float delta) { }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override public String toString() { return "Item: "+id+", description: "+description+"\n";}

}
