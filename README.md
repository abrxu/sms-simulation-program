# Simulacao e Modelagem de Sistemas - 01/2025 - CESUCA

## Sobre o programa:
> Há três medidas de desempenho, são elas:
1. O atraso médio na fila: **d(n)**
2. O numéro médio de clientes na fila: **q(n)**
3. A proporcão de tempo em que o servidor está ocupado: **u(n)**

---

> As variáveis de estado necessárias para estimar d(n), q(n) e u(n) são: 
1. O status do servidor (0 para ocioso e 1 para ocupado);
2. O número de clientes em fila;
3. A hora de chegada de cada cliente atualmente na fila (representado como uma lista);
4. A hora do último evento (evento mais recente).

