package dev.swirlingskies.util;

import org.joml.Vector2f;

public final class Collision2D {
    private Collision2D() {}

    // ---------- Basic overlap -------`---
    public static boolean intersects(Vector2f posA, Vector2f sizeA,
                                     Vector2f posB, Vector2f sizeB) {
        return posA.x < posB.x + sizeB.x &&
                posA.x + sizeA.x > posB.x &&
                posA.y < posB.y + sizeB.y &&
                posA.y + sizeA.y > posB.y;
    }

    /**
     * Minimum Translation Vector (MTV) to separate overlapping AABBs.
     * Returns vector to add to A's position to push it out of B.
     * If not overlapping, returns (0,0).
     */
    public static Vector2f overlapVector(Vector2f posA, Vector2f sizeA,
                                         Vector2f posB, Vector2f sizeB,
                                         Vector2f out) {
        float aMinX = posA.x,          aMaxX = posA.x + sizeA.x;
        float aMinY = posA.y,          aMaxY = posA.y + sizeA.y;
        float bMinX = posB.x,          bMaxX = posB.x + sizeB.x;
        float bMinY = posB.y,          bMaxY = posB.y + sizeB.y;

        float dx1 = bMaxX - aMinX; // push A left  (+)
        float dx2 = aMaxX - bMinX; // push A right (+)
        float dy1 = bMaxY - aMinY; // push A down  (+)
        float dy2 = aMaxY - bMinY; // push A up    (+)

        if (dx1 <= 0 || dx2 <= 0 || dy1 <= 0 || dy2 <= 0) {
            return out.set(0f, 0f); // no overlap
        }

        float mx = (dx1 < dx2) ? +dx1 : -dx2; // choose smaller axis
        float my = (dy1 < dy2) ? +dy1 : -dy2;

        if (Math.abs(mx) < Math.abs(my)) return out.set(mx, 0f);
        else                              return out.set(0f, my);
    }

    /**
     * Swept AABB (continuous collision): A moves by vel (for THIS frame), B static.
     * Returns time-of-impact t in [0,1] (or 1 if no hit). 'normalOut' is the collision normal at impact.
     * NOTE: velA is the frame displacement (vel*dt), not per-second velocity.
     */
    public static float sweptAABB(Vector2f posA, Vector2f sizeA,
                                  Vector2f velA,  // displacement this frame
                                  Vector2f posB, Vector2f sizeB,
                                  Vector2f normalOut) {

        // Minkowski: expand B by A's size, cast A's TOP-LEFT point against it.
        float bMinX = posB.x - sizeA.x;
        float bMinY = posB.y - sizeA.y;
        float bMaxX = posB.x + sizeB.x;
        float bMaxY = posB.y + sizeB.y;

        float px = posA.x;
        float py = posA.y;

        float invDx = (velA.x != 0f) ? 1f / velA.x : Float.POSITIVE_INFINITY;
        float invDy = (velA.y != 0f) ? 1f / velA.y : Float.POSITIVE_INFINITY;

        float tNearX = (bMinX - px) * invDx;
        float tFarX  = (bMaxX - px) * invDx;
        if (tNearX > tFarX) { float t = tNearX; tNearX = tFarX; tFarX = t; }

        float tNearY = (bMinY - py) * invDy;
        float tFarY  = (bMaxY - py) * invDy;
        if (tNearY > tFarY) { float t = tNearY; tNearY = tFarY; tFarY = t; }

        float tEnter = Math.max(tNearX, tNearY);
        float tExit  = Math.min(tFarX,  tFarY);

        if (tEnter > tExit || (tFarX < 0f && tFarY < 0f) || tEnter > 1f) {
            normalOut.set(0f, 0f);
            return 1f; // no hit
        }

        // Which axis hit first?
        if (tNearX > tNearY) {
            normalOut.set((velA.x > 0f) ? -1f : 1f, 0f);
        } else {
            normalOut.set(0f, (velA.y > 0f) ? -1f : 1f);
        }
        return Math.max(0f, tEnter);
    }
}
