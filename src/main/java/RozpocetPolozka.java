package main.java;

public class RozpocetPolozka extends Polozka {
    private double suma;

    public RozpocetPolozka() {
    }

    public RozpocetPolozka(String nazov, String kategoria, double suma) {
        super(nazov, kategoria);
        setSuma(suma);
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        if (suma < 0) {
            throw new IllegalArgumentException("Suma nemoze byt zaporna.");
        }
        this.suma = suma;
    }
}