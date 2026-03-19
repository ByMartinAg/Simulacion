import java.util.Random;

public class Simulacion {

    // Aqui van a modificar las variables con los valores ahora si, los que tenemos para todos los escenarios

    static final int    TOTAL_PERSONAS   = 2000;  // cantidad de asistentes
    static final int    NUM_REVISORES    = 2;     // numero de revisores

    static final int    MIN_LLEGADA      = 1;     // segundos minimo entre llegadas
    static final int    MAX_LLEGADA      = 10;    // segundos maximo entre llegadas

    static final int    MIN_ATENCION     = 15;    // segundos minimo de atencion
    static final int    MAX_ATENCION     = 40;    // segundos maximo de atencion

    static final double PROB_RECHAZO     = 0.03;  // probabilidad de rechazo (0.03 = 3%)

    // ================================================================

    private Random random;
    private int[]  tiempoLibre;
    private int[]  atendidosPorRevisor;

    // Resultados de esta corrida
    int tiempoFinTotal;
    int esperaMaxima;
    int personasQueEsperaron;
    int boletosRechazados;
    int filaMaxima;
    private int filaActual;

    public Simulacion() {
        random               = new Random();
        tiempoLibre          = new int[NUM_REVISORES];
        atendidosPorRevisor  = new int[NUM_REVISORES];
        tiempoFinTotal       = 0;
        esperaMaxima         = 0;
        personasQueEsperaron = 0;
        boletosRechazados    = 0;
        filaMaxima           = 0;
        filaActual           = 0;

        for (int i = 0; i < NUM_REVISORES; i++) {
            tiempoLibre[i] = 0;
        }
    }

    private int aleatorio(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    private int buscarRevisorOptimo() {
        int idx = 0;
        for (int i = 1; i < NUM_REVISORES; i++) {
            if (tiempoLibre[i] < tiempoLibre[idx]) {
                idx = i;
            }
        }
        return idx;
    }

    public void ejecutar() {
        int tiempoLlegada = 0;

        for (int persona = 1; persona <= TOTAL_PERSONAS; persona++) {

            tiempoLlegada += aleatorio(MIN_LLEGADA, MAX_LLEGADA);

            int idx            = buscarRevisorOptimo();
            int inicioAtencion = Math.max(tiempoLlegada, tiempoLibre[idx]);
            int espera         = inicioAtencion - tiempoLlegada;

            if (espera > 0) {
                personasQueEsperaron++;
                filaActual++;
                if (filaActual > filaMaxima) filaMaxima = filaActual;
                if (espera > esperaMaxima)   esperaMaxima = espera;
            } else {
                if (filaActual > 0) filaActual--;
            }

            int tiempoAtencion      = aleatorio(MIN_ATENCION, MAX_ATENCION);
            tiempoLibre[idx]        = inicioAtencion + tiempoAtencion;
            atendidosPorRevisor[idx]++;

            if (random.nextDouble() < PROB_RECHAZO) {
                boletosRechazados++;
            }
        }

        for (int i = 0; i < NUM_REVISORES; i++) {
            if (tiempoLibre[i] > tiempoFinTotal) {
                tiempoFinTotal = tiempoLibre[i];
            }
        }
    }

    // Devuelve una linea CSV con los resultados de esta ejecucion
    public String toCSV(int ejecucion) {
        int    minutos      = tiempoFinTotal / 60;
        int    segundos     = tiempoFinTotal % 60;
        int    espMaxMin    = esperaMaxima / 60;
        int    espMaxSeg    = esperaMaxima % 60;
        int    aceptados    = TOTAL_PERSONAS - boletosRechazados;

        StringBuilder sb = new StringBuilder();
        sb.append(ejecucion).append(",");
        sb.append(aceptados).append(",");
        sb.append(boletosRechazados).append(",");
        sb.append(minutos + "m " + segundos + "s").append(",");
        sb.append(espMaxMin + "m " + espMaxSeg + "s").append(",");
        sb.append(personasQueEsperaron).append(",");
        sb.append(filaMaxima);

        // atendidos por cada revisor
        for (int i = 0; i < NUM_REVISORES; i++) {
            sb.append(",").append(atendidosPorRevisor[i]);
        }

        return sb.toString();
    }
}