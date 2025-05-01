package com.rpg.main.util;

import com.rpg.main.audio.Sound;
import com.rpg.main.graphics.Texture;

import java.util.HashMap;

public class Assets {
    //HashMap of all textures in the asset pool.
    private static final HashMap<String,Texture> textures = new HashMap<>();
    //HashMap of all sounds in the asset pool.
    private static final HashMap<String,Sound> sounds = new HashMap<>();

    //Initializes all assets into the asset pool. Automatically ran the first time the Assets class is used.
    static {
        //TEXTURES
        addTexture("crossTile", "textures/crossTile.png");
        addTexture("emptyTile", "textures/emptyTile.png");
        addTexture("testTile", "textures/testTile.png");

        //SOUNDS
        addSound("entrance1", "sounds/entrance.ogg", true);
        addSound("entrance2", "sounds/entrance2.ogg", true);
        addSound("entrance_code", "sounds/entrance_code.ogg", true);
    }

    /**
     * Returns an OpenGL texture from the asset pool.
     * @param texture (String) The texture to grab.
     * @return (Texture) An OpenGL texture.
     */
    public static Texture getTexture(String texture) {
        return textures.get(texture);
    }

    /**
     * Creates a new OpenGL texture to add to the asset pool.
     * @param texture (String) The name to list the asset under.
     * @param path (String) The path of the image to supply into the texture.
     */
    public static void addTexture(String texture, String path) {
        textures.put(texture,new Texture(path));
    }

    /**
     * Returns a sound from the asset pool.
     * @param sound (String) The name of the sound to grab.
     * @return (Sound) The returned sound from the asset pool.
     */
    public static Sound getSound(String sound) {
        return sounds.get(sound);
    }

    /**
     * Adds a new sound to the asset pool.
     * @param sound (String) The name to list the sound under.
     * @param path (String) The path in the files to use.
     * @param loops (Boolean) Whether the sound should loop.
     */
    public static void addSound(String sound, String path, boolean loops) {
        sounds.put(sound,new Sound(path,loops));
    }
}