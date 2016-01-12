package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controller;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Erou;
import com.mygdx.game.content.objects.items.*;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.extdata.GameData;

import static com.mygdx.game.content.creatures.Erou.EqpSlot.LHAND;
import static com.mygdx.game.content.creatures.Erou.EqpSlot.RHAND;

public class UI  extends Stage {
    private Controller contr;
    DragAndDrop dnd;
    public float scrw, scrh;

    /*** components ******************************/
        //stage
    private Table hud, eroDat, targetDat, rbut,
                       actionbar ;
    Label ehp, est, thp;
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
        //TODO action bar
    }

    private void buildInvWin() {
        int h=(int)(scrh*.85f/42-1), w= (int)(scrw*.32f/42);

//        inventory = new UiContainer(contr.level.erou.inv,h,w,dnd);  /// 40 cellsize not hardcode TODO
        inventory = new UiContainer(contr.level.erou.inv,9,6,dnd);  /// 40 cellsize not hardcode TODO

        contr.level.erou.inv.view= inventory;
        //2click listener special for inventory
        for (final UiContainer.ContainerSlot slot: inventory.slots){
            slot.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if ( slot.it!=null && getTapCount() >= 2)
                        if (slot.it instanceof Echipabil){
                            switch (slot.it.tip){
                                case WEAPON:
                                    ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                            contr.level.erou.eqp.get(Erou.EqpSlot.RHAND),1);
                                    slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.RHAND)
                                            .swapItem((Echipabil) slot.it);
                                    slot.qt=1;
                                    slot.update();

                                    //unequip left hand item for 2handed equipped
                                    if (!((Weapon)contr.level.erou.eqp.get(Erou.EqpSlot.RHAND)).onehanded) {
                                       Echipabil it=contr.level.erou.eqpView.slots.get(LHAND).swapItem(null);
                                        if (it != null) {
                                            contr.level.erou.inv.items.put(it, contr.level.erou.inv.items.containsKey(it) ?
                                                    contr.level.erou.inv.items.get(it)+1: 1);
                                            contr.level.erou.inv.view.refresh();
                                        }
                                    }
                                    break;
                                case HELM:
                                    ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                            contr.level.erou.eqp.get(Erou.EqpSlot.HEAD),1);
                                    slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.HEAD)
                                            .swapItem((Echipabil) slot.it);
                                    slot.qt=1;
                                    slot.update();
                                    break;
                                case ARMOR:
                                    ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                            contr.level.erou.eqp.get(Erou.EqpSlot.BODY),1);
                                    slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.BODY)
                                            .swapItem((Echipabil) slot.it);
                                    slot.qt=1;
                                    slot.update();break;
                                case BOOTS:
                                    ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                            contr.level.erou.eqp.get(Erou.EqpSlot.LEGS),1);
                                    slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.LEGS)
                                            .swapItem((Echipabil) slot.it);
                                    slot.qt=1;
                                    slot.update();break;
                                case SHIELD:
                                    if (contr.level.erou.eqp.get(RHAND)==null ||
                                            (contr.level.erou.eqp.get(RHAND)!=null && ((Weapon)contr.level.erou.eqp.get(RHAND)).onehanded)){
                                    ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                            contr.level.erou.eqp.get(Erou.EqpSlot.LHAND),1);
                                    slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.LHAND)
                                            .swapItem((Echipabil) slot.it);
                                    slot.qt=1;
                                    slot.update();
                                }
                                   break;
                                case AMULET:
                                    ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                            contr.level.erou.eqp.get(Erou.EqpSlot.NECK),1);
                                    slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.NECK)
                                            .swapItem((Echipabil) slot.it);
                                    slot.qt=1;
                                    slot.update();break;
                                case RING:
                                    ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                            contr.level.erou.eqp.get(Erou.EqpSlot.FINGER1),1);
                                    slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.FINGER1)
                                            .swapItem((Echipabil) slot.it);
                                    slot.qt=1;
                                    slot.update();break;
                                case TOOL:
                                    if (contr.level.erou.eqp.get(RHAND)==null ||
                                            (contr.level.erou.eqp.get(RHAND)!=null && ((Weapon)contr.level.erou.eqp.get(RHAND)).onehanded)){
                                        ItemContainer.updateContainers(contr.level.erou.inv, slot.it, 1, null,
                                                contr.level.erou.eqp.get(Erou.EqpSlot.LHAND),1);
                                        slot.it= contr.level.erou.eqpView.slots. get(Erou.EqpSlot.LHAND)
                                                .swapItem((Echipabil) slot.it);
                                        slot.qt=1;
                                        slot.update();
                                    }
                                    break;
                            }
                        } else if (slot.it instanceof Consumabil){
                            //TODO consume
                        }
                } });
        }

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

