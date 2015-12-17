package com.mygdx.game.utils.extdata;

import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.utils.Assets;

public class ItemDesc {
    //*******************************
    String sprite;
    boolean stack;
    String description;
    String name;
    String tip;
    //**********************************
    public ItemDesc() {}

    public void to(Item it){
        it.spriteN= sprite;
        Assets.loadSprite(sprite);
        it.sprite= Assets.getSprite(sprite);
        it.stacks= stack;
        it.description= description;
        it.name=name;
        it.tip= Enum.valueOf(Item.ItemTip.class, tip);
    }

}

   /* public void from(Item it){
        sprite= it.spriteN;

        mods = new ObjectMap<String, Integer>();
        for (Creatura.Stat stat: Creatura.Stat.values())
            mods.put(stat.name(),it.mods.get(stat));
    }*/
