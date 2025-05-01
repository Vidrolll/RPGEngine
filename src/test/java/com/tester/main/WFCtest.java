package com.tester.main;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL2;
import com.rpg.main.Game;
import com.rpg.main.math.noise.waveFunctionCollapse.WFC;
import com.rpg.main.math.noise.waveFunctionCollapse.WFC_Grid;
import com.rpg.main.math.noise.waveFunctionCollapse.WFC_Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class WFCtest extends Game {

    WFC_Tile[] tileSet = new WFC_Tile[6];
    WFC_Grid grid;
    Image[] tileSprites = new Image[3];

    public WFCtest(){

        // load images
        try {
            tileSprites[0] = ImageIO.read(new File("resources/textures/emptyTile.png"));
            tileSprites[1] = ImageIO.read(new File("resources/textures/testTile.png"));
            tileSprites[2] = ImageIO.read(new File("resources/textures/crossTIle.png"));
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

        // initialize tileset
        tileSet[0] = new WFC_Tile(true, new String[]{"0", "0", "0", "0"},0 , tileSprites[0]);
        tileSet[1] = new WFC_Tile(true, new String[]{"1", "1", "0", "1"},1, tileSprites[1]);
        tileSet[2] = tileSet[1].rotate(1, 2);
        tileSet[3] = tileSet[1].rotate(2, 3);
        tileSet[4] = tileSet[1].rotate(3, 4);
        tileSet[5] = new WFC_Tile(true, new String[]{"1","1","1","1"}, 5, tileSprites[2]);

        grid = new WFC_Grid(100,100,1, tileSet);
        WFC.calculateNewWFC(grid);

        long start = System.currentTimeMillis();
        grid = WFC.collapse(grid);
        long end = System.currentTimeMillis();

        System.out.println(Arrays.toString(grid.getIdGrid1D()));

        System.out.println("grid with "+grid.getSize()+" elements generated in " + (end-start) +"ms");

    }

    @Override
    public void draw(GL2 gl) {

    }


    @Override
    public void update() {

    }

    @Override
    public void input(KeyEvent e) {

    }

    @Override
    public void input(MouseEvent e) {

    }

    public static void main(String[] args){
        new WFCtest();
    }

}
