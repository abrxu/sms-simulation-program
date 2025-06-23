package domain.core;

import domain.evento.Evento;
import domain.evento.TipoEvento;
import domain.helpers.GeradorAleatorio;

import java.util.*;

public class Simulador {

    private final GeradorAleatorio gerador;
    private final double tempoTotalSimulacao;
    private final double mediaChegadas;
    private final double minAtendimento;
    private final double maxAtendimento;

    /**
     * 0 -> Ocioso
     * <br>
     * 1 -> Ocupado
     */
    private int statusServidor;
    private int proximoIdCliente;
    private int totalClientesAtendidos;

    private Queue<Cliente> fila;
    private PriorityQueue<Evento> listaEventosFuturos;

    private double tempoUltimoEvento;
    private double somaAtrasosFila;
    private double areaSobQt;
    private double areaSobBt;
    private double relogio;

    public Simulador(double tempoTotalSimulacao, GeradorAleatorio gerador, double mediaChegadas, double minAtendimento, double maxAtendimento) {
        this.tempoTotalSimulacao = tempoTotalSimulacao;
        this.gerador = gerador;
        this.mediaChegadas = mediaChegadas;
        this.minAtendimento = minAtendimento;
        this.maxAtendimento = maxAtendimento;
    }

    public Map<String, Double> executar() {
        rotinaInicializacao();

        while (relogio < tempoTotalSimulacao) {
            Evento proximoEvento = rotinaTemporizacao();
            if (proximoEvento == null) break;

            relogio = proximoEvento.getTempo();

            if (relogio > tempoTotalSimulacao) {
                listaEventosFuturos.add(proximoEvento);
                break;
            }

            rotinaEvento(proximoEvento);
        }

        finalizarClientesRestantes();

        return gerarRelatorio();
    }

    private void rotinaInicializacao() {
        relogio = 0.0;
        statusServidor = 0;
        fila = new LinkedList<>();
        listaEventosFuturos = new PriorityQueue<>();
        tempoUltimoEvento = 0.0;
        proximoIdCliente = 1;

        somaAtrasosFila = 0.0;
        totalClientesAtendidos = 0;
        areaSobQt = 0.0;
        areaSobBt = 0.0;

        double tempoPrimeiraChegada = gerador.gerarTempoExponencial(mediaChegadas);
        agendarEvento(TipoEvento.CHEGADA, tempoPrimeiraChegada, new Cliente(proximoIdCliente++, tempoPrimeiraChegada));
    }

    private Evento rotinaTemporizacao() {
        return listaEventosFuturos.poll();
    }

    private void rotinaEvento(Evento evento) {
        double tempoDecorrido = relogio - tempoUltimoEvento;
        areaSobQt += fila.size() * tempoDecorrido;
        areaSobBt += statusServidor * tempoDecorrido;

        tempoUltimoEvento = relogio;

        switch (evento.getTipo()) {
            case CHEGADA:
                processarChegada(evento);
                break;
            case SAIDA:
                processarSaida(evento);
                break;
        }
    }

    private void processarChegada(Evento eventoChegada) {
        Cliente cliente = eventoChegada.getCliente();

        double tempoProximaChegada = relogio + gerador.gerarTempoExponencial(mediaChegadas);
        agendarEvento(TipoEvento.CHEGADA, tempoProximaChegada, new Cliente(proximoIdCliente++, tempoProximaChegada));

        if (statusServidor == 0) {
            statusServidor = 1;
            double atraso = 0.0;
            somaAtrasosFila += atraso;
            totalClientesAtendidos++;

            double tempoServico = gerador.gerarTempoUniforme(minAtendimento, maxAtendimento);
            agendarEvento(TipoEvento.SAIDA, relogio + tempoServico, cliente);
        } else {
            fila.add(cliente);
        }
    }

    private void processarSaida(Evento eventoSaida) {
        if (!fila.isEmpty()) {
            Cliente proximoCliente = fila.poll();
            double atraso = relogio - proximoCliente.getTempoChegada();
            somaAtrasosFila += atraso;
            totalClientesAtendidos++;

            double tempoServico = gerador.gerarTempoUniforme(minAtendimento, maxAtendimento);
            agendarEvento(TipoEvento.SAIDA, relogio + tempoServico, proximoCliente);
        } else {
            statusServidor = 0;
        }
    }

    private void finalizarClientesRestantes() {
        listaEventosFuturos.removeIf(e -> e.getTipo() == TipoEvento.CHEGADA);

        while (!listaEventosFuturos.isEmpty() || !fila.isEmpty()) {
            Evento proximoEvento = null;
            if (listaEventosFuturos.isEmpty() && !fila.isEmpty() && statusServidor == 0) {
                if (relogio < tempoUltimoEvento) relogio = tempoUltimoEvento;

                Cliente clienteDaFila = fila.poll();
                double atraso = relogio - clienteDaFila.getTempoChegada();
                somaAtrasosFila += atraso;
                totalClientesAtendidos++;
                statusServidor = 1;

                double tempoServico = gerador.gerarTempoUniforme(minAtendimento, maxAtendimento);
                proximoEvento = new Evento(TipoEvento.SAIDA, relogio + tempoServico, clienteDaFila);
            } else {
                proximoEvento = rotinaTemporizacao();
                if (proximoEvento == null) break;
            }

            relogio = proximoEvento.getTempo();
            rotinaEvento(proximoEvento);
        }
    }

    private Map<String, Double> gerarRelatorio() {
        double tempoMedioFila = (totalClientesAtendidos > 0) ? somaAtrasosFila / totalClientesAtendidos : 0;
        double numeroMedioFila = areaSobQt / relogio;
        double taxaOcupacao = (areaSobBt / relogio) * 100.0;

        Map<String, Double> resultados = new LinkedHashMap<>();
        resultados.put("Tempo Medio em Fila", tempoMedioFila);
        resultados.put("Numero Medio em Fila", numeroMedioFila);
        resultados.put("Taxa de Ocupacao do Servidor (%)", taxaOcupacao);
        return resultados;
    }

    private void agendarEvento(TipoEvento tipo, double tempo, Cliente cliente) {
        listaEventosFuturos.add(new Evento(tipo, tempo, cliente));
    }
}