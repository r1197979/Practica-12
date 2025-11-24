package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Conexion {
    //atributos de battleship p2p
    private static final int PUERTO = 12345;
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private Scanner scanner;

    public Conexion(){
        scanner= new Scanner(System.in);
    }

    public void esperarConexion() {
        try {
            System.out.println("\nIniciando servidor en puerto " + PUERTO + "...");
            serverSocket = new ServerSocket(PUERTO);
            System.out.println("Esperando conexión de otro jugador...");

            socket = serverSocket.accept();
            System.out.println("¡Jugador conectado desde: " + socket.getInetAddress() + "!");

            configurarFlujos();
        } catch (IOException e) {
            System.err.println("Error al esperar conexión: " + e.getMessage());
        }
    }

    public void conectarAPartida() {
        try {
            System.out.print("\nIngresa la IP del otro jugador: ");
            String ip = scanner.nextLine();

            System.out.println("Conectando a " + ip + ":" + PUERTO + "...");
            socket = new Socket(ip, PUERTO);
            System.out.println("¡Conectado exitosamente!");

            configurarFlujos();
            //intercambiarNombres();
            //iniciarJuego();

        } catch (IOException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            System.out.println("¿Deseas intentar nuevamente? (s/n)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                conectarAPartida();
            }
        }
    }

    private void configurarFlujos() throws IOException {
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void cerrarConexion() {
        try {
            if (entrada != null)
                entrada.close();
            if (salida != null)
                salida.close();
            if (socket != null)
                socket.close();
            if (serverSocket != null)
                serverSocket.close();
            scanner.close();
            System.out.println("Conexión cerrada.");
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    //metodos para manejar la entrada y salida de mensajes
    //(printwriter y bufferedreader)

    public void mandarMensaje(String m){
        salida.println(m);
    }
    public String recibirMensaje() throws IOException{
        if(entrada!=null){
            return entrada.readLine();
        }
        return null;
    }
}
