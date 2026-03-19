import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    static final int simulaciones = 100;

    public static void main(String[] args) {

        String nombreArchivo = "resultados_simulacion.csv"+1;

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {

            // ── Encabezado con datos base ──────────────────────────────
            pw.println("SIMULACION - CONCIERTO LOS INQUIETOS DEL NORTE");
            pw.println("Revisores:," + Simulacion.NUM_REVISORES);
            pw.println("Total personas:," + Simulacion.TOTAL_PERSONAS);
            pw.println("Intervalo llegada:," + Simulacion.MIN_LLEGADA + " - " + Simulacion.MAX_LLEGADA + " seg");
            pw.println("Intervalo atencion:," + Simulacion.MIN_ATENCION + " - " + Simulacion.MAX_ATENCION + " seg");
            pw.println("Probabilidad rechazo:," + (Simulacion.PROB_RECHAZO * 100) + "%");
            pw.println("Total corridas:," + simulaciones);
            pw.println();

            // ── Encabezado de columnas ─────────────────────────────────
            StringBuilder header = new StringBuilder();
            header.append("Corrida,Aceptados,Rechazados,Tiempo total,Espera maxima,Personas que esperaron,Fila maxima");
            for (int i = 1; i <= Simulacion.NUM_REVISORES; i++) {
                header.append(",Atendidos R").append(i);
            }
            pw.println(header);

            // ── 100 corridas ───────────────────────────────────────────
            for (int i = 1; i <= simulaciones; i++) {
                Simulacion sim = new Simulacion();
                sim.ejecutar();
                pw.println(sim.toCSV(i));
                System.out.println("Simulacion " + i + " completada.");
            }

            System.out.println("\nArchivo generado: " + nombreArchivo);

        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}