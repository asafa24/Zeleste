package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class DashGhost {
    private double x,y;
    private Player player;

    public DashGhost(Player player, double x, double y){
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public void render(GraphicsContext gc){
        gc.setFill(Color.ROYALBLUE);

    }
}
