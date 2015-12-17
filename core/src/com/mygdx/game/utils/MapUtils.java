package com.mygdx.game.utils;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Controller;
import com.mygdx.game.content.Level.CellFlag;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.objects.*;


public class MapUtils {
    public static void parseObjects(Controller c) {

        Array<RectangleMapObject> rectmapObjects = c.tiledmap.getLayers().get("objects").getObjects()
                .getByType(RectangleMapObject.class);
        for (RectangleMapObject robj : rectmapObjects) {

            if (robj.getProperties().get("type",String.class).equals("obiect")){
                Obiect.makeObiect(c.level, robj.getProperties());
            }

            else if (robj.getProperties().get("type", String.class).equals("wall")){
                c.level.addWalls(robj);
            }
            else if (robj.getProperties().get("type", String.class).equals("decor")){
                c.level.addDecor(robj);
            }
        }
    }
}

 /* if (robj.getProperties().get("type",String.class).equals("creatura")){
                Creatura cre;
                cre= Creatura.makeCreatura(c.level,robj.getProperties());
                c.level.actori.add(cre);

                if (robj.getProperties().get("tip",String.class).equals("monstru"))
                    c.level.cells[(int)cre.poz.x][(int)cre.poz.y].add( CellFlag.MONST);

                else if (robj.getProperties().get("tip",String.class).equals("npc"))
                    c.level.cells[(int)cre.poz.x][(int)cre.poz.y].add( CellFlag.NPC);
            }else */





/* if          (ob instanceof ItemContainer){
                    c.level.cells[(int)ob.poz.x][(int)ob.poz.y]. add(CellFlag.LOOT);
                    c.level.loots.add((ItemContainer)ob);
                } else
                if (ob instanceof Trigger) {
//                    c.level.cells[(int)ob.poz.x][(int)ob.poz.y]. add(CellFlag.TRIGGER);
//                    c.level.triggs.add((Trigger)ob);
                } else
                if (ob instanceof Trap)   {
                   *//* c.level.cells[(int)ob.poz.x][(int)ob.poz.y]. add(CellFlag.TRAP);
                    c.level.traps.add((Trap)ob);*//*
//                    c.level.actori.add(ob);
                } else
                if (ob instanceof Door){
                    *//*c.level.cells[(int)ob.poz.x][(int)ob.poz.y]. add(CellFlag.DOOR);
                    c.level.doors.add((Door)ob);
                    if (!((Door) ob).opened) {
                        c.level.cells[(int) ob.poz.x][(int)((Door) ob).poz.y].add(CellFlag.BLOC_MV);
                        c.level.cells[(int) ob.poz.x][(int)ob.poz.y].add(CellFlag.BLOC_V);
                    }*//*
                }*/