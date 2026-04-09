// created by kev1jen1

package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Strawberry extends Collectible{
    private List<Image> frames;
    private double frameDuration = 0.1;

    public Strawberry(List<Image> strawberries, double x, double y) {
        super(x, y);
        this.frames = strawberries;
    }
    public Strawberry(double x, double y) {
        super(x, y);
    }

    @Override

    public void applyEffect(Player p) {
//        if (isTouched(p)){
//            System.out.println("Fraise touchée");
//        }
    }

    public void render(GraphicsContext gc, double timer) {
        if(frames == null || frames.isEmpty()) return;
        int frameIndex = (int) (timer/frameDuration) % frames.size();
        Image currentFrame = frames.get(frameIndex);
        double bounce = Math.sin(timer*3)*3;
        gc.drawImage(currentFrame, getPos().x, getPos().y + bounce);
        // if(strawberry != null) gc.drawImage(strawberry, getPos().x, getPos().y);
    }
}
