package battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BarcoTest {
    private String TIPO_BARCO="CRUCERO";
    private int LONGITUD= 3;
    private Barco crucero;

    @BeforeEach
    void iniciar(){
        crucero= new Barco(TIPO_BARCO, LONGITUD);
    }

    @Test
    void testEstaHundido() {
        //antes de impactar
        assertFalse(crucero.estaHundido());
        crucero.recibirDisparo();
        crucero.recibirDisparo();
        assertFalse(crucero.estaHundido());
        //tercer disparo
        crucero.recibirDisparo();
        assertTrue(crucero.estaHundido());
    }

    @Test
    void testGetImpactosRecibidos() {
        //estado inicial
        assertEquals(0, crucero.getImpactosRecibidos());
        crucero.recibirDisparo();
        crucero.recibirDisparo();
        //verificar impactos
        assertEquals(2, crucero.getImpactosRecibidos());
    }

    @Test
    void testGetLongitud() {
        assertEquals(LONGITUD, crucero.getLongitud());
    }

    @Test
    void testGetTipo() {
        assertEquals(TIPO_BARCO, crucero.getTipo());
    }
}
