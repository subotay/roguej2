package com.mygdx.game.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controller;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.extdata.GameData;

public class UI  extends Stage {
    private Controller contr;
    public float scrw, scrh;
    private Stack stack;
        //
    private Table hud, tdat;
        //
    private Window opt,chw;

    public UI(Viewport viewport, Controller contr) {
        super(viewport);
        this.contr= contr;
    }

    /** apelat de fiecare data cand playscreen devine activ*/
    public void rebuild() {
        scrw= Gdx.graphics.getWidth();
        scrh= Gdx.graphics.getHeight();
        clear();
//        setDebugAll(true);          //debug

        stack= new Stack();
        addActor(stack);
        stack.setFillParent(true);
        hud = new Table(Assets.skin);
        buildHud();
        stack.addActor(hud);
        chw=new Window("",Assets.skin);
        buildCharWind();
        stack.addActor(chw);
    }

    private void buildCharWind() {
        chw.setFillParent(true);
        chw.setResizable(false);
        chw.setModal(true);
        chw.setMovable(false);
        chw.setVisible(false);

        TextButton close=new TextButton("X", Assets.skin);
        close.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chw.setVisible(false);
            }
        });
        chw.getTitleTable().top().right().add(close).size(20,20).pad(2);
    }

    private void buildHud() {
        hud.setFillParent(true);
        hud.left().top();
        hud.row().expand();

            //data erou (bars+ buffs)
        Table erodat= new Table();
        ResBar hpbar= new ResBar(Assets.skin.getDrawable("borderbar"), Assets.skin.getDrawable("redbar"),contr.level.erou);
        erodat.add(hpbar).size(scrw / 5, 15).top().left().pad(3);;
        erodat.row();
        erodat.add(new ResBar(Assets.skin.getDrawable("borderbar"), Assets.skin.getDrawable("greenbar"), contr.level.erou) {
            { val = (float) cre.stam / cre.mstam();}
            @Override public void act(float delta) {
                val = (float) cre.stam / cre.mstam();
            }
        }).size(scrw/5, 15 ).top().left().pad(3);
        hud.add(erodat).top().left();

            //target data(bar+buffs)
        tdat=new Table();
        ResBar tbar=new ResBar(Assets.skin.getDrawable("borderbar"), Assets.skin.getDrawable("redbar"));
        tdat.add(tbar).pad(3).size(scrw * .35f, 15);
        hud.add(tdat).expandX().top();

            //right buttons
        Table rbut= new Table();
        TextButton quit= new TextButton("Quit", Assets.skin),
                    ch= new TextButton("Char", Assets.skin);
        rbut.add(quit).width(60).height(30);
        rbut.row();
        rbut.add(ch).width(60).height(30);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameData.saveLvl(contr.level);
                GameData.saveErou(contr.level.erou);
                contr.joc.setScreen(contr.joc.menu);
            }
        });
        ch.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               chw.setVisible(true);
            }
        });
        hud.add(rbut).top().right();

    }

    @Override
    public void act() {
        ResBar thb= (ResBar) tdat.getChildren().get(0);
        if (contr.level.erou.target!=null){
            thb.cre= contr.level.erou.target;
        } else{
            thb.cre=null;
        }
        super.act();
    }

    /**  a resource bar*/
    public static class ResBar extends Actor{
        protected Drawable back, front;
        public Creatura cre;
        protected float val;

        public ResBar(Drawable back, Drawable front) {
            this.back = back;
            this.front = front;
            setVisible(false);
        }

        public ResBar(Drawable back, Drawable front, Creatura cre) {
            this.back = back;
            this.front = front;
            this.cre= cre;
            val=(float)cre.hp/cre.mhp();
        }

        @Override public void act(float delta) {
            if (cre!=null){
                val=(float)cre.hp/cre.mhp();
                setVisible(true);
            } else setVisible(false);
        }

        @Override public void draw(Batch batch, float parentAlpha) {
            back.draw(batch,  getX(), getY(), getWidth(), getHeight() );
            front.draw(batch, getX()+2, getY()+2,(getWidth()-4)*val, getHeight()-4);
        }
    }

    @Override
    public boolean keyDown(int keyCode) { return chw.isVisible();}

}
