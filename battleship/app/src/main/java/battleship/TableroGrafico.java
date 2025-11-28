package battleship;

import javax.swing.*;
import java.awt.*;

public class TableroGrafico implements ITablero {

    private static final int REN = 10;
    private static final int COL = 10;
    private JFrame frame; 
    private JPanel panelEnemigo;
    private JPanel panelPropio;
    private JButton[][] casillasEnemigo;
    private JButton[][] casillasLocal;
    private int[] disparoSeleccionado;

    public TableroGrafico() {
        crearVentana();
        crearTableros();
        agregarTableros();
    }



    private void crearVentana() {
        frame = new JFrame("Juego Battleship");
        frame.setSize(800, 400);
        frame.setLayout(new GridLayout(1, 2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }

    private void crearTableros() {

        casillasEnemigo = new JButton[REN][COL];
        casillasLocal = new JButton[REN][COL];

        panelEnemigo = new JPanel(new GridLayout(REN, COL));
        panelEnemigo.setBorder(BorderFactory.createTitledBorder("ENEMIGO"));
        panelPropio = new JPanel(new GridLayout(REN, COL)); 
        panelPropio.setBorder(BorderFactory.createTitledBorder("LOCAL"));

        for (int i=0; i< REN; i++) {
            for (int j=0; j<COL; j++) {

                JButton botonEnemigo = new JButton("?");
                int filaSeleccionada= i;
                int colSeleccionada= j;

                botonEnemigo.addActionListener(e -> {
                    if (disparoSeleccionado == null) {
                        disparoSeleccionado= new int[2];
                        disparoSeleccionado[0]= filaSeleccionada;
                        disparoSeleccionado[1]= colSeleccionada;
                        botonEnemigo.setBackground(Color.YELLOW);
                    }
                });
                casillasEnemigo[i][j] = botonEnemigo;
                panelEnemigo.add(botonEnemigo);

                JButton botonPropio = new JButton("~");
                casillasLocal[i][j] = botonPropio;
                panelPropio.add(botonPropio);
            }
        }
    }

    private void agregarTableros() {
        frame.add(panelEnemigo);
        frame.add(panelPropio);
        frame.setVisible(true);
    }

    @Override
    public void dibujar(Tablero tablero) {

        char[][] local = tablero.getTableroPropio();
        char[][] enemigo = tablero.getTableroEnemigo();

        for (int fila = 0; fila < REN; fila++) {
            for (int col = 0; col < COL; col++) {
                casillasLocal[fila][col].setText("" + local[fila][col]);
                casillasEnemigo[fila][col].setText("" + enemigo[fila][col]);

                char valor = enemigo[fila][col];
                if (valor == 'X') {
                    casillasEnemigo[fila][col].setBackground(Color.PINK);
                    casillasEnemigo[fila][col].setOpaque(true);
                }
                else if (valor == 'O') {
                    casillasEnemigo[fila][col].setBackground(Color.LIGHT_GRAY);
                    casillasEnemigo[fila][col].setOpaque(true);
                }
                else {
                    casillasEnemigo[fila][col].setBackground(null);
                    casillasEnemigo[fila][col].setOpaque(true);
                }
                    }
                }
    }

    @Override
    public int[] obtenerDisparoJugador(Tablero tablero) {
        disparoSeleccionado = null;

        JOptionPane.showMessageDialog(frame,"Tu turno");
        while (disparoSeleccionado == null) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
        return disparoSeleccionado;
    }
}
