package com.mygdx.game.content.creatures;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.content.Level;

import java.util.EnumSet;

import static com.mygdx.game.content.Level.CellFlag.*;

public abstract class GenAction implements Action{
    public final Creatura actor;
    public final int cost; //def 100

    public GenAction(Creatura actor){
        this.actor= actor;
        cost= 100;
    }
    public GenAction(Creatura actor, int cost){
        this.actor= actor;
        this.cost= cost;
    }
    public abstract void executa();

    @Override
    public int cost() {
        return cost;
    }


    // --------------  generice --------------------

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
        public void executa() {
            float nx=actor.poz.x+ MathUtils.clamp(x -actor.poz.x,-1,1),
                    ny= actor.poz.y+ MathUtils.clamp(y -actor.poz.y, -1,1);
            System.out.println("act walking: opos:"+actor.poz+" npos:"+new Vector2(nx,ny)+" fpos:"+new Vector2(x,y));        //debug

            if (nx==x && ny==y)  actor.step++;

            EnumSet<Level.CellFlag> cell = actor.level.cells[(int) nx][(int) ny];

            if (actor instanceof Badguy) {
                if (cell.contains(HERO)){
                    actor.atkMelee(actor.level.erou);
                    return;
                }
                if ( cell.contains(NPC)) {
                    actor.path= null;
                    actor.target= actor.level.getCreaturAt(nx,ny);
                    actor.atkMelee(actor.target);
                    return ;
                }
                if (cell.contains(MONST)) {
                    actor.path=null;
                    return ;
                }
                if (cell.contains(TRAP))
                        actor.interractTrap(nx, ny);

                //muta monstru+ update cells
                actor.level.cells[(int) actor.poz.x][(int) actor.poz.y].remove(MONST);
                actor.poz.x = nx;
                actor.poz.y = ny;
                cell.add(MONST);

                return ;
            }

            if (actor instanceof Npc) {
                    if (cell.contains(MONST)) {
                        actor.path= null;
                        actor.target= actor.level.getCreaturAt(nx,ny);
                        actor.atkMelee(actor.target);
                        return ;
                    }
                    if (cell.contains(NPC) || cell.contains(HERO)) {
                        actor.path= null;
                        return ;
                    }
                    if (cell.contains(TRAP))
                            actor.interractTrap(nx, ny);

                    //muta npc+ update cells
                    actor.level.cells[(int) actor.poz.x][(int) actor.poz.y].remove(NPC);
                    actor.poz.x = nx;
                    actor.poz.y = ny;
                    cell.add(NPC);
                }
        }
    }


    public static class Rest extends GenAction {
        public Rest(Creatura actor) {super(actor);}

        public Rest(Creatura actor, int cost) {
            super(actor, cost);
        }

        @Override
        public void executa() {
            System.out.println("    act: resting at "+actor.poz);      //debug
            actor.hp+=actor.hpreg();
            if (actor.hp>actor.mhp()) actor.hp= actor.mhp();
            //TODO rest

        }
    }

    public static class AtkRange extends GenAction {
        public AtkRange(Creatura actor) {super(actor);}

        public AtkRange(Creatura actor, int cost) {
            super(actor, cost);
        }

        @Override
        public void executa() {
            if (actor.target != null) {
                System.out.println("     act: range attack on"+ actor.target.poz);
                actor.atKRange(actor.target);
            }
        }
    }

}
