package ru.yuriyyurinskiy.perceptron;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {

    private Application() throws HeadlessException {
        super("Меню");

        JButton btnOpenDivide = new JButton("Персептрон разделения точек на плоскости");
        btnOpenDivide.addActionListener(e -> {
            new DividePointsOnPlate();
            this.dispose();
        });

        JButton btnOpenTransport = new JButton("Транспортная задача разделения точек на плоскости");
        btnOpenTransport.addActionListener(e -> {
            new TransportTaskOnPlate();
            this.dispose();
        });

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().add(btnOpenDivide);
        getContentPane().add(btnOpenTransport);

        setSize(400,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Application();
    }
}
