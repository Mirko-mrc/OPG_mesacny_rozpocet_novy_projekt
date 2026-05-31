package main.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvRozpocetRepository {
    public Rozpocet nacitaj(Path subor, String nazovRozpoctu) throws IOException {
        Rozpocet rozpocet = new Rozpocet(nazovRozpoctu);
        if (!Files.exists(subor)) {
            return rozpocet;
        }

        try (BufferedReader reader = Files.newBufferedReader(subor)) {
            reader.readLine();
            String riadok;
            while ((riadok = reader.readLine()) != null) {
                if (riadok.isBlank()) {
                    continue;
                }
                List<String> hodnoty = rozdelCsv(riadok);
                if (hodnoty.size() != 3) {
                    throw new IOException("Neplatny CSV riadok: " + riadok);
                }
                rozpocet.pridaj(new RozpocetPolozka(hodnoty.get(1), hodnoty.get(0), Double.parseDouble(hodnoty.get(2))));
            }
        }
        return rozpocet;
    }

    public void uloz(Path subor, Rozpocet rozpocet) throws IOException {
        Path parent = subor.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(subor)) {
            writer.write("kategoria,nazov,suma");
            writer.newLine();
            for (RozpocetPolozka p : rozpocet.getPolozky()) {
                writer.write(escape(p.getKategoria()) + "," + escape(p.getNazov()) + "," + p.getSuma());
                writer.newLine();
            }
        }
    }

    private List<String> rozdelCsv(String riadok) {
        List<String> hodnoty = new ArrayList<>();
        StringBuilder aktualna = new StringBuilder();
        boolean uvodzovky = false;
        for (int i = 0; i < riadok.length(); i++) {
            char znak = riadok.charAt(i);
            if (znak == '"') {
                if (uvodzovky && i + 1 < riadok.length() && riadok.charAt(i + 1) == '"') {
                    aktualna.append('"');
                    i++;
                } else {
                    uvodzovky = !uvodzovky;
                }
            } else if (znak == ',' && !uvodzovky) {
                hodnoty.add(aktualna.toString());
                aktualna.setLength(0);
            } else {
                aktualna.append(znak);
            }
        }
        hodnoty.add(aktualna.toString());
        return hodnoty;
    }

    private String escape(String hodnota) {
        if (hodnota.contains(",") || hodnota.contains("\"")) {
            return "\"" + hodnota.replace("\"", "\"\"") + "\"";
        }
        return hodnota;
    }
}