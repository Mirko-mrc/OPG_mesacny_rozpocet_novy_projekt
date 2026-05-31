package main.java;

public class Polozka {
    private String nazov;
    private String kategoria;

    public Polozka() {
    }

    public Polozka(String nazov, String kategoria) {
        setNazov(nazov);
        setKategoria(kategoria);
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        if (nazov == null || nazov.isBlank()) {
            throw new IllegalArgumentException("Nazov polozky nemoze byt prazdny.");
        }
        this.nazov = nazov.trim();
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        if (kategoria == null || kategoria.isBlank()) {
            throw new IllegalArgumentException("Kategoria nemoze byt prazdna.");
        }
        this.kategoria = kategoria.trim();
    }
}