package com.mygdx.game.content;

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
    public transient int worldw, worldh, tilew, tileh;
//    private int currActor=0;
    public transient long turn=0;  //?????

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
    public transient EnumSet<CellFlag> [][] cells;
    public transient boolean[][] fov;
    public transient Erou erou;
    public transient Array<Door> doors;
    public transient Array<Trigger> triggs;

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
//        doors.ordered= true;
//        triggs.ordered= true;
//        traps.ordered= true;
//        loots.ordered= true;
//        actori.ordered= true;
    }


    public void update(float delta) {

        if (erou.act==null) return; //wait input
        System.out.println(erou);        //debug
        System.out.println("------------ turn"+turn );      //debug
        erou.energ+= erou.speed();
        if (erou.act.cost() <= erou.energ) {
            System.out.println("   //before act: energ" + erou.energ);                           //debug
            erou.act.executa();
            erou.energ-= erou.act.cost();
            erou.act= null;
            System.out.println("    //after act: energ" + erou.energ);                 //debug
        }
        erou.update(delta);

        updateFov();
        turn++;


        for (Trap trap: traps){
//            System.out.println(tarp);
            //TODO trap action
        }

        for (Creatura creatura: actori) {
            if (creatura.act == null) continue;
            System.out.println(creatura);                           //debug
            creatura.energ += creatura.speed();
            if (creatura.act.cost() <= creatura.energ) {
                System.out.println("    //before act: energ" + creatura.energ);          //debug
                creatura.act.executa();
                creatura.energ -= creatura.act.cost();
                creatura.act = null;
                System.out.println("    //after act: energ" + creatura.energ);        //debug
            }
            creatura.update(delta);
        }

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
//        System.out.println("(x" + x + " ,y" + y + "; w" + w + " h" + h + ")wall");                  //debug
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
//        System.out.println("(x"+x+" ,y"+y+"; w"+w+" h"+h+")decor");                  //debug
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