package ru.yuriyyurinskiy.perceptron;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.yuriyyurinskiy.perceptron.Component.DrawPanel;
import ru.yuriyyurinskiy.perceptron.Entity.Point;

public class Main extends JFrame {
    private final int width = 800;
    private final int height = 800;

    private List<Point> points = new ArrayList<>();

    private JPanel mainPanel = new JPanel(),
            southPanel = new JPanel(),
            westPanel = new JPanel();
    private DrawPanel drawPanel = new DrawPanel();

    private static JTextArea logging = new JTextArea();
    private JTextArea inputData = new JTextArea();

    private JButton btnStart = new JButton("Начать обучение"),
            btnDrawPoint = new JButton("Построить точки"),
            btnClear = new JButton("Очистить всё"),
            btnClearDraw = new JButton("Очистить построения"),
            btnClearLog = new JButton("Отчистить лог");

    private int step = 1;
    private Perceptron perceptron;

    private Main() {
        super("Перцептрон");

        initVerticalSplit();
        initLogging();
        initMainLayout();
        initButton();

        inputData.setText("0 0 0\n1 1 1\n-1 1 2");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(width, height);
        setVisible(true);
    }

    private void initButton() {
        btnStart.setEnabled(false);
        btnClear.setEnabled(false);
        btnClearDraw.setEnabled(false);

        btnDrawPoint.addActionListener(e -> {
            if (inputData.getText().isEmpty()) {
                logLn("Ничего не введено");
                return;
            }

            // TO DO
            int number_line = 1;
            try {
                for (String line : inputData.getText().split("\\n")) {
                    String[] array = line.split(" ");
                    double x = Double.valueOf(array[0]);
                    double y = Double.valueOf(array[1]);
                    int type = Integer.valueOf(array[2]) % 3;

                    points.add(new Point(x, y, type));

                    number_line++;
                }

                drawPanel.addAllPoints(points);
                logLn("Точки построены");

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        perceptron = new Perceptron(3, points);
                    }
                });
                t.start();

                btnDrawPoint.setEnabled(false);

                btnStart.setEnabled(true);
                btnClearDraw.setEnabled(true);
            } catch (Exception ex) {
                logLn("Ошибка чтения данных в строке " + number_line);
            }
        });

        btnStart.addActionListener(e -> {
            // TO DO
            try {

                for (double[][] weight : perceptron.getHistory()) {

                    step++;
                }

                if (points.size() < 2)
                    throw new IOException("mes");
            } catch (Exception ex) {
                logLn("Ошибка на " + step + " шагу построения");
                step = 1;
                btnStart.setText("Начать обучение");
                btnStart.setEnabled(false);
            }

            btnStart.setText("Следующий шаг");
            btnClear.setEnabled(true);
        });

        btnClearDraw.addActionListener(e -> {
            btnClearDraw.setEnabled(false);
            btnStart.setText("Начать обучение");
            btnStart.setEnabled(false);

            // TO DO
            points.clear();
            drawPanel.clearPoint();

            logLn("Область построений очищена");
            btnDrawPoint.setEnabled(true);
        });

        btnClear.addActionListener(e -> {
            btnClear.setEnabled(false);
            btnClearDraw.setEnabled(false);

            inputData.setText("");
            logging.setText("");
            drawPanel.clearPoint();
            step = 1;

            btnStart.setText("Начать обучение");
            btnStart.setEnabled(true);
            btnDrawPoint.setEnabled(true);
        });

        btnClearLog.addActionListener(e -> {
            logging.setText("");
        });
    }

    private void initMainLayout() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(drawPanel, BorderLayout.CENTER);
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));

        westPanel.add(new JLabel("Введите паттерны"));
        westPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        westPanel.add(new JScrollPane(inputData));
        westPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        inputData.setColumns(10);

        southPanel.setLayout(new FlowLayout());
        southPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        southPanel.add(btnDrawPoint);
        southPanel.add(btnClearDraw);
        southPanel.add(btnStart);
        southPanel.add(btnClear);
        southPanel.add(btnClearLog);
    }

    private void initVerticalSplit() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(height - 350);
        splitPane.setTopComponent(mainPanel);
        splitPane.setBottomComponent(new JScrollPane(logging));
        getContentPane().add(splitPane);
    }

    private void initLogging() {
        logging.setEditable(false);
    }

    public static void main(String[] args) {
        new Main();
    }

    public static void logLn(String message) {
        if (logging != null) {
            logging.append(getTime());
            logging.append(" ---- ");
            logging.append(message);
            logging.append("\n");
        }
    }

    public static void log(String message) {
        if (logging != null) {
            logging.append(getTime());
            logging.append(" ---- ");
            logging.append(message);
        }
    }

    private static String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
