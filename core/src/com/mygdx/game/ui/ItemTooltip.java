package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.content.creatures.Erou;
import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.utils.Assets;


public class ItemTooltip extends Tooltip<Window> {


    TooltipManager manag;
    public static ItemTooltip inst;
    private Label nameL, txt;
    private Window win;

    private ItemTooltip(Window contents) {
        super(contents, new TooltipManager());
        manag= getManager();
        manag.animations= false;
        manag.initialTime= .75f;
        manag.subsequentTime= .75f;

        nameL= new Label("", Assets.skin.get("brown_16", Label.LabelStyle.class));
        nameL.setWrap(true);
        nameL.setAlignment(Align.topLeft);

        txt = new Label("~~", Assets.skin.get("black_14", Label.LabelStyle.class));
        txt.setWrap(true);
        txt.setAlignment(Align.topLeft);

        win= new Window("", Assets.skin);
        Window.WindowStyle st= new Window.WindowStyle(Assets.skin.get("default", Window.WindowStyle.class));
        st.background= Assets.skin.getDrawable("mydef-rect");
        win.setStyle(st);
        win.top().left();
        win.add(nameL).size(250, 16).expand().fill();
        win.row();
        win.add(txt). size(250,250).expand().fill();
        setActor(win);
    }


    public static ItemTooltip getInst() {
        if (inst==null) inst= new ItemTooltip(null);
        return inst;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Item it=null;
        if (event.getListenerActor() instanceof UiContainer.ContainerSlot){
            UiContainer.ContainerSlot slot= (UiContainer.ContainerSlot) event.getListenerActor();
            it= slot.it;

        } else if (event.getListenerActor() instanceof EqpTable.UiEqpSlot) {
            EqpTable.UiEqpSlot slot = (EqpTable.UiEqpSlot) event.getListenerActor();
            it= slot.getItem();
        }

        if (it==null) return;
        nameL.setText(" "+it.name);
        txt.setText(it.longDesc());
        super.enter(event, x, y, pointer, fromActor);
    }

}
