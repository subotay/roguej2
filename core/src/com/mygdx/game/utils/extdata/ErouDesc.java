package com.mygdx.game.utils.extdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Erou;
import com.mygdx.game.content.objects.items.*;

public class ErouDesc {
    //***************************************
    float px, py;
    //  attributes
    int STR;
    int AGI;
    int VIT;
    int END;
    int SPI;
    ObjectMap<String, Integer> stts;

    int hp,stam,lvl, xp;
    boolean dumb;
    boolean ranged; //def melee
    float energ;
    int vraza;

    String levelName;
    ObjectMap<String, Integer> inv; //  item.id-> amount
    ObjectMap<String,String> eqp;   //eqp slot- item id

    public ErouDesc() { }

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
        stts= new ObjectMap<String, Integer>();
        inv= new ObjectMap<String, Integer>();
        eqp= new ObjectMap<String, String>();

        for (Creatura.Stat stat: Creatura.Stat.values())
            stts.put(stat.name(),er.stts.get(stat));

        for (Item it: er.inv.items.keys())
            inv.put(it.id, er.inv.items.get(it) );

        for (Erou.EqpSlot slot : er.eqp.keySet())
            eqp.put(slot.name(), er.eqp.get(slot).id);
    }

    //*************************************************
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

        //build stats
        for (ObjectMap.Entry<String, Integer> entry: stts.entries())
            er.stts.put(Enum.valueOf(Creatura.Stat.class, entry.key),
                    entry.value);

        //build items: inv
        JsonReader reader= new JsonReader();
        JsonValue
                weapons= reader.parse(Gdx.files.internal("items/weapons.dat")),
                equips= reader.parse(Gdx.files.internal("items/equips.dat")),
                cons= reader.parse(Gdx.files.internal("items/cons.dat")),
                miscs= reader.parse(Gdx.files.internal("items/miscs.dat"));
        Json json= new Json();
        ItemDesc desc= new ItemDesc();

        for (String id:inv.keys()){
            Item it= null;
            String itd=null;
            switch (id.charAt(0)){
                case 'w':
                    it= new Weapon();
                    itd= weapons.get(id).toString();
                    desc= json.fromJson(WeaponDesc.class, itd.substring(itd.indexOf("{")));
                    break;
                case 'e':
                    it= new Echipabil();
                    itd= equips.get(id).toString();
                    desc= json.fromJson(EchipDesc.class, itd.substring(itd.indexOf("{")));
                    break;
                case 'c':
                    it= new Consumabil();
                    itd= cons.get(id).toString();
                    desc= json.fromJson(ConsDesc.class, itd.substring(itd.indexOf("{")));
                    break;
                case 'm':
                    it= new Misc();
                    itd= miscs.get(id).toString();
                    desc= json.fromJson(ItemDesc.class, itd.substring(itd.indexOf("{")));
                    break;
            }
            it.id= id;
            desc.to(it);

            er.inv.items.put(it, inv.get(id));
        }
        //eqp
        String id;
        for (String slot:eqp.keys()){  //eqp==map <eqp slot, item id>
            Item it= null;
            id= eqp.get(slot);
            String itd=null;

            switch (id.charAt(0)){
                case 'w':
                    it= new Weapon();
                    itd= weapons.get(id).toString();
                    desc= json.fromJson(WeaponDesc.class, itd.substring(itd.indexOf("{")));
                    break;
                case 'e':
                    it= new Echipabil();
                    itd= equips.get(id).toString();
                    desc= json.fromJson(EchipDesc.class, itd.substring(itd.indexOf("{")));
                    break;
                case 'c':
                    it= new Consumabil();
                    itd= cons.get(id).toString();
                    desc= json.fromJson(ConsDesc.class, itd.substring(itd.indexOf("{")));
                    break;
                case 'm':
                    it= new Misc();
                    itd= miscs.get(id).toString();
                    desc= json.fromJson(ItemDesc.class, itd.substring(itd.indexOf("{")));
                    break;
            }
            desc.to(it);

            er.eqp.put(Enum.valueOf(Erou.EqpSlot.class, slot), it);
        }

        System.out.println("erou inv: "+er.inv);    //debug
        System.out.println();
        System.out.println("erou equip: "+er.eqp);  //debug
    }
}
