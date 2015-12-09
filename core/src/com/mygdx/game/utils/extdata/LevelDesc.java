package com.mygdx.game.utils.extdata;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Badguy;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Npc;
import com.mygdx.game.content.objects.Container;
import com.mygdx.game.content.objects.Item;

import java.util.HashMap;
import java.util.Scanner;

public class LevelDesc {
    public ObjectMap<String, Array<Vector2>> creaturi;
    public ObjectMap<String, Array<String>> containers;  //poz-> list of items id

    public LevelDesc(){
        creaturi= new ObjectMap<String, Array<Vector2>>();
        containers= new ObjectMap<String, Array<String>>();
    }


    public void to(Level lvl){
        System.out.println("Creaturi lvl desc "+creaturi);   //debug
        System.out.println("Containers lvl desc "+containers);     //debug

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


        //
        for (String poz: containers.keys()){
            Container c=new Container();
            c.level= lvl;
            lvl.loots.add(c);
            Scanner sc=new Scanner(poz);
            c.poz.set(sc.nextFloat(), sc.nextFloat());
            lvl.cells[((int) c.poz.x)][((int) c.poz.y)].add(Level.CellFlag.LOOT);

            Array<String> content=containers.get(poz);
                for (String itId:content)
                    c.items.add(new Item(itId));
        }
    }


    public void from(Level lvl){
        for (Creatura actor:lvl.actori)
                if (creaturi.containsKey(actor.id))
                    creaturi.get(actor.id).add(actor.home);
                else {
                    Array<Vector2> pozl= new Array<Vector2>();
                    pozl.add(actor.home);
                    creaturi.put(actor.id,pozl);
                }


        for (Container cont: lvl.loots){
            if (cont.sprite!=null) continue;  //salvez doar chest, unlooted drop se pierde

            Array<String> content= new Array<String>();
            for (Item it: cont.items)
                    content.add(it.id);

            containers.put(cont.poz.x + " " + cont.poz.y, content);
        }
    }
}

