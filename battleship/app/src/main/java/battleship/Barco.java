package battleship;

public class Barco {
    //atributos de juegobattleship
    private String tipo;
    private int longitud;
    private int impactosRecibidos;

    public Barco(String t, int l){
        tipo = t;
        longitud = l;
    }

    public void recibirDisparo(){
        impactosRecibidos++;
    }

    public boolean estaHundido(){
        return impactosRecibidos >= longitud;
    }

    public String getTipo(){
        return tipo;
    }
    public int getLongitud(){
        return longitud;
    }
    public int getImpactosRecibidos(){
        return impactosRecibidos;
    }
}
