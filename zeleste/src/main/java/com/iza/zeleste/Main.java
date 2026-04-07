package com.iza.zeleste;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main extends Application {

    private VBox menuNode;
    private boolean gameStarted = false;
    private VBox pauseNode;
    private boolean isPaused = false;


    private Image backgroundImage;
    private Font renogare;

    private HashSet<KeyCode> keys;
    private Player zadeline;
    private Level level;
    public static List<Collectible> collectibles;

    public static final double WIDTH = 800;
    public static final double HEIGHT = 600;

    private boolean spacePressedLastFrame = false;

    private int currentRoom;


    // Transitions
    private boolean isSliding = false;
    private double slideOffset = 0;
    private int nextRoomId = 0;
    private double slideDirection = 0;
    private final double SLIDE_SPEED = 1500;

    private int deathCount = 0;
    private double timer = 0;
    private static int score = 0;


    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("menu.fxml"));
        menuNode = loader.load();

        FXMLLoader pauseLoader = new FXMLLoader(Main.class.getResource("pause.fxml"));
        pauseNode = pauseLoader.load();
        pauseNode.setVisible(false);


        Controller controller = loader.getController();
        controller.setMainApp(this);

        Controller pauseController = pauseLoader.getController();
        pauseController.setMainApp(this);

        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, menuNode, pauseNode);

        keys = new HashSet<>();
        zadeline = new Player(10, HEIGHT-20);
        level = new Level();
        collectibles = new ArrayList<>();

        try {
            backgroundImage = new Image(Main.class.getResourceAsStream("images/background-zeleste.png"));
            renogare = Font.loadFont(Main.class.getResourceAsStream("fonts/Renogare-Regular.otf"), 24);
            if (renogare == null) renogare = Font.font("Arial", 24);
        } catch (NullPointerException e){
            System.err.println("Error loading");
        }
        System.out.println("Font : " + renogare.getFamily());

        AnimationTimer gameLoop = new AnimationTimer() {
            private long tempsPrecedent = 0;
            @Override
            public void handle(long now) {
                if (tempsPrecedent == 0){
                    tempsPrecedent = now;
                    return;
                }
                double dt = (now - tempsPrecedent) / 1000000000.0;
                if(gameStarted && !isPaused) update(dt);
                render(gc);
                tempsPrecedent = now;

            }
        };

        currentRoom = 0;
        level.loadRoom(currentRoom);


        gameLoop.start();


        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        scene.setOnKeyPressed(event -> keys.add(event.getCode()));
        scene.setOnKeyReleased(event -> keys.remove(event.getCode()));

        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                togglePause();
            }
            keys.add(e.getCode());
        });


        stage.setTitle("Zeleste");
        stage.setScene(scene);
        stage.show();
    }

    public void startGame(){
        menuNode.setVisible(false);
        gameStarted = true;
        zadeline.die();
        deathCount = 0;
        timer = 0;
    }

    public void togglePause(){
        if(!gameStarted) return;
        isPaused = !isPaused;
        pauseNode.setVisible(isPaused);
    }

    public void update(double dt) {

        if(isSliding){
            slideOffset += dt * SLIDE_SPEED;

            if(slideDirection == 1) zadeline.getPos().x -= dt * SLIDE_SPEED;
            else zadeline.getPos().x += dt * SLIDE_SPEED;



            if(slideOffset >= WIDTH) {
                currentRoom = nextRoomId;
                level.loadRoom(currentRoom);
                slideOffset = 0;
                isSliding = false;

                zadeline.getPos().x = (slideDirection == 1) ? 10 : WIDTH - zadeline.getWIDTH() - 10;
                zadeline.setVel(0, 0);
            }
            return;
        }

        if(!isSliding) timer+=dt;

        if(zadeline.getPos().x > WIDTH && currentRoom < level.getTotalRooms()-1){
            isSliding = true;
            nextRoomId = currentRoom + 1;
            slideDirection = 1;
            slideOffset = 0;
        }
        if(zadeline.getPos().x < -zadeline.getWIDTH() && currentRoom >= currentRoom - level.getTotalRooms()){
            isSliding = true;
            nextRoomId = currentRoom - 1;
            slideDirection = -1;
            slideOffset = 0;
        }


        double accel = 1500;
        double friction = 1200;
        double max_speed = 250;
        double gravity = 981;

        double velX = zadeline.getVel().x;
        double velY = zadeline.getVel().y;

        double dashSpeed = 700;
        double dashDuration = 0.10;

        boolean jumpJustPressed = keys.contains(KeyCode.SPACE) && !spacePressedLastFrame;



        if ((keys.contains(KeyCode.SHIFT) || keys.contains(KeyCode.J)) && zadeline.canDash) {
            zadeline.isDashing = true;
            zadeline.canDash = false;
            zadeline.dashTimer = dashDuration;

            double dirX = 0;
            double dirY = 0;

            if(keys.contains(KeyCode.RIGHT) || keys.contains(KeyCode.D)) dirX = 1;
            if(keys.contains(KeyCode.LEFT) || keys.contains(KeyCode.Q)) dirX = -1;
            if(keys.contains(KeyCode.UP) || keys.contains(KeyCode.Z)) dirY = -1;
            if(keys.contains(KeyCode.DOWN) || keys.contains(KeyCode.S)) dirY = 1;

            if (dirX == 0 && dirY == 0) dirX = (velX>= 0) ? 1 : -1;

            double length = Math.sqrt(dirX * dirX + dirY * dirY);
            velX = (dirX/length) * dashSpeed;
            velY = (dirY/length) * dashSpeed;

            zadeline.onGround = false;
        }

        if (zadeline.isDashing) {
            zadeline.dashTimer -= dt;
            //velY = 0;

            if (zadeline.dashTimer <= 0) {
                zadeline.isDashing = false;
                velX *= 0.5;
                velY *= 0.5;
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

            // Wall Jump
            if(!zadeline.onGround && jumpJustPressed && (zadeline.touchingwallLeft || zadeline.touchingwallRight)){
                velY = -350;
                velX = zadeline.touchingwallLeft ? 300 : -300;
            }
            //

            if (Math.abs(velX) > max_speed /*&& !zadeline.isDashing*/) {
                velX = Math.max(-max_speed, Math.min(max_speed, velX));
            }

            // Saut et Gravité
            velY += gravity * dt;

            if (zadeline.onGround && jumpJustPressed) {
                zadeline.jump(75);
                velY = zadeline.getVel().y;
            }
            if (!zadeline.onGround && (zadeline.touchingwallLeft || zadeline.touchingwallRight) && velY > 0){
                velY = 50;
            }

            if (!keys.contains(KeyCode.SPACE) && velY < 0) {
                velY *= 0.5;
            }

        }

        zadeline.setVelX(velX);
        zadeline.setVelY(velY);

        zadeline.touchingwallLeft = false;
        zadeline.touchingwallRight = false;

        // X
        zadeline.getPos().x += velX * dt;

        if (velX > 0) {
            if (level.isSolid(zadeline.getPos().x + zadeline.getWIDTH(), zadeline.getPos().y + 2) ||
                    level.isSolid(zadeline.getPos().x + zadeline.getWIDTH(), zadeline.getPos().y + zadeline.getHEIGHT() - 2)) {
                int tileX = (int) ((zadeline.getPos().x + zadeline.getWIDTH()) / Level.TILE_SIZE);
                zadeline.getPos().x = (tileX * Level.TILE_SIZE) - zadeline.getWIDTH();
                zadeline.touchingwallRight = true;
                zadeline.setVelX(0);
            }
        } else if (velX < 0) {
            if (level.isSolid(zadeline.getPos().x, zadeline.getPos().y + 2) ||
                    level.isSolid(zadeline.getPos().x, zadeline.getPos().y + zadeline.getHEIGHT() - 2)) {
                int tileX = (int) (zadeline.getPos().x / Level.TILE_SIZE);
                zadeline.getPos().x = (tileX + 1) * Level.TILE_SIZE;
                zadeline.touchingwallLeft = true;
                zadeline.setVelX(0);
            }
        }

        // Y
        zadeline.getPos().y += velY * dt;

        if (velY > 0) { // Check Sol
            if (level.isSolid(zadeline.getPos().x + 2, zadeline.getPos().y + zadeline.getHEIGHT()) ||
                    level.isSolid(zadeline.getPos().x + zadeline.getWIDTH() - 2, zadeline.getPos().y + zadeline.getHEIGHT())) {

                int tileY = (int) ((zadeline.getPos().y + zadeline.getHEIGHT()) / Level.TILE_SIZE);
                zadeline.getPos().y = (tileY * Level.TILE_SIZE) - zadeline.getHEIGHT();
                zadeline.setVelY(0);
                zadeline.onGround = true;
            } else {
                zadeline.onGround = false;
            }
        } else if (velY < 0) { // Check Plafond
            if (level.isSolid(zadeline.getPos().x + 2, zadeline.getPos().y) ||
                    level.isSolid(zadeline.getPos().x + zadeline.getWIDTH() - 2, zadeline.getPos().y)) {

                int tileY = (int) (zadeline.getPos().y / Level.TILE_SIZE);
                zadeline.getPos().y = (tileY + 1) * Level.TILE_SIZE;
                zadeline.setVelY(0);
            }
        }
        double marge = 4;
        if (level.isSpike(zadeline.getPos().x + marge, zadeline.getPos().y + marge) ||
            level.isSpike(zadeline.getPos().x + zadeline.getWIDTH() - marge, zadeline.getPos().y + marge)) {
            zadeline.die();
            deathCount++;
        }

        spacePressedLastFrame = keys.contains(KeyCode.SPACE);
    }

    public void render(GraphicsContext gc){
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        if(backgroundImage != null) gc.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT);

        if(!gameStarted) return;

        if(isSliding){
            if(slideDirection == 1){
                level.draw(gc, currentRoom, -slideOffset);
                level.draw(gc, nextRoomId, WIDTH - slideOffset);
            } else{
                level.draw(gc, currentRoom, slideOffset);
                level.draw(gc, nextRoomId, -WIDTH + slideOffset);
            }
            zadeline.render(gc);
        } else{
            level.draw(gc, currentRoom, 0);
            zadeline.render(gc);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(renogare);
        gc.fillText("Morts : " + deathCount, 20, 20);
        gc.setStroke(Color.BLACK);
        gc.strokeText("Morts : " + deathCount, 20, 20);

        gc.setFill(Color.WHITE);
        String timeString = String.format("%02d:%02d", (int) (timer / 60), (int) (timer % 60));
        gc.fillText("Temps : " + timeString, WIDTH - 200, 20);
        gc.setStroke(Color.BLACK);
        gc.strokeText("Temps : " + timeString, WIDTH - 200, 20);
    }



    public static void main(String[] args) {
        launch();
    }

}
