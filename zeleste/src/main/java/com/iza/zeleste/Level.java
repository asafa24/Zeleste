package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Level {
    public static final int TILE_SIZE = 32;

    private int[][] grille;


    public Level() {
        String[] mapData = {
                "1111111111111111111111111",
                "1.......................1",
                "1.......1111............1",
                "1.......................1",
                "1..121..........111.....1",
                "1...1......11...........1",
                "1......1112.............1",
                "1211......1.............1",
                "111...111...............1",
                "1.........1111..........1",
                "1..............1........1",
                "1.............1111......1",
                "1................11.....1",
                "1....................11.1",
                "1.................11....1",
                "1.............1111......1",
                "1.......1111............1",
                "1.222.111...............1",
                "1111111111111111111111111",
        };

        grille = new int[mapData.length][mapData[0].length()];
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length(); x++) {
                if (mapData[y].charAt(x) == '1') {
                    grille[y][x] = 1;
                }
                if (mapData[y].charAt(x) == '2') {
                    grille[y][x] = 2;
                }
            }
        }
    }

    public void draw(GraphicsContext gc) {
        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[y].length; x++) {
                if (grille[y][x] == 1) {
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                else if (grille[y][x] == 2) {
                    gc.setFill(Color.RED);
                    double xPos = x * TILE_SIZE;
                    double yPos = y * TILE_SIZE;
                    gc.fillPolygon(new double[]{xPos, xPos + TILE_SIZE/2, xPos + TILE_SIZE},
                            new double[]{yPos + TILE_SIZE, yPos, yPos + TILE_SIZE}, 3);
                }
            }
        }
    }

    public boolean isSpike(double x, double y){
        int tileX = (int) (x / TILE_SIZE);
        int tileY = (int) (y / TILE_SIZE);
        if(tileX < 0 || tileX >= grille[0].length || tileY < 0 || tileY >= grille.length) return false;
        return grille[tileY][tileX] == 2;
    }
    public boolean isSolid(double x, double y){
        int tileX = (int) (x / TILE_SIZE);
        int tileY = (int) (y / TILE_SIZE);

        if(tileX < 0 || tileX >= grille[0].length || tileY < 0 || tileY >= grille.length) return false;

        return grille[tileY][tileX] == 1;
    }

}
