package com.mygdx.game.content.creatures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.content.Level;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.Fov;
import com.mygdx.game.utils.Pathfind;

public class Badguy extends Creatura{
    private static int aggrorad=7;

    public Badguy(){}

    @Override
    public void update(float delta) {
        System.out.println("            //update    target"+ (target!=null?target.poz:"none"));     //debug

        if (target==null && level.fov[(int)poz.x][(int)poz.y]
                && Pathfind.cebdist((int) poz.x, (int)poz.y, (int)level.erou.poz.x, (int)level.erou.poz.y)<=aggrorad )
            target= level.erou;

        if (target!=null) {
            if (ranged) {
                if (Fov.lineOS(level, (int)poz.x, (int)poz.y, (int)target.poz.x, (int)target.poz.y)){
                    act=new GenAction.AtkRange(this);
                    path= null;
                }else {
                    findpath(target);
                    if (path.size() > 0) {
                        System.out.println(">>>>> path init,step " + step);   //debug
                        Pathfind.Node nxt = path.get(step);
                        act= new GenAction.WalkAt(this, nxt.x, nxt.y);
                    } else {
                        path = null;
                        act= new GenAction.Rest(this);
                    }
                }
            }
            else {  // melee
                if (path == null) {
                    findpath(target);
                    if (path.size() > 0) {
                        System.out.println(">>>>> path init,step " + step);   //debug
                        Pathfind.Node nxt = path.get(step);
                        act= new GenAction.WalkAt(this, nxt.x, nxt.y);
                    } else {
                        path = null;
                        act=  new GenAction.Rest(this);
                    }
                } else {// has path
                    if (Pathfind.cebdist((int) lastpos.x, (int) lastpos.y, (int) target.poz.x, (int) target.poz.y) > 1
                        || step >= path.size()) {
                        System.out.println(">>>>> path recalc");   //debug
                        findpath(target);
                    }

                    if (path.size() > 0) {
                        System.out.println("path advanc, step " + step);   //debug
                        Pathfind.Node nxt = path.get(step);
                        act= new GenAction.WalkAt(this, nxt.x, nxt.y);
                    } else {
                        path = null;
                        act= new GenAction.Rest(this);
                    }
                }
            }

            if (Pathfind.cebdist((int) poz.x, (int) poz.y, (int) target.poz.x, (int) target.poz.y) > aggrorad) { //ranged <melee  TODO
                target = null;   //act == walk|| rest || etc ^
            }
        }
        //no target
        else {
            path= null;
            act= new GenAction.Rest(this);
        }

        sprite.setBounds(poz.x, poz.y, 1, 1);

        System.out.println("            update//");     //debug
    }

    private void findpath(Creatura target) {
        path= Pathfind.pathJPS(this,(int)target.poz.x,(int)target.poz.y);
        System.out.println(">>>>> new path: "+path);  //debug
        step=1;
        lastpos.set(target.poz);
    }

    @Override
    public boolean invalid(int x, int y) {
        return (          !(x>=0 && x<level.worldw && y>=0 && y<level.worldh)
                || (level.cells[x][y].contains(Level.CellFlag.BLOC_MV))
                || (!dumb && level.cells[x][y].contains(Level.CellFlag.TRAP))
                || (level.cells[x][y].contains(Level.CellFlag.MONST))
        );
    }

    @Override
    public void onHit(Creatura hitter) {
        super.onHit(hitter);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }

}

/* Badguy(Level level, MapProperties props) {
        super(level, props);
        energ= 100;
        speed= 8;
        //TODO aici ar trebui in fctie de props
        Assets.INST.loadSprite(Assets.MONST1);
        Assets.INST.loadSprite(Assets.MONST2);
        if (dumb)
            sprite= Assets.INST.getSprite(Assets.MONST2);
        else
            sprite= Assets.INST.getSprite(Assets.MONST1);

        id= "m_melee1";
        sprite.setBounds(poz.x,poz.y,1,1);

    }*/
