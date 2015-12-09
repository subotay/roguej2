package com.mygdx.game.content.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.mygdx.game.content.Level;
import com.mygdx.game.utils.Assets;

public class Door extends Obiect {
    public boolean opened;

    public Door(Level level, MapProperties props) {
        super(level, props);
        // TODO aici ar trebui in fctie de props
        opened= false;
        Assets.INST.loadSprite(Assets.DOORC);
        Assets.INST.loadSprite(Assets.DOORO);
        sprite= Assets.INST.getSprite(Assets.DOORC);
        sprite.setBounds(poz.x, poz.y, 1, 1);

        level.cells[(int)poz.x][(int)poz.y]. add(Level.CellFlag.DOOR);
        level.doors.add(this);
        if (!opened) {
            level.cells[(int) poz.x][(int)poz.y].add(Level.CellFlag.BLOC_MV);
            level.cells[(int) poz.x][(int)poz.y].add(Level.CellFlag.BLOC_V);
        }

    }


    @Override
    public void update(float delta) {

    }
    public void open(){
        sprite= Assets.INST.getSprite(Assets.DOORO);
        sprite.setBounds(poz.x, poz.y, 1, 1);
        opened= true;
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }

}
