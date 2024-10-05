package com.rpg.main.util;

import com.rpg.main.audio.Sound;
import com.rpg.main.opengl.Texture;

import java.io.File;
import java.util.HashMap;

public class Assets {
    private static HashMap<String,Texture> textures = new HashMap<String,Texture>();
    private static HashMap<String,Sound> sounds = new HashMap<String, Sound>();

    public static Texture getTexture(String path) {
        File file = new File(path);
        if(!textures.containsKey(file.getAbsolutePath()))
            textures.put(file.getAbsolutePath(),new Texture(file.getAbsolutePath()));
        return textures.get(file.getAbsolutePath());
    }
    public static Sound getSound(String path, boolean loops) {
        File file = new File(path);
        if(!sounds.containsKey(file.getAbsolutePath()))
            sounds.put(file.getAbsolutePath(),new Sound(file.getAbsolutePath(),loops));
        return sounds.get(file.getAbsolutePath());
    }
}
