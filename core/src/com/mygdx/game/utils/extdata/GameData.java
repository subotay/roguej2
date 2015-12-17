package com.mygdx.game.utils.extdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Erou;

public class GameData {
    private GameData(){}

    public static void saveErou(Erou erou){
        ErouDesc desc = new ErouDesc();
        desc.from(erou);

        Json saver= new Json();
        saver.setOutputType(JsonWriter.OutputType.json);
        saver.setUsePrototypes(false);

        String sav= saver.prettyPrint(desc,140);
        Gdx.files.local("sav.dat").writeString(sav, false);
    }

    public static void loadErou(Erou erou) {
        Json loader= new Json();
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

        String lvldata= saver.prettyPrint(ldesc,140);
        Gdx.files.local("levels/"+level.numeNivel+".dat").writeString(lvldata, false);
    }
}

