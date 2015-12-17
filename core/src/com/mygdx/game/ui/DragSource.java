package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.content.objects.items.ItemContainer;

public class DragSource extends DragAndDrop.Source{
    UiContainer.ContainerSlot sursa;

    public DragSource(UiContainer.ContainerSlot actor) {
        super(actor);
        sursa=actor;
    }

    //TODO caz special eqp ( trebuie modificat continutul eqp in Erou class)

    /*  nu fac update la sursa inca; doar la final*/
    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
        if (sursa.it==null|| sursa.qt==0)
            return null;

        DragAndDrop.Payload payload= new DragAndDrop.Payload();
        payload.setObject(sursa);

        Image img= new Image(sursa.it.sprite);
        payload.setDragActor(img);
        payload.setValidDragActor(img);
        payload.setInvalidDragActor(img);
        return payload;
    }

    /* aici fac update */
    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
        UiContainer.ContainerSlot load= (UiContainer.ContainerSlot) payload.getObject();
        ItemContainer sursa= load.getContainer();

        if (target!=null) {
            UiContainer.ContainerSlot tinta= (UiContainer.ContainerSlot) target.getActor();
            ItemContainer dest= tinta.getContainer();

            if (tinta.it==null ){ //empty
                updateContainers(sursa, load.it, load.qt, dest, null, 0);
                tinta.it= load.it;
                tinta.qt= load.qt;
                tinta.update();

                load.it= null;
                load.qt=0;
                load.update();

                return;
            }

            updateContainers(sursa, load.it, load.qt,  dest, tinta.it, tinta.qt);

            if (tinta.it.id.equals(load.it.id) && tinta.it.stacks){ //same kind+  stackable
                tinta.qt+= load.qt;
                tinta.update();
                //empty load
                load.it=null;
                load.qt=0;
                load.update();
            }
            else { //swap: valabil pt tipuri diferite sau nonstackable
                Item s_it=load.it;
                int  s_qt= load.qt;

                load.it= tinta.it;
                load.qt=tinta.qt;
                load.update();

                tinta.it= s_it;
                tinta.qt= s_qt;
                tinta.update();
            }
        } else {
            /*sursa.it= load.it;  sursa.qt= load.qt;  sursa.update();*/ //nimic
        }
    }

    private void updateContainers(ItemContainer sursa, Item sursaIt, int sursaQt, ItemContainer tinta, Item tintaIt, int tintaQt){
        if (sursa==tinta) return;

        // sursa item non null always
        tinta.items.put(sursaIt,
                tinta.items.containsKey(sursaIt) ?
                        tinta.items.get(sursaIt) + sursaQt
                        : sursaQt );
        sursa.items.put(sursaIt, sursa.items.get(sursaIt)-sursaQt);
        if (sursa.items.get(sursaIt) ==0)
            sursa.items.remove(sursaIt);


        if (tintaIt !=null) {
            sursa.items.put(tintaIt,
                    sursa.items.containsKey(tintaIt) ?
                            sursa.items.get(tintaIt) + tintaQt
                            : tintaQt );
            tinta.items.put(tintaIt, tinta.items.get(tintaIt) - tintaQt);
            if (tinta.items.get(tintaIt) ==0)
                tinta.items.remove(tintaIt);
        }

    }
}

