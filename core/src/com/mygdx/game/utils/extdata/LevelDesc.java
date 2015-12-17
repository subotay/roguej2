package com.mygdx.game.utils.extdata;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Badguy;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Npc;
import com.mygdx.game.content.objects.items.*;

import java.util.Scanner;

public class LevelDesc {
    public ObjectMap<String, Array<Vector2>> creaturi;    //save actor.id -> home position
    public ObjectMap<String, ObjectMap<String,Integer>> containers;  //save container pos -> map of (item.id, item amount)

    public LevelDesc(){ }


    public void to(Level lvl){
        System.out.println("Creaturi lvl desc:\n   "+creaturi);   //debug
        System.out.println("Containers lvl desc:\n  "+containers);     //debug

        JsonReader reader= new JsonReader();
        JsonValue mobs= reader.parse(Gdx.files.internal("actors/mobs.dat")),
                 npcs= reader.parse(Gdx.files.internal("actors/npcs.dat"));
        Json json=new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        json.setElementType(CreatDesc.class,"stts", ObjectMap.class);

        for (String id:creaturi.keys()){

            if (id.startsWith("m")){  //mob
                String md= mobs.get(id).toString();
                CreatDesc mobdesc= json.fromJson(CreatDesc.class, md.substring(md.indexOf("{")));

                for (Vector2 pos: creaturi.get(id)){
                    Badguy mob= new Badguy();
                    mobdesc.to(mob);
                    lvl.actori.add(mob);
                    mob.level= lvl;
                    mob.id=id;
                    mob.poz.set(pos.x, pos.y);
                    lvl.cells[(int)mob.poz.x][(int)mob.poz.y].add(Level.CellFlag.MONST);
                    mob.sprite.setBounds(mob.poz.x, mob.poz.y, 1, 1);
                    mob.home= new Vector2(mob.poz);
                }
            }
            else { //npc
                String nd= npcs.get(id).toString();
                CreatDesc npcdesc= json.fromJson(CreatDesc.class, nd.substring(nd.indexOf("{")));

                for (Vector2 pos: creaturi.get(id)){
                    Npc npc= new Npc();
                    lvl.actori.add(npc);
                    npc.level= lvl;
                    npcdesc.to(npc);
                    npc.id=id;
                    npc.poz.set(pos.x, pos.y);
                    lvl.cells[(int)npc.poz.x][(int)npc.poz.y].add(Level.CellFlag.NPC);
                    npc.sprite.setBounds(npc.poz.x, npc.poz.y, 1, 1);
                    npc.home= new Vector2(npc.poz);
                }
            }
        }

        // build loot containers
        JsonValue
                weapons= reader.parse(Gdx.files.internal("items/weapons.dat")),
                equips= reader.parse(Gdx.files.internal("items/equips.dat")),
                cons= reader.parse(Gdx.files.internal("items/cons.dat")),
                miscs= reader.parse(Gdx.files.internal("items/miscs.dat"));
        ItemDesc desc= new ItemDesc();

        for (String poz: containers.keys()){
            ItemContainer container=new ItemContainer();
            container.level= lvl;
            lvl.loots.add(container);
            Scanner sc=new Scanner(poz);
            container.poz.set(sc.nextFloat(), sc.nextFloat());
            lvl.cells[((int) container.poz.x)][((int) container.poz.y)].add(Level.CellFlag.LOOT);

            //items inside --  map of (item.id, item amount)
            ObjectMap<String, Integer> content=containers.get(poz);
            for (String id:content.keys()){
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

                container.items.put(it, content.get(id));
            }
        }
    }

    //*****************************************************************
    public void from(Level lvl){
        creaturi= new ObjectMap<String, Array<Vector2>>();
        containers= new ObjectMap<String, ObjectMap<String, Integer>>();

        for (Creatura actor:lvl.actori)
                if (creaturi.containsKey(actor.id))
                    creaturi.get(actor.id).add(actor.home);
                else {
                    Array<Vector2> pozl= new Array<Vector2>();
                    pozl.add(actor.home);
                    creaturi.put(actor.id,pozl);
                }


        for (ItemContainer cont: lvl.loots){
            if (cont.sprite!=null) continue;  //salvez doar chest, unlooted drop se pierde

            ObjectMap<String, Integer> content= new ObjectMap<String, Integer>();
            for (Item it: cont.items.keys())  {
                content.put(it.id, cont.items.get(it)); //id- amount
            }
            containers.put(cont.poz.x + " " + cont.poz.y, content);
        }
    }
}

