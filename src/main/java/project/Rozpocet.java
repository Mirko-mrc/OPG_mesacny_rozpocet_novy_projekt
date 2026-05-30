package project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Rozpocet {
    private final String nazov;
    private final List<RozpocetPolozka> polozky = new ArrayList<>();

    public Rozpocet(String nazov) {
        if (nazov == null || nazov.isBlank()) {
            throw new IllegalArgumentException("Nazov rozpoctu nemoze byt prazdny.");
        }
        this.nazov = nazov.trim();
    }

    public String getNazov() {
        return nazov;
    }

    public void pridaj(RozpocetPolozka polozka) {
        if (polozka == null) {
            throw new IllegalArgumentException("Polozka nemoze byt null.");
        }
        polozky.add(polozka);
    }

    public boolean odober(RozpocetPolozka polozka) {
        return polozky.remove(polozka);
    }

    public List<RozpocetPolozka> getPolozky() {
        return new ArrayList<>(polozky);
    }

    public List<RozpocetPolozka> getPolozkyPodlaSumyZostupne() {
        return polozky.stream()
                .sorted(Comparator.comparingDouble(RozpocetPolozka::getSuma).reversed())
                .toList();
    }

    public double getCelkovaSuma() {
        return polozky.stream().mapToDouble(RozpocetPolozka::getSuma).sum();
    }

    public Map<String, Double> getSuctyPodlaKategorie() {
        Map<String, Double> sucty = new TreeMap<>();
        for (RozpocetPolozka polozka : polozky) {
            sucty.merge(polozka.getKategoria(), polozka.getSuma(), Double::sum);
        }
        return sucty;
    }
}