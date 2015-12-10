package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Joc;
import com.mygdx.game.utils.Assets;

public class MainMenuScreen implements Screen {
    private final Joc joc;
    private Stage stage;
    private float screenw, screenh;
    private Cell topcell;

    public MainMenuScreen(Joc joc) {this.joc = joc;}

    @Override
    public void show() {
        initStage();
    }

    private void initStage() {
        screenw= Gdx.graphics.getWidth();
        screenh= Gdx.graphics.getHeight();

        stage=new Stage(new ScreenViewport());
//        stage.setDebugAll(true);        //debug
        Gdx.input.setInputProcessor(stage);

        stage.clear();
        Table table=new Table(Assets.skin);
        stage.addActor(table);
        table.setFillParent(true);

        final TextButton play=new TextButton("PLAY",Assets.skin),
                        opt=new TextButton("OPTIONS", Assets.skin),
                        exit=new TextButton("EXIT",Assets.skin);

//        opt.setStyle(skin.get("toggle",TextButton.TextButtonStyle.class));
        table.top().right();
        topcell= table.add(play).width(160).height(50).padBottom(20).padTop(screenh*.25f);
        table.row();
        table.add(opt).width(160).height(50).padBottom(20);
        table.row();
        table.add(exit).width(160).height(50).padBottom(20);

        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!joc.play.loaded){
                    joc.setScreen(joc.load);
                    joc.play.load();
                }
                joc.setScreen(joc.play);
            }
        });
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f,.1f,.1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();;
    }

    @Override
    public void resize(int width, int height) {
        screenw= width;
        screenh= height;
        stage.getViewport().update(width, height,true);
        topcell.padTop(screenh * .25f);
    }

    @Override
    public void pause() {
//        dispose();
    }

    @Override
    public void resume() {
//        initStage();
    }

    @Override
    public void hide() {
        // dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
