package com.rpg.main.level;

import com.rpg.main.entity.Entity;
import com.rpg.main.entity.entites.SimpleEntity;
import com.rpg.main.math.Polygon;
import com.rpg.main.math.vector.Vector2;
import com.rpg.main.player.PlayerController;
import com.rpg.main.tiles.Tile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class LevelLoader {
    private static final HashMap<String, Class<Tile>> tiles = new HashMap<>();

    static {
        tiles.put("rectangle", Tile.class);
    }

    /**
     * Returns a new Level object, which holds information for an
     * individual level that can then be rendered or updated.
     *
     * @param levelPath (String) The file location of the JSON file to load
     *                  a new level from.
     * @return (Level) The loaded in level from the JSON file.
     */
    public static Level loadLevel(String levelPath) {
        try {
            InputStream stream = LevelLoader.class.getClassLoader().getResourceAsStream("levels/" + levelPath + ".json");
            assert stream != null;
            JSONObject levelData = (JSONObject) new JSONParser().parse(new InputStreamReader(stream));
            PlayerController plrController = createController(levelData);
            Level level = new Level(plrController);
            JSONObject tileArray = (JSONObject) levelData.get("tiles");
            for (int i = 0; i < tileArray.size(); i++) {
                String tile = (String) tileArray.keySet().toArray()[i];
                level.map.put(tile, createTile((JSONArray) (tileArray.values().toArray()[i])));
            }
            return level;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that creates a new PlayerController.
     *
     * @param levelFile (JSONObject) The inputted JSON file to get player information from.
     * @return (PlayerController) The PlayerController this level will use.
     */
    private static PlayerController createController(JSONObject levelFile) {
        JSONArray plrData = (JSONArray) levelFile.get("player");
        Vector2[] plrHitbox = new Vector2[(plrData.size() - 2) / 2];
        for (int i = 0; i < plrHitbox.length; i++)
            plrHitbox[i] = new Vector2((long) (plrData.get((i * 2) + 2)), (long) (plrData.get((i * 2) + 3)));
        Entity plr = new SimpleEntity(new Vector2((long) plrData.get(0), (long) plrData.get(1)), new Polygon(new Vector2(), plrHitbox));
        return new PlayerController(plr);
    }

    /**
     * Helper method that creates a new Tile.
     *
     * @param polyData (JSONArray) The inputted JSON tile array to get polygon information from.
     * @return (Tile) The tile this level will use.
     */
    private static Tile createTile(JSONArray polyData) {
        Vector2[] polygon = new Vector2[(polyData.size() - 2) / 2];
        for (int i = 0; i < polygon.length; i++)
            polygon[i] = new Vector2((long) (polyData.get((i * 2) + 3)), (long) (polyData.get((i * 2) + 4)));
        Polygon hitbox = new Polygon(new Vector2((long) polyData.get(1), (long) polyData.get(2)), polygon);
        try {
            return (Tile) LevelLoader.class.getClassLoader()
                    .loadClass("com.rpg.main.tiles.tileTypes." + polyData.get(0))
                    .getConstructor(Polygon.class).newInstance(hitbox);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
