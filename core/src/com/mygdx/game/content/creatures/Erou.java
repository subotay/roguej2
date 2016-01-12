package com.mygdx.game.content.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.objects.Door;
import com.mygdx.game.content.objects.items.Echipabil;
import com.mygdx.game.content.objects.items.Item;
import com.mygdx.game.content.objects.items.ItemContainer;
import com.mygdx.game.content.objects.items.Weapon;
import com.mygdx.game.ui.EqpTable;
import com.mygdx.game.utils.Assets;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import static com.mygdx.game.content.objects.items.Item.ItemTip.*;

import static com.mygdx.game.content.Level.CellFlag.*;

public class Erou extends Creatura {
    public int lvl, xp;
    public static final int BRAZA = 6;

    public int vraza;
    public  String levelName;

    public Creatura lastHitter;

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

        private  EqpSlot(Item.ItemTip... accepted){
            this.accepted = EnumSet.noneOf(Item.ItemTip.class);
            Collections.addAll(this.accepted, accepted);
        }
    }
    public EnumMap<EqpSlot, Echipabil> eqp;
    public EqpTable eqpView;
    //***************************************************

    public Erou() {
        stts=new EnumMap<Stat, Integer>(Stat.class);
        eqp = new EnumMap<EqpSlot, Echipabil>(EqpSlot.class);
        inv = new ItemContainer();
        Assets.loadSprite(Assets.EROU);
        this.sprite= Assets.getSprite(Assets.EROU);;

        act= new GenAction.Rest(this);
    }


    //TODO dual wield dmg ??
    @Override
    public int dmg() {
        Weapon wep= (Weapon) eqp.get(EqpSlot.RHAND);
        return
                (wep==null|| wep.melee ? 2*STR: 2*AGI)  //stat bonus (melee/ranged) in fct de wep equiped
                +stts.get(Stat.BONDMG );                //include bonus dat de wep dmg pt equiped wep.
    }

        @Override
    public void updateAI(float delta) {
        if (target!=null && target.dead)
            target=null;
        if (lastHitter!=null  && lastHitter.dead)
            lastHitter=null;
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }


    //TODO interactiuni
    @Override
    public void interractTrap(float x, float y) {
        super.interractTrap(x, y);
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

    /**   fac swap, pt talk alta action( press e key ex.)*/
    public void swapNPC(Npc npc) {
        npc.path=null;
        level.cells[((int) npc.poz.x)][((int) npc.poz.y)].remove(NPC);
        level.cells[((int) poz.x)][((int) poz.y)].add(NPC);
        npc.poz.set(poz);
        npc.updateSprite(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void onHitBy(Creatura hitter) {
        lastHitter= hitter;
    }

    public void setTarget(Creatura cr){
        cr.sprite.setColor(Color.GOLD);
        target= cr;
    }

    public void removeTarget(){
        if (target!=null){
            target.sprite.setColor(Color.WHITE);
            target= null;
        }
    }

    /** returns null if slot was empty, the old item otherwise*/
    public Echipabil unEquip( EqpSlot eqslot) {
        Echipabil it= eqp.get(eqslot);
        if (it!=null) {
            for (Stat stat: Stat.values()){
                stts.put(stat, stts.get(stat)- it.mods.get(stat)); }
            if (hp>mhp()) hp=mhp();
            if (stam>mstam()) stam=mstam();
            eqp.put(eqslot,null);
        }
        return it;
    }

    /** verif valid prior */
    public void equip(Echipabil it, EqpSlot slot){
        if (it!=null){
            for (Stat stat: Stat.values()){
                stts.put(stat, stts.get(stat) + it.mods.get(stat)); }
        }
        eqp.put(slot, it);
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
