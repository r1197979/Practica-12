package battleship;

public class Main {

    public static void main(String[] args) {
        TableroConsola ui = new TableroConsola();
        int modo= ui.elegirModo();
        String nombre= ui.obtenerNombreJugador();

        boolean esServidor= (modo ==1);
        Jugador jugador= new Jugador(nombre, esServidor);
        Tablero tablero= new Tablero();
        Conexion conexion= new Conexion();
    
        JuegoBattleship juego= new JuegoBattleship(tablero, conexion, ui);
        
        try {
            System.out.println("Iniciando juego...");
            juego.iniciarJuego();
        } catch (Exception e) {
            System.err.println("Error");
        }
    }
}
