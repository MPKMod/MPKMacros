package io.github.kurrycat.mpkmod.module.macros;

import io.github.kurrycat.mpkmod.util.MathUtil;

public class TickHeaderFormat {
    public static final String CSV_SEPARATOR = "[,\t]";

    private int W = -1, A = -1, S = -1, D = -1, P = -1, N = -1, J = -1,
            YAW = -1, PITCH = -1, L = -1, R = -1, COUNT = -1;

    public TickHeaderFormat(String header) {
        String[] cols = header.split(CSV_SEPARATOR);
        for (int i = 0; i < cols.length; i++) {
            String col = cols[i].toUpperCase();
            //noinspection IfCanBeSwitch
            if (col.equals("W")) W = i;
            else if (col.equals("A")) A = i;
            else if (col.equals("S")) S = i;
            else if (col.equals("D")) D = i;
            else if (col.equals("P")) P = i;
            else if (col.equals("N")) N = i;
            else if (col.equals("J")) J = i;
            else if (col.equals("YAW")) YAW = i;
            else if (col.equals("PITCH")) PITCH = i;
            else if (col.equals("L")) L = i;
            else if (col.equals("R")) R = i;
            else if (col.equals("COUNT")) COUNT = i;
        }
    }

    public static String getCSVHeader() {
        return "W,A,S,D,P,N,J,YAW,PITCH,L,R,COUNT";
    }

    public boolean getW(String[] row) {
        return W != -1 && (row[W].equals("true") || row[W].equals("1"));
    }

    public boolean getA(String[] row) {
        return A != -1 && (row[A].equals("true") || row[A].equals("1"));
    }

    public boolean getS(String[] row) {
        return S != -1 && (row[S].equals("true") || row[S].equals("1"));
    }

    public boolean getD(String[] row) {
        return D != -1 && (row[D].equals("true") || row[D].equals("1"));
    }

    public boolean getP(String[] row) {
        return P != -1 && (row[P].equals("true") || row[P].equals("1"));
    }

    public boolean getN(String[] row) {
        return N != -1 && (row[N].equals("true") || row[N].equals("1"));
    }

    public boolean getJ(String[] row) {
        return J != -1 && (row[J].equals("true") || row[J].equals("1"));
    }

    public float getYaw(String[] row) {
        return YAW == -1 ? 0F : MathUtil.parseFloat(row[YAW], 0F);
    }

    public float getPitch(String[] row) {
        return PITCH == -1 ? 0F : MathUtil.parseFloat(row[PITCH], 0F);
    }

    public int getL(String[] row) {
        return L == -1 ? 0 : MathUtil.parseInt(row[L], 0);
    }

    public int getR(String[] row) {
        return R == -1 ? 0 : MathUtil.parseInt(row[R], 0);
    }

    public int getCount(String[] row) {
        return COUNT == -1 ? 1 : MathUtil.parseInt(row[COUNT], 1);
    }

}
