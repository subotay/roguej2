package com.mygdx.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.content.Level;

import java.util.ArrayList;
import java.util.List;

public class Fov {
    private static Level level;
    private static int ox,oy, raza,  mapw, maph;
    private static int[] lim;  //de modif pt cerc
    private static boolean[][] fovtest;   //false initial, true la final dc poz e visibila


    public static void shadow(Level level,int px, int py, boolean[][] fov, int raza){
        Fov.level= level;
        fovtest= fov;
        mapw=level.worldw;
        maph= level.worldh;
        ox=px; oy=py;
        Fov.raza= raza;

        //TODO pt cerc
        lim = new int[raza+1];
        for (int i = 0; i < lim.length; i++){
//            lim[i]= (int) Math.ceil(Math.sqrt(BRAZA*BRAZA-i*i));
            lim[i]= (int)Math.min( i, Math.round( raza * Math.cos( Math.asin( i / (raza + 0.5) ))));
        }

        fovtest[ox][oy]= true;
        level.cells[ox][oy].add(Level.CellFlag.MAPPED);

        //          xx  yx  xy  yy
        chk1(1, 0, 1, 1, 0, 0, 1); //1
        chk2(1, 0, 1, 0, 1, 1, 0); //2
        chk1(1, 0, 1, 0, -1, 1, 0); //3
        chk2(1, 0, 1f, 1, 0, 0, -1); //4
        chk1(1, 0, 1, -1, 0, 0, -1); //5
        chk2(1, 0, 1f, 0, -1, -1, 0); //6
        chk1(1, 0, 1, 0, 1, -1, 0); //7
        chk2(1, 0, 1f, -1, 0, 0, 1); //8
    }

    private static void chk1(int r, float a, float b, int xx, int yx, int xy, int yy ){
        if (a>=b) return;
        float na=a;
        boolean block= false; //marks prior(left) blocks

        for (int i = r; i <=raza ; i++) {
            int rlim= lim[i], j0= MathUtils.ceil(na*i), j1= MathUtils.ceil(b * i);

            for (int j = j0; j<j1 && j<=rlim  ; j++) {

                int cx = ox + i * xy + j * xx,
                        cy = oy + i * yy + j * yx;
                float a1= (float) j/(i+1),  a2=(float) (j+1)/i;
//                float   a1 = (j-.5f)/(i+.5f),     a2= (j+.5f)/(i-.5f);
//                float a1=(float)j/i-.5f/i,      a2= (float)j/i+.5f/i;

                if (cx>=0 && cx<mapw &&cy>=0 && cy<maph) {  //in map
                    if (level.cells[cx][cy].contains(Level.CellFlag.BLOC_V)){ //this blocks
                        if (!block) { //prior doesn't
                            chk1(i + 1, na, a1, xx, yx, xy, yy); //here na=a
                            block = true;
                        } else {
                            //prior does too
                        }
                        /*level.cells[cx][cy].add(Level.CellFlag.MAPPED);
                        fovtest[cx][cy] = true;*/
                        na = a2;
                    } else {
                        level.cells[cx][cy].add(Level.CellFlag.MAPPED);
                        fovtest[cx][cy] = true;
                        block = false;
                    }
                }
            }
        }
    }

    private static void chk2(int r, float a, float b, int xx, int yx, int xy, int yy ){
        if (a>=b) return;
        float na=a;
        boolean block= false; //marks prior(left) blocks

        for (int i = r; i <=raza ; i++) {
            int rlim= lim[i], j0= MathUtils.ceil(na*i), j1= MathUtils.ceil(b * i);

            for (int j = j0; j<= j1 && j<= rlim  ; j++) {

                int cx = ox + i * xy + j * xx,
                        cy = oy + i * yy + j * yx;

                float a1= (float) j/(i+1),  a2=(float) (j+1)/i;
//                float   a1 = (j-.5f)/(i+.5f),  a2= (j+.5f)/(i-.5f);
//                float a1=(float)j/i-.5f/i,      a2= (float)j/i+.5f/i;

                if (cx>=0 && cx<mapw &&cy>=0 && cy<maph) {  //in map
                    if (level.cells[cx][cy].contains(Level.CellFlag.BLOC_V)){    //this blocks
                        if (!block) { //prior doesn't
                            chk2(i + 1, na, a1, xx, yx, xy, yy); //here na=a
                            block = true;
                        } else {
                            //prior does too
                        }
                        /*level.cells[cx][cy].add(Level.CellFlag.MAPPED);
                        fovtest[cx][cy] = true;*/
                        na = a2;
                    } else {
                        level.cells[cx][cy].add(Level.CellFlag.MAPPED);
                        fovtest[cx][cy] = true;
                        block = false;
                    }
                }
            }
        }
    }



    public static boolean lineOS(Level level, int x0, int y0, int x1, int y1){
        int dx=Math.abs(x1-x0), dy= Math.abs(y1-y0),
                sx= x0<x1?1:-1,  sy= y0<y1? 1:-1,
                e=dx-dy;

        while (true){
            if (level.cells[x0][y0].contains(Level.CellFlag.BLOC_V))
                return false;
            if (x0==x1 &&y0==y1) break;

            int de=e*2;
            if (de>-dx){
                e-=dy;
                x0+=sx;
            }
            if (de<dx){
                e+=dx;
                y0+=sy;
            }
        }
        return true;
    }
}


/*public static boolean lineOS( int x0, int y0, int x1, int y1){
        List<Pathfind.Node> debug= new ArrayList<Pathfind.Node>();

        boolean steep= Math.abs(y1-y0)>Math.abs(x1-x0);
        if (steep){
            int t=x0; x0=y0; y0=t;
                t=x1; x1=y1; y1=t;
        }
        if (x0>x1){
            int t=x0; x0=x1; x1=t;
                t=y0; y0=y1; y1=t;
        }
        int dx=x1-x0, dy= Math.abs(y1-y0), y=y0, sy= y0<y1? 1:-1,
                e= dx/2;

        for (int x=x0; x<=x1; x++){
            if (steep){
                debug.add(new Pathfind.Node(y,x));
            }
            else {
                debug.add(new Pathfind.Node(x,y));
            }

            e-=dy;
            if (e<0){
                y+=sy;
                e+=dx;
            }
        }

        System.out.println(debug);
        return true;
    }*/





