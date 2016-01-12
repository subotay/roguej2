package com.mygdx.game.content.objects.items;

import com.mygdx.game.content.creatures.Creatura;

import java.util.EnumMap;

public class Echipabil extends Item {
    public EnumMap<Creatura.Stat,Integer> mods;
    //Condition ?
    //Ability ?

    public Echipabil() { }

    @Override
    public String longDesc() {
        StringBuilder b=new StringBuilder("\n");
        for (Creatura.Stat stat: mods.keySet()){
            if (mods.get(stat)!=null && mods.get(stat)!=0) {
                b.append(ml.get(stat.name())).append(mods.get(stat)).append("\n");
            }
        }
        b.append("\n").append(super.longDesc());
        return b.toString();
    }
}
