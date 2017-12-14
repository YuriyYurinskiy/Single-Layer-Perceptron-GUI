package ru.yuriyyurinskiy.perceptron;

import java.util.Arrays;
import java.util.HashMap;

public class Transport {
    private final String XXX = "xxx";
    private final String BAZ = "baz";
    private final boolean MIN = true;
    private final boolean MAX = false;

    private int m; // число строк в матрице c - число образов
    private int n; // число столбцов в матрице с - число признаков

    private int k; // число базовых элементов

    private double[][] c;

    private double[] a;
    private double[] b;

    private double[][] maximize, minimize, zz;

    Transport(double[][] c, double[] a, double[] b, int m, int n) {
        this.c = c;
        this.m = m;
        this.n = n;
        this.k = m + n - 1;
        this.a = a;
        this.b = b;

        init();
    }

    private void init() {
        HashMap<String, double[][]> dataNorthWest = solveTransportTask(c, a, b, MIN);

        double[][] baz = dataNorthWest.get(BAZ);
        minimize = dataNorthWest.get(XXX);

        log(baz, "Базис");
        log(minimize, "Свешение");
        TransportTaskOnPlate.logLn("Ответ");

        TransportTaskOnPlate.logLn("Минимум " + solve(minimize, c));

        dataNorthWest = solveTransportTask(c, a, b, MAX);

        baz = dataNorthWest.get(BAZ);
        maximize = dataNorthWest.get(XXX);

        log(baz, "Базис");
        log(maximize, "Свешение");
        TransportTaskOnPlate.logLn("Ответ");

        TransportTaskOnPlate.logLn("Максимум " + solve(maximize, c));

//        log(subMatrix(minimize, maximize), "minimize - maximize");
//        log(tMatrix(subMatrix(minimize, maximize)), "(minimize - maximize)T");
//        log(multiTo(c, tMatrix(subMatrix(minimize, maximize))), "C*(minimize - maximize)T");
        zz = subMatrix(minimize, maximize);
    }

    private double[][] addMatrix(double[][] minimize, double[][] maximize) {
        double[][] zz = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                zz[i][j] = minimize[i][j] + maximize[i][j];
        return zz;
    }

    private double[][] subMatrix(double[][] minimize, double[][] maximize) {
        double[][] zz = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                zz[i][j] = minimize[i][j] - maximize[i][j];
        return zz;
    }

    private double[][] tMatrix(double[][] z) {
        double[][] zz = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                zz[i][j] = z[j][i];
        return zz;
    }

