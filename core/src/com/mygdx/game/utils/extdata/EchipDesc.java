package com.mygdx.game.utils.extdata;

import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.objects.items.Echipabil;
import com.mygdx.game.content.objects.items.Item;

import java.util.EnumMap;


public class EchipDesc extends ItemDesc {
    //***********************
     ObjectMap<String, Integer> mods;
    //*****************************
    public EchipDesc() { }

    @Override
    public void to(Item it) {
        super.to(it);

        Echipabil eit= (Echipabil) it;

        eit.mods= new EnumMap<Creatura.Stat, Integer>(Creatura.Stat.class);
        for (String mod: mods.keys())
            eit.mods.put(Enum.valueOf(Creatura.Stat.class,mod),mods.get(mod));
    }
}
