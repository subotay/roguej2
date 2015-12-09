package com.mygdx.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
    public static final Assets INST =new Assets();
    public final AssetManager man;
        //sprites
    public static final String NEUTRU1= "tiles/dc-mon/angel.png";
    public static final String MONST1= "tiles/dc-mon/big_kobold.png";
    public static final String MONST2= "tiles/dc-mon/brown_ooze.png";
    public static final String EROU= "tiles/dc-mon/deep_elf_knight.png";

    public static final String TRAP1= "tiles/dc-misc/blood_red.png";
    public static final String LOOT1= "tiles/item/misc/misc_box.png";
//    public static final String TRIGGER1= "tiles/dc-dngn/gateways/fleshy_orifice_closed.png";
    public static final String DOORC= "tiles/dc-dngn/dngn_closed_door.png";
    public static final String DOORO= "tiles/dc-dngn/dngn_open_door.png";
        //sounds
    public static  final String S_RHIT = "sounds/range_atk.wav";
    public static  final String S_MHIT = "sounds/melee_atk.wav";
    public static  final String S_LOOT = "sounds/pickup.wav";


    private Assets(){
        man=new AssetManager();
    }

    public void initAssets() {
        man.load("ui/uiskin.atlas", TextureAtlas.class);
    }

    public void loadSprite(String name){
        man.load(name, Texture.class);
    }

    public Sprite getSprite(String name){
        man.finishLoadingAsset(name);
        Texture txt= man.get(name, Texture.class);
        Sprite sprite= new Sprite(txt);
        sprite.setSize(1, 1);
        return sprite;
    }

    @Override
    public void dispose() {
        man.dispose();
    }
}
