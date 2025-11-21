package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocoloBattleshipTest {

    ProtocoloBattleship protocolo;

    @BeforeEach
    void iniciar(){
        protocolo = new ProtocoloBattleship();
    }

    @Test
    void testConstruirMensajeDisparo() {
        //llamar metodo y verificar que no se encuentre vacio
        String mensaje= protocolo.construirMensajeDisparo(3, 5);
        assertNotNull(mensaje);

        String mensajeObtenido= protocolo.construirMensajeDisparo(4, 3);
        String mensajeEsperado= "DISPARAR|4,3";
        assertEquals(mensajeEsperado, mensajeObtenido);
    }

    @Test
    void testConstruirMensajeResultado() {
        String fallo= "FALLO|3,3|CRUCERO";
        String obtenidoFallo= protocolo.construirMensajeResultado("FALLO", 3, 3, "CRUCERO");
        assertEquals(fallo, obtenidoFallo); 

        String impacto= "IMPACTO|2,6|SUBMARINO";
        String obtenidoImpacto= protocolo.construirMensajeResultado("IMPACTO", 2, 6, "SUBMARINO");
        assertEquals(impacto, obtenidoImpacto);

        String hundido = "HUNDIDO|3,7|ACORAZADO";
        String obtenidoHundido= protocolo.construirMensajeResultado("HUNDIDO",3,7,"ACORAZADO");
        assertEquals(hundido, obtenidoHundido);
    }

    @Test
    void testParsearMensaje() {
        String disparo= "DISPARAR|5,8";
        ProtocoloBattleship.Mensaje mensaje= protocolo.parsearMensaje(disparo);
        //probando case DISPARAR
        assertEquals(5, mensaje.x);
        assertEquals(8,mensaje.y);

        String hundido= "HUNDIDO|9,0|CRUCERO";
        ProtocoloBattleship.Mensaje mensaje2= protocolo.parsearMensaje(hundido);
        //case HUNDIDO
        assertEquals(9,mensaje2.x);
        assertEquals(0,mensaje2.y);
    }
}
