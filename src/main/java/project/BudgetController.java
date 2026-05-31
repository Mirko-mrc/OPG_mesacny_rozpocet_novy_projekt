package project;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;

public class BudgetController {
    private final CsvRozpocetRepository repository = new CsvRozpocetRepository();
    private final ObservableList<RozpocetPolozka> polozky = FXCollections.observableArrayList();
    private final NumberFormat mena = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("sk-SK"));
    private Rozpocet rozpocet;

    @FXML private ComboBox<String> budgetComboBox;
    @FXML private TableView<RozpocetPolozka> itemsTable;
    @FXML private TableColumn<RozpocetPolozka, String> nameColumn;
    @FXML private TableColumn<RozpocetPolozka, String> categoryColumn;
    @FXML private TableColumn<RozpocetPolozka, String> amountColumn;
    @FXML private Label currentBudgetLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        budgetComboBox.getItems().addAll("rodina", "student");
        budgetComboBox.setValue("rodina");
        nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNazov()));
        categoryColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKategoria()));
        amountColumn.setCellValueFactory(p -> new SimpleStringProperty(mena.format(p.getValue().getSuma())));
        amountColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        itemsTable.setItems(polozky);
        statusLabel.setText("Vyber rozpocet a nacitaj data.");
    }

    @FXML
    private void handleLoadBudget() {
        String nazov = budgetComboBox.getValue();
        try {
            rozpocet = repository.nacitaj(Path.of("data", nazov + ".csv"), nazov);
            polozky.setAll(rozpocet.getPolozky());
            currentBudgetLabel.setText(nazov);
            totalAmountLabel.setText(mena.format(rozpocet.getCelkovaSuma()));
            statusLabel.setText("Rozpocet bol nacitany.");
        } catch (IOException e) {
            statusLabel.setText("Subor sa nepodarilo nacitat.");
        }
    }
}