package com.mygdx.game.utils.extdata;

import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Erou;

import java.util.HashMap;
import java.util.Map;

public class ErouDesc {
    //***************************************
    //
    public ErouDesc() {
        stts = new HashMap<String, Integer>();
    }


    public float px, py;
    //  attributes
    public int STR;
    public int AGI;
    public int VIT;
    public int END;
    public int SPI;
    //
    public HashMap<String, Integer> stts;

    public int hp,stam,lvl, xp;
    public boolean dumb;
    protected boolean ranged; //def melee
    public float energ;
    public int vraza;

    public  String levelName;

    //*************************************************

    public void from(Erou er){
        px= er.poz.x;
        py= er.poz.y;
        STR=er.STR;
        AGI=er.AGI;
        VIT=er.VIT;
        END=er.END;
        SPI=er.SPI;

        hp=er.hp;
        stam=er.stam;
        lvl=er.lvl;
        xp=er.xp;
        dumb=er.dumb;
        ranged=er.ranged;
        energ=er.energ;
        vraza=er.vraza;
        levelName=er.levelName;

        for (Creatura.Stat stat: Creatura.Stat.values())
            stts.put(stat.name(),er.stts.get(stat));
    }


    public void to(Erou er) {
        er.poz.x= px;
        er.poz.y=py;
        er.sprite.setBounds(px,py,1,1);
        er.STR=STR;
        er.AGI= AGI;
        er.VIT=VIT;
        er.END=END;
        er.SPI=SPI;

        er.hp=hp;
        er.stam=stam;
        er.lvl=lvl;
        er.xp=xp;
        er.dumb=dumb;
        er.ranged=ranged;
        er.energ=energ;
        er.vraza=vraza;
        er.levelName=levelName;

        for (Map.Entry<String, Integer> entry: stts.entrySet())
            er.stts.put(Enum.valueOf(Creatura.Stat.class,entry.getKey()),
                        entry.getValue());
    }
}

/* private static ErouDesc inst;

    public static ErouDesc getInst(){
        if (inst==null)
            inst= new ErouDesc();
        else{
            inst.stts.clear();
            inst.px= 0;
            inst.py= 0;
            inst.STR=0;
            inst.AGI=0;
            inst.VIT=0;
            inst.END=0;
            inst.SPI=0;

            inst.hp=0;
            inst.stam=0;
            inst.lvl=0;
            inst.xp=0;
            inst.dumb=false;
            inst.ranged=false;
            inst.energ=0;
            inst.vraza=0;
            inst.levelName=null;
        }

        return inst;
    }*/