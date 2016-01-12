package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.content.objects.items.ItemContainer;
import com.mygdx.game.utils.Assets;

public class UiContainer extends Table {
    ItemContainer itCont;
    int rows;
    int cols;
    DragAndDrop dnd;
    Array<ContainerSlot> slots; //slots in this Window


    public UiContainer(ItemContainer itContainer, int rws, int cls, DragAndDrop dnd)
    {
        this.rows =rws;
        this.cols= cls;
        this.dnd = dnd;

        top().left();
        defaults().pad(1).size(40, 40).center().fill();
        row();

        //populate empty slots
        slots=  new Array<ContainerSlot>();
        slots.ordered= true;
        for (int i = 0; i < rows; i++)  addRow();

        setToItemContainer(itContainer);
    }

    void setToItemContainer(ItemContainer itContainer){
        if (itCont== itContainer) return;
        itCont= itContainer;

        refresh();
    }

    void refresh(){
        //clear prev content if needed (only needed for loot)
        for (ContainerSlot slot: slots){
            slot.it= null;
            slot.qt=0;
            slot.update();
        }
        //add items from new ItemContainer
        int i=0;
        for(ObjectMap.Entry<Item, Integer> ent: itCont.items.entries()){
            ContainerSlot slot;

            if (!ent.key.stacks){
                int q=ent.value;
                do{
                    if (i >= rows*cols) {
                        addRow();
                        rows++ ;
                    }
                    slot= slots.get(i++);
                    slot.it= ent.key;
                    slot.qt= 1;
                    slot.update();
                } while (--q>0);
            } else {
                if (i >= rows*cols) {
                    addRow();
                    rows++ ;
                }
                slot= slots.get(i++);
                slot.it= ent.key;
                slot.qt=ent.value;
                slot.update();

            }
        }
    }



    private void addRow(){
        for (int i = 0; i < cols; i++) {
            final ContainerSlot slot= new ContainerSlot();
            add(slot);
            slots.add(slot);
            if (dnd!=null){
                dnd.addSource(new DragContainerSLotSource(slot));
                dnd.addTarget(new DragTarget(slot));
            }
            slot.addListener(ItemTooltip.getInst());
        }
        row();
    }



    /**Stack:
     *   ImageButton: default(empty style)+ optional Image(item sprite)
     *    +
     *   Label (in Container cell) :  quantity (optional)
     * */
    public class ContainerSlot extends Stack {
        ImageButton backslot;
        ImageButton.ImageButtonStyle empty;
        Item it;
        int qt;
        SpriteDrawable drw, overDrw;
        Container qtDisplay;

        public ContainerSlot(){
            backslot= new ImageButton(Assets.skin);
            empty = Assets.skin.get("default", ImageButton.ImageButtonStyle.class);
            qtDisplay= new Container();

            add(backslot);
            add(qtDisplay);
        }

        public ContainerSlot(Item it, int qt) {
            this();
            this.it= it;
            this.qt= qt;
            update();
        }

        public ItemContainer getItemContainer(){  return UiContainer.this.itCont; }
        public UiContainer getUiContainer(){ return UiContainer.this;}

        /** apelat cand se modifica continutul*/
        public void update() {

            if (it == null || qt==0) { //clear if needed
                backslot.setStyle(empty);
                it=null;
                qt=0;
                qtDisplay.clear();
            }
            else {  //item not null
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

                if (qt>1){  // add qt
                    qtDisplay.setActor(new Label(""+qt, Assets.skin,"white_14"));
                    qtDisplay.top().right();
                } else qtDisplay.clear();
            }
        }
    }
}

/* void setToSlots(Array<ContainerSlot> slots){
        clear();
        this.slots= new Array<ContainerSlot>(slots);
        this.itCont= slots.get(0).getItemContainer();
        int col=0;
        for (ContainerSlot slot: slots){
            if (col++>= cols) {row(); col=1;}
            add(slot);
        }
    }*/