package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Joc;
import com.mygdx.game.utils.Assets;

public class TransScreen implements Screen {
    private final Joc joc;
    private ShapeRenderer sr;
    private float x,y;

    public TransScreen(Joc joc) {
        this.joc = joc;
    }

    @Override
    public void show() {
        sr=new ShapeRenderer();
        x=45f; y=65;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateMic();

        sr.setColor(Color.WHITE);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.polygon(new float[]{45,65,65,45,45,25,25,45});
        sr.setColor(Color.LIGHT_GRAY);
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x, y, 5);
        sr.end();

        updateAssets(delta);
    }

    private void updateAssets(float delta) {
        if (Assets.man.update()){
            if (!joc.play.loaded){
                joc.play.load();
            }
            joc.setScreen(joc.play);
        }
    }

    private void updateMic() {
        if (x>=45 && x<65 && y<=65 && y>45) {x++; y--;}
        if (x<=65 && x>45 && y<=45 && y>25) {x--; y--;}
        if (x<=45 && x>25 && y>=25&& y<45) { x--; y++;}
        if (x>=25 && x<45 && y>=45 && y<65) { x++; y++;}
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
