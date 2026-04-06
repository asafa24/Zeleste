package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Player {
    private Vector2D pos;
    private Vector2D vel;
    private static final double WITDH = 16;
    private static final double HEIGHT = 16;
    public boolean onGround = false;

    public boolean isDashing = false;
    public double dashTimer = 0;
    public boolean canDash = true;

    private Color color;


    public Player(double x, double y){
        this.pos = new Vector2D(x, y);
        this.vel = new Vector2D(0, 0);
        this.color = Color.LIGHTSALMON;
    }

    public void render(GraphicsContext gc){
        if(!canDash) this.color = Color.LIGHTBLUE;
        else this.color = Color.LIGHTSALMON;
        gc.setFill(this.color);
        gc.fillRect(this.pos.x, this.pos.y, WITDH, HEIGHT);
    }

    public void move(double t){
        this.pos.x += this.vel.x * t;
        this.pos.y += this.vel.y * t;
    }

    public void die(){
        pos.x = Main.WIDTH/2;
        pos.y = Main.HEIGHT/2;
        vel.x = 0;
        vel.y = 0;
        canDash = true;
    }

    public void jump(double force){
        if(onGround){
            this.vel.y = -Math.sqrt(2 * 981 * force);
            onGround = false;
        }
    }

    public void setVel(Vector2D vel) {
        this.vel = vel;
    }
    public void setVel(double x, double y){
        this.vel.x = x;
        this.vel.y = y;
    }
    public void setVelX(double x) {
        this.vel.x = x;
    }

    public void setVelY(double y) {
        this.vel.y = y;
    }

    public Vector2D getPos() {
        return pos;
    }
    public void setPosX(double x){
        this.pos.x = x;
    }


    public Vector2D getVel() {
        return vel;
    }

    public static double getWITDH() {
        return WITDH;
    }
    public static double getHEIGHT() {
        return HEIGHT;
    }
}
