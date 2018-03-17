package com.radar;

import java.util.Arrays;
import java.util.Map;

public class RadarHeadfile {
    private Map<String,String> dimention;
    private Map<String,Variable4Json> variable;
    private Map<String,String> attribute;
    private double[][] bounds;
    private String time;
    private String name;

    public void setDimention(Map dimention) {
        this.dimention = dimention;
    }

    public void setVariable(Map variable) {
        this.variable = variable;
    }

    public void setAttribute(Map attribute) {
        this.attribute = attribute;
    }

    public Map<String, String> getDimention() {
        return dimention;
    }

    public Map<String, String> getAttribute() {
        return attribute;
    }

    public Map<String, Variable4Json> getVariable() {
        return variable;
    }

    public double[][] getBounds() {
        return bounds;
    }

    public void setBounds(double[][] bounds) {
        this.bounds = bounds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RadarHeadfile{" +
                "dimention=" + dimention +
                ", variable=" + variable +
                ", attribute=" + attribute +
                ", bounds=" + Arrays.toString(bounds) +
                ", time='" + time + '\'' +
                '}';
    }
}
