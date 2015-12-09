package com.mygdx.game.utils.extdata;


import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.utils.Assets;

public class CreatDesc {
    //  attributes
    public int STR;
    public int AGI;
    public int VIT;
    public int END;
    public int SPI;
    //
    public ObjectMap<String, Integer> stts;
    public boolean dumb;
    protected boolean ranged; //def melee
    public float energ;
    public String sprite;


    public CreatDesc(){
        stts=new ObjectMap<String, Integer>();
    }

    public void to(Creatura cre){
        cre.STR= STR;
        cre.AGI= AGI;
        cre.VIT=VIT;
        cre.END=END;
        cre.SPI=SPI;
        cre.dumb= dumb;
        cre.ranged= ranged;
        cre.energ= energ;

        Assets.INST.loadSprite(sprite);
        cre.sprite= Assets.INST.getSprite(sprite);

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
        ranged= cre.ranged;
//        energ= cre.energ;

        for (Creatura.Stat st: cre.stts.keySet()){
            stts.put(st.name(), cre.stts.get(st));
        }
    }*/

