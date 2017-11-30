package ru.yuriyyurinskiy.perceptron.Entity;

/**
 * 29.11.2017  19:55
 *
 * @author Yuriy Yurinskiy
 */
public class Point {
    private double x;
    private double y;
    private int type;

    public Point(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public double get(int i) {
        if (i==0)
            return x;
        if (i==1)
            return y;
        else
            return 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
