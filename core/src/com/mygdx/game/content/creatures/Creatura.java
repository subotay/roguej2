package com.mygdx.game.content.creatures;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.content.Entitate;
import com.mygdx.game.content.Level;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.Pathfind;

import java.util.EnumMap;
import java.util.LinkedList;


public abstract class Creatura extends Entitate {


    public enum Stat {
        MHP,HPREG,
        MSTAM, STAREG,
        HIT, EVA,
        FATK,FRES,
        EATK, ERES,
        CRIT,
        ARMOR,
        BONDMG
    }
    public enum Dir{N,NE,E,SE,S,SW,W,NW,STAY;}

    public  Creatura target;
    public   Action act;

    protected  Vector2 lastpos; //last known target position
    protected  LinkedList<Pathfind.Node> path;
    protected  int step;

    public  Dir walkDir=Dir.STAY;
    public  Level level;
    public Vector2 home;
    public String name;
    //description

    public boolean slowed, hasted;

    /*------------------------------------------*/
    //  attributes
    public int STR;
    public int AGI;
    public int VIT;
    public int END;
    public int SPI;
    //
    public  EnumMap<Stat, Integer> stts;
    //
    public int hp,stam;

    public boolean dumb;
    public boolean melee; //def melee
    public float energ;
    public int atkcost;

    /*------------------------------------------*/

    public int mhp(){ return VIT*10+ stts.get(Stat.MHP); }
    public int mstam(){ return END*2+ stts.get(Stat.MSTAM);}
    float accHp;
    public float hpreg(){ return VIT*.2f+ stts.get(Stat.HPREG); }
    float accStam;
    public float stareg(){ return END*.2f+ stts.get(Stat.STAREG);}

    public int hit(){ return (STR+ AGI-10)/2+ stts.get(Stat.HIT);}
    public int eva(){ return AGI-5+ stts.get(Stat.EVA);}

    public int fatk(){ return STR-5+ stts.get(Stat.FATK);}
    public int fres(){ return VIT-5+ stts.get(Stat.FRES);}
    public int eatk(){ return SPI-5+ stts.get(Stat.EATK);}
    public int eres() { return END-5+ stts.get(Stat.ERES);}
        //dmg   //+ weapon dmg(erou)
    public int dmg(){ return  stts.get(Stat.BONDMG ); }
/* crit direct  //poate depinde de skill
   armor direct
*/

    public Creatura() {
        stts=new EnumMap<Stat, Integer>(Stat.class);
        lastpos= new Vector2();
        target= null;
        act= new GenAction.Rest(this);  //dir stay
        path=null;
    }

//--------------------------------------------------

    /** get dir */
    public static Dir getDir(int cx, int cy, int nx, int ny){
        int dx= MathUtils.clamp(nx-cx, -1, 1),
                dy= MathUtils.clamp(ny-cy, -1,1);
        if (dx==-1){
            switch (dy) {
                case 0: return Dir.W;
                case 1: return Dir.NW;
                case -1: return Dir.SW;
            }
        }else if (dx==1){
            switch (dy) {
                case 0: return Dir.E;
                case 1: return Dir.NE;
                case -1: return Dir.SE;
            }
        }
            switch (dy) {
                case 0: return Dir.STAY;
                case 1: return Dir.N;
                case -1: return Dir.S;
            }
        return Dir.STAY; //nu
    }

    /** next pos in i steps8*/
    public float[] getNextPos(int i){
        Vector2 res= new Vector2();
        switch (walkDir){
            case N:
                res.set(poz.x, poz.y+i);
                break;
            case NE:
                res.set(poz.x+i, poz.y+i);
                break;
            case E:
                res.set(poz.x+i, poz.y);
                break;
            case SE:
                res.set(poz.x+i, poz.y-i);
                break;
            case S:
                res.set(poz.x, poz.y-i);
                break;
            case SW:
                res.set(poz.x-i, poz.y-i);
                break;
            case W:
                res.set(poz.x-i, poz.y);
                break;
            case NW:
                res.set(poz.x-i, poz.y+i);
                break;
            case STAY:
                res.set(poz);
                break;
        }
        return new float[]{res.x, res.y};
    }

    public abstract boolean invalid(int x, int  y);

    /***********************************************************************************/
    /** update end turn*/
    @Override
    public void update(float delta) {
        if (hp < mhp()) {
            accHp+= hpreg();
            hp+= Math.floor(accHp);
            accHp-=Math.floor(accHp);
            if (hp>=mhp()){
                hp=mhp();
                accHp=0;
            }
        }
        if (stam >= mstam()) {
            accStam+= stareg();
            stam+= Math.floor(accStam);
            accStam-= Math.floor(accStam);
            if (stam>=mstam()){
                stam=mstam();
                accStam=0;
            }
        }
    }

    /**  after act only */
    public void updateSprite(float delta){
        sprite.setBounds(poz.x, poz.y, 1, 1);
    };

    /**  each 20 ticks sau each turn ?*/
    public abstract void updateAI(float delta);

    /********************************************************************************************/

    public  void interractTrap(float x, float y){
        Assets.man.get(Assets.S_RHIT, Sound.class).play();

        //TODO trap activation
    }

    public void atkMelee(Creatura target) {
        Assets.man.get(Assets.S_MHIT, Sound.class).play();
        target.onHitBy(this);
        target.hp-= (dmg()- target.stts.get(Stat.ARMOR));
        if (target.hp<0) target.hp=0;
        //TODO attack
    }

    public void atKRange(Creatura target){
        Assets.man.get(Assets.S_RHIT, Sound.class).play();
        target.onHitBy(this);
        target.hp-= (dmg()- target.stts.get(Stat.ARMOR));
        if (target.hp<0) target.hp=0;
        //TODO attack
    }

    public void onHitBy(Creatura hitter){
        target= hitter;
    };

    public void onDeath(){

    }


//--------------------------------------------
    @Override public String toString() {return "Creatura "+id+" "+poz+" hp:"+hp+"/"+mhp();}

}


/*   *//**  1 pas doar *//*
    public void walk(Dir dir){
        switch (dir){
            case N:
                move(0,1);break;
            case NE:
                move(1, 1);break;
            case E:
                move(1, 0);break;
            case SE:
                move(1, -1);break;
            case S:
                move(0, -1); break;
            case SW:
                move(-1,-1);break;
            case W:
                move(-1, 0);break;
            case NW:
                move(-1, 1);break;
            case STAY:
                move(0, 0);
                break;
        }
    }
    private void move(float x, float y){
        poz.x+=x;
        poz.y+=y;
    }*/

