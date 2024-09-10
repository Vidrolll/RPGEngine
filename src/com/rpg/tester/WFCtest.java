package com.rpg.tester;

import com.rpg.main.Game;
import com.rpg.main.waveFunctionCollapse.WFC;
import com.rpg.main.waveFunctionCollapse.WFC_Grid;
import com.rpg.main.waveFunctionCollapse.WFC_Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WFCtest extends Game {

    WFC_Tile[] tileSet = new WFC_Tile[5];
    WFC_Grid grid;
    Image[] tileSprites = new Image[5];

    public WFCtest(){

        // load images
        try {
            tileSprites[0] = ImageIO.read(new File("resources/textures/emptyTile.png"));
            tileSprites[1] = ImageIO.read(new File("resources/textures/testTile.png"));
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

        // initialize tileset
        tileSet[0] = new WFC_Tile(true, new String[]{"0", "0", "0", "0"}, tileSprites[0]);
        tileSet[1] = new WFC_Tile(true, new String[]{"1", "1", "0", "1"}, tileSprites[1]);
        tileSet[2] = tileSet[1].rotate(1);
        tileSet[3] = tileSet[1].rotate(2);
        tileSet[4] = tileSet[1].rotate(3);

        grid = new WFC_Grid(2,2,1, tileSet);
        WFC.calculateNewWFC(grid);

        grid = WFC.collapseStep(grid);
        grid = WFC.collapseStep(grid);

    }

    @Override
    public void draw(Graphics2D g) {

        // draw grid
        WFC.draw(g, grid);

    }

    public static void main(String[] args){
        new WFCtest();
    }

}
