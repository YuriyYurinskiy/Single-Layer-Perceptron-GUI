package ru.yuriyyurinskiy.perceptron;

import java.util.ArrayList;
import java.util.List;

import ru.yuriyyurinskiy.perceptron.Entity.Point;

/**
 * 30.11.2017  9:48
 *
 * @author Yuriy Yurinskiy
 */
public class Perceptron {
    private int n;
    private List<Point> points;

    private double c;

    private double[][] weights;

    private List<double[][]> history = new ArrayList<>();

    public List<double[][]> getHistory() {
        return history;
    }

    public Perceptron(int n, List<Point> points, double c) {
        this.n = n;
        this.points = points;
        this.weights = new double[n][3];
        this.c = c;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.weights[i][j] = 0;

        Main.logLn("Перцептрон инициализирован");
        Main.logLn("Начинаем обучение");
        study();
    }

    private boolean changeWeights(Point point, double[] out) {
        boolean error = false;
        for (int i = 0; i < n; i++) {
            if (check(out, point.getType()))
                break;
            else if (i == point.getType()) {
                for (int j = 0; j < weights[i].length; j++)
                    if (j < 2)
                        weights[i][j] += point.get(j);
                    else
                        weights[i][j] += c;
                error = true;
            } else {
                for (int j = 0; j < weights[i].length; j++)
                    if (j < 2)
                        weights[i][j] -= point.get(j);
                    else
                        weights[i][j] -= c;
                error = true;
            }
        }
        double[][] temp = new double[n][3];
        for (int i=0; i<n; i++)
            System.arraycopy(weights[i], 0, temp[i], 0, 3);
        history.add(temp);
        return error;
    }

    private boolean check(double[] out, int type) {
        if (out[type] < 0) return false;
        for (int i = 0; i < n; i++) {
            if (i == type && out[type] >= 0);
            else if (out[i] >= 0) return false;
        }

        return true;
    }

    // Результат перемножения матрицы весов на образ
    private double[] getD(Point point, int step) {
        double[] out = new double[n];
        for (int i = 0; i < weights.length; i++) {
            out[i] = 0;
            for (int j = 0; j < weights[i].length; j++) {
                if (j < 2)
                    out[i] += point.get(j) * weights[i][j];
                else
                    out[i] += c * weights[i][j];
            }
        }

        log(out, "d(" + step + ") = \n");

        return out;
    }


    private void study() {
        boolean flag;
        int error = 0, step = 1;
        do {
            flag = true;
            for (Point point : points) {
                Main.logLn("Шаг обучения " + step);
                log(weights, "w(" + step + ")\n");
                log(point, "x(" + step + ")\n");
                if (changeWeights(point, getD(point, step))) {
                    Main.logLn("Имелись ошибки на шаге " + step + "\n");
                    error = 0;
                } else {
                    Main.logLn("Ошибок не было уже " + (error + 1) + " раз подряд\n");
                    error++;
                    if (error >= points.size()) {
                        flag = false;
                        break;
                    }
                }
                step++;
            }
        } while (flag && step < 100000);

        Main.logLn("Обучение закончено, спустя " + step + " шагов\n\n");
    }
    // Вывод в лог
    private void log(Point point, String startString) {
        StringBuilder stringBuilder = new StringBuilder(startString);
        stringBuilder.append("\t");
        stringBuilder.append(point.getX()).append("    ");
        stringBuilder.append(point.getY()).append("    ");
        stringBuilder.append(c);
        Main.logLn(stringBuilder.toString());
    }

    // Вывод в лог
    private void log(double[] out, String startString) {
        StringBuilder stringBuilder = new StringBuilder(startString);
        stringBuilder.append("\t");
        for (double item : out)
            stringBuilder.append(item).append("    ");
        Main.logLn(stringBuilder.toString());
    }

    // Вывод в лог
    private void log(double[][] weights, String startString) {
        StringBuilder stringBuilder = new StringBuilder(startString);
        for (double[] line : weights) {
            stringBuilder.append("\t");
            for (double item : line)
                stringBuilder.append(item).append("    ");
            stringBuilder.append("\n");
        }
        Main.log(stringBuilder.toString());
    }
}
