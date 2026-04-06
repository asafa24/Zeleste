package com.iza.zeleste;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.HashSet;

public class Main extends Application {

    private HashSet<KeyCode> keys;
    private Player zadeline;
    private Level level;

    public static final double WIDTH = 800;
    public static final double HEIGHT = 600;




    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("ui.fxml"));
        AnchorPane uiPane = loader.load();

        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, uiPane);

        keys = new HashSet<>();
        zadeline = new Player(WIDTH/2, HEIGHT/2);
        level = new Level();


        AnimationTimer gameLoop = new AnimationTimer() {
            private long tempsPrecedent = 0;
            @Override
            public void handle(long now) {
                if (tempsPrecedent == 0){
                    tempsPrecedent = now;
                    return;
                }
                double dt = (now - tempsPrecedent) / 1000000000.0;
                update(dt);
                render(gc);
                tempsPrecedent = now;

            }
        };

        gameLoop.start();


        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed(event -> keys.add(event.getCode()));
        scene.setOnKeyReleased(event -> keys.remove(event.getCode()));


        stage.setTitle("Zeleste");
        stage.setScene(scene);
        stage.show();
    }

    public void update(double dt) {
        double accel = 1500;
        double friction = 1200;
        double max_speed = 250;
        double gravity = 981;

        double velX = zadeline.getVel().x;
        double velY = zadeline.getVel().y;

        double dashSpeed = 700;
        double dashDuration = 0.15;

        if ((keys.contains(KeyCode.SHIFT) || keys.contains(KeyCode.J)) && zadeline.canDash) {
            zadeline.isDashing = true;
            zadeline.canDash = false;
            zadeline.dashTimer = dashDuration;
            velX = (keys.contains(KeyCode.LEFT) || keys.contains(KeyCode.Q)) ? -dashSpeed : dashSpeed;
            velY = 0;
        }

        if (zadeline.isDashing) {
            zadeline.dashTimer -= dt;
            velY = 0;

            if (zadeline.dashTimer <= 0) {
                zadeline.isDashing = false;
                velX *= 0.5;
            }
        } else {
            if (zadeline.onGround) zadeline.canDash = true;

            if (keys.contains(KeyCode.RIGHT) || keys.contains(KeyCode.D)) {
                velX += accel * dt;
            } else if (keys.contains(KeyCode.LEFT) || keys.contains(KeyCode.Q)) {
                velX -= accel * dt;
            } else {
                if (velX > 0) velX = Math.max(0, velX - friction * dt);
                else if (velX < 0) velX = Math.min(0, velX + friction * dt);
            }

            velX = Math.max(-max_speed, Math.min(max_speed, velX));

            // Saut et Gravité
            velY += gravity * dt;

            if (zadeline.onGround && keys.contains(KeyCode.SPACE)) {
                zadeline.jump(75);
                velY = zadeline.getVel().y;
            }

            if (!keys.contains(KeyCode.SPACE) && velY < 0) {
                velY *= 0.5;
            }
        }

        zadeline.setVelX(velX);
        zadeline.setVelY(velY);

        // X
        zadeline.getPos().x += velX * dt;

        if (velX > 0) {
            if (level.isSolid(zadeline.getPos().x + zadeline.getWITDH(), zadeline.getPos().y + 2) ||
                    level.isSolid(zadeline.getPos().x + zadeline.getWITDH(), zadeline.getPos().y + zadeline.getHEIGHT() - 2)) {
                int tileX = (int) ((zadeline.getPos().x + zadeline.getWITDH()) / Level.TILE_SIZE);
                zadeline.getPos().x = (tileX * Level.TILE_SIZE) - zadeline.getWITDH();
                zadeline.setVelX(0);
            }
        } else if (velX < 0) {
            if (level.isSolid(zadeline.getPos().x, zadeline.getPos().y + 2) ||
                    level.isSolid(zadeline.getPos().x, zadeline.getPos().y + zadeline.getHEIGHT() - 2)) {
                int tileX = (int) (zadeline.getPos().x / Level.TILE_SIZE);
                zadeline.getPos().x = (tileX + 1) * Level.TILE_SIZE;
                zadeline.setVelX(0);
            }
        }

        // Y
        zadeline.getPos().y += velY * dt;

        if (velY > 0) { // Check Sol
            if (level.isSolid(zadeline.getPos().x + 2, zadeline.getPos().y + zadeline.getHEIGHT()) ||
                    level.isSolid(zadeline.getPos().x + zadeline.getWITDH() - 2, zadeline.getPos().y + zadeline.getHEIGHT())) {

                int tileY = (int) ((zadeline.getPos().y + zadeline.getHEIGHT()) / Level.TILE_SIZE);
                zadeline.getPos().y = (tileY * Level.TILE_SIZE) - zadeline.getHEIGHT();
                zadeline.setVelY(0);
                zadeline.onGround = true;
            } else {
                zadeline.onGround = false;
            }
        } else if (velY < 0) { // Chech Plafond
            if (level.isSolid(zadeline.getPos().x + 2, zadeline.getPos().y) ||
                    level.isSolid(zadeline.getPos().x + zadeline.getWITDH() - 2, zadeline.getPos().y)) {

                int tileY = (int) (zadeline.getPos().y / Level.TILE_SIZE);
                zadeline.getPos().y = (tileY + 1) * Level.TILE_SIZE;
                zadeline.setVelY(0);
            }
        }
        double marge = 4;
        if (level.isSpike(zadeline.getPos().x + marge, zadeline.getPos().y + marge) ||
            level.isSpike(zadeline.getPos().x + zadeline.getWITDH() - marge, zadeline.getPos().y + marge)) zadeline.die();
    }

    public void render(GraphicsContext gc){
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        level.draw(gc);
        zadeline.render(gc);

    }



    public static void main(String[] args) {
        launch();
    }




}
