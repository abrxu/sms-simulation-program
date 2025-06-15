package domain;

import java.util.Scanner;

public class GeradorNPA {

    private long m;
    private long a;
    private long c;
    private long x0;
    private long x_atual;

    // Método para  inicialziar os valores
    public void inicializarParametros(Scanner scanner) {

        System.out.print("Digite o valor de m: ");
        m = scanner.nextLong();

        System.out.print("Digite o valor de a: ");
        a = scanner.nextLong();

        System.out.print("Digite o valor de c: ");
        c = scanner.nextLong();

        System.out.print("Digite o valor de x0: ");
        x0 = scanner.nextLong();

        // Inicializa x_atual com x0
        x_atual = x0;
    }

    // Método para gerar o próximo número
    public long gerarProximo() {
        x_atual = (a * x_atual + c) % m;
        return x_atual;
    }

    /**
     * TAREFA 3: Este é o novo método que você precisa adicionar.
     * Ele usa o método acima para gerar um número inteiro e depois o transforma
     * em um número decimal entre 0 e 1.
     */
    public double gerarProximoNormalizado() {
        long proximoNumero = gerarProximo();
        return (double) proximoNumero / (m - 1);
    }

    // O método main agora reflete as mudanças
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        GeradorNPA gerador = new GeradorNPA();
        gerador.inicializarParametros(leitor);
        System.out.println("\n10 Números pseudo-aleatórios normalizados (entre 0 e 1):");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%.4f\n", gerador.gerarProximoNormalizado());
        }
        leitor.close();
    }
}