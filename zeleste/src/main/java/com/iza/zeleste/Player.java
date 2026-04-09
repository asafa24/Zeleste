package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;


public class Player {
    private Vector2D pos;
    private Vector2D vel;

    private static final double WIDTH = 18;
    private static final double HEIGHT = 24;
    private Color color;

    private PlayerState state = PlayerState.IDLE;
    private double animTimer = 0;
    private boolean facingRight = true;

    public boolean isDashing = false;
    public double dashTimer = 0;
    public boolean canDash = true;

    public boolean onGround = false;
    public boolean touchingwallLeft = false;
    public boolean touchingwallRight = false;

    private HashMap<PlayerState, Animation> animations = new HashMap<>();






    public Player(double x, double y){
        this.pos = new Vector2D(x, y);
        this.vel = new Vector2D(0, 0);
        this.color = Color.LIGHTSALMON;
    }

    public void render(GraphicsContext gc) {
        Animation currentAnim = animations.get(state);

        if (currentAnim != null) {
            currentAnim.draw(gc, pos.x, pos.y, animTimer, !facingRight);
        } else {
            gc.setFill(canDash ? Color.LIGHTSALMON : Color.LIGHTBLUE);
            gc.fillRect(pos.x, pos.y, WIDTH, HEIGHT);
        }
    }

    public void move(double t){
        this.pos.x += this.vel.x * t;
        this.pos.y += this.vel.y * t;
    }

    public void die(){
        pos.x = 36;
        pos.y = Main.HEIGHT-33;
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

    public void addAnimation(PlayerState state, Animation anim){
        animations.put(state, anim);
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

    public static double getWIDTH() {
        return WIDTH;
    }
    public static double getHEIGHT() {
        return HEIGHT;
    }

    public void setAnimTimer(double animTimer) {
        this.animTimer = animTimer;
    }

    public double getAnimTimer() {
        return animTimer;
    }

    public void setState(PlayerState state) {
        if(this.state != state) this.state = state;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
}
