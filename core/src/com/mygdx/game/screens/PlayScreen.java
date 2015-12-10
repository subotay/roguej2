package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Controller;
import com.mygdx.game.Joc;
import com.mygdx.game.Renderer;
import com.mygdx.game.utils.Assets;

public class PlayScreen implements Screen{
    private final Joc joc;
    private InputProcessor inhandler;
    private Controller controller;
    private Renderer renderer;
    public boolean paused, loaded;

    public PlayScreen(Joc joc) {
        this.joc = joc;
        loaded=false;
    }

    @Override
    public void show() {
        if (!loaded) {  //nu ar trebui sa intre aici
            load();
        }
        inhandler =new InputMultiplexer(renderer.ui,controller);
        Gdx.input.setInputProcessor(inhandler);
    }

    public void load(){
        controller=new Controller(joc);
        renderer=new Renderer(controller);
        Assets.man.load(Assets.S_RHIT, Sound.class);
        Assets.man.load(Assets.S_MHIT, Sound.class);
        Assets.man.load(Assets.S_LOOT, Sound.class);
        Assets.man.finishLoading();  //texturi entitati
        loaded= true;
    }

    @Override
    public void render(float delta) {
        if (!paused)
            controller.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override public void pause() {
        paused=true;
    }

    @Override public void resume() {
        paused=false;
    }

    @Override public void hide() {
        loaded=false;
        //dispose();
    }

    @Override public void dispose() {
        renderer.dispose();
        controller.dispose();
    }


}
