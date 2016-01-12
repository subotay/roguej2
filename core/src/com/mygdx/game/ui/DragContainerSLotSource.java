package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.game.content.creatures.Erou;
import com.mygdx.game.content.objects.items.Echipabil;
import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.content.objects.items.ItemContainer;
import com.mygdx.game.content.objects.items.Weapon;

import java.util.EnumMap;

import static com.mygdx.game.content.creatures.Erou.EqpSlot.*;

public class DragContainerSLotSource extends DragAndDrop.Source{
    UiContainer.ContainerSlot sursa;

    public DragContainerSLotSource(UiContainer.ContainerSlot actor) {
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
        ItemContainer sursaContainer= load.getItemContainer();

        if (target!=null) {
            //
            //
            // ItemContainer--> ItemContainer
            if (target.getActor() instanceof UiContainer.ContainerSlot){
                UiContainer.ContainerSlot tinta= (UiContainer.ContainerSlot) target.getActor();
                ItemContainer destContainer= tinta.getItemContainer();

                if (tinta.it==null ){ //empty
                    ItemContainer.updateContainers(sursaContainer, load.it, load.qt, destContainer, null, 0);
                    tinta.it= load.it;
                    tinta.qt= load.qt;
                    tinta.update();

                    load.it= null;
                    load.qt=0;
                    load.update();

                    return;
                }

                ItemContainer.updateContainers(sursaContainer, load.it, load.qt, destContainer, tinta.it, tinta.qt);

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
            }
            //
            //
            // ItemContainer--> Eqp
            else if (target.getActor() instanceof EqpTable.UiEqpSlot && load.it instanceof Echipabil){
                EqpTable.UiEqpSlot ui= (EqpTable.UiEqpSlot) target.getActor();
                Erou  erou= ui.getEqpTable().erou;
                EnumMap<Erou.EqpSlot, EqpTable.UiEqpSlot> slots=  ui.getEqpTable().slots;

                if (ui.slot.accepted.contains(load.it.tip))
                    switch (load.it.tip){
                        case WEAPON:
                            if (!((Weapon)load.it).onehanded){
                                if (ui.slot== RHAND){
                                    ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                    load.it= ui.swapItem((Echipabil) load.it);
                                    load.qt=1;
                                    load.update();

                                    //unequip left hand
                                    Echipabil lhandIt= slots.get(LHAND).swapItem(null);
                                    if (lhandIt!=null) {
                                        sursaContainer.items.put(lhandIt,
                                                sursaContainer.items.containsKey(lhandIt) ? sursaContainer.items.get(lhandIt) + 1 : 1);

                                        load.getUiContainer().refresh();
                                    }
                                }
                            } else if (ui.slot== RHAND ||
                                    ui.slot== LHAND && erou.eqp.get(RHAND)!=null && ((Weapon)erou.eqp.get(RHAND)).onehanded )
                            {
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }
                            break;
                        case HELM:
                            if (ui.slot==HEAD){
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }
                            break;
                        case ARMOR:
                            if (ui.slot== BODY){
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }break;
                        case BOOTS:
                            if (ui.slot==LEGS){
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }break;
                        case SHIELD:
                            if (ui.slot== LHAND && (erou.eqp.get(RHAND)==null || (erou.eqp.get(RHAND)!=null && ((Weapon)erou.eqp.get(RHAND)).onehanded))){
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }break;
                        case AMULET:
                            if (ui.slot==NECK){
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }break;
                        case RING:
                            if (ui.slot==FINGER1 || ui.slot==FINGER2){
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }
                            break;
                        case TOOL:
                            if (ui.slot==LHAND && (erou.eqp.get(RHAND)==null || (erou.eqp.get(RHAND)!=null && ((Weapon)erou.eqp.get(RHAND)).onehanded))){
                                ItemContainer.updateContainers(sursaContainer, load.it, 1, null, erou.eqp.get(ui.slot), 1);
                                load.it= ui.swapItem((Echipabil) load.it);
                                load.qt=1;
                                load.update();
                            }break;
                    }
            }
        }
        else {
            /*sursa.it= load.it;  sursa.qt= load.qt;  sursa.update();*/ //nimic
        }
    }


}

