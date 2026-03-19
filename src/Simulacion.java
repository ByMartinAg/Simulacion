import java.util.Random;

public class Simulacion {

    // ================================================================
    //  CONFIGURACION — modifica estos valores para cada escenario
    // ================================================================

    static final int    TOTAL_PERSONAS   = 2000;  // cantidad de asistentes
    static final int    NUM_REVISORES    = 3;     // numero de revisores

    static final int    MIN_LLEGADA      = 1;     // segundos minimo entre llegadas
    static final int    MAX_LLEGADA      = 10;    // segundos maximo entre llegadas

    static final int    MIN_ATENCION     = 15;    // segundos minimo de atencion
    static final int    MAX_ATENCION     = 45;    // segundos maximo de atencion

    static final double PROB_RECHAZO     = 0.03;  // probabilidad de rechazo (0.03 = 3%)

    // ================================================================

    private Random random;
    private int[]  tiempoLibre;
    private int[]  atendidosPorRevisor;

    private int tiempoFinTotal;
    private int esperaMaxima;
    private int personasQueEsperaron;
    private int boletosRechazados;
    private int filaMaxima;
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

            // 1. GENERAR TIEMPO DE LLEGADA
            tiempoLlegada += aleatorio(MIN_LLEGADA, MAX_LLEGADA);

            // 2. ASIGNAR AL REVISOR QUE QUEDA LIBRE PRIMERO
            int idx = buscarRevisorOptimo();

            // 3. CALCULAR INICIO DE ATENCION
            int inicioAtencion = Math.max(tiempoLlegada, tiempoLibre[idx]);
            int espera         = inicioAtencion - tiempoLlegada;

            // 4. REGISTRAR ESPERA
            if (espera > 0) {
                personasQueEsperaron++;
                filaActual++;
                if (filaActual > filaMaxima) filaMaxima = filaActual;
                if (espera > esperaMaxima)   esperaMaxima = espera;
            } else {
                if (filaActual > 0) filaActual--;
            }

            // 5. CALCULAR FIN DE ATENCION Y ACTUALIZAR REVISOR
            int tiempoAtencion      = aleatorio(MIN_ATENCION, MAX_ATENCION);
            tiempoLibre[idx]        = inicioAtencion + tiempoAtencion;
            atendidosPorRevisor[idx]++;

            // 6. VALIDAR BOLETO
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

    public void mostrarResultados() {
        int    minutos          = tiempoFinTotal / 60;
        int    segundos         = tiempoFinTotal % 60;
        int    esperaMaxMin     = esperaMaxima / 60;
        int    esperaMaxSeg     = esperaMaxima % 60;
        int    aceptados        = TOTAL_PERSONAS - boletosRechazados;
        double pctEsperaron     = (double) personasQueEsperaron / TOTAL_PERSONAS * 100;

        System.out.println("================================================");
        System.out.println("  SIMULACION - CONCIERTO LOS INQUIETOS DEL NORTE");
        System.out.println("================================================");
        System.out.println("Revisores activos    : " + NUM_REVISORES);
        System.out.println("Personas procesadas  : " + TOTAL_PERSONAS);
        System.out.println("Boletos aceptados    : " + aceptados);
        System.out.println("Boletos rechazados   : " + boletosRechazados);
        System.out.println("------------------------------------------------");
        System.out.println("Tiempo total         : " + minutos + " min " + segundos + " seg");
        System.out.println("Espera maxima        : " + esperaMaxMin + " min " + esperaMaxSeg + " seg");
        System.out.println("Personas que esperaron: " + personasQueEsperaron + " (" + String.format("%.1f", pctEsperaron) + "%)");
        System.out.println("Fila maxima          : " + filaMaxima + " personas");
        System.out.println("------------------------------------------------");
        for (int i = 0; i < NUM_REVISORES; i++) {
            System.out.println("Atendidos por R" + (i + 1) + "     : " + atendidosPorRevisor[i]);
        }
        System.out.println("================================================");
    }
}