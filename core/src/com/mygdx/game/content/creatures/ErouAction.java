package com.mygdx.game.content.creatures;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.content.Level;
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

    public abstract void executa() ;

    @Override
    public int cost() {
        return cost;
    }

    //--------------------------------------------------------------
    public static  class Walk extends ErouAction{
        public Walk(Erou erou) {
            super(erou);
        }

        public Walk(Erou actor, int cost) {
            super(actor, cost);
        }

        @Override
        public void executa() {
            float[] npos= actor.getNextPos(1);
            // out of map
            if (npos[0]<0 || npos[0]>= actor
                    .level.worldw || npos[1]<0 || npos[1]>= actor.level.worldh )
                return ;

            EnumSet<Level.CellFlag> cell= actor.level.cells[(int)npos[0]][((int) npos[1])];

            if (cell.contains(BLOC_MV))
                if (cell.contains(DOOR)) {
                    actor.interractDoor(npos[0], npos[1]);
                    return ;
                } else return ;  //walks in walls

            if (cell.contains(MONST)){
                Creatura target= actor.level.getCreaturAt(npos[0],npos[1]);
                actor.atkMelee(target);
                return ;
            }

            if (cell.contains(NPC)){
                actor.interactNPC((Npc)actor.level.getCreaturAt(npos[0],npos[1]));
//                return ;
            }


            if (cell.contains(TRAP)) {
                actor.interractTrap(npos[0], npos[1]);
            }

              //nimic nu bloc.
            actor.level.cells[(int) actor.poz.x][(int) actor.poz.y].remove(HERO);
            actor.poz.x = npos[0];
            actor.poz.y = npos[1];
            cell.add(HERO);
        }


}

    /** interact loot*/
    public static class InteractLoot extends ErouAction{
        private float x,y;

        public InteractLoot(Erou erou, float x, float y) {
            super(erou);
            this.x= x;
            this.y=y ;
        }

        public InteractLoot(Erou actor, int cost, float x, float y) {
            super(actor, cost);
            this.x = x;
            this.y = y;
        }

        @Override
        public void executa(){
            //get loot at  xy
            Assets.INST.man.get(Assets.S_LOOT, Sound.class).play();
            //TODO loot window
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
    *//**  swap position ex: friendly, non-interact. guard*//*
    public static class Swap extends ErouAction{
        private Npc target;

        public Swap(Erou actor, Npc target) {
            super(actor);
            this.target= target;
        }
        @Override
        public boolean executa() {
            return true;
        }

    }
    *//** interact door*//*
    public static class InteractDoor extends  ErouAction{
        private Door targ;

        public InteractDoor(Erou actor, Door target) {
            super(actor);
            targ= target;
        }
        @Override public boolean executa() {

            return true;
        }

    }*/
