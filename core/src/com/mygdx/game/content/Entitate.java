package com.mygdx.game.content;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class Entitate implements Disposable{
    public transient final  Vector2 poz;
    public transient Sprite sprite;
    public String id;


    protected Entitate() {
        this.poz = new Vector2();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public abstract void update(float delta);

    public abstract void render(float delta, SpriteBatch batch);

    @Override
    public void dispose() {
//        sprite.getTexture().dispose();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entitate entitate = (Entitate) o;

        return id.equals(entitate.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
