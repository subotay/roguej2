package com.mygdx.game.content.objects;


import com.badlogic.gdx.maps.MapProperties;
import com.mygdx.game.content.Entitate;
import com.mygdx.game.content.Level;

public abstract class Obiect extends Entitate  {

    public Level level;

    public Obiect(){}

    /*  pt loot */
    public Obiect(Level level){
        this.level= level;
    }

    public Obiect(Level level, MapProperties props) {
        this.level= level;
        float   x=props.get("x",float.class),
                y= props.get("y",float.class),
                w=props.get("width",float.class),
                h=props.get("height",float.class);
        poz.set(x/w,y/h);
        System.out.print(poz);      //debug
    }

    public static void makeObiect(Level level, MapProperties props){
        String tip=props.get("tip",String.class);

        if  (tip.equals("trigger"))
            new Trigger(level,props);

        else
        if (tip.equals("door"))
            new Door(level,props);

        else
        if (tip.equals("trap"))
            new Trap(level,props);

    }

    @Override public String toString() {return "Obiect "+id+" "+poz;}
}
