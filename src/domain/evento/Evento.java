package domain.evento;

import domain.core.Cliente;

public class Evento implements Comparable<Evento> {
    private final TipoEvento tipo;
    private final double tempo;
    private final Cliente cliente;

    public Evento(TipoEvento tipo, double tempo, Cliente cliente) {
        this.tipo = tipo;
        this.tempo = tempo;
        this.cliente = cliente;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public double getTempo() {
        return tempo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public int compareTo(Evento outro) {
        return Double.compare(this.tempo, outro.tempo);
    }

    @Override
    public String toString() {
        return "Evento{" + "tipo=" + tipo + ", tempo=" + String.format("%.2f", tempo) + ", " + cliente + '}';
    }
}