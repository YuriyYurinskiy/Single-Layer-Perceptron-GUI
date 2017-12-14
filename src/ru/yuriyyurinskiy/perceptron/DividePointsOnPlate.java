package ru.yuriyyurinskiy.perceptron;

import ru.yuriyyurinskiy.perceptron.Component.DrawingJFrame;
import ru.yuriyyurinskiy.perceptron.Entity.Line;
import ru.yuriyyurinskiy.perceptron.Entity.Point;

import java.awt.event.ActionListener;

public class DividePointsOnPlate extends DrawingJFrame {

    private Perceptron perceptron;

    @Override
    protected Thread threadCalculation() {
        return new Thread(() -> perceptron = new Perceptron(3, points, Double.valueOf(inputC.getText())));
    }

    @Override
    protected ActionListener btnStartListener() {
        return (e -> {
            // TO DO
            try {
                drawPanel.clearLines();
                logLn("Построена матрицы сформированная на шаге " + step + " обучения");
                double[][] weight = perceptron.getHistory().get(step - 1);
                for (int i = 0; i < weight.length; i++) {
                    Line line = makeLine(weight[i], i);
                    if (line != null)
                        drawPanel.addLine(line);
                }

                if (step >= perceptron.getHistory().size() - points.size()) {
                    logLn("Положение прямых больше не менялось (" + step + " - " + (step + points.size()) + ")");
                    btnStart.setText("Начать обучение");
                    btnStart.setEnabled(false);
                }

                step++;
            } catch (Exception ex) {
                logLn("Ошибка на " + step + " шагу построения");
                step = 1;
                btnStart.setText("Начать обучение");
                btnStart.setEnabled(false);
            }

            btnStart.setText("Следующий шаг");
            btnClear.setEnabled(true);
        });
    }

    @Override
    protected ActionListener btnStartToEndListener() {
        return (e -> new Thread(() -> {
            btnStart.setEnabled(false);
            btnDrawPoint.setEnabled(false);
            btnClearLog.setEnabled(false);
            btnClear.setEnabled(false);
            btnStartToEnd.setEnabled(false);
            btnClearDraw.setEnabled(false);

            for (int k = 0; k < perceptron.getHistory().size() - points.size(); k++) {
                logLn("Построена матрицы сформированная на шаге " + (k+1) + " обучения");
                drawPanel.clearLines();

                double[][] weight = perceptron.getHistory().get(k);
                for (int i = 0; i < weight.length; i++) {
                    Line line = makeLine(weight[i], i);
                    if (line != null)
                        drawPanel.addLine(line);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            logLn("Положение прямых больше не менялось (" +
                    (perceptron.getHistory().size() - points.size()) +
                    " - " +
                    (perceptron.getHistory().size() - points.size() + points.size()) +
                    ")"
            );

            btnClear.setEnabled(true);
            btnClearDraw.setEnabled(true);
            btnClearLog.setEnabled(true);
        }).start());
    }

    DividePointsOnPlate() {
        super("Перцептрон");
    }
}
