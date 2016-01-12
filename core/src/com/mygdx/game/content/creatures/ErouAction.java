package com.mygdx.game.content.creatures;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.objects.items.Weapon;
import com.mygdx.game.utils.Assets;

import java.util.EnumSet;

import static com.mygdx.game.content.Level.CellFlag.*;

public abstract class ErouAction implements Action {
    public final  Erou actor;
    public final int cost;

    public ErouAction(Erou actor){
        this.actor= actor;
        cost= 100;
    }
    public ErouAction(Erou actor, int cost){
        this.actor= actor;
        this.cost= cost;
    }

    public abstract boolean executa() ;

    @Override
    public int cost() {
        return cost;
    }

    //********************************************************************************
    public static  class Walk extends ErouAction{
        public Walk(Erou erou) {
            super(erou);
        }

        public Walk(Erou actor, int cost) {
            super(actor, cost);
        }

        @Override
        public boolean executa() {
            float[] npos= actor.getNextPos(1);
            // out of map
            if (npos[0]<0 || npos[0]>= actor
                    .level.worldw || npos[1]<0 || npos[1]>= actor.level.worldh )
                return true;

            EnumSet<Level.CellFlag> cell= actor.level.cells[(int)npos[0]][((int) npos[1])];

            if (cell.contains(BLOC_MV)) {
                if (cell.contains(DOOR))
                    actor.interractDoor(npos[0], npos[1]);  //contine walk
                return true;  //walks in walls
            }

            if (cell.contains(MONST)){
                Creatura target= actor.level.getCreaturAt(npos[0],npos[1]);
//                actor.target= target;
                actor.removeTarget();
                actor.setTarget(target);
                actor.act=new GenAction.AtkMelee(actor,target, ((Weapon)actor.eqp.get(Erou.EqpSlot.RHAND)).atkcost);
                return false;
            }

            if (cell.contains(NPC))
                actor.swapNPC((Npc) actor.level.getCreaturAt(npos[0], npos[1]));  //contine swap doar pt npc
            if (cell.contains(TRAP))
                actor.interractTrap(npos[0], npos[1]);

              //nimic nu bloc.
            actor.level.cells[(int) actor.poz.x][(int) actor.poz.y].remove(HERO);
            actor.poz.x = npos[0];
            actor.poz.y = npos[1];
            cell.add(HERO);
            return true;
        }
    }

}

   /* *//**  interact NPC*//*
    public  static class InteractNPC extends ErouAction{
        private Npc target;

        public InteractNPC(Erou actor, Npc target) {
            super(actor);
            this.target= target;
        }
        @Override
        public boolean executa() {
            return true;
        }

    }

    */
