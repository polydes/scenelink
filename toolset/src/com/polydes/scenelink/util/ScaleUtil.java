package com.polydes.scenelink.util;

import java.awt.*;

public class ScaleUtil
{
    public static Rectangle scale(Rectangle o, float scale)
    {
        o = o.getBounds();
        o.x = (int) (o.x * scale);
        o.y = (int) (o.y * scale);
        o.width = (int) (o.width * scale);
        o.height = (int) (o.height * scale);
        return o;
    }

    public static Rectangle unscale(Rectangle o, float scale)
    {
        o = o.getBounds();
        o.x = (int) (o.x / scale);
        o.y = (int) (o.y / scale);
        o.width = (int) (o.width / scale);
        o.height = (int) (o.height / scale);
        return o;
    }

    public static Dimension scale(Dimension o, float scale)
    {
        o = o.getSize();
        o.width = (int) (o.width * scale);
        o.height = (int) (o.height * scale);
        return o;
    }

    public static Dimension unscale(Dimension o, float scale)
    {
        o = o.getSize();
        o.width = (int) (o.width / scale);
        o.height = (int) (o.height / scale);
        return o;
    }

    public static Point scale(Point o, float scale)
    {
        o = o.getLocation();
        o.x = (int) (o.x * scale);
        o.y = (int) (o.y * scale);
        return o;
    }

    public static Point unscale(Point o, float scale)
    {
        o = o.getLocation();
        o.x = (int) (o.x / scale);
        o.y = (int) (o.y / scale);
        return o;
    }
}
