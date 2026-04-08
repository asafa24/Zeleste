// created by kev1jen1

package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class Strawberry extends Collectible{
    private Image strawberry;

    public Strawberry(double x, double y) {
        super(x, y);
        try{
            strawberry = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/strawberry.png")), 32, 32, true, true);
        } catch (NullPointerException e){
            System.err.println("Image pas chargé");
        }
    }

    public void applyEffect(Player p) {
        if (isTouched(p)){
            System.out.println("Fraise touchée");
        }
    }

    public void render(GraphicsContext gc) {
        if(strawberry != null) gc.drawImage(strawberry, getPos().x, getPos().y);
    }
}
