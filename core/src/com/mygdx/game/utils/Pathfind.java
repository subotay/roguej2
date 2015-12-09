package com.mygdx.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.content.Level;
import com.mygdx.game.content.creatures.Creatura;

import java.util.*;

public class Pathfind {

    public static class Node implements Comparable<Node>{
        public int x,y, f,g;
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;

        }

        @Override
        public int compareTo(Node o) {
            if (this.f < o.f) return -1;
            else if (this.f>o.f) return +1;
            else return 0;
        }

        @Override public String toString() {return "Node "+x+"-"+y;}
    }
    //---------------------------------------------------------
    /** manh. dist*/
    public static int cebdist(int ax, int ay, int bx, int by){
        return Math.max(Math.abs(ax-bx),Math.abs(ay-by));
    }

    private static Creatura creatura;
    private static HashSet<Node> closed;
    private static PriorityQueue<Node> open;
    private static HashMap<Node, Node> parr;

    private static long debug;     //debug

    private static Deque<Node> makePath(HashMap<Node, Node> parr, Node curr) {
        Deque<Node> path= new LinkedList<Node>();
        path.push(curr);
        while (parr.containsKey(curr)){
            path.push(parr.get(curr));
            curr= parr.get(curr);
        }
        return path;
    }

    /*//invalid location
    private static boolean invalid(int x, int y){
        return (          !(x>=0 && x<creatura.level.worldw && y>=0 && y<creatura.level.worldh)
                        || (creatura.level.cells[x][y].contains(Level.CellFlag.BLOC_MV))
                        || (!creatura.dumb && creatura.level.cells[x][y].contains(Level.CellFlag.TRAP))  );    }*/

//*************************************************************
    /**  jump point search*/
    private static List<Node> succ2(Node x, Node parr, Node end){
        if (parr==null)  //start node, ret all neighbors
            return Arrays.asList(neighbors(x));

        int dx= MathUtils.clamp(x.x-parr.x,-1,+1),
            dy= MathUtils.clamp(x.y - parr.y, -1, +1);
        List<Node> pruned= prune(x, dx, dy);
        List<Node> res= new ArrayList<Node>();

        for (Node pr: pruned){
            dx= MathUtils.clamp(pr.x-x.x,-1,+1);
            dy= MathUtils.clamp(pr.y - x.y, -1, +1);
            Node n= jump(x,dx,dy, end);
            if (n!=null) res.add(n);
        }
        return res;
    }

    private static Node jump(Node x, int dx, int dy, Node end) {
        Node n= new Node(x.x+dx, x.y+dy);
        if (n.equals(end)) return n;
        if (creatura.invalid(n.x, n.y)) return null;

        if (dx==0 || dy==0){     // +
            if (creatura.invalid(n.x + dy, n.y + dx) || creatura.invalid(n.x - dy, n.y - dx)) return n; //forced
            return jump(n,dx,dy,end);
        }else {                 //  x                   !!!!   Nu sare decat pe oriz/vert
            if (creatura.invalid(n.x, n.y - dy) || creatura.invalid(n.x - dx, n.y)) return n;//forced
            if (jump(n,dx,0,end)!=null) return n;
            if (jump(n,0,dy,end)!=null) return n;
            return jump(n,dx,dy,end);
        }
    }

    /** d- dir from parent*/
    private static List<Node> prune(Node n, int dx, int dy) {
        List<Node> res= new ArrayList<Node>();
        if (dx==0 || dy==0){ //line dir
                res.add(new Node(n.x+dx, n.y+dy));

            if (creatura.invalid(n.x + dy, n.y + dx))
                res.add(new Node(n.x+dx+dy, n.y+dx+dy));
            if (creatura.invalid(n.x - dy, n.y - dx))
                res.add(new Node(n.x+dx-dy, n.y-dx+dy));
        } else { //diag dir
                res.add(new Node(n.x+dx, n.y+dy));
                res.add(new Node(n.x+dx, n.y));
                res.add(new Node(n.x, n.y+dy));

            if (creatura.invalid(n.x, n.y - dy))
                res.add(new Node(n.x+dx, n.y-dy));
            if (creatura.invalid(n.x - dx, n.y))
                res.add(new Node(n.x-dx, n.y+dy));
        }
        return res;
    }

    /* jps partial ?*/
    public static LinkedList<Node> pathJPS(Creatura creat, int ex, int ey){
        Pathfind.creatura= creat;
        Node start= new Node((int)creatura.poz.x, (int)creatura.poz.y),
             end= new Node(ex,ey);
        LinkedList<Node> pt= new LinkedList<Node>();

        debug=0;

        closed= new HashSet<Node>();
        open= new PriorityQueue<Node>();
        parr= new HashMap<Node, Node>();
        start.g= 0;
        start.f= cebdist(start.x,start.y, end.x, end.y);
        open.add(start);

        while (!open.isEmpty()){
            Node curr= open.poll();
            if (curr.equals(end)) {
                pt= (LinkedList<Node>) makePath(parr,curr);
                System.out.println("pathfind nr evals: "+debug);
                return pt;
            }
            closed.add(curr);

            for (Node n : succ2(curr, parr.get(curr), end)){
                debug++;

                if (closed.contains(n)) continue;
                if ( creatura.invalid(n.x, n.y)  ) continue;

                int gscore= curr.g+ 1 ;

                if (!open.contains(n) || n.g> gscore){
                    n.g= gscore;
                    n.f= gscore+ + cebdist(n.x, n.y, end.x, end.y);
                    parr.put(n, curr);

                    if (open.contains(n))
                        open.remove(n);
                    open.add(n);
                }
            }
        }
        return pt;
    }
    //**********************************************************************
    //a star succesors
    private static Node[] neighbors(Node n){
        return new Node[]{new Node(n.x-1,n.y),  new Node(n.x+1, n.y),
                new Node(n.x-1,n.y+1), new Node(n.x, n.y+1), new Node(n.x+1, n.y+1),
                new Node(n.x-1, n.y-1), new Node(n.x, n.y-1), new Node(n.x+1, n.y-1)
        };
    }

   /*  *//* a star*//*
    public static LinkedList<Node> path(Creatura creat, int ex, int ey){
        Pathfind.creatura= creat;
        Node start= new Node((int)creatura.poz.x, (int)creatura.poz.y),
                end= new Node(ex,ey);
        LinkedList<Node> pt= new LinkedList<Node>();

        debug=0;

        closed= new HashSet<Node>();
        open= new PriorityQueue<Node>();
        parr= new HashMap<Node, Node>();
        start.g= 0;
        start.f= cebdist(start,end);
        open.add(start);

        while (!open.isEmpty()){
            Node curr= open.poll();
            if (curr.equals(end)){
                pt= (LinkedList<Node>) makePath(parr,curr);
                System.out.println(">>>>>>>>>   pathfind nr evals: "+debug);
                return pt;
            }
            closed.add(curr);

            for (Node n: neighbors(curr)){
                debug++;

                if (closed.contains(n)) continue;
                if ( invalid(n.x, n.y)  ) continue;

                int gscore= curr.g+ 1 ;

                if (!open.contains(n) || n.g> gscore){
                    n.g= gscore;
                    n.f= gscore+ + cebdist(n, end);
                    parr.put(n, curr);

                    if (open.contains(n))
                        open.remove(n);
                    open.add(n);
                }
            }
        }
        return pt;
    }*/
}
