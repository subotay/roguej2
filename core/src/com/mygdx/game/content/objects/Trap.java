package com.mygdx.game.content.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.content.Level;
import com.mygdx.game.utils.Assets;

public class Trap extends Obiect {
    public int dmg;


    public Trap(Level level, MapProperties props) {
        super(level, props);

        id= props.get("id", String.class);
        JsonValue trapData= new JsonReader().parse(Gdx.files.internal("actors/traps.dat")).get(id);


        dmg= trapData.get("dmg").asInt();
        String sprN= trapData.get("sprite").asString();
        Assets.INST.loadSprite(sprN);
        sprite= Assets.INST.getSprite(sprN);
        sprite.setBounds(poz.x, poz.y, 1, 1);

        level.cells[(int)poz.x][(int)poz.y]. add(Level.CellFlag.TRAP);
        level.traps.add(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public String toString() {
        return "Trap"+id+" dmg:"+dmg;
    }
}
