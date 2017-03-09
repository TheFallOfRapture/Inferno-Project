package com.inferno.util;

/**
 * Created by Fernando on 3/8/2017.
 */
public class BlendingUtils {
    // Cubic Bezier Curves
    // See: https://en.wikipedia.org/wiki/Bézier_curve
    public static float solveCubicBezier(float t, float a, float b, float c, float d) {
        return (a * (1-t) * (1-t) * (1-t)) + (b * 3 * (1-t) * (1-t) * t) + (c * 3 * (1 - t) * t * t) + (d * (t * t * t));
    }

    public static float easeInExpo(float t) {
        return solveCubicBezier(t, 0.95f, 0.05f, 0.795f, 0.035f);
    }

    public static float easeInQuartic(float t) {
        return solveCubicBezier(t, 0.755f, 0.05f, 0.855f, 0.06f);
    }

    public static float linear(float t) {
        return t;
    }
}
