package com.mygdx.game.content.creatures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.objects.Door;
import com.mygdx.game.utils.Assets;

import java.util.EnumMap;

import static com.mygdx.game.content.Level.CellFlag.*;

public class Erou extends Creatura {
    public int lvl, xp;
    public static final int BRAZA = 6;

    public int vraza;
    public  String levelName;

    public Erou() {
        stts=new EnumMap<Stat, Integer>(Stat.class);
        act= new GenAction.Rest(this);
        Assets.loadSprite(Assets.EROU);
        this.sprite= Assets.getSprite(Assets.EROU);;
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
