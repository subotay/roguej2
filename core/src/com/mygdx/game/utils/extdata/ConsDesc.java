package com.mygdx.game.utils.extdata;


import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.objects.items.Consumabil;
import com.mygdx.game.content.objects.items.Item;

import java.util.EnumMap;

public class ConsDesc extends ItemDesc {
    //***************************
    ObjectMap<String, Integer> mods;
    int duration;
    //***************************

    public ConsDesc() {}

    @Override
    public void to(Item it) {
        super.to(it);
        Consumabil cit= (Consumabil) it;

        cit.duration= duration;

        cit.mods= new EnumMap<Creatura.Stat, Integer>(Creatura.Stat.class);
        for (String mod: mods.keys())
            cit.mods.put(Enum.valueOf(Creatura.Stat.class,mod),mods.get(mod));
    }
}