    public double[][] multiTo(double[][] a, double[][] b) {
        double[][] z = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++) {
                z[i][j] = 0;
                for (int k = 0; k < a.length; k++)
                    z[i][j] += a[i][k] * b[k][j];
            }
        return z;
    }

    private double[][] divMatrix(double[][] z, double a) {
        double[][] zz = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                zz[i][j] = (double) z[i][j] / (double) a;
        return zz;
    }

    private double solve(double[][] xxx, double[][] c) {
        double result = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result += c[i][j] * xxx[i][j];
            }
        }

        return result;
    }

    private HashMap<String, double[][]> solveTransportTask(double[][] c, double[] a, double[] b, boolean min) {
        HashMap<String, double[][]> ss = northWest(c, a, b);
        double[][] x = ss.get(XXX);
        double[][] baz = ss.get(BAZ);

        double[] pot = potential(c, baz);
        double[][] new1 = newyazki(c, pot);
        int[] nw;
        if (min) {
            nw = newPlusMax(new1);
        } else {
            nw = newPlusMin(new1);
        }
        double fff = nw[0];
        int k = 0;

        while (fff == 1) {
            double[][] baz1 = wich(nw[1], nw[2], baz);
            int[] cont = cotnur(nw[1], nw[2], x, baz1);
            ss = changePotential(nw[1], nw[2], cont, c, x, baz);
            x = ss.get(XXX);
            baz = ss.get(BAZ);
            pot = potential(c, baz);
            new1 = newyazki(c, pot);
            if (min) {
                nw = newPlusMax(new1);
            } else {
                nw = newPlusMin(new1);
            }
            fff = nw[0];
            k++;
        }

        return ss;
    }

    // Вывод в лог
    @SuppressWarnings("Duplicates")
    private void log(double[][] weights, String startString) {
        StringBuilder stringBuilder = new StringBuilder(startString);
        stringBuilder.append("\n");
        for (double[] line : weights) {
            stringBuilder.append("\t");
            for (double item : line)
                stringBuilder.append(item).append("    ");
            stringBuilder.append("\n");
        }
        TransportTaskOnPlate.log(stringBuilder.toString());
    }

    // Вывод в лог
    @SuppressWarnings("Duplicates")
    private void log(double[] weights, String startString) {
        StringBuilder stringBuilder = new StringBuilder(startString);
        stringBuilder.append("\n");
        for (double item : weights)
            stringBuilder.append(item).append("    ");
        stringBuilder.append("\n");

        TransportTaskOnPlate.log(stringBuilder.toString());
    }

    // Вывод в лог
    private void log(int[] weights, String startString) {
        StringBuilder stringBuilder = new StringBuilder(startString);
        stringBuilder.append("\n");
        for (int item : weights)
            stringBuilder.append(item).append("    ");
        stringBuilder.append("\n");

        TransportTaskOnPlate.log(stringBuilder.toString());
    }

    // Метод северо-западного пути
    private HashMap<String, double[][]> northWest(double[][] c, double[] a, double[] b) {
        double[] aa = Arrays.copyOf(a, a.length);
        double[] bb = Arrays.copyOf(b, b.length);

        double[][] xxx = new double[m][n];
        double[][] baz = new double[m][n];

        int f = 0, i = 0, j = 0;

        while (f == 0) {
            xxx[i][j] = Math.min(aa[i], bb[j]);
            aa[i] -= xxx[i][j];
            bb[j] -= xxx[i][j];
            baz[i][j] = 1;
            int ff = 0;
            if (aa[i] < 0.1 && bb[j] > 0.1)
                ff = 1;
            if (aa[i] > 0.1 && bb[j] < 0.1)
                ff = 2;
            if (aa[i] < 0.1 && bb[j] < 0.1)
                ff = 3;

            if (ff == 1)
                i++;
            if (ff == 2)
                j++;
            if (ff == 3) {
                if (i < m - 1)
                    baz[i + 1][j] = 1;
                i++;
                j++;
            }

            if (i > m - 1 || j > n - 1)
                f = 1;
        }

        HashMap<String, double[][]> map = new HashMap<>();
        map.put(XXX, xxx);
        map.put(BAZ, baz);
        return map;
    }

    @SuppressWarnings("Duplicates")
    private double[][] wich(int x, int y, double[][] baz) {
        double[][] baz1 = new double[m][];
        for (int i = 0; i < m; i++)
            baz1[i] = Arrays.copyOf(baz[i], baz[i].length);

        baz1[x][y] = 1;

        int flag = 1;

        while (flag == 1) {
            flag = 0;
            for (int i = 0; i < m; i++) {
                int s = 0;
                int jj = -1;
                for (int j = 0; j < n; j++) {
                    if (baz1[i][j] > 0) {
                        s++;
                        jj = j;
                    }
                }
                if (s == 1) {
                    flag = 1;
                    baz1[i][jj] = 0;
                }
            }
            for (int j = 0; j < n; j++) {
                int s = 0;
                int ii = -1;
                for (int i = 0; i < m; i++) {
                    if (baz1[i][j] > 0) {
                        s++;
                        ii = i;
                    }
                }
                if (s == 1) {
                    flag = 1;
                    baz1[ii][j] = 0;
                }
            }
        }

        return baz1;
    }

    private boolean zeroWich(double[][] baz) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (baz[i][j] > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private double[] potential(double[][] c, double[][] baz) {
        double[] pot = new double[m + n];
        for (int i = 0; i < m + n; i++)
            pot[i] = 1000;

        pot[0] = 0;

        boolean flag = true;

        while (flag) {
            flag = false;
            for (int i = 0; i < m; i++) {
                if (pot[i] < 1000) {
                    for (int j = 0; j < n; j++) {
                        if (baz[i][j] == 1) {
                            pot[m + j] = c[i][j] - pot[i];
                        }
                    }
                }
                if (pot[i] == 1000) {
                    flag = true;
                }
            }
            flag = false;
            for (int j = 0; j < n; j++) {
                if (pot[m + j] < 1000) {
                    for (int i = 0; i < m; i++) {
                        if (baz[i][j] == 1) {
                            pot[i] = c[i][j] - pot[m + j];
                        }
                    }
                }
                if (pot[m + j] == 1000) {
                    flag = true;
                }
            }
        }

        return pot;
    }

    private double[][] newyazki(double[][] c, double[] pot) {
        double[][] newyazki = new double[m][];
        for (int i = 0; i < m; i++)
            newyazki[i] = Arrays.copyOf(c[i], c[i].length);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                newyazki[i][j] = pot[i] + pot[m + j] - c[i][j];
            }
        }

        return newyazki;
    }

    private int[] newPlusMax(double[][] newyazki) {
        int[] nw = new int[3];
        double nn = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (newyazki[i][j] > nn) {
                    nw[1] = i;
                    nw[2] = j;
                    nn = newyazki[i][j];
                    nw[0] = 1;
                }
            }
        }

        return nw;
    }

    private int[] newPlusMin(double[][] newyazki) {
        int[] nw = new int[3];
        double nn = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (newyazki[i][j] < nn) {
                    nw[1] = i;
                    nw[2] = j;
                    nn = newyazki[i][j];
                    nw[0] = 1;
                }
            }
        }

        return nw;
    }

    private int[] cotnur(int ii, int jj, double[][] x, double[][] baz) {
        int[] cont = new int[n + m + 3 + 1];
        cont[0] = ii;
        cont[1] = jj;
        int it = ii;
        int jt = jj;

        double[][] baz1 = new double[m][];
        for (int i = 0; i < m; i++)
            baz1[i] = Arrays.copyOf(baz[i], baz[i].length);

        int u = 0;
        int k = 1;

        cont[n + m + 2] = 1000;
        while (u == 0) {
            k++;
            int i = 0;
            cont[n + m + 3] = k / 2;
            int f1 = 0;
            int ff = 0;

            while (f1 == 0) {
                if (baz1[i][jt] == 1 && k == 2 && i == it) {
                    ff = 1;
                }
                if (baz1[i][jt] == 1 && ff == 0) {
                    it = i;
                    f1 = 1;
                }
                ff = 0;
                i++;
            }

            baz1[it][jt] = 0;
            cont[k] = it;
            k++;

            if (x[it][jt] < cont[n + m + 2])
                cont[n + m + 2] = (int) x[it][jt];

            int j = 0;
            int f2 = 0;

            while (f2 == 0) {
                if (baz1[it][j] == 1) {
                    jt = j;
                    f2 = 1;
                }
                j++;
            }

            cont[k] = jt;
            baz1[it][jt] = 0;

            if (it == ii && jt == jj) {
                u = 1;
            }
        }

        return cont;
    }

    private HashMap<String, double[][]> changePotential(int ii, int jj, int[] cont, double[][] c, double[][] x, double[][] baz) {
        double[][] xxx = new double[m][];
        for (int i = 0; i < m; i++)
            xxx[i] = Arrays.copyOf(x[i], x[i].length);

        double[][] baz1 = new double[m][];
        for (int i = 0; i < m; i++)
            baz1[i] = Arrays.copyOf(baz[i], baz[i].length);

        double cc = 0;
        int im = 1000;
        int jm = 1000;
        int t = 0;

        for (int i = 1; i <= cont[n + m + 3]; i++) {
            int iii = cont[t];
            int jjj = cont[t + 1];
            int iiii = cont[t + 2];
            xxx[iii][jjj] += cont[n + m + 2];
            xxx[iiii][jjj] -= cont[n + m + 2];

            if (c[iiii][jjj] >= cc && xxx[iiii][jjj] == 0) {
                im = iiii;
                jm = jjj;
            }

            t += 2;
        }

        baz1[ii][jj] = 1;
        baz1[im][jm] = 0;


        HashMap<String, double[][]> map = new HashMap<>();
        map.put(XXX, xxx);
        map.put(BAZ, baz1);
        return map;
    }

    public double[][] getMaximize() {
        return maximize;
    }

    public double[][] getMinimize() {
        return minimize;
    }

    public double[][] getZz() {
        return zz;
    }
}
