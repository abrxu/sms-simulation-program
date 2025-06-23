import domain.core.Simulador;
import domain.helpers.GeradorAleatorio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("--- Gerador de Numeros Pseudo-Aleatorios (LCG) ---");

        System.out.print("Digite o valor de m: ");
        long m = sc.nextLong();
        System.out.print("Digite o valor de a: ");
        long a = sc.nextLong();
        System.out.print("Digite o valor de c: ");
        long c = sc.nextLong();

        System.out.println("\n--- Parametros do Sistema ---");

        System.out.print("Digite o tempo medio entre chegadas (exponencial): ");
        double mediaChegadas = sc.nextDouble();
        System.out.print("Digite o tempo minimo de atendimento (uniforme): ");
        double minAtendimento = sc.nextDouble();
        System.out.print("Digite o tempo maximo de atendimento (uniforme): ");
        double maxAtendimento = sc.nextDouble();

        System.out.println("\n--- Configuracao da Simulacao ---");

        System.out.print("Digite o numero de execucoes: ");
        int numExecucoes = sc.nextInt();
        System.out.print("Digite a semente inicial (x0): ");
        long sementeInicial = sc.nextLong();

        sc.close();

        List<Map<String, Double>> todasAsExecucoes = new ArrayList<>();
        long sementeAtual = sementeInicial;

        for (int i = 0; i < numExecucoes; i++) {
            System.out.printf("\n--- Executando Simulacao %d/%d (Semente x0 = %d) ---\n", (i + 1), numExecucoes, sementeAtual);

            GeradorAleatorio gerador = new GeradorAleatorio(a, c, m, sementeAtual);

            Simulador simulador = new Simulador(480.0, gerador, mediaChegadas, minAtendimento, maxAtendimento);
            Map<String, Double> resultadosDaRodada = simulador.executar();

            resultadosDaRodada.forEach((key, value) ->
                    System.out.printf("%s: %.4f\n", key, value)
            );

            todasAsExecucoes.add(resultadosDaRodada);

            sementeAtual = (a * sementeAtual + c) % m;
        }

        escreverCSV(todasAsExecucoes);
    }

    private static void escreverCSV(List<Map<String, Double>> resultados) {
        if (resultados.isEmpty()) return;

        String nomeArquivo = "resultados_simulacao.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("Execucao;" + String.join(";", resultados.get(0).keySet()));

            for (int i = 0; i < resultados.size(); i++) {
                String linha = (i + 1) + ";" +
                        resultados.get(i).values().stream()
                                .map(value -> String.format(Locale.US, "%.4f", value))
                                .collect(Collectors.joining(";"));
                writer.println(linha);
            }

            System.out.printf("\nResultados das %d execucoes salvos em '%s'.\n", resultados.size(), nomeArquivo);

        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo CSV: " + e.getMessage());
        }
    }
}
