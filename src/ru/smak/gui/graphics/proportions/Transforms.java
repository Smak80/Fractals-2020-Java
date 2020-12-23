package ru.smak.gui.graphics.proportions;

import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.video.MediaTools;

import java.util.ArrayDeque;

public class Transforms{

    private static final ArrayDeque<CartesianScreenPlane> areas = new ArrayDeque<>();
    private static final CartesianScreenPlane base = new CartesianScreenPlane(0, 0, 0, 0, 0, 0);
    private static int areasLimit = 9;
    //private static ProportionsSaver saver;

    public static void setLimit(int lim){
        areasLimit = lim;
    }

    public static void addArea(CartesianScreenPlane plane){
        if(areas.size() == areasLimit)
            areas.removeFirst();
        areas.add(new CartesianScreenPlane(plane.getWidth(), plane.getHeight(), plane.xMin, plane.xMax, plane.yMin, plane.yMax));
    }

    public static void compose(CartesianScreenPlane plane, int width, int height){
        MediaTools.fillPlane(base, plane, width, height);
        /*saver = new ProportionsSaver(plane, plane.getWidth(), plane.getHeight());
        saver.setSaving(true);*/
    }

    public static CartesianScreenPlane executeLast(){
        if(areas.size() > 0){
            return areas.pollLast();
        }
        return null;
    }

    public static CartesianScreenPlane toHome(){
        return base;
    }
}