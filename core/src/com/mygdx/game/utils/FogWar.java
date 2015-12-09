package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.mygdx.game.content.Level;

public class FogWar {
    private static Pixmap pm;
    private static Texture texture;
    public static Sprite sprite;

    public static Sprite getFog(Level level){
        int w= level.worldw, h= level.worldh;
        pm= new Pixmap(w,h, Pixmap.Format.RGBA8888);
        Pixmap.setBlending(Pixmap.Blending.None);
        pm.setColor(0, 0, 0, 1);
        pm.fill();

        for (int i = 0; i < level.worldw; i++) {
            for (int j = 0; j < level.worldh; j++) {
                if (level.cells[i][j].contains(Level.CellFlag.MAPPED))
                    pm.drawPixel(i,h-j-1, Color.argb8888(0, 0, 0, .5f));
                if (level.fov[i][j])
                    pm.drawPixel(i,h-j-1, Color.argb8888(0, 0, 0, 0f));
            }
        }
        texture = new Texture(new PixmapTextureData(pm, pm.getFormat(), false, false));
        pm.dispose();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(texture);
//        texture.dispose();
        sprite.setBounds(0,0,w,h);
        return sprite;
    }
}
