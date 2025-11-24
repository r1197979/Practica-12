package battleship;

public class Jugador {
    //antes en battleshipp2p
    private boolean esServidor;
    private String nombreJugador;

    public Jugador(String nombre, boolean servidor){
        nombreJugador= nombre;
        esServidor= servidor;
    }

    public String getNombre(){
        return nombreJugador;
    }
    public boolean esServidor(){
        return esServidor;
    }
}
