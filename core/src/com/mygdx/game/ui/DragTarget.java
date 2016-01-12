package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

public class DragTarget extends DragAndDrop.Target{
    Actor tinta;

    public DragTarget(Actor actor) {
        super(actor);
        tinta= actor;
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
        //
    }

}
