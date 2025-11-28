package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== BATTLESHIP =====");
        System.out.println("Elige el modo de juego:");
        System.out.println("1) Consola");
        System.out.println("2) Gráfico");
        System.out.print("Opcion: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        System.out.print("Ingresa tu nombre: ");
        String nombre = sc.nextLine();
        System.out.print("¿Eres servidor? (s/n): ");
        boolean esServidor = sc.nextLine().equalsIgnoreCase("s");

        String ip = null;
        if (!esServidor) {
            System.out.print("Ingresa la IP del servidor: ");
            ip = sc.nextLine();
        }

        Tablero tablero = new Tablero();
        Jugador jugador = new Jugador(nombre, esServidor);
        Conexion conexion = new Conexion();
        ITablero vista;

        if(opcion==1){
            vista= new TableroConsola(sc);
        }else{
            vista= new TableroGrafico();
        }

        try {
            if (esServidor) {
                System.out.println("Esperando conexión...");
                conexion.esperarConexion();
            } else {
                System.out.println("Conectando a " + ip + "...");
                conexion.conectarAPartida(ip);
            }

            JuegoBattleship juego = new JuegoBattleship(tablero, conexion, vista, jugador);
            juego.intercambiarNombres();
            juego.iniciarJuego();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.cerrarConexion();
        }
    }
}
