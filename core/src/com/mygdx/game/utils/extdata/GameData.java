package com.mygdx.game.utils.extdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Erou;

import java.util.HashMap;

public class GameData {
    public static final GameData INST =new GameData();
    private GameData(){}


    public static void saveErou(Erou erou){
        ErouDesc desc = new ErouDesc();
        desc.from(erou);

        Json saver= new Json();
        saver.setElementType(ErouDesc.class, "stts", HashMap.class);
        saver.setOutputType(JsonWriter.OutputType.json);
        saver.setUsePrototypes(false);

        String sav= saver.prettyPrint(desc);
        Gdx.files.local("sav.dat").writeString(sav, false);
    }

    public static void loadErou(Erou erou) {

        Json loader= new Json();
        loader.setElementType(ErouDesc.class, "stts", HashMap.class);
        loader.setOutputType(JsonWriter.OutputType.json);
        loader.setUsePrototypes(false);

        String sav= Gdx.files.local("sav.dat").readString();
        ErouDesc desc = loader.fromJson(ErouDesc.class,sav);
        desc.to(erou);
    }


    public static void loadLvl(Level level) {
        Json loader=new Json();
        loader.setOutputType(JsonWriter.OutputType.json);
        loader.setUsePrototypes(false);

        String lvldata= Gdx.files.local("levels/"+level.numeNivel+".dat").readString();
        LevelDesc ld = loader.fromJson(LevelDesc.class,lvldata);
        ld.to(level);
    }

    public static void saveLvl(Level level){
        LevelDesc ldesc= new LevelDesc();
        ldesc.from(level);

        Json saver= new Json();
        saver.setOutputType(JsonWriter.OutputType.json);
        saver.setUsePrototypes(false);

        String lvldata= saver.prettyPrint(ldesc);
        Gdx.files.local("levels/"+level.numeNivel+".dat").writeString(lvldata, false);
    }
}

//private Preferences prefs= Gdx.app.getPreferences("rg_savedata.prefs");

    /*public void storeData(HashMap<String, String> data){
        prefs.put(data);
        prefs.flush();
    }

    public HashMap<String,String> loadData(){
        HashMap<String, String> dm= new HashMap<String, String>();
        dm.put("nivel",prefs.getString("nivel","maps/nivel1.tmx"));
        dm.put("erou_x", prefs.getString("erou_x", "15"));
        dm.put("erou_y", prefs.getString("erou_y", "0"));
        return dm;
    }*/
//--------------------------------------------------------