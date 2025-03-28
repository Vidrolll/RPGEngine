package com.rpg.main.graphics.objects;

import com.rpg.main.math.vector.Vector4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Experimental code I wrote for some 3-D stuff, should be ignored for all intents and purposes.
 */
@Deprecated
public class OBJLoader {
    public static float[] loadOBJ(String filePath) {
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<float[]> tempVertices = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");

                if (parts.length == 0) continue;

                if (parts[0].equals("v")) { // Vertex data
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);
                    tempVertices.add(new float[]{x, y, z});
                } else if (parts[0].equals("f")) { // Face data
                    for (int i = 1; i < parts.length; i++) {
                        int index = Integer.parseInt(parts[i].split("/")[0]) - 1; // 1-based index
                        indices.add(index);
                    }
                }
            }

            // Flatten vertex list
            for (int index : indices) {
                float[] v = tempVertices.get(index);
                vertices.add(v[0]);
                vertices.add(v[1]);
                vertices.add(v[2]);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Convert List<Float> to float[]
        float[] array = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            array[i] = vertices.get(i);
        }
        return array;
    }

    public static List<Vector4> loadWireframeOBJ(String filePath) {
        List<Vector4> vertices = new ArrayList<>();
        Map<String, Boolean> edges = new HashMap<>();
        List<float[]> tempVertices = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");

                if (parts.length == 0) continue;

                if (parts[0].equals("v")) { // Vertex data
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);
                    tempVertices.add(new float[]{x, y, z});
                } else if (parts[0].equals("f")) { // Face data
                    int[] indices = new int[parts.length - 1];

                    for (int i = 1; i < parts.length; i++) {
                        indices[i - 1] = Integer.parseInt(parts[i].split("/")[0]) - 1;
                    }

                    // Add unique edges
                    for (int i = 0; i < indices.length; i++) {
                        int v1 = indices[i];
                        int v2 = indices[(i + 1) % indices.length];

                        String key = Math.min(v1, v2) + "_" + Math.max(v1, v2);
                        if (!edges.containsKey(key)) {
                            edges.put(key, true);
                            vertices.add(new Vector4(tempVertices.get(v1)[0], tempVertices.get(v1)[1], tempVertices.get(v1)[2], 1.0f));
                            vertices.add(new Vector4(tempVertices.get(v2)[0], tempVertices.get(v2)[1], tempVertices.get(v2)[2], 1.0f));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return vertices;
    }
}
