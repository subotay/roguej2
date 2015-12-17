package com.mygdx.game.content.creatures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.objects.Door;
import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.content.objects.items.ItemContainer;
import com.mygdx.game.content.objects.items.Weapon;
import com.mygdx.game.utils.Assets;
import java.util.EnumMap;
import java.util.EnumSet;
import static com.mygdx.game.content.objects.items.Item.ItemTip.*;

import static com.mygdx.game.content.Level.CellFlag.*;

public class Erou extends Creatura {
    public int lvl, xp;
    public static final int BRAZA = 6;

    public int vraza;
    public  String levelName;

    public ItemContainer inv;
    //*****************************************************
    public enum EqpSlot{
        HEAD(HELM),
        BODY(ARMOR),
        LEGS(BOOTS),
        NECK(AMULET),
        FINGER1(RING),
        FINGER2(RING),
        RHAND(WEAPON),
        LHAND(WEAPON, TOOL, SHIELD); //+EMPTY

        public final EnumSet<Item.ItemTip> accepted;
        private Item equiped;

        private  EqpSlot(Item.ItemTip... accepted){
            this.accepted = EnumSet.noneOf(Item.ItemTip.class);
            for (Item.ItemTip tip: accepted)
                this.accepted.add(tip);
        }

        public Item getEquiped() { return equiped; }

        /** if new valid(or null) - swap old with new, returns the old equiped, or null (if old==null )
         *  if new invalid - keep old, return null*/
        public Item setEquiped(Item newItem) {
            if (newItem==null){
                Item oldItem= equiped;
                equiped= null;
                return oldItem;
            }
            if ( accepted.contains(newItem.tip)) {
                Item oldItem= equiped;
                equiped= newItem;
                return oldItem;
            }
            return null;
        }
    }

    public EnumMap<EqpSlot, Item> eqp;
    //***************************************************

    public Erou() {
        stts=new EnumMap<Stat, Integer>(Stat.class);
        eqp = new EnumMap<EqpSlot, Item>(EqpSlot.class);

        inv = new ItemContainer();
        act= new GenAction.Rest(this);
        Assets.loadSprite(Assets.EROU);
        this.sprite= Assets.getSprite(Assets.EROU);;
    }

    //TODO dual wield dmg ??
    @Override
    public int dmg() {
        Weapon wep= (Weapon) eqp.get(EqpSlot.RHAND);
        return stts.get(Stat.BONDMG)+ (wep!=null ? wep.dmg: 0);
    }

    @Override
    public void update(float delta) {
        sprite.setBounds(poz.x, poz.y, 1, 1);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }


    //TODO interactiuni
    @Override
    public void interractTrap(float x, float y) {
        super.interractTrap(x,y);
    }

    public boolean interractTrigger(float x, float y) {
        return true;
    }

    public void interractDoor(float tx, float ty) {
        Door target= level.getDoorAt(tx,ty);
        target.open();
        level.cells[(int)tx][(int)ty].remove(BLOC_V);
        level.cells[(int)tx][(int)ty].remove(BLOC_MV);
        level.cells[(int)tx][(int)ty].add(HERO);
        level.cells[(int)poz.x][(int)poz.y].remove(HERO);
        poz.set(tx,ty);
    }

    public void interractLoot(float x, float y) {}


    /**   fac swap, pt talk alta action( press e key ex.)*/
    public void interactNPC(Npc npc) {
        npc.path=null;
        level.cells[((int) npc.poz.x)][((int) npc.poz.y)].remove(NPC);
        level.cells[((int) poz.x)][((int) poz.y)].add(NPC);
        npc.poz.set(poz);
    }

    @Override
    public void atkMelee(Creatura target) {super.atkMelee(target);}

    @Override
    public void onHit(Creatura hitter) {
        super.onHit(hitter);

    }


    @Override
    public boolean invalid(int x, int y) {
        return (          !(x>=0 && x<level.worldw && y>=0 && y<level.worldh)
                || (level.cells[x][y].contains(Level.CellFlag.BLOC_MV))
                || (!dumb && level.cells[x][y].contains(Level.CellFlag.TRAP))
                || (level.cells[x][y].contains(Level.CellFlag.NPC))
        );
    }

    @Override
    public String toString() {
        return "Erou :"+hp+"/"+mhp()+" stamina:"+stam+"target"+(target==null?"none":target);
    }
}
