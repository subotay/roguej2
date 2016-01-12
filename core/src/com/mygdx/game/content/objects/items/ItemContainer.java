package com.mygdx.game.content.objects.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.objects.Obiect;
import com.mygdx.game.ui.UiContainer;


public class ItemContainer extends Obiect {
    public final ObjectMap<Item, Integer> items;
    public UiContainer view;

    /** erou inv, eqp*/
    public ItemContainer(){  this.items= new ObjectMap<Item, Integer>(); }

    /**   spawn on death, drops*/
    public ItemContainer(Level level, Item... items){
        super(level);
        this.items= new ObjectMap<Item, Integer>();
        for (Item it:items)
            this.items.put(it, 1);
    }



    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        if (sprite!=null)
            sprite.draw(batch);
    }

    @Override
    public void dispose() {
    }

    @Override
    public String toString() {
        return "\n"+items;
    }

    /** din sursa: sursaIt pleaca , tintaIt soseste, etc*/
    public static void updateContainers(ItemContainer sursa, Item sursaIt, int sursaQt, ItemContainer tinta, Item tintaIt, int tintaQt){
        if (sursa==tinta) return;

        // sursa item non null always
        if (tinta!=null){
            tinta.items.put(sursaIt,
                    tinta.items.containsKey(sursaIt) ?
                            tinta.items.get(sursaIt) + sursaQt
                            : sursaQt );

        }

        sursa.items.put(sursaIt, sursa.items.get(sursaIt)-sursaQt);
        if (sursa.items.get(sursaIt) ==0)
            sursa.items.remove(sursaIt);


        if (tintaIt !=null) {
            sursa.items.put(tintaIt,
                    sursa.items.containsKey(tintaIt) ?
                            sursa.items.get(tintaIt) + tintaQt
                            : tintaQt );

            if (tinta!=null){
                tinta.items.put(tintaIt, tinta.items.get(tintaIt) - tintaQt);
                if (tinta.items.get(tintaIt) ==0)
                    tinta.items.remove(tintaIt);

            }
        }
    }
}

