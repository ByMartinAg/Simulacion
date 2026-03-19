public class Persona {

    private int id;
    private int tiempoLlegada;
    private int tiempoAtencion;
    private boolean bolotoAceptado;

    public Persona(int id, int tiempoLlegada, int tiempoAtencion, boolean boletoAceptado) {
        this.id = id;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoAtencion = tiempoAtencion;
        this.bolotoAceptado = boletoAceptado;
    }

    public int getId() { return id; }
    public int getTiempoLlegada() {
        return tiempoLlegada;
    }
    public int getTiempoAtencion() {
        return tiempoAtencion;
    }
    public boolean isBoletoAceptado() {
        return bolotoAceptado;
    }
}