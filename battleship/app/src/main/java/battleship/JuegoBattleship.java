package battleship;

import java.io.IOException;

public class JuegoBattleship {
    private Tablero t;
    private Conexion conexion;
    private ITablero vista;
    private Jugador j;

    public JuegoBattleship(Tablero t,  Conexion c, ITablero vista){
        this.t= t;
        conexion= c;
        this.vista = vista;
    }
    //metodos de battleship p2p

    //modificado porque se delegaron responsabilidades a jugador y conexion
    public void intercambiarNombres() throws IOException {
        if (j.esServidor()) {
            // Esperar nombre del cliente
            String nombreOponente = conexion.recibirMensaje();
            conexion.mandarMensaje(j.getNombre());
            System.out.println("Jugando contra: " + nombreOponente);
        } else {
            conexion.mandarMensaje(j.getNombre());
            String nombreOponente = conexion.recibirMensaje();
            System.out.println("Jugando contra: " + nombreOponente);
        }
    }

    //modificado para implementar clase Conexion, encargada de entradas y salidas
    public void iniciarJuego() throws IOException {
        System.out.println("\n=== INICIANDO JUEGO ===");

        // Colocar barcos automáticamente
        //usa tablero en lugar de jugador
        t.colocarBarcosAutomaticamente();
        System.out.println("Tus barcos han sido colocados automáticamente.");
        vista.dibujar(t);

        boolean juegoActivo = true;
        boolean miTurno = j.esServidor(); // El servidor comienza

        try {
            conexion.mandarMensaje(ProtocoloBattleship.LISTO);
            String respuesta = conexion.recibirMensaje();

            if (respuesta == null) {
                System.out.println("El oponente se desconectó durante la inicialización.");
                return;
            }

            if (ProtocoloBattleship.LISTO.equals(respuesta)) {
                System.out.println("¡Ambos jugadores listos! El juego comienza.");

                if (miTurno) {
                    System.out.println("\n¡Tú comienzas!");
                } else {
                    System.out.println("\nEl oponente comienza...");
                }

                while (juegoActivo) {
                    if (miTurno) {
                        juegoActivo = turnoLocal();
                        if (juegoActivo) {
                            miTurno = false;
                        }
                    } else {
                        juegoActivo = turnoRemoto();
                        if (juegoActivo) {
                            miTurno = true;
                        }
                    }

                    // Pequeña pausa para estabilizar la comunicación
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    //modificado para implementar clase TableroConsola
    public boolean turnoLocal() throws IOException {
        System.out.println("\n=== TU TURNO ===");
        vista.dibujar(t);

        int[] disparo = vista.obtenerDisparoJugador(t);
        conexion.mandarMensaje(ProtocoloBattleship.construirMensajeDisparo(disparo[0],disparo[1]));
        String respuesta = conexion.recibirMensaje();

        // VERIFICACIÓN DE NULL AÑADIDA
        if (respuesta == null) {
            System.out.println("El oponente se desconectó o hubo un error en la comunicación.");
            return false;
        }

        try {
            ProtocoloBattleship.Mensaje mensaje = ProtocoloBattleship.parsearMensaje(respuesta);

            switch (mensaje.comando) {
                case ProtocoloBattleship.IMPACTO:
                    System.out.println("¡IMPACTO en (" + mensaje.x + "," + mensaje.y + ")!");
                    t.registrarImpacto(mensaje.x, mensaje.y);
                    return true;

                case ProtocoloBattleship.FALLO:
                    System.out.println("FALLO en (" + mensaje.x + "," + mensaje.y + ")");
                    t.registrarFallo(mensaje.x, mensaje.y);
                    return true;

                case ProtocoloBattleship.HUNDIDO:
                    System.out.println("¡HUNDIDO! " + mensaje.tipoBarco + " en (" + mensaje.x + "," + mensaje.y + ")");
                    t.registrarImpacto(mensaje.x, mensaje.y);
                    return true;

                case ProtocoloBattleship.JUEGO_TERMINADO:
                    System.out.println("¡FELICIDADES! ¡HAS GANADO!");
                    return false;

                default:
                    System.out.println("Respuesta inesperada: " + respuesta);
                    return true;
            }
        } catch (Exception e) {
            System.out.println("Error procesando respuesta: " + e.getMessage());
            System.out.println("Respuesta recibida: " + respuesta);
            return false;
        }
    }

    //con ajustes en la entrada y salida de datos
    public boolean turnoRemoto() throws IOException {
        System.out.println("\n=== TURNO DEL OPONENTE ===");
        System.out.println("Esperando disparo del oponente...");
        
        String mensajeEntrante = conexion.recibirMensaje();
        
        if (mensajeEntrante == null) {
            System.out.println("El oponente se desconectó.");
            return false;
        }
        
        try {
            ProtocoloBattleship.Mensaje mensaje = ProtocoloBattleship.parsearMensaje(mensajeEntrante);
            
            if (ProtocoloBattleship.DISPARAR.equals(mensaje.comando)) {
                boolean impacto = t.recibirDisparo(mensaje.x, mensaje.y);
                
                if (impacto) {
                    String tipoBarco = t.obtenerTipoBarcoEn(mensaje.x, mensaje.y);
                    
                    // VERIFICACIÓN MEJORADA
                    if (tipoBarco.equals("DESCONOCIDO")) {
                        // No podemos determinar el tipo de barco, solo decimos IMPACTO
                        conexion.mandarMensaje(ProtocoloBattleship.construirMensajeResultado(
                            ProtocoloBattleship.IMPACTO, mensaje.x, mensaje.y, null));
                        System.out.println("El oponente impactó en (" + mensaje.x + "," + mensaje.y + ")");
                    } else if (t.estaBarcoHundido(tipoBarco)) {
                        conexion.mandarMensaje(ProtocoloBattleship.construirMensajeResultado(
                            ProtocoloBattleship.HUNDIDO, mensaje.x, mensaje.y, tipoBarco));
                        
                        if (t.todosBarcosHundidos()) {
                            conexion.mandarMensaje(ProtocoloBattleship.JUEGO_TERMINADO);
                            System.out.println("El oponente hundió tu " + tipoBarco);
                            System.out.println("¡HAS PERDIDO!");
                            return false;
                        } else {
                            System.out.println("El oponente hundió tu " + tipoBarco + " en (" + mensaje.x + "," + mensaje.y + ")");
                        }
                    } else {
                        conexion.mandarMensaje(ProtocoloBattleship.construirMensajeResultado(
                            ProtocoloBattleship.IMPACTO, mensaje.x, mensaje.y, null));
                        System.out.println("El oponente impactó en (" + mensaje.x + "," + mensaje.y + ")");
                    }
                } else {
                    conexion.mandarMensaje(ProtocoloBattleship.construirMensajeResultado(
                        ProtocoloBattleship.FALLO, mensaje.x, mensaje.y, null));
                    System.out.println("El oponente falló en (" + mensaje.x + "," + mensaje.y + ")");
                }
            }
            
            vista.dibujar(t);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error procesando mensaje del oponente: " + e.getMessage());
            e.printStackTrace(); // Esto te dará más detalles del error
            return false;
        }
    }
}
