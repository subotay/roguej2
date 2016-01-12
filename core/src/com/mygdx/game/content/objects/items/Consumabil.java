package com.mygdx.game.content.objects.items;

import com.mygdx.game.content.creatures.Creatura;

import java.util.EnumMap;

public class Consumabil extends Item {
    public EnumMap<Creatura.Stat,Integer> mods;
    public int duration; //nr of turns active effect
    //Condition ? TODO


    public Consumabil() { }

    @Override
    public String longDesc() {
        return super.longDesc();
    }
}
