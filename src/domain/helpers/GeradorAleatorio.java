package domain.helpers;

public class GeradorAleatorio {
    private long a, c, m, x;

    public GeradorAleatorio(long a, long c, long m, long x0) {
        this.a = a;
        this.c = c;
        this.m = m;
        this.x = x0;
    }

    private double nextRandom() {
        x = (a * x + c) % m;
        return (double) x / m;
    }

    public double gerarTempoExponencial(double media) {
        return -media * Math.log(1 - nextRandom());
    }

    public double gerarTempoUniforme(double min, double max) {
        return min + (max - min) * nextRandom();
    }
}