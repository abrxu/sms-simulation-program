package domain.core;

public class Cliente {
    private final int id;
    private final double tempoChegada;

    public Cliente(int id, double tempoChegada) {
        this.id = id;
        this.tempoChegada = tempoChegada;
    }

    public double getTempoChegada() {
        return tempoChegada;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", tempoChegada=" + String.format("%.2f", tempoChegada) + '}';
    }
}


