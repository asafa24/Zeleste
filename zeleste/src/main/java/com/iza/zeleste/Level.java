package com.iza.zeleste;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Level {
    public static final int TILE_SIZE = 32;

    private int[][] grille;
    private String[][] allRooms;
    private Image girder;

    public Level() {
        this.allRooms = new String[][]{{
                "1111111111111111111111111",
                "1.......7...............1",
                "1.......1111............0",
                "1...7...........7.......1",
                "1..121..........111.....1",
                "1...1......11...........1",
                "1......1112.............1",
                "1211......1.............1",
                "111...111..........7....1",
                "1.........1111..........1",
                "1..............1........1",
                "1......7......1111......1",
                "1................11.....1",
                "1.......................1",
                "1...........7.......22..1",
                "1.............1111..11..1",
                "1.......1111........11..1",
                "1...2.111......2222211..1",
                "1111111111111111111111111",
        }, {
                "1111111111111111111111111",
                "1..........7............1",
                "0.......1111............1",
                "1.......................1",
                "1..121..................1",
                "1...1......11......7....1",
                "1......1112.............1",
                "1211......1.............1",
                "111...111...............1",
                "1.........1111..........1",
                "1..............1........1",
                "1.........7...1111......1",
                "1................11.....1",
                "1....................11.1",
                "1...7.............11....1",
                "1.............1111......1",
                "1........1.1............1",
                "1.....111........22.....0",
                "1111111111111111111111111",
        }, {
                "1111111111111111111111111",
                "1.......................1",
                "1.......7..........7....1",
                "1.......2211......22....1",
                "1.................11....1",
                "1...7...........1.11....1",
                "1..2222...........11....1",
                "1...7.......1111..11....1",
                "1..1211...........11....1",
                "1..............12.11....1",
                "1...21112.........11....1",
                "1..........1121..721....1",
                "1...7.............11....1",
                "1..12121..........21....1",
                "1.................11....1",
                "1............111..21....1",
                "1....11111........11....1",
                "0...............27112.7.1",
                "1111111111111111111111111",
        }};
        loadRoom(0);
        try {
            this.girder = new Image(Main.class.getResourceAsStream("images/girder-tile.png"));
        } catch(NullPointerException e){
            e.printStackTrace();
        }
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
                if (mapData[y].charAt(x) == '7') {
                    grille[y][x] = 7;

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
                    if(girder == null){
                        gc.setFill(Color.LIGHTSLATEGRAY);
                        gc.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
                    } else{
                        gc.drawImage(girder, drawX, drawY, TILE_SIZE, TILE_SIZE);
                    }

                }
                else if (tile == '2') {
                    gc.setFill(Color.RED);
                    gc.fillPolygon(new double[]{drawX, drawX + TILE_SIZE/2, drawX + TILE_SIZE},
                            new double[]{drawY + TILE_SIZE, drawY, drawY + TILE_SIZE}, 3);
                    gc.setFill(Color.BLACK);
                    gc.setLineWidth(2);
                    gc.strokePolygon(new double[]{drawX, drawX + TILE_SIZE/2, drawX + TILE_SIZE},
                            new double[]{drawY + TILE_SIZE, drawY, drawY + TILE_SIZE}, 3);
                }
                else if(tile == '7'){
//                    Strawberry s = new Strawberry(drawX, drawY);
//                    Main.strawberries.add(s);
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

    public String[][] getAllRooms() {
        return allRooms;
    }


    public int getTotalRooms(){
        return allRooms.length;
    }
}
