package battleship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

//viene de juegobattleship
public class Tablero {
    private static final int TAMANIO_TABLERO = 10;
    private char[][] tableroPropio;
    private char[][] tableroEnemigo;
    private Set<String> posicionesDisparadas;
    //private Map<String, Integer> barcos;
    //private Map<String, Integer> impactosPorBarco;
    //reemplazados ya que existe la clase Barco, se agrega:
    private Set<Barco> barcos;

    public Tablero(){
        tableroPropio = new char[TAMANIO_TABLERO][TAMANIO_TABLERO];
        tableroEnemigo = new char[TAMANIO_TABLERO][TAMANIO_TABLERO];
        inicializarTableros();
        
        barcos = new HashSet<>();

        Map<String, Integer> tiposDeBarco= new HashMap<>();
        tiposDeBarco.put("PORTAAVIONES", 5);
        tiposDeBarco.put("ACORAZADO", 4);
        tiposDeBarco.put("CRUCERO", 3);
        tiposDeBarco.put("SUBMARINO", 3);
        tiposDeBarco.put("DESTRUCTOR", 2);
        
        //reemplazo de impactosPorBarco
        for (Map.Entry<String, Integer> entrada : tiposDeBarco.entrySet()) {
            barcos.add(new Barco(entrada.getKey(), entrada.getValue()));
        }
        
        posicionesDisparadas = new HashSet<>();
    }

    private void inicializarTableros() {
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            for (int j = 0; j < TAMANIO_TABLERO; j++) {
                tableroPropio[i][j] = '~'; // Agua
                tableroEnemigo[i][j] = '?'; // Desconocido
            }
        }
    }

    public void colocarBarcosAutomaticamente() {
        Random random = new Random();
        
        for (Barco b : barcos) {
            String nombreBarco = b.getTipo();
            int tamanio = b.getLongitud();
            boolean colocado = false;
            
            while (!colocado) {
                boolean horizontal = random.nextBoolean();
                int fila = random.nextInt(TAMANIO_TABLERO);
                int columna = random.nextInt(TAMANIO_TABLERO);
                
                if (puedeColocarBarco(fila, columna, tamanio, horizontal)) {
                    colocarBarco(fila, columna, tamanio, horizontal, nombreBarco.charAt(0));
                    colocado = true;
                }
            }
        }
    }

    private boolean puedeColocarBarco(int fila, int columna, int tamanio, boolean horizontal) {
        if (horizontal) {
            if (columna + tamanio > TAMANIO_TABLERO) return false;
            for (int i = columna; i < columna + tamanio; i++) {
                if (tableroPropio[fila][i] != '~') return false;
            }
        } else {
            if (fila + tamanio > TAMANIO_TABLERO) return false;
            for (int i = fila; i < fila + tamanio; i++) {
                if (tableroPropio[i][columna] != '~') return false;
            }
        }
        return true;
    }

    private void colocarBarco(int fila, int columna, int tamanio, boolean horizontal, char simbolo) {
        if (horizontal) {
            for (int i = columna; i < columna + tamanio; i++) {
                tableroPropio[fila][i] = simbolo;
            }
        } else {
            for (int i = fila; i < fila + tamanio; i++) {
                tableroPropio[i][columna] = simbolo;
            }
        }
    }

    //implementado en BARCO
    public boolean recibirDisparo(int fila, int columna) {
        // Verificar si ya fue disparado aquí
        if (tableroPropio[fila][columna] == 'X' || tableroPropio[fila][columna] == 'O') {
            return false; // Ya fue disparado aquí
        }
        
        if (tableroPropio[fila][columna] != '~') {
            // ¡Impacto! - encontrar qué barco fue golpeado
            char caracterBarco = tableroPropio[fila][columna];
            String tipoBarco = obtenerTipoBarcoDesdeCaracter(caracterBarco);
            
            //modificado para implementar la clase Barco
            for(Barco b : barcos){
                if(b.getTipo().equals(tipoBarco)){
                    b.recibirDisparo();
                }
            }
            
            tableroPropio[fila][columna] = 'X'; // Barco impactado
            return true;
        } else {
            tableroPropio[fila][columna] = 'O'; // Agua impactada
            return false;
        }
    }

    public void registrarImpacto(int fila, int columna) {
        tableroEnemigo[fila][columna] = 'X';
        posicionesDisparadas.add(fila + "," + columna);
    }

    public void registrarFallo(int fila, int columna) {
        tableroEnemigo[fila][columna] = 'O';
        posicionesDisparadas.add(fila + "," + columna);
    }

    public boolean yaDisparado(int fila, int columna) {
        return posicionesDisparadas.contains(fila + "," + columna);
    }

    public String obtenerTipoBarcoEn(int fila, int columna) {
        char c = tableroPropio[fila][columna];
        // Si es un impacto previo, buscar en la posición original
        if (c == 'X') {
            // En un juego real necesitarías guardar el tipo de barco original
            // Por ahora retornamos "DESCONOCIDO"
            return "DESCONOCIDO";
        }
        return obtenerTipoBarcoDesdeCaracter(c);
    }

    private String obtenerTipoBarcoDesdeCaracter(char c) {
        switch (c) {
            case 'P': return "PORTAAVIONES";
            case 'A': return "ACORAZADO";
            case 'C': return "CRUCERO";
            case 'S': return "SUBMARINO";
            case 'D': return "DESTRUCTOR";
            default: return "DESCONOCIDO";
        }
    }

    public boolean estaBarcoHundido(String tipoBarco) {
        //ajustado para implementar la clase Barco
        for(Barco b : barcos){
            if(b.getTipo().equals(tipoBarco)){
                return b.estaHundido();
            }
        }
        return false;
    }

    public boolean todosBarcosHundidos() {
        //ajustado
        for(Barco b : barcos){
            if(!b.estaHundido()){
                return false;
            }
        }
        return true;
    }

    
    
    public char[][] getTableroPropio(){
        return tableroPropio;
    }
    public char[][] getTableroEnemigo(){
        return tableroEnemigo;
    }

    //metodos auxiliares para las pruebas unitarias
    public void colocarBarcoTest(int r,int c, String tipo){
        tableroPropio[r][c]= tipo.charAt(0);
        //longitud para CRUCERO
        Barco b= new Barco(tipo, 3);
        barcos.add(b);
    }
    public void colocarBarcoUno(int r,int c,String tipo){
        tableroPropio[r][c]= tipo.charAt(0);
        Barco b= new Barco(tipo, 1);
        barcos.add(b);
    }
    public void limpiarBarcosParaTest() {
        this.barcos.clear(); 
    }
}
