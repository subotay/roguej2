package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Erou;
import com.mygdx.game.content.creatures.ErouAction;
import com.mygdx.game.content.creatures.GenAction;
import com.mygdx.game.content.objects.Trigger;
import com.mygdx.game.utils.CamUtil;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.utils.MapUtils;
import com.mygdx.game.utils.extdata.GameData;

import java.util.HashMap;

import static com.mygdx.game.content.Level.CellFlag.LOOT;
import static com.mygdx.game.content.Level.CellFlag.TRIGGER;

public class Controller extends InputAdapter implements Disposable {
    public final Joc joc;

    public TiledMap tiledmap;
    public float world_w,world_h, tile_w, tile_h;
    public CamUtil camUtil;
    public Level level;

    private boolean gameOver;

    public Controller(Joc joc) {
        this.joc = joc;
        init();
    }

    public void init() {
        camUtil=new CamUtil();

        Erou erou= new Erou();
        GameData.loadErou(erou);
        level= new Level();
        level.erou= erou;
        level.numeNivel= erou.levelName;
        erou.level= level;

        camUtil.lockOnTarget(new Vector2(level.erou.poz.x + .5f, level.erou.poz.y + .5f));
        camUtil.bound(Constants.VIEW_W / 2, Constants.VIEW_H / 2,
                world_w - Constants.VIEW_W / 2, world_h - Constants.VIEW_H / 2);

        initLevel();
    }

    /** citeste tmx */
    private void initLevel() {
        tiledmap= new TmxMapLoader().load("maps/" + level.numeNivel + ".tmx");
        world_w=tiledmap.getProperties().get("width", Integer.class);
        world_h=tiledmap.getProperties().get("height", Integer.class);
        tile_w=tiledmap.getProperties().get("tilewidth",Integer.class);
        tile_h=tiledmap.getProperties().get("tileheight",Integer.class);
        level.initCells(world_w, world_h, tile_w, tile_h);
        MapUtils.parseObjects(this);
        GameData.loadLvl(level);
    }

    public void update(float delta) {
        if (!gameOver) {
//            handleInput();  //event
            level.update(delta);

            camUtil.lerpToTarget(new Vector2(level.erou.poz.x + .5f, level.erou.poz.y + .5f));
            camUtil.bound(Constants.VIEW_W / 2, Constants.VIEW_H / 2,
                    world_w - Constants.VIEW_W / 2, world_h - Constants.VIEW_H / 2);
        }
    }


    // -------------- INPUT  EVENTS
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.NUMPAD_8:
                level.erou.walkDir= Creatura.Dir.N;
                level.erou.act=  new ErouAction.Walk(level.erou);
                break;
            case Input.Keys.NUMPAD_2:
                level.erou.walkDir= Creatura.Dir.S;
                level.erou.act= new ErouAction.Walk(level.erou);
                break;
            case Input.Keys.NUMPAD_4:
                level.erou.walkDir= Creatura.Dir.W;
                level.erou.act= new ErouAction.Walk(level.erou);
                break;
            case Input.Keys.NUMPAD_6:
                level.erou.walkDir= Creatura.Dir.E;
                level.erou.act= new ErouAction.Walk(level.erou);
                break;
            case  Input.Keys.NUMPAD_5:
                level.erou.walkDir= Creatura.Dir.STAY;
                level.erou.act= new GenAction.Rest(level.erou);
                break;
            case  Input.Keys.NUMPAD_1:
                level.erou.walkDir= Creatura.Dir.SW;
                level.erou.act= new ErouAction.Walk(level.erou);
                break;
            case  Input.Keys.NUMPAD_3:
                level.erou.walkDir= Creatura.Dir.SE;
                level.erou.act= new ErouAction.Walk(level.erou);
                break;
            case  Input.Keys.NUMPAD_7:
                level.erou.walkDir= Creatura.Dir.NW;
                level.erou.act= new ErouAction.Walk(level.erou);
                break;
            case  Input.Keys.NUMPAD_9:
                level.erou.walkDir= Creatura.Dir.NE;
                level.erou.act= new ErouAction.Walk(level.erou);
                break;
            case Input.Keys.G:
                if (level.cells[(int) level.erou.poz.x][((int) level.erou.poz.y)].contains(LOOT)){
//                    level.erou.STR++;
                    level.erou.act= new ErouAction.InteractLoot(level.erou,level.erou.poz.x, level.erou.poz.y);
                }
                break;
            case Input.Keys.ENTER:
                if (level.cells[((int) level.erou.poz.x)][(int)level.erou.poz.y].contains(TRIGGER)){
                    Trigger trig= level.getTriggerAt(level.erou.poz.x, level.erou.poz.y);

                    level.erou.levelName= trig.nivel;
                    level.erou.poz.set(trig.pozn[0], trig.pozn[1]);
                    GameData.saveErou(level.erou);
                    GameData.saveLvl(level);
                    joc.setScreen(joc.transition);
                }
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        camUtil.zoom(amount);
        camUtil.bound(Constants.VIEW_W / 2, Constants.VIEW_H / 2,
                world_w - Constants.VIEW_W / 2, world_h - Constants.VIEW_H / 2);
        return true;
    }



    @Override
    public void dispose() {
        level.dispose();
        tiledmap.dispose();
    }
}


 /* @Override
    public boolean keyTyped(char character) {
        switch (character){
            case Input.Keys.W:
                level.erou.walkDir= Creatura.Dir.N;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case Input.Keys.NUMPAD_2:
                level.erou.walkDir= Creatura.Dir.S;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case Input.Keys.NUMPAD_4:
                level.erou.walkDir= Creatura.Dir.W;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case Input.Keys.NUMPAD_6:
                level.erou.walkDir= Creatura.Dir.E;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case  Input.Keys.NUMPAD_5:
                level.erou.walkDir= Creatura.Dir.STAY;
                level.erou.act= new GenAction.Rest(level.erou);
                break;
            case  Input.Keys.NUMPAD_1:
                level.erou.walkDir= Creatura.Dir.SW;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case  Input.Keys.NUMPAD_3:
                level.erou.walkDir= Creatura.Dir.SE;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case  Input.Keys.NUMPAD_7:
                level.erou.walkDir= Creatura.Dir.NW;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case  Input.Keys.NUMPAD_9:
                level.erou.walkDir= Creatura.Dir.NE;
                level.erou.act= new ErouAction.WalkAt(level.erou);
                break;
            case Input.Keys.G:
                if (level.cells[(int) level.erou.poz.x][((int) level.erou.poz.y)].contains(LOOT)){
                    level.erou.act= new ErouAction.InteractLoot(
                            level.erou, level.getLootAt(level.erou.poz.x, level.erou.poz.y));
                }
                break;
            default:
        }
        return true;
    }*/


                    /*HashMap <String, String> data=new HashMap<String, String>();
                    data.put("nivel",trig.nivel);
                    data.put("erou_x", trig.pozn[0]+"");
                    data.put("erou_y", trig.pozn[1] + "");
                    GameData.INST.storeData(data);*/