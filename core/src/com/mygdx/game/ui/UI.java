package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controller;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.extdata.GameData;

public class UI  extends Stage {
    private Controller contr;
    DragAndDrop dnd;
    public float scrw, scrh;

    /*** components ******************************/
        //stage
    private Table hud, targetDat, actionbar ;
        //loot+inventory
    private UiContainer inventory,  loot;
    private Window invW, lootW;
        //char window
    private Window charW;
    private Table left, cent, right, bot;

    public UI(Viewport viewport, Controller contr) {
        super(viewport);
        this.contr= contr;
        dnd= new DragAndDrop();
    }

    /** apelat de fiecare data cand playscreen devine activ
     * - la resize, layout se revalideaza daca reapelez  */
    public void rebuild() {
        scrw= Gdx.graphics.getWidth();
        scrh= Gdx.graphics.getHeight();
        clear();
//        setDebugAll(true);          //debug

        hud = new Table(Assets.skin);
        buildHud();
        addActor(hud);
        buildInvWin();
        addActor(invW);
        buildLootWin();
        addActor(lootW);
        buildCharWin();
        addActor(charW);
    }

    private void buildInvWin() {
        int h=(int)(scrh*.85f/42-1), w= (int)(scrw*.32f/42);
        inventory = new UiContainer(contr.level.erou.inv,h,w,dnd);  /// 40 cellsize not hardcode TODO

        ScrollPane scrP= new ScrollPane(inventory, Assets.skin);
        inventory.setFillParent(false);   //  !!!! don't fill the parent, scrollpane
        scrP.setFadeScrollBars(false);   // bug causes other skin parts to fade

        invW=new Window("Inventory", Assets.skin);
        invW.setMovable(false);
        invW.setVisible(false);

        TextButton close=new TextButton(" X ", Assets.skin);
        close.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                invW.setVisible(false);
            }
        });
        invW.getTitleTable().right().add(close).height(invW.getPadTop());
        invW.setPosition(scrw - w * 42 - 5, scrh * .15f);
        invW.add(scrP).size(w * 42 + 26, scrh * .4f);
        scrP.setFillParent(false); //!!!! scrollpane not filling window parent also
        invW.setUserObject(scrP);
        invW.pack();
    }

    private void buildCharWin() {
        charW=new Window("", Assets.skin);
//        charW.setColor(Color.BROWN);
        charW.setFillParent(true);
        charW.setModal(true);
        charW.setMovable(false);
        charW.setVisible(false);

        TextButton close=new TextButton(" X ", Assets.skin);
        close.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                charW.setVisible(false);
            }
        });
        charW.getTitleTable().add(close).right().height(charW.getPadTop());

        Table root= new Table();
        root.setDebug(true, true); //debug

        left= new Table(); cent= new Table(); right= new Table(); bot= new Table();

        charW.left().top();
        charW.add(root).width(scrw*.96f).height(scrh*.96f).expand().fill();

        root.defaults().width(scrw*.32f).fill();//.uniform();
        root.add(left).uniform();
        root.add(cent).uniform();
        root.add(right).uniform();
        root.row();
        root.add(bot).height(60).colspan(3);

        //inventory
        right.top().left();
        ScrollPane pane = new ScrollPane(inventory, Assets.skin);
        inventory.setFillParent(false);   //  !!!! don't fill the parent, scrollpane
        pane.setFadeScrollBars(false);   // bug causes other skin parts to fade
        right.add(pane).expand().fill();
        right.setUserObject(pane);

        //equip+stats part
        cent.top().left();
        Table eqt=new Table();
        eqt.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        //TODO
        //equipment containr slot cu empty=Assets.skin.get("equip_btn", ImageButton.ImageButtonStyle.class);
        cent.add(eqt).expandX().height(scrh * .45f).fill();
        cent.row();
        Table statt=new Table();
        statt.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        statt.add(new Label("Stats here", Assets.skin));
        cent.add(statt).expand().fill();

        //skills etc part
        left.top().left();
        left.add(new Label("Skills here", Assets.skin));

        //bot part
        bot.left();
        bot.add(new Label("Actionbars here", Assets.skin));

        charW.pack();
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
        targetDat =new Table();
        ResBar tbar=new ResBar(Assets.skin.getDrawable("borderbar"), Assets.skin.getDrawable("redbar"));
        targetDat.add(tbar).pad(3).size(scrw * .25f, 15);
        hud.add(targetDat).expandX().top();

            //right buttons
        Table rbut= new Table();
        TextButton quit= new TextButton("Quit", Assets.skin),  inve= new TextButton("Inv", Assets.skin),
                    ch= new TextButton("Char", Assets.skin);
        rbut.defaults().width(60).height(30);
        rbut.add(quit);  rbut.row(); rbut.add(inve);  rbut.row();  rbut.add(ch);
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
//                charW.setVisible(true);
                showChar();
            }
        });
        inve.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (invW.isVisible()) invW.setVisible(false);
                else showInv();
            }
        });
        hud.add(rbut).top().right();

    }


    private void buildLootWin() {
        loot= new UiContainer(null, 3, 6, dnd);
        ScrollPane scrP=new ScrollPane(loot, Assets.skin);
        loot.setFillParent(false);
        scrP.setFadeScrollBars(false);

        lootW = new Window("", Assets.skin);
        TextButton close=new TextButton(" X ", Assets.skin);
        close.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lootW.setVisible(false);
            }
        });
        TextButton lootall=new TextButton("Take all", Assets.skin);
        lootall.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Item it : loot.itCont.items.keys()) {
                    contr.level.erou.inv.items.put(it,
                            contr.level.erou.inv.items.containsKey(it) ?
                                    Integer.valueOf(loot.itCont.items.get(it) + contr.level.erou.inv.items.get(it))
                                    : loot.itCont.items.get(it));
                    loot.itCont.items.remove(it);
                }
                loot.refresh();
                inventory.refresh();
            }
        });
       /* lootW.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UiContainer.ContainerSlot slot = (UiContainer.ContainerSlot) actor.getParent();
                if (slot.it==null) return;
                contr.level.erou.inv.items.put(slot.it,
                        contr.level.erou.inv.items.containsKey(slot.it) ?
                                Integer.valueOf(loot.itCont.items.get(slot.it) + contr.level.erou.inv.items.get(slot.it))
                                : loot.itCont.items.get(slot.it));
                loot.itCont.items.remove(slot.it);
                loot.refresh();
                inventory2.refresh();
                inventory.refresh();
            }
        });*/

        lootW.setVisible(false);
        lootW.setMovable(false);
        lootW.getTitleTable().right().add(close).height(lootW.getPadTop());
        lootW.setPosition(10, scrh * .33f);
        lootW.add(scrP).size(278, 124);
        scrP.setFillParent(false); //!!!! scrollpane not filling window parent also
        lootW.row().height(25);
        lootW.add(lootall).expandX().center();
        lootW.pack();
    }

    private void showInv(){
        ((ScrollPane)invW.getUserObject()).setWidget(inventory);
        inventory.setFillParent(false);
        invW.setVisible(true);
    }

    private  void showChar(){
        if (invW.isVisible())  invW.setVisible(false);
        ((ScrollPane)right.getUserObject()).setWidget(inventory);
        inventory.setFillParent(false);
        charW.setVisible(true);
    }


    @Override
    public void act(float delta) {
        ResBar tbar = (ResBar) targetDat.getChildren().get(0);
        if (contr.level.erou.target!=null)
            tbar.cre= contr.level.erou.target;
        else
            tbar.cre=null;
        super.act(delta);
    }

    /**  a resource bar */
    public static class ResBar extends Actor{
        protected Drawable back, front;
        public Creatura cre;

        protected float val;

        public ResBar(Drawable back, Drawable front) {
            this.back = back;
            this.front = front;
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
            } else {
                setVisible(false);
            };
        }

        @Override public void draw(Batch batch, float parentAlpha) {
            /* * Usually in an actor's draw method you would first set the color to the actor's color, with the
            * alpha multiplied by the parent alpha.  --> avoid fade out bug*/
            batch.setColor(1,1,1,1);
            back.draw(batch,  getX(), getY(), getWidth(), getHeight() );
            front.draw(batch, getX()+2, getY()+2,(getWidth()-4)*val, getHeight()-4);
        }
        @Override
        public String toString() { return "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!bar+ target: "+cre; }
    }


    /*****    INPUT *****************************************************************************************/
    @Override
    public boolean keyDown(int keyCode) {
        if (!charW.isVisible()) {
          switch (keyCode) {
                case Input.Keys.E:
                    if ((contr.level.cells[(int) contr.level.erou.poz.x][((int) contr.level.erou.poz.y)].contains(Level.CellFlag.LOOT))) {
                        loot.setToItemContainer(contr.level.getLootAt(contr.level.erou.poz.x, contr.level.erou.poz.y));
                        lootW.setVisible(true);
                        showInv();;
                    }
                    break;
                case Input.Keys.I:
                    if (invW.isVisible()) invW.setVisible(false);
                    else  showInv();
                    break;
              case Input.Keys.C:
                  lootW.setVisible(false);
                  showChar();
                  break;
                default:
                    lootW.setVisible(false);
                    invW.setVisible(false);
                    return false;
          }
        } else{  //charw open
            switch (keyCode) {
                case Input.Keys.C:
                    charW.setVisible(false);
                    break;
            }
        }
        return true;
    }
}