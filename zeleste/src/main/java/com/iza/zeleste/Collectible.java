package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;

public abstract class Collectible {
    private Vector2D pos;
    private final static double WIDTH = 16;

    public Collectible(double x, double y) {
        this.pos = new Vector2D(x, y);
    }

    public boolean isTouched(Player p){
        return this.pos.x < p.getPos().x + p.getWIDTH() &&
                this.pos.x + WIDTH > p.getPos().x &&
                this.pos.y < p.getPos().y + p.getWIDTH() &&
                this.pos.y + WIDTH > p.getPos().y;
    }

    public abstract void applyEffect(Player p);

    public abstract void render(GraphicsContext gc);

    public Vector2D getPos() {
        return pos;
    }

    public static double getWIDTH() {
        return WIDTH;
    }

}
