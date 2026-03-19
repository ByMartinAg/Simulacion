public class Revisor {

    private int id;
    private boolean disponible;
    private int personasAtendidas;

    public Revisor(int id) {
        this.id = id;
        this.disponible = true;
        this.personasAtendidas = 0;
    }

    public void ocupar() {
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
        this.personasAtendidas++;
    }

    public int getId() { return id; }
    public boolean isDisponible() {
        return disponible;
    }
    public int getPersonasAtendidas() {
        return personasAtendidas;
    }
}