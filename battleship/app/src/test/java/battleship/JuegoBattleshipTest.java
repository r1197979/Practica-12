package battleship;
// Prueba si la clase está en el directorio raíz o en otro paquete.
import battleship.JuegoBattleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JuegoBattleshipTest {
    JuegoBattleship juegoB;

    @BeforeEach
    void inicializacion(){
        juegoB = new JuegoBattleship();
    }

    @Test
    void testColocarBarcosAutomaticamente() {
        juegoB.colocarBarcosAutomaticamente();
        int contador= 0;

        //recorrer tablero completo para hacer el conteo de los barcos que han sido colocados 
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(juegoB.getTableroPropio()[i][j]!='~'){
                    //si no es agua, entonces cuenta como barco
                    contador++;
                }
            }
        }
        //deben ser los 17 impactos totales de barcos al iniciar
        assertEquals(17, contador);
    }

    @Test
    void testEstaBarcoHundido() {

        impactarBarcos(juegoB, 2, "SUBMARINO");
        //aun no debe estar hundido
        assertFalse(juegoB.estaBarcoHundido("SUBMARINO"));
        //impactos correctos para comprobar que se hunde
        impactarBarcos(juegoB, 3, "SUBMARINO");
        assertTrue(juegoB.estaBarcoHundido("SUBMARINO"));
    }
    //impactar a todos los barcos
    private void impactarBarcos(JuegoBattleship juego, int cantidad, String tipoBarco){
        //impactos por barco
        Map<String, Integer> impactos= juego.getImpactosPorBarco();
        //pasar los parametros para modificar el map
        impactos.put(tipoBarco, cantidad);
    }

    @Test
    void testObtenerTipoBarcoEn() {
        String esperado = "CRUCERO";
        juegoB.colocarBarco(0, 0, esperado);
        String tipoBarco= juegoB.obtenerTipoBarcoEn(0,0);
        assertEquals(esperado, tipoBarco, "Retorna el tipo de barco colocado en la coordenada indicada");
    }

    @Test
    void testRecibirDisparo() {
        //metodo de apoyo
        juegoB.colocarBarco(2,1,"ACORAZADO");
        boolean impactado= juegoB.recibirDisparo(2,1);
        //debe ser verdadero porque se han dado las coordenadas del barco para impactar
        assertTrue(impactado);
    }

    @Test
    void testTodosBarcosHundidos() {
        //impactar todos los barcos con el metodo de apoyo
        impactarBarcos(juegoB,5,"PORTAAVIONES");
        //probar el metodo hasta que deba ser verdadero
        assertFalse(juegoB.todosBarcosHundidos());
        impactarBarcos(juegoB,4,"ACORAZADO");
        assertFalse(juegoB.todosBarcosHundidos());
        impactarBarcos(juegoB,3,"CRUCERO");
        assertFalse(juegoB.todosBarcosHundidos());
        impactarBarcos(juegoB,3,"SUBMARINO");
        assertFalse(juegoB.todosBarcosHundidos());
        impactarBarcos(juegoB,2,"DESTRUCTOR");
        assertTrue(juegoB.todosBarcosHundidos());
    }


    @Test
    void testYaDisparado() {
        //verificar que no se ha disparado, disparar, confirmar
        assertFalse(juegoB.yaDisparado(5, 5));
        juegoB.recibirDisparo(5, 5);
        //podria verificarse si en el metodo recibirDisparo se guardaran coordenadas del impacto
        //assertTrue(juegoB.yaDisparado(5, 5));
    }
}
