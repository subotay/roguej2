package com.mygdx.game.content.creatures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.content.Level;
import com.mygdx.game.utils.Fov;
import com.mygdx.game.utils.Pathfind;

public class Npc extends Creatura {
    private static int aggrorad=6;


    public Npc(){
        act=new GenAction.Rest(this);
    }

    @Override
    public void updateAI(float delta) {

//        System.out.println("  //update    target" + (target != null ? target.poz : "none"));     //debug

        if (level.fov[(int)poz.x][(int)poz.y] && level.erou.target!=null
                &&(Pathfind.cebdist((int) poz.x, (int) poz.y,
                                    (int) level.erou.target.poz.x, (int) level.erou.target.poz.y) <= aggrorad))
            target= level.erou.target;

        if (target!=null ){
            if (!melee) {
                if (Fov.lineOS(level, (int) poz.x, (int) poz.y, (int) target.poz.x, (int) target.poz.y)){
                    act=new GenAction.AtkRange(this);
                    path= null;
                }else {
                    findpath(target);
                    if (path.size() > 0) {
//                        System.out.println("        >>>>> path init,step " + step);   //debug
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
//                        System.out.println("        >>>>> path init,step " + step);   //debug
                        Pathfind.Node nxt = path.get(step);
                        act= new GenAction.WalkAt(this, nxt.x, nxt.y);
                    } else {
                        path = null;
                        act=  new GenAction.Rest(this);
                    }
                } else {// has path
                    if (Pathfind.cebdist((int) lastpos.x, (int) lastpos.y, (int) target.poz.x, (int) target.poz.y) > 1
                            || step >= path.size()) {
//                        System.out.println(">>>>> path recalc");   //debug
                        findpath(target);
                    }

                    if (path.size() > 0) {
//                        System.out.println("path advanc, step " + step);   //debug
                        Pathfind.Node nxt = path.get(step);
                        act= new GenAction.WalkAt(this, nxt.x, nxt.y);
                    } else {
                        path = null;
                        act= new GenAction.Rest(this);
                    }
                }
            }

            if (Pathfind.cebdist((int) poz.x, (int) poz.y, (int) target.poz.x, (int) target.poz.y) > aggrorad) { //melee <melee  TODO
                target = null;   //act == walk|| rest || etc ^
            }
        }
        else {   //no target
            step=1;
            if (!poz.equals(home)){
                path= (path==null ? Pathfind.pathJPS(this,(int) home.x, (int) home.y):null);
                if (path!=null && path.size() > 0) {
                    Pathfind.Node nxt = path.get(step);
                    act= new GenAction.WalkAt(this, nxt.x, nxt.y);
                } else {
                    path = null;
                    act= new GenAction.Rest(this);
                }
            }
            else {
                path= null;
                act= new GenAction.Rest(this);
            }
        }
    }

    private void findpath(Creatura target) {
        path= Pathfind.pathJPS(this,(int)target.poz.x,(int)target.poz.y);
//        System.out.println(">>>>> new path: "+path);  //debug
        step=1;
        lastpos.set(target.poz);
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
    }


    @Override
    public boolean invalid(int x, int y) {
        return (          !(x>=0 && x<level.worldw && y>=0 && y<level.worldh)
                || (level.cells[x][y].contains(Level.CellFlag.BLOC_MV))
                || (!dumb && level.cells[x][y].contains(Level.CellFlag.TRAP))
                || (level.cells[x][y].contains(Level.CellFlag.NPC))
                || (level.cells[x][y].contains(Level.CellFlag.HERO))
        );
    }

    @Override
    public void onHitBy(Creatura hitter) {
        super.onHitBy(hitter);
    }

}

