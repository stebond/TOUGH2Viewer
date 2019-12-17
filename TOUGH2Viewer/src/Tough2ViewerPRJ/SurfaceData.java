/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.util.ArrayList;
import javax.vecmath.Color3f;

/**
 *
 * @author stefano.bondua
 */
public class SurfaceData {

    private Color3f shape_color3f;
    private boolean isSurfaceVisible;
    private boolean isPolyVisible;
    private boolean is_a_closed_surface;
    private String file_name;
    private int type;//0=raster;1=ply
    private GraphData graphData;
    private PLYParser plyData;
    private ArrayList PolyLine;
    private int i_ply;
    private int i_raster;

    SurfaceData() {
        //constructor

    }

    void set_shape_color3f(Color3f value) {
        shape_color3f = value;
    }

    Color3f get_shape_color3f() {
        return shape_color3f;
    }

    void set_isSurfaceVisible(boolean value) {
        isSurfaceVisible = value;
    }

    boolean isSurfaceVisible() {
        return isSurfaceVisible;
    }

    void set_isPolyVisible(boolean value) {
        isPolyVisible = value;
    }

    boolean isPolyVisible() {
        return isPolyVisible;
    }

    void set_is_a_closed_surface(boolean value) {
        is_a_closed_surface = value;
    }

    boolean is_a_closed_surface() {
        return is_a_closed_surface;
    }

    void set_file_name(String value) {
        file_name = value;
    }

    String get_file_name() {
        return file_name;
    }

    void set_type(int value) {
        type = value;
    }

    int get_type() {
        return type;
    }

    void set_GraphData(GraphData value) {
        graphData = value;
    }

    GraphData get_GraphData() {
        return graphData;
    }

    void set_PLYParser(PLYParser value) {
        plyData = value;
    }

    PLYParser get_PLYParser() {
        return plyData;
    }

    void set_PolyShape(ArrayList value) {
        PolyLine = value;
    }

    ArrayList get_PolyShape() {
        return PolyLine;
    }
}
