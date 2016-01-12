package com.mygdx.game.content.objects.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.objects.Obiect;

import java.util.EnumMap;

public abstract class Item extends Obiect {
    static ObjectMap<String, String> ml;// mods labels
    static {
        ml = new ObjectMap<String, String>();
        ml.put("MHP","    maximum health +");
        ml.put("HPREG","    health regeneration +");
        ml.put("MSTAM","    maximum stamina +");
        ml.put("STAREG","   stamina regeneration +");
        ml.put("HIT","    hit chance +");
        ml.put("EVA","    evasion +");
        ml.put("FATK","    physical attack +");
        ml.put("FRES","    physical resistance +");
        ml.put("EATK","    elemental attack +");
        ml.put("ERES","    elemental resistance +");
        ml.put("CRIT","    critical chance +");
        ml.put("ARMOR","    armor +");
        ml.put("BONDMG","    damage +");
    }
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
    public enum ItemTip {
        WEAPON,
        HELM, ARMOR, BOOTS, SHIELD, AMULET, RING, TOOL,
        CONS,
        MISC
    }
//---------------------------------

    public Item() {
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public String toString() {
        return "Item: " + id + ", description: " + description + "\n";
    }

    public  String longDesc(){
        StringBuilder b=new StringBuilder();
        b.append("   ").append(description);
        return b.toString();
    };
}
