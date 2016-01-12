package com.mygdx.game.content.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.content.Level;
import com.mygdx.game.utils.Assets;

import java.util.EnumSet;

import static com.mygdx.game.content.Level.CellFlag.*;

public abstract class GenAction implements Action{
    public final Creatura actor;
    public int cost; //def 100

    public GenAction(Creatura actor){
        this.actor= actor;
        cost= 100;
    }
    public GenAction(Creatura actor, int cost){
        this.actor= actor;
        this.cost= cost;
    }
    public abstract boolean executa();

    @Override
    public int cost() {
        return cost;
    }


    // *******************************  generice ********************************************************
    /** walk **/
    public static class WalkAt extends GenAction {
        private float x,y;

        public WalkAt(Creatura actor, float x, float y) {
            super(actor);
            this.x=x; this.y=y;
        }

        public WalkAt(Creatura actor, int cost, float x, float y) {
            super(actor, cost);
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean executa() {
            float nx=actor.poz.x+ MathUtils.clamp(x -actor.poz.x,-1,1),
                    ny= actor.poz.y+ MathUtils.clamp(y -actor.poz.y, -1,1);
//            System.out.println("act walking: opos:"+actor.poz+" npos:"+new Vector2(nx,ny)+" fpos:"+new Vector2(x,y));        //debug

            if (nx==x && ny==y)  actor.step++;

            EnumSet<Level.CellFlag> cell = actor.level.cells[(int) nx][(int) ny];

            if (actor instanceof Badguy) {
                if (cell.contains(HERO)){
                    actor.act= new AtkMelee(actor, actor.level.erou, actor.atkcost);
                    return false;
                }
                if ( cell.contains(NPC)) {
                    actor.path= null;
                    Creatura target=  actor.level.getCreaturAt(nx,ny);
                    actor.target= target;
                    actor.act= new  AtkMelee(actor, target, actor.atkcost);
                    return false;
                }
                if (cell.contains(MONST)) {
                    actor.path=null;
                    return true; //same as walking in wall
                }

                if (cell.contains(TRAP))
                        actor.interractTrap(nx, ny);
                //muta monstru+ update cells
                actor.level.cells[(int) actor.poz.x][(int) actor.poz.y].remove(MONST);
                actor.poz.x = nx;
                actor.poz.y = ny;
                cell.add(MONST);

                return true;
            }

            if (actor instanceof Npc) {
                    if (cell.contains(MONST)) {
                        actor.path= null;
                        Creatura target=  actor.level.getCreaturAt(nx,ny);
                        actor.target= target;
                        actor.act= new  AtkMelee(actor, target, actor.atkcost);
                        return false;
                    }
                    if (cell.contains(NPC) || cell.contains(HERO)) {
                        actor.path= null;
                        return true;
                    }

                    if (cell.contains(TRAP))
                            actor.interractTrap(nx, ny);
                    //muta npc+ update cells
                    actor.level.cells[(int) actor.poz.x][(int) actor.poz.y].remove(NPC);
                    actor.poz.x = nx;
                    actor.poz.y = ny;
                    cell.add(NPC);
                }
            return true;
        }
    }

    //****************************************************************************************************
    public static class AtkMelee extends GenAction {
        Creatura target;
        public AtkMelee(Creatura actor, Creatura target, int cost) {
            super(actor, cost);
            this.target= target;
        }

        @Override
        public boolean executa() {
            Assets.man.get(Assets.S_MHIT, Sound.class).play();
            target.onHitBy(actor);
            int efdmg= MathUtils.clamp(actor.dmg()- target.stts.get(Creatura.Stat.ARMOR),0,10000);
            Gdx.app.log("dmg done", efdmg+"");
            target.hp-= efdmg;
            if (target.hp<0) {
                target.hp=0;
                target.dead= true;
            }
            //TODO attack
            return true;
        }
    }

    //***************************************************************************************************
    public static class AtkRange extends GenAction {
        Creatura target;
        public AtkRange(Creatura actor, Creatura target, int cost) {
            super(actor, cost);
            this.target= target;
        }

       @Override
       public boolean executa() {
            Assets.man.get(Assets.S_RHIT, Sound.class).play();
            target.onHitBy(actor);
            target.hp-= (MathUtils.clamp(actor.dmg()- target.stts.get(Creatura.Stat.ARMOR),0,10000));
            if (target.hp<0){
                target.hp=0;
                target.dead= true;
            }
            //TODO attack
            return true;
       }
    }

    //***************************************************************************************************
    /**  deocamdata face acelasi lucru ca si update end turn*/
    public static class Rest extends GenAction {
        public Rest(Creatura actor) {
            super(actor);
        }

        @Override
        public boolean executa() {
            if (actor.hp < actor.mhp()) {
                actor.accHp+= actor.hpreg();
                System.out.println("hp:"+actor.hp+", acchp:"+actor.accHp);
                actor.hp+= Math.floor(actor.accHp);
                actor.accHp-=Math.floor(actor.accHp);
                if (actor.hp>=actor.mhp()){
                    actor.hp=actor.mhp();
                    actor.accHp=0;
                }
                System.out.println("hp:"+actor.hp+", acchp:"+actor.accHp);

            }
            if (actor.stam < actor.mstam()) {
                actor.accStam+= actor.stareg();
                System.out.println("st:"+actor.stam+", accst:"+actor.accStam);
                actor.stam+= Math.floor(actor.accStam);
                actor.accStam-= Math.floor(actor.accStam);
                if (actor.stam>=actor.mstam()){
                    actor.stam=actor.mstam();
                    actor.accStam=0;
                }
                System.out.println("st:"+actor.stam+", accst:"+actor.accStam);
            }
            return true;
        }
    }
}
