package com.mygdx.game.content.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.mygdx.game.content.Level;

public class Trigger extends Obiect {
    public String nivel; //nume next/prev nivel
    public final int[] pozn = new int[2];

    public Trigger(Level level, MapProperties props) {
        super(level, props);
        //  !!!render in back dc e nevoie, nu mai fol sprite

        if (props.get("triggers", String.class).equals("tranzit")){
            nivel= props.get("nivel", String.class);
            pozn[0]=Integer.parseInt(props.get("px", String.class));
            pozn[1]= Integer.parseInt(props.get("py", String.class));
        } else {
            nivel=null;
        }
        level.cells[((int) poz.x)][(int)poz.y].add(Level.CellFlag.TRIGGER);
        level.triggs.add(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta, SpriteBatch batch) {
    }

    @Override
    public void dispose() {

    }



}
