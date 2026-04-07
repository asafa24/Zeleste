package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Strawberry extends Collectible{
    private Image strawberry;

    public Strawberry(double x, double y) {
        super(x, y);
        try{
            strawberry = new Image(getClass().getResourceAsStream("/images/strawberry.png"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void applyEffect(Player p) {

    }

    public void render(GraphicsContext gc) {
        if(strawberry != null) gc.drawImage(strawberry, getPos().x, getPos().y);
    }
}
