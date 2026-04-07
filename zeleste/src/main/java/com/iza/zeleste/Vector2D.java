package com.iza.zeleste;

public class Vector2D{
    public double x, y;

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D v){
        this.x += v.x;
        this.y += v.y;
    }

    public void multiply(double scal){
        this.x *= scal;
        this.y *= scal;
    }

    public double magnitude(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public void normalize(){
        double mag = this.magnitude();
        if(mag != 0){
            this.x /= mag;
            this.y /= mag;
        }
    }
}
