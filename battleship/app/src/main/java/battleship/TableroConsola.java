package battleship;

import java.util.Scanner;

public class TableroConsola implements ITablero{
    
    private Scanner scanner;
    
    public TableroConsola(Scanner s){
        scanner= s;
    }

    public int elegirModo() {
        while (true) {
            System.out.println("\nSelecciona modo:");
            System.out.println("1. Crear partida (Esperar conexión)");
            System.out.println("2. Unirse a partida (Conectar a otro jugador)");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            if ("1".equals(opcion)) return 1;
            if("2".equals(opcion)) return 2;
            System.out.println("Opción inválida. Intenta nuevamente.");
        }
    }

    //de battleship p2p
    @Override
    public int[] obtenerDisparoJugador(Tablero tablero) {
        while (true) {
            try {
                System.out.print("Ingresa coordenadas para disparar (fila,columna 0-9): ");
                String entrada = scanner.nextLine();
                String[] coordenadas = entrada.split(",");

                if (coordenadas.length != 2) {
                    System.out.println("Formato inválido. Usa: fila,columna");
                    continue;
                }

                int fila = Integer.parseInt(coordenadas[0].trim());
                int columna = Integer.parseInt(coordenadas[1].trim());

                if (fila >= 0 && fila < 10 && columna >= 0 && columna < 10) {
                    if (!tablero.yaDisparado(fila, columna)) {
                        return new int[] { fila, columna };
                    } else {
                        System.out.println("Ya disparaste en esa posición.");
                    }
                } else {
                    System.out.println("Coordenadas fuera de rango. Usa números del 0 al 9.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa números válidos.");
            }
        }
    }

    public void mostrarTableroPropio(Tablero tablero) {
        System.out.println("\n=== TU TABLERO ===");
        mostrarTablero(tablero.getTableroPropio());

    }
    
    public void mostrarTableroEnemigo(Tablero tablero) {
        System.out.println("\n=== TABLERO ENEMIGO ===");
        mostrarTablero(tablero.getTableroEnemigo());
    }
    
    public void mostrarTablero(char[][] tablero) {
        System.out.print("  ");
        for (int i = 0; i < tablero.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        for (int i = 0; i < tablero.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < tablero.length; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
        
        System.out.println("\nLeyenda: ~=Agua, ?=Desconocido, X=Impacto, O=Fallo, Letras=Barcos");
    }

    //metodo implementado
    public String obtenerNombreJugador(){
        System.out.println("Ingresa tu nombre: ");
        return scanner.nextLine();
    }

    @Override
    public void dibujar(Tablero tablero){
        mostrarTableroEnemigo(tablero);
        mostrarTableroPropio(tablero);
    }


    public String obtenerIpOponente(){
        System.out.println("\nIngresa la IP del otro jugador: ");
        return scanner.nextLine();
    }
}
