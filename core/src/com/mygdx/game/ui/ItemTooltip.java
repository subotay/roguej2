package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.utils.Assets;


public class ItemTooltip extends Tooltip<Window> {
    TooltipManager manag;
    public static ItemTooltip inst;
    private Label txt;
    private Window win;

    private ItemTooltip(Window contents) {
        super(contents, new TooltipManager());
        manag= getManager();
        manag.animations= false;
        manag.initialTime= .75f;
        manag.subsequentTime= .75f;

        txt = new Label("~~", Assets.skin);
        txt.setWrap(true);
        txt.setAlignment(Align.topLeft);
        win= new Window("", Assets.skin);
        Window.WindowStyle st= new Window.WindowStyle(Assets.skin.get("default", Window.WindowStyle.class));
        st.background= Assets.skin.getDrawable("mydef-rect");
        win.setStyle(st);
        win.top().left();
        win.add(txt). size(200,200).expand().fill();
        setActor(win);
    }

    public static ItemTooltip getInst() {
        if (inst==null) inst= new ItemTooltip(null);
        return inst;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        UiContainer.ContainerSlot slot= (UiContainer.ContainerSlot) event.getListenerActor();
        if (slot.it==null) return;
        txt.setText(slot.it.description);
        super.enter(event, x, y, pointer, fromActor);
    }

}
