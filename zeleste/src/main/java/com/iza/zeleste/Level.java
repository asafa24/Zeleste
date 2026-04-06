package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Level {
    public static final int TILE_SIZE = 32;

    private int[][] grille;
    private String[][] allRooms;

    public Level() {
        this.allRooms = new String[][]{{
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
                "1.222.111...............0",
                "1111111111111111111111111",
        }, {
                "1111111111111111111111111",
                "1.......................1",
                "1.......1111............1",
                "1.......................1",
                "1..121..................1",
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
                "1........1.1............1",
                "0.....111...............1",
                "1111111111111111111111111",
        }};
        loadRoom(0);
    }

    public void loadRoom(int id){
        String[] mapData = allRooms[id];
        grille = new int[mapData.length][mapData[0].length()];
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length(); x++) {
                if(mapData[y].charAt(x) == '0'){
                    grille[y][x] = 0;
                }
                if (mapData[y].charAt(x) == '1') {
                    grille[y][x] = 1;
                }
                if (mapData[y].charAt(x) == '2') {
                    grille[y][x] = 2;
                }
            }
        }

    }

    public void draw(GraphicsContext gc, int roomId, double offsetX) {
        // On récupère les données de la salle demandée (pas forcément la grille actuelle)
        String[] mapData = allRooms[roomId];

        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length(); x++) {

                double drawX = (x * TILE_SIZE) + offsetX;
                double drawY = y * TILE_SIZE;

                char tile = mapData[y].charAt(x);

                if (tile == '1') {
                    gc.setFill(Color.LIGHTSLATEGRAY);
                    gc.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
                else if (tile == '2') {
                    gc.setFill(Color.RED);
                    gc.fillPolygon(new double[]{drawX, drawX + TILE_SIZE/2, drawX + TILE_SIZE},
                            new double[]{drawY + TILE_SIZE, drawY, drawY + TILE_SIZE}, 3);
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
