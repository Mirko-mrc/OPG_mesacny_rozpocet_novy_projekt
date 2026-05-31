package main.java;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

public class BudgetController {
    private static final String VSETKY_KATEGORIE = "Vsetky kategorie";
    private final CsvRozpocetRepository repository = new CsvRozpocetRepository();
    private final ObservableList<RozpocetPolozka> polozky = FXCollections.observableArrayList();
    private final FilteredList<RozpocetPolozka> filtrovane = new FilteredList<>(polozky);
    private final NumberFormat mena = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("sk-SK"));
    private Rozpocet rozpocet;
    private Path aktualnySubor;

    @FXML private ComboBox<String> budgetComboBox, filterComboBox;
    @FXML private TableView<RozpocetPolozka> itemsTable;
    @FXML private TableColumn<RozpocetPolozka, String> nameColumn, categoryColumn, amountColumn;
    @FXML private TextField nameField, categoryField, amountField;
    @FXML private ListView<String> categoryTotalsList;
    @FXML private Label currentBudgetLabel, itemCountLabel, totalAmountLabel, statusLabel;

    @FXML
    private void initialize() {
        budgetComboBox.getItems().addAll("rodina", "student", "dochodca", "slobodny_muz");
        budgetComboBox.setValue("rodina");
        filterComboBox.setOnAction(event -> filtruj());
        nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNazov()));
        categoryColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKategoria()));
        amountColumn.setCellValueFactory(p -> new SimpleStringProperty(mena.format(p.getValue().getSuma())));
        amountColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        itemsTable.setItems(filtrovane);
        obnovPrehlad();
        nastavStatus("Vyber rozpocet a nacitaj data.");
    }

    @FXML
    private void handleLoadBudget() {
        String nazov = budgetComboBox.getValue();
        try {
            aktualnySubor = Path.of("data", nazov + ".csv");
            rozpocet = repository.nacitaj(aktualnySubor, nazov);
            polozky.setAll(rozpocet.getPolozky());
            nastavStatus("Rozpocet bol nacitany.");
            obnovPrehlad();
        } catch (IOException e) {
            chyba("Subor sa nepodarilo nacitat.");
        }
    }

    @FXML
    private void handleAdd() {
        if (!jeNacitanyRozpocet()) return;
        String nazov = nameField.getText().trim();
        String kategoria = categoryField.getText().trim();
        String sumaText = amountField.getText().trim().replace(',', '.');
        if (nazov.isEmpty() || kategoria.isEmpty() || sumaText.isEmpty()) {
            chyba("Vypln nazov, kategoriu aj sumu.");
            return;
        }
        try {
            RozpocetPolozka polozka = new RozpocetPolozka(nazov, kategoria, Double.parseDouble(sumaText));
            rozpocet.pridaj(polozka);
            polozky.add(polozka);
            nameField.clear(); categoryField.clear(); amountField.clear();
            nastavStatus("Polozka bola pridana.");
            obnovPrehlad();
        } catch (NumberFormatException e) {
            chyba("Suma musi byt cislo.");
        } catch (IllegalArgumentException e) {
            chyba(e.getMessage());
        }
    }

    @FXML
    private void handleRemove() {
        if (!jeNacitanyRozpocet()) return;
        RozpocetPolozka vybrana = itemsTable.getSelectionModel().getSelectedItem();
        if (vybrana == null) {
            chyba("Najprv vyber polozku v tabulke.");
            return;
        }
        rozpocet.odober(vybrana);
        polozky.remove(vybrana);
        nastavStatus("Polozka bola odstranena.");
        obnovPrehlad();
    }

    @FXML
    private void handleSave() {
        if (!jeNacitanyRozpocet()) return;
        try {
            repository.uloz(aktualnySubor, rozpocet);
            nastavStatus("Rozpocet bol ulozeny.");
        } catch (IOException e) {
            chyba("Subor sa nepodarilo ulozit.");
        }
    }

    @FXML
    private void handleClearFilter() {
        filterComboBox.setValue(VSETKY_KATEGORIE);
        filtruj();
    }

    private void obnovPrehlad() {
        currentBudgetLabel.setText(rozpocet == null ? "Nevybrany" : rozpocet.getNazov());
        itemCountLabel.setText(String.valueOf(polozky.size()));
        totalAmountLabel.setText(mena.format(rozpocet == null ? 0 : rozpocet.getCelkovaSuma()));
        TreeSet<String> kategorie = new TreeSet<>();
        for (RozpocetPolozka polozka : polozky) kategorie.add(polozka.getKategoria());
        ObservableList<String> filter = FXCollections.observableArrayList();
        filter.add(VSETKY_KATEGORIE); filter.addAll(kategorie);
        String povodna = filterComboBox.getValue();
        filterComboBox.setItems(filter);
        filterComboBox.setValue(filter.contains(povodna) ? povodna : VSETKY_KATEGORIE);
        ObservableList<String> sucty = FXCollections.observableArrayList();
        if (rozpocet != null) {
            for (Map.Entry<String, Double> entry : rozpocet.getSuctyPodlaKategorie().entrySet()) {
                sucty.add(entry.getKey() + "    " + mena.format(entry.getValue()));
            }
        }
        categoryTotalsList.setItems(sucty.isEmpty() ? FXCollections.observableArrayList("Ziadne polozky") : sucty);
        filtruj();
    }

    private void filtruj() {
        String kategoria = filterComboBox.getValue();
        filtrovane.setPredicate(p -> kategoria == null || VSETKY_KATEGORIE.equals(kategoria) || p.getKategoria().equals(kategoria));
    }

    private boolean jeNacitanyRozpocet() {
        if (rozpocet == null) {
            chyba("Najprv nacitaj rozpocet.");
            return false;
        }
        return true;
    }

    private void nastavStatus(String text) { statusLabel.setText(text); }
    private void chyba(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING, text);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}