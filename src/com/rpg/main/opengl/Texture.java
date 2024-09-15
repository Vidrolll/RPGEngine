package com.rpg.main.opengl;

import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Texture {
    //Texture variables
    private com.jogamp.opengl.util.texture.Texture texture;
    private BufferedImage image;

    /**
     * Creates a new image for a texture object.
     * @param path (String) The path of the image.
     */
    public Texture(String path) {
        try {
            image = ImageIO.read(Objects.requireNonNull(Texture.class.getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        image.flush();
    }

    /**
     * Creates a texture object off of the class image object.
     * @return (com.jogamp.opengl.util.texture.Texture) An OpenGL texture object.
     */
    public com.jogamp.opengl.util.texture.Texture getTexture() {
        if(texture==null) {
            texture = AWTTextureIO.newTexture(Renderer.getProfile(),image,true);
        }
        return texture;
    }
}
