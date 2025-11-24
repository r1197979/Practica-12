package battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JugadorTest {
    @Test
    void testEsServidor() {
        Jugador servidor = new Jugador("servidor", true);
        assertTrue(servidor.esServidor());

        Jugador cliente= new Jugador("cliente", false);
        assertFalse(cliente.esServidor());
    }

    @Test
    void testGetNombre() {
        //jugador servidor
        String servidor= "servidor";
        Jugador j1= new Jugador(servidor,true);
        assertEquals(servidor, j1.getNombre());
        //jugador que es cliente
        String cliente= "cliente";
        Jugador j2= new Jugador(cliente, false);
        assertEquals(cliente, j2.getNombre());
    }
}
