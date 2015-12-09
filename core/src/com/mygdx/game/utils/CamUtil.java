package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CamUtil {
    private Vector2 position;
    private float zoom;

    public CamUtil() {
        position=new Vector2();
        zoom=1f;
    }

    public void lockOnTarget(Vector2 target){
        position.set(target.x, target.y);
    }

    public void lerpToTarget(Vector2 target){
         Vector2 old=new Vector2(position);
         position.set(old.x + (target.x - old.x) * .35f,
                 old.y + (target.y - old.y) * .35f);
    }

    /** limita in rect: [startx, starty, endx, endy] */
    public void bound( float x1, float y1,float x2,float y2){
        Vector2 pos=new Vector2(position);
        if (pos.x<x1) pos.x=x1;
        if (pos.y<y1) pos.y=y1;
        if (pos.x>x2) pos.x=x2;
        if (pos.y>y2) pos.y=y2;
        position.set(pos);
    }

    public void zoom(float ct){
        zoom+=ct/10;
        MathUtils.clamp(zoom, .1f, 10f);
    }

    public void setCamTo(OrthographicCamera cam){
        cam.position.set(new Vector3(position.x, position.y, 0));
        cam.zoom= zoom;
        cam.update();
    }
}