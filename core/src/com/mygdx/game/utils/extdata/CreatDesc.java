package com.mygdx.game.utils.extdata;


import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.utils.Assets;

public class CreatDesc {
    //  attributes
     int STR;
     int AGI;
     int VIT;
     int END;
     int SPI;
    //
     ObjectMap<String, Integer> stts;
     boolean dumb;
     boolean melee; //def melee
     String sprite;
     String name;
     int atkcost;

    public CreatDesc(){ }

    public void to(Creatura cre){
        cre.STR= STR;
        cre.AGI= AGI;
        cre.VIT=VIT;
        cre.END=END;
        cre.SPI=SPI;
        cre.dumb= dumb;
        cre.melee= melee;
        cre.name= name;
        cre.atkcost=atkcost;

        Assets.loadSprite(sprite);
        cre.sprite= Assets.getSprite(sprite);

        for (String stt: stts.keys())
            cre.stts.put(Enum.valueOf(Creatura.Stat.class,stt), stts.get(stt));

        cre.hp= cre.mhp();
        cre.stam= cre.mstam();
    }
}



   /* public void from(Creatura cre){
        STR= cre.STR;
        AGI= cre.AGI;
        VIT=cre.VIT;
        END=cre.END;
        SPI=cre.SPI;
        dumb= cre.dumb;
        melee= cre.melee;
//        energ= cre.energ;

        for (Creatura.Stat st: cre.stts.keySet()){
            stts.put(st.name(), cre.stts.get(st));
        }
    }*/

