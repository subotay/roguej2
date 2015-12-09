package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Joc;
import com.mygdx.game.utils.Assets;

public class MainMenuScreen implements Screen {
    private final Joc joc;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(Joc joc) {this.joc = joc;}

    @Override
    public void show() {
        initStage();
    }

    private void initStage() {
        stage=new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        skin=new Skin(Gdx.files.internal("ui/uiskin.json"),
                Assets.INST.man.get("ui/uiskin.atlas", TextureAtlas.class));
        stage.clear();

        Table table=new Table(skin);
        stage.addActor(table);
        table.setFillParent(true);

        final TextButton play=new TextButton("PLAY",skin),
//                load=new TextButton("LOAD",skin),
                opt=new TextButton("OPTIONS", skin),
            exit=new TextButton("EXIT",skin);
        float w=Gdx.graphics.getWidth()/4,
            h= Gdx.graphics.getHeight()/8;
        table.top().center();
        table.row();
        table.add(play).width(w).height(h).center().padBottom(20);
//        table.row();
//        table.add(load).width(w).height(h).center().padBottom(20);
        table.row();
        table.add(opt).width(w).height(h).center().padBottom(20);
        table.row();
        table.add(exit).width(w).height(h).center().padBottom(20);

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
        stage.getViewport().update(width, height);
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
        skin.dispose();
    }
}
