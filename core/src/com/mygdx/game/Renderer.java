package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.FogWar;
import com.mygdx.game.utils.extdata.GameData;

import java.util.HashMap;

import static com.mygdx.game.utils.Constants.VIEW_H;
import static com.mygdx.game.utils.Constants.VIEW_W;

public class Renderer implements Disposable{
    private final Controller controller;
    public Stage hud;
    private Skin skin;

    private OrthographicCamera cam,hudcam;
    private FitViewport camview;
    private OrthogonalTiledMapRenderer maprend;
    private int[] back={0,1},fore={3};
    private SpriteBatch batch;

    public Renderer(Controller controller) {
        this.controller = controller;
        init();
    }

    public void init() {
        cam=new OrthographicCamera(VIEW_W, VIEW_H);
        camview= new FitViewport(VIEW_W,VIEW_H,cam);
        hud= new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch=new SpriteBatch();
        maprend=new OrthogonalTiledMapRenderer(controller.tiledmap, 1/32f);
        rebuildHud();
    }

    /** apelat de fiecare data cand playscreen devine activ*/
    public void rebuildHud() {
        hud.clear();
        skin= new Skin(Gdx.files.internal("ui/uiskin.json"),
                Assets.INST.man.get("ui/uiskin.atlas", TextureAtlas.class));;
        Table table= new Table(skin);
        hud.addActor(table);
        table.setFillParent(true);

        TextButton quit= new TextButton("Quit", skin);
        table.top().right();
        table.add(quit).width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getHeight() / 10);

        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameData.saveLvl(controller.level);
                GameData.saveErou(controller.level.erou);
                controller.joc.setScreen(controller.joc.menu);
            }
        });

        //TODO hud...
    }

    public void render(float delta) {
        hud.act(delta);

        controller.camUtil.setCamTo(cam);

        maprend.setView(cam);
        /*maprend.setView(cam.combined, controller.level.erou.poz.x+.5f-Erou.BRAZA, controller.level.erou.poz.y+.5f-Erou.BRAZA, 2*Erou.BRAZA, 2*Erou.BRAZA-1);*/
        maprend.render(back);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        controller.level.render(delta, batch);
        batch.end();
        maprend.render(fore);

        Sprite fog= FogWar.getFog(controller.level);
        batch.begin();
        fog.draw(batch);
        batch.end();

        hud.draw();
    }

    public void resize(int width, int height) {
        camview.update(width, height);
        hud.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        hud.dispose();
        skin.dispose();
        batch.dispose();
        maprend.dispose();
    }
}

/* HashMap<String, String> data=new HashMap<String, String>();
                data.put("nivel", controller.level.numeNivel);
                data.put("erou_x", controller.level.erou.poz.x+"");
                data.put("erou_y", controller.level.erou.poz.y+"");
                GameData.INST.storeData(data);*/