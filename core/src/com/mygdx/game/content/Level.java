package com.mygdx.game.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.content.creatures.Badguy;
import com.mygdx.game.content.creatures.Creatura;
import com.mygdx.game.content.creatures.Erou;
import com.mygdx.game.content.creatures.Npc;
import com.mygdx.game.content.objects.Door;
import com.mygdx.game.content.objects.items.ItemContainer;
import com.mygdx.game.content.objects.Trap;
import com.mygdx.game.content.objects.Trigger;
import com.mygdx.game.utils.Fov;

import java.util.Arrays;
import java.util.EnumSet;

/** contine erou+actori+obiecte
 * */
public class Level implements Disposable {
    public String numeNivel;
    public int worldw, worldh, tilew, tileh;

    public int turn=0;
    public static final int TICKS=20;

    public enum CellFlag{ //  TODO wip
        MONST,
        NPC,
        HERO,
        //-----
        DOOR,
        TRIGGER,
        TRAP,
        LOOT,
        BLOC_MV,
        BLOC_V, //pot avea un decor ce block_mv, dar nu bloc_view
        MAPPED,
        //VISIB,  //de catre player, dc un monstru e la loc., e in range (vice versa)
    }
    /* TODO  de updatat la modificare celula*/
    public  EnumSet<CellFlag> [][] cells;
    public  boolean[][] fov;
    public  Erou erou;
    public  Array<Door> doors;
    public  Array<Trigger> triggs;

        //  -----------------------  dynamic data
    public Array<Creatura> actori;
    public Array<Trap> traps;
    public Array<ItemContainer> loots;
        // ----------------------------------
    public Level() {
        actori =new Array<Creatura>();
        doors= new Array<Door>();
        triggs= new Array<Trigger>();
        traps= new Array<Trap>();
        loots= new Array<ItemContainer>();
        doors.ordered= false;
        triggs.ordered= false;  //??TODO order vs unorder ??
        traps.ordered= false;
        loots.ordered= false;
        actori.ordered= false;
    }


    public void update(float delta) {
        if (erou.act==null) return; //wait input

        if (erou.hp<=0 ) {
            erou.dead= true;
            cells[((int) erou.poz.x)][(int)erou.poz.y].remove(CellFlag.HERO);
            return;
        }

        turn=(turn+TICKS)% 100;

        Gdx.app.log("ticks ", turn + "");
        Gdx.app.log("erou ", erou.act.getClass().getSimpleName());

        erou.energ+= TICKS*(erou.slowed? .5f: 1)*(erou.hasted? 2:1);
        if (erou.act.cost() <= erou.energ) {
            boolean done= erou.act.executa();
            while (!done )
                done= erou.act.executa();
            erou.energ-= erou.act.cost();
//            Gdx.app.log("  act executed cost", erou.act.cost()+"");
            erou.act= null;
            erou.updateSprite(delta);  //update sprite
            updateFov();
        }
        erou.updateAI(delta);

        if (turn==0){
            erou.update(delta);

            for (Trap trap: traps) {}   //TODO trap action
        }

        for (int i=0; i<actori.size; i++) {
            Creatura creatura= actori.get(i);
            Gdx.app.log("creatura ", creatura.name +creatura.poz+ " act" + (creatura.act == null ? "null" : creatura.act.getClass().getSimpleName()));

            if (creatura.hp<=0) {  //dead
                creatura.dead= true;
                cells[((int) creatura.poz.x)][((int) creatura.poz.y)]
                        .remove((creatura instanceof Badguy ? CellFlag.MONST : CellFlag.NPC));
                actori.removeValue(creatura, true);
                i--;
                continue;
            }

            creatura.energ += TICKS*(creatura.slowed? .5f: 1)*(creatura.hasted? 2:1);

            if (creatura.act.cost() <= creatura.energ) {
                boolean done= creatura.act.executa();
                while (!done )
                    done= creatura.act.executa();
                creatura.act.executa();
                creatura.energ -= creatura.act.cost();
                Gdx.app.log("  act executed cost", creatura.act.cost()+"");
                creatura.act = null;
                creatura.updateSprite(delta);
                creatura.updateAI(delta);
            }
            if (turn == 0) {
                creatura.update(delta);
            }
        }

        if (turn==0)       Gdx.app.log("     //endturn", "");
    }

    private void updateFov() {
        //all cells blind each step
        for (boolean[] aFov : fov) {
            Arrays.fill(aFov, false);
        }
        Fov.shadow(this,(int)erou.poz.x, (int) erou.poz.y,fov, erou.vraza);
    }


    public void render(float delta, SpriteBatch batch) {
        for (Trap o:traps)
            if (o.poz.dst(erou.poz)<=erou.vraza +1)
                o.render(delta, batch);
        for (Door o:doors)
            if (o.poz.dst(erou.poz)<=erou.vraza +1)
                o.render(delta, batch);
        for (ItemContainer o:loots)
            if (o.poz.dst(erou.poz)<=erou.vraza +1)
                o.render(delta, batch);
        //triggers invis.
        erou.render(delta, batch);

        for (Entitate c: actori){
            if (c instanceof Trap) continue;
            if (fov[((int) c.poz.x)][((int) c.poz.y)])
                c.render(delta,batch);
        }
    }


    public void initCells(float world_w, float world_h, float tilew, float tileh) {
        worldw=(int) world_w;
        worldh=(int) world_h;
        this.tilew= (int) tilew;
        this.tileh= (int)tileh;
        cells= new EnumSet [worldw][worldh];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j]=EnumSet.noneOf(CellFlag.class);
        }}
        cells[(int)erou.poz.x][(int)erou.poz.y].add(CellFlag.HERO);
        fov= new boolean[worldw][worldh];
    }

    /**  doar intern  wall, blocm+ blocview */
    public void addWalls(RectangleMapObject obj) {
        Rectangle rect=obj.getRectangle();
        float x=rect.getX()/tilew, y= rect.getY()/tileh
                ,w= rect.getWidth()/tilew, h= rect.getHeight()/tileh;
        for (int i = (int)x; i <(int) x+w; i++) {
            for (int j = (int)y; j <(int)y+h; j++) {
                cells[i][j].add(CellFlag.BLOC_MV);
                cells[i][j].add(CellFlag.BLOC_V);
            }
        }
    }

    /**  intern decor, blocmove, nu blocview*/
    public void addDecor(RectangleMapObject obj) {
        Rectangle rect=obj.getRectangle();
        float x=rect.getX()/tilew, y= rect.getY()/tileh
                ,w= rect.getWidth()/tilew, h= rect.getHeight()/tileh;
        for (int i = (int) x; i <(int) x+w; i++) {
            for (int j = (int)y; j <(int)y+h; j++) {
                cells[i][j].add(CellFlag.BLOC_MV);
            }
        }
    }

//-----------------------------------------------------------------
    public Creatura getCreaturAt(float x, float y){
        for (int i=0; i<actori.size; i++){
            Entitate e= actori.get(i);
            if (e.poz.x == x && e.poz.y == y)
                return (Creatura) e;
        }
        return null;
    }

    public Badguy getMobAt(float x, float y){
        for (int i=0; i<actori.size; i++){
            Creatura e= actori.get(i);
            if (e.poz.x==x && e.poz.y == y && e instanceof Badguy)
                return (Badguy) e;
        }
        return null;
    }

    public Npc getNpcAt(float x, float y){
        for (int i=0; i<actori.size; i++){
            Creatura e= actori.get(i);
            if (e.poz.x==x && e.poz.y == y && e instanceof Npc )
                return (Npc) e;
        }
        return null;
    }

    public ItemContainer getLootAt(float x, float y){
        for (ItemContainer ob: loots)
            if (ob.poz.x==x &&  ob.poz.y==y  )
                return ob;
        return null;
    }

    public Door getDoorAt(float x, float y){
        for (Door ob: doors)
            if (ob.poz.x==x &&  ob.poz.y==y )
                return ob;
        return null;
    }

    public Trigger getTriggerAt(float x, float y) {
        for (Trigger ob: triggs)
            if (ob.poz.x==x &&  ob.poz.y==y )
                return ob;
        return null;
    }

    public Trap getTrapAt(float x, float y) {
        for (Trap ob: traps)
            if (ob.poz.x==x &&  ob.poz.y==y )
                return ob;
        return null;
    }

    @Override public void dispose() {

    }
}



/*//----------------------------------------v1 render after each actor ---------------------------
        Entitate entitate= actori.get(currActor);

        if (entitate instanceof Creatura) {
            Creatura actor=(Creatura)entitate;
            System.out.println("actor " + currActor + " " + actor.poz);                                       //debug
//            System.out.println(Gdx.graphics.getDeltaTime());                    //debug
            if (actor.act == null) return; //act ==null doar la erou
            actor.giveSpeed();
            System.out.println("speed" + actor.speed + " energ" + actor.energ);                           //debug

            if (Constants.COST <= actor.energ) {
                actor.act.executa();
                actor.consumeEnergy(Constants.COST);
                actor.act = null;
                System.out.println("speed" + actor.speed + " energ" + actor.energ);                 //debug
            }
            actor.update(delta);
        } else{
            Trap trap=(Trap) entitate;
            System.out.println("trap "+currActor+ " "+ trap.poz);           //debug
        }

        if (currActor == actori.size) turn++;
        currActor=(currActor+1)%actori.size;
        //--------------end vv1-------------------------------------------*/