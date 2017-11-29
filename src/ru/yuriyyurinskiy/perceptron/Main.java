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
    private final int height = 600;

    private List<Point> points = new ArrayList<>();

    private JPanel mainPanel = new JPanel(),
            southPanel = new JPanel(),
            westPanel = new JPanel();
    private DrawPanel drawPanel = new DrawPanel();

    private JTextArea logging = new JTextArea(),
            inputData = new JTextArea();

    private int step = 1;


    private JButton btnStart = new JButton("Начать обучение"),
            btnDrawPoint = new JButton("Построить точки"),
            btnClear = new JButton("Очистить всё"),
            btnClearDraw = new JButton("Очистить построения");

    private Main() {
        super("Перцептрон");

        initVerticalSplit();
        initLogging();
        initMainLayout();
        initButton();

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
                log("Ничего не введено");
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

                    points.add(new Point(x,y, type));

                    number_line++;
                }

                drawPanel.addAllPoints(points);

                btnDrawPoint.setEnabled(false);

                log("Точки построены");

                btnStart.setEnabled(true);
                btnClearDraw.setEnabled(true);
            } catch (Exception ex) {
                log("Ошибка чтения данных в строке " + number_line);
            }
        });

        btnStart.addActionListener(e -> {
            // TO DO
            try {

                if (points.size() < 2)
                    throw new IOException("mes");

                log("Обучение шаг " + step);
                step++;
            } catch (Exception ex) {
                log("Ошибка на " + step + " шагу обучения");
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

            log("Область построений очищена");
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
    }

    private void initMainLayout() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(drawPanel, BorderLayout.CENTER);
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));

        westPanel.add(new JLabel("Введите паттерны"));
        westPanel.add(Box.createRigidArea(new Dimension(0,5)));
        westPanel.add(new JScrollPane(inputData));
        westPanel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        inputData.setColumns(10);

        southPanel.setLayout(new FlowLayout());
        southPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        southPanel.add(btnDrawPoint);
        southPanel.add(btnClearDraw);
        southPanel.add(btnStart);
        southPanel.add(btnClear);
    }

    private void initVerticalSplit() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(height - 150);
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

    private void log(String message) {
        logging.append(getTime());
        logging.append(" ---- ");
        logging.append(message);
        logging.append("\n");
    }

    private String getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
