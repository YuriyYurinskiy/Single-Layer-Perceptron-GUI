package ru.yuriyyurinskiy.perceptron;

import ru.yuriyyurinskiy.perceptron.Component.DrawingJFrame;
import ru.yuriyyurinskiy.perceptron.Entity.Line;
import ru.yuriyyurinskiy.perceptron.Entity.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TransportTaskOnPlate extends DrawingJFrame {
    private Transport transport;
    private int m, n;
    private double[][] c;
    private double[] a, b;

    private JTextField inputA = new JTextField("");

    private JTextField inputB = new JTextField("");

    TransportTaskOnPlate() {
        super("Транспортная задача");
        inputData.setText("0 0 1\n" +
                "1 1 1\n" +
                "-1 1 1");
        inputA.setText("100 100 100");
        inputB.setText("100 100 100");
        init();
    }

    private void init() {
        westPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        westPanel.add(new JLabel("Введите массив A"));
        westPanel.add(inputA);
        inputA.setMaximumSize(new Dimension(400, 24));
        westPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        westPanel.add(new JLabel("Введите массив B"));
        westPanel.add(inputB);
        inputB.setMaximumSize(new Dimension(400, 24));

        btnStart.setVisible(false);
    }

    @Override
    protected ActionListener btnDrawPointListener() {
        return e -> {
            if (inputData.getText().isEmpty()) {
                logLn("Ничего не введено");
                return;
            }

            try {
                String[] lines = inputData.getText().split("\\n");
                m = lines.length;
                n = lines[0].trim().split("\\s+").length;

                c = new double[m][n];

                for (int i = 0; i < m; i++) {
                    String[] line = lines[i].trim().split("\\s+");
                    for (int j = 0; j < n; j++)
                        c[i][j] = Double.valueOf(line[j].trim());
                }

                if (n == 3)
                    for (int i = 0; i < n; i++) {
                        double x = c[i][0];
                        double y = c[i][1];
                        int type = i + 1;
                        points.add(new Point(x, y, type));
                    }

                a = new double[m];
                String[] line = inputA.getText().split("\\s+");
                for (int i = 0; i < m; i++) {
                    a[i] = Double.valueOf(line[i]);
                }

                b = new double[n];
                line = inputB.getText().split("\\s+");
                for (int i = 0; i < n; i++) {
                    b[i] = Double.valueOf(line[i]);
                }

                if (Arrays.stream(a).sum() != Arrays.stream(b).sum())
                    throw new Exception("Сумма элементов массива A не равна сумме элементов массива B");

                drawPanel.addAllPoints(points);
                logLn("Точки построены");

                Thread thread = threadCalculation();
                if (thread != null) {
                    thread.start();
                } else {
                    throw new Exception("Вычисление не определено");
                }

                btnDrawPoint.setEnabled(false);

                btnStart.setEnabled(true);
                btnClearDraw.setEnabled(true);
                btnStartToEnd.setEnabled(true);
            } catch (Exception ex) {
                logLn("Ошибка чтения данных: " + ex.getMessage());
            }
        };
    }

    @Override
    protected Thread threadCalculation() {
        return new Thread(() -> {
            if (c != null)
                transport = new Transport(c, a, b, m, n);
        });
    }

    @Override
    protected ActionListener btnStartListener() {
        return null;
    }

    @Override
    protected ActionListener btnStartToEndListener() {
        return e -> {
            try {
                drawPanel.clearLines();

                double[][] weight = transport.getZz();
                for (int i = 0; i < weight.length; i++) {
                    Line line = makeLine(weight[i], i + 1);
                    if (line != null)
                        drawPanel.addLine(line);
                }

                btnStart.setEnabled(false);
                btnStartToEnd.setEnabled(false);

            } catch (Exception ex) {
                logLn("Ошибка построения");
                btnStart.setEnabled(false);
            }
        };
    }
}
