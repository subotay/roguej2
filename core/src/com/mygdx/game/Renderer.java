package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.ui.UI;
import com.mygdx.game.utils.FogWar;

import static com.mygdx.game.utils.Constants.VIEW_H;
import static com.mygdx.game.utils.Constants.VIEW_W;

public class Renderer implements Disposable{
    private final Controller contr;
    public UI ui;

    private OrthographicCamera cam,hudcam;
    private FitViewport viewcam;
    private OrthogonalTiledMapRenderer maprend;
    private int[] back={0,1},fore={3};
    private SpriteBatch batch;
    private Sprite fog;

    public Renderer(Controller contr) {
        this.contr = contr;
        init();
    }

    public void init() {
        cam=new OrthographicCamera();
        viewcam = new FitViewport( VIEW_W, VIEW_H, cam);
//        viewcam.apply();
//        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
//        cam.update();

        ui = new UI(new ScreenViewport(),contr);
        batch=new SpriteBatch();
        maprend=new OrthogonalTiledMapRenderer(contr.tiledmap, 1/32f);
        ui.rebuild();
    }

    public void render(float delta) {
        ui.act(delta);

        contr.camUtil.setCamTo(cam);
        viewcam.apply();

        maprend.setView(cam);
        /*maprend.setView(cam.combined, contr.level.erou.poz.x+.5f-Erou.BRAZA,
                                        contr.level.erou.poz.y+.5f-Erou.BRAZA, 2*Erou.BRAZA, 2*Erou.BRAZA-1);*/
        maprend.render(back);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        contr.level.render(delta, batch);
        batch.end();
        maprend.render(fore);

        fog= FogWar.getFog(contr.level);
        batch.begin();
        fog.draw(batch);
        batch.end();

        ui.getViewport().apply(true);
        ui.draw();
    }

    public void resize(int width, int height) {
        viewcam.update(width, height);
//        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        ui.getViewport().update(width, height,true);
        ui.rebuild();
    }

    @Override
    public void dispose() {
        ui.dispose();
        batch.dispose();
        maprend.dispose();
        fog.getTexture().dispose();
    }
}
