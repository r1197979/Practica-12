package battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TableroTest {
    Tablero tablero;
    String tipoBarco= "CRUCERO";
    int x=0;
    int y=0;

    @BeforeEach
    void iniciar(){
        tablero = new Tablero();
        tablero.colocarBarcosAutomaticamente();
    }

    @Test
    void testEstaBarcoHundido() {
        tablero.limpiarBarcosParaTest();
        tablero.colocarBarcoUno(3,3,"CRUCERO");
        String tipo = "CRUCERO";
        //estado inicial
        assertFalse(tablero.estaBarcoHundido(tipo));
        tablero.recibirDisparo(3, 3);
        assertTrue(tablero.estaBarcoHundido(tipo));
    }

    @Test
    void testGetTableroEnemigo() {
        assertNotNull(tablero.getTableroEnemigo());
    }

    @Test
    void testGetTableroPropio() {
        assertNotNull(tablero.getTableroPropio());
    }

    @Test
    void testObtenerTipoBarcoEn() {
        boolean barcoEncontrado= false;

        //asumir posicion vacia
        assertEquals("DESCONOCIDO", tablero.obtenerTipoBarcoEn(4,5));

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                //obtener que hay en cada posicion
                String tipo= tablero.obtenerTipoBarcoEn(i, j);
                //si no es agua o vacio es un barco
                if(!tipo.equals("AGUA")&&!tipo.equals("DESCONOCIDO")){
                    barcoEncontrado= true;
                }
            }
        }

        assertTrue(barcoEncontrado);
    }

    @Test
    void testRecibirDisparo() {
        tablero.colocarBarcoTest(3,3,"CRUCERO");
        boolean impactado= tablero.recibirDisparo(3, 3);
        assertTrue(impactado);

        boolean fallo= tablero.recibirDisparo(2, 8);
        assertFalse(fallo);
    }

    @Test
    void testTodosBarcosHundidos() {
        assertFalse(tablero.todosBarcosHundidos());
    }

    @Test
    void testYaDisparado() {
        //impactos
        tablero.registrarImpacto(2, 5);
        assertTrue(tablero.yaDisparado(2, 5));
        //fallos tambien son marcados como disparados
        tablero.registrarFallo(3, 8);
        assertTrue(tablero.yaDisparado(3, 8));

        assertFalse(tablero.yaDisparado(0, 0));
    }
}
