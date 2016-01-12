package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.game.content.creatures.Erou;
import com.mygdx.game.content.objects.items.Echipabil;
import com.mygdx.game.utils.Assets;
import java.util.EnumMap;
import static com.mygdx.game.content.creatures.Erou.EqpSlot.*;

public class EqpTable extends Table {
    Erou erou;
    EnumMap<Erou.EqpSlot, UiEqpSlot> slots;
    DragAndDrop dnd;

    public EqpTable(Erou er, DragAndDrop dnd) {
        this.dnd= dnd;
        erou= er;
        slots= new EnumMap<Erou.EqpSlot, UiEqpSlot>(Erou.EqpSlot.class);

        for (Erou.EqpSlot slot: Erou.EqpSlot.values()) {
            final UiEqpSlot uislot= new UiEqpSlot(slot);
            slots.put(slot, uislot);
            if (dnd!=null){
                dnd.addTarget(new DragTarget(uislot));
            }
            uislot.addListener(new ClickListener(){
                @Override public void clicked(InputEvent event, float x, float y) {
                    if (getTapCount()>=2 ){
                        Echipabil removed= uislot.swapItem(null);
                        if (removed!=null){
                            erou.inv.items.put(removed, (erou.inv.items.containsKey(removed) ?
                                    erou.inv.items.get(removed)+1: 1));
                            erou.inv.view.refresh();
                        }
                    } }});
            uislot.addListener(ItemTooltip.getInst());
        }

        defaults().pad(1).size(40, 40).center().fill();
        add(slots.get(HEAD)).colspan(3);
        row();
        add(slots.get(FINGER1));
        add(slots.get(NECK));
        add(slots.get(FINGER2));
        row();
        add(slots.get(RHAND));
        add(slots.get(BODY));
        add(slots.get(LHAND));
        row();
        add(slots.get(LEGS)).colspan(3);
    }

    public void refreshView(Erou.EqpSlot eqslot){
        slots.get(eqslot).updateVisual(erou.eqp.get(eqslot));
    }

    public void refreshViewAll(){
        for (Erou.EqpSlot slot: Erou.EqpSlot.values())
            refreshView(slot);
    }

    //******************************************************************************************************************
    public  class UiEqpSlot extends Stack {
        public final Erou.EqpSlot slot;
        ImageButton.ImageButtonStyle empty;
        ImageButton backslot;
        SpriteDrawable drw, overDrw;

        public UiEqpSlot(Erou.EqpSlot slot) {
            this.slot = slot;
            empty = Assets.skin.get("equip_btn", ImageButton.ImageButtonStyle.class);
            backslot= new ImageButton(empty);
            add(backslot);
            if (erou.eqp.get(slot)!=null)
                updateVisual(erou.eqp.get(slot));
        }

        /**if invalid new item, keep old+ return null;
         * includes updateVisual(newIt) */
        public Echipabil swapItem(Echipabil newIt){
            Echipabil old=null;

            if (newIt==null || slot.accepted.contains(newIt.tip)){
                Gdx.app.log("erou dmg before", erou.dmg() + "");       //debug
                old= erou.unEquip(slot);
                erou.equip(newIt,slot);
                updateVisual(newIt);
                Gdx.app.log("erou dmg after", erou.dmg() + "");        //debug
            }

            return old;
        }

        Echipabil getItem(){  return erou.eqp.get(slot);}

        void updateVisual(Echipabil it){
            if (it == null) {
                backslot.setStyle(empty);
            } else {
                drw = new SpriteDrawable(it.sprite);
                overDrw = drw.tint(Color.LIGHT_GRAY);

                ImageButton.ImageButtonStyle full= new ImageButton.ImageButtonStyle(empty);
                full.imageDisabled= drw;
                full.imageChecked= drw;
                full.imageUp= drw;
                full.imageDown=overDrw;
                full.imageOver=overDrw;
                full.imageCheckedOver= overDrw;

                backslot.setStyle(full);
                backslot.getImageCell().fill().expand();
            }
        }

        EqpTable getEqpTable(){return EqpTable.this;}
    }
}
