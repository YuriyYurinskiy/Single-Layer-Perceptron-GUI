package ru.yuriyyurinskiy.perceptron.Entity;

/**
 * 30.11.2017  16:34
 *
 * @author Yuriy Yurinskiy
 */
public class Line {
    private double x_1;
    private double y_1;
    private double x_2;
    private double y_2;
    private int type;

    public Line(double x_1, double y_1, double x_2, double y_2, int type) {
        this.x_1 = x_1;
        this.y_1 = y_1;
        this.x_2 = x_2;
        this.y_2 = y_2;
        this.type = type;
    }

    public double getX_1() {
        return x_1;
    }

    public void setX_1(double x_1) {
        this.x_1 = x_1;
    }

    public double getY_1() {
        return y_1;
    }

    public void setY_1(double y_1) {
        this.y_1 = y_1;
    }

    public double getX_2() {
        return x_2;
    }

    public void setX_2(double x_2) {
        this.x_2 = x_2;
    }

    public double getY_2() {
        return y_2;
    }

    public void setY_2(double y_2) {
        this.y_2 = y_2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