//        invW.setPosition(scrw - w * 42 - 5, scrh * .15f);
        invW.setPosition(scrw - 6 * 42 - 5, scrh * .15f);
//        invW.add(scrP).size(w * 42 + 26, scrh * .4f);
        invW.add(scrP).size(6 * 42 + 26, scrh * .4f);

        scrP.setFillParent(false); //!!!! scrollpane not filling window parent also
        invW.setUserObject(scrP);
        invW.pack();
    }

    private void buildCharWin() {
        charW=new Window("", Assets.skin);
//        charW.setColor(Color.BLACK);

        charW.setFillParent(false);

        Vector2 pos= screenToStageCoordinates(new Vector2(100,50)),
                siz= screenToStageCoordinates(new Vector2(10,10));

        charW.setSize(800, 480);
        charW.setPosition((scrw-800)/2, (scrh-480)/2);

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
        root.setBackground(Assets.skin.getDrawable("mydef-scroll"));
//        root.setDebug(true, true); //debug
        charW.add(root).fill().expand();

        left= new Table();
        left.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        cent= new Table();
        right= new Table();
        bot= new Table();
        bot.setBackground(Assets.skin.getDrawable("mydef-scroll"));

        root.defaults().fill().pad(2);
        root.add(left).uniform().expand();
        root.add(cent).uniform().expand();
        root.add(right);
        root.row();
        root.add(bot).height(60).colspan(3).expandX();

        //
        // inventory
//        right.top().left();
        ScrollPane pane = new ScrollPane(inventory, Assets.skin);
        inventory.setFillParent(false);   //  !!!! don't fill the parent, scrollpane
//        pane.setFadeScrollBars(false);   // bug causes other skin parts to fade
        right.add(pane).width(6 * 42 +4).expand().fill();
        right.setUserObject(pane);

        //
        // equip
        cent.top().left();
        EqpTable eqt= new EqpTable(contr.level.erou, dnd);
        contr.level.erou.eqpView= eqt;
        eqt.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        cent.add(eqt).expand().fill();

        //
        // stats
        cent.row();
        Table statt=new Table();
        statt.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        Label erouStats= buildStatsPanel();
        statt.top().left();
        statt.add(erouStats);
        cent.add(statt).expand().fill();

        //
        // skills etc part
        left.top().left();
        left.add(new Label("Skills here", Assets.skin)).fill().expand();
        //
        // bot part
        bot.left().top();
        bot.add(new Label("Actionbars here", Assets.skin)).fill().expand();

//        charW.pack(); //don't! or do?
    }

    private Label buildStatsPanel() {
        Label res= new Label("", Assets.skin.get("black_14", Label.LabelStyle.class));
        res.setWrap(true);
        res.setAlignment(Align.topLeft);
        StringBuilder b=new StringBuilder("\n\n");
        b.append("  Strength:       ").append(contr.level.erou.STR).append("\n");
        b.append("  Agility:            ").append(contr.level.erou.AGI).append("\n");
        b.append("  Vitality:          ").append(contr.level.erou.VIT).append("\n");
        b.append("  Endurance:  ").append(contr.level.erou.END).append("\n");
        b.append("  Spirit:             ").append(contr.level.erou.SPI).append("\n");
        res.setText(b.toString());
        return res;
    }

    private void buildHud() {
        hud.setFillParent(true);
//        hud.setDebug(true);     //debug
        hud.left().top();
            //
            //
            // data erou (bars+ buffs)
        eroDat = new Table();

        hud.add(eroDat).align(Align.topLeft).width(scrw * .3f).expandY();
        eroDat.top().left();
        ehp= new Label("", Assets.skin.get("white_14", Label.LabelStyle.class));
        est= new Label("", Assets.skin.get("white_14", Label.LabelStyle.class));
        thp= new Label("", Assets.skin.get("white_14", Label.LabelStyle.class));
        ResBar hpbar= new ResBar(Assets.skin.getDrawable("borderbar"),
                Assets.skin.getDrawable("redbar"),contr.level.erou);
        Stack ehpbar= new Stack();
        ehpbar.add(hpbar);
        ehpbar.add(ehp);
        eroDat.add(ehpbar).size(scrw / 5, 22).align(Align.topLeft).pad(3);;
        eroDat.row();
        Stack estbar= new Stack();
        estbar.add(new ResBar(Assets.skin.getDrawable("borderbar"),
                Assets.skin.getDrawable("greenbar"), contr.level.erou) {
            {
                val = (float) cre.stam / cre.mstam();
            }
            @Override
            public void act(float delta) {
                val = (float) cre.stam / cre.mstam();
            }
        });
        estbar.add(est);
        eroDat.add(estbar).size(scrw / 5, 22).align(Align.topLeft).pad(3);
            //
            //
            // target data(bar+buffs)
        targetDat =new Table();

        hud.add(targetDat).top().width(scrw* .6f).expandY();
        targetDat.top();
        targetDat.add(new Label("", Assets.skin.get("gold_14", Label.LabelStyle.class)))
                .pad(3).size(scrw / 5, 22);
        targetDat.row();
        Stack thpbar= new Stack();
        thpbar.add(new ResBar(Assets.skin.getDrawable("borderbar"), Assets.skin.getDrawable("redbar")));
        thpbar.add(thp);
        targetDat.add(thpbar).pad(3).size(scrw / 5, 22); //.center();
         //
         //
         //    right buttons
        Table rbut= new Table();

        hud.add(rbut).top().right().width(scrw* .1f);
        rbut.defaults().height(30).width(60).right();
        TextButton quit= new TextButton("Quit", Assets.skin),  inve= new TextButton("Inv", Assets.skin),
                    ch= new TextButton("Char", Assets.skin);
        rbut.top().right();
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
        //
        //
        // actionbar TODO
    }


    private void buildLootWin() {
        loot= new UiContainer(null, 3, 6, dnd);
        ScrollPane scrP=new ScrollPane(loot, Assets.skin);
        loot.setFillParent(false);
        scrP.setFadeScrollBars(false);
        for (final UiContainer.ContainerSlot slot: loot.slots){
            slot.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (slot.it!=null &&  getTapCount()>=2){
                        ItemContainer.updateContainers(loot.itCont, slot.it, slot.qt,
                                inventory.itCont, null, 0);
                        loot.refresh();
                        inventory.refresh();
                    }
                }
            });
        }

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
        ehp.setText("  "+contr.level.erou.hp+" / "+ contr.level.erou.mhp());
        est.setText("  "+contr.level.erou.stam+" / "+ contr.level.erou.mstam());

        Label nam= (Label) targetDat.getChildren().get(0);
        ResBar tbar = (ResBar) ((Stack)targetDat.getChildren().get(1)).getChildren().get(0);
        if (contr.level.erou.target!=null){
            tbar.cre= contr.level.erou.target;
            nam.setText(contr.level.erou.target.name);
            thp.setText("  "+tbar.cre.hp+" / "+ tbar.cre.mhp());
        }
        else{
            tbar.cre=null;
            nam.setText("");
            thp.setText("");
        }
        super.act(delta);
    }

    /**  a resource bar ****************************************************************************/
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
            batch.setColor(1, 1, 1, 1);
            back.draw(batch, getX(), getY(), getWidth(), getHeight());
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


    /*private void buildCharWin() {
        charW=new Window("", Assets.skin);
//        charW.setColor(Color.BLACK);
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
        root.setBackground(Assets.skin.getDrawable("mydef-scroll"));
//        root.setDebug(true, true); //debug

        left= new Table();
        left.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        cent= new Table();
        right= new Table();
        bot= new Table();
        bot.setBackground(Assets.skin.getDrawable("mydef-scroll"));

        charW.left().top();
        charW.add(root).width(scrw * .98f).height(scrh * .96f).expand().fill();
//        charW.add(root).width(800).height(480).expand().fill();

        root.defaults().width(scrw*.32f).expand().fill().pad(2);
        root.add(left);//.expandX().fill();;
        root.add(cent);//.width(256);
        root.add(right);//.width(256);
        root.row();
        root.add(bot).height(60).colspan(3).width(scrw*.9f);

        //
        // inventory
        right.top().left();
        ScrollPane pane = new ScrollPane(inventory, Assets.skin);
        inventory.setFillParent(false);   //  !!!! don't fill the parent, scrollpane
        pane.setFadeScrollBars(false);   // bug causes other skin parts to fade
        right.add(pane).expand().fill();
        right.setUserObject(pane);

        //
        // equip
        cent.top().left();
        EqpTable eqt= new EqpTable(contr.level.erou, dnd);
        contr.level.erou.eqpView= eqt;
        eqt.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        cent.add(eqt).expandX().height(scrh * .4f).fill();

        //
        // stats
        cent.row();
        Table statt=new Table();
        statt.setBackground(Assets.skin.getDrawable("mydef-scroll"));
        Label erouStats= buildStatsPanel();
        ScrollPane pane1=new ScrollPane(erouStats);
        cent.add(statt).expand().fill();

        //
        // skills etc part
        left.top().left();
        left.add(new Label("Skills here", Assets.skin));

        //
        // bot part
        bot.left().top();
        bot.add(new Label("Actionbars here", Assets.skin));

        charW.pack();
    }*/