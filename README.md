# Mesačný Správca Rozpočtu

## Popis

Táto aplikácia slúži na správu mesačných výdavkov pomocou grafického rozhrania JavaFX. Používateľ môže načítať pripravené rozpočty zo CSV súborov, pridávať nové položky, odstraňovať existujúce položky, filtrovať ich podľa kategórie a ukladať zmeny späť do súboru.

## Funkcie

- Načítanie rozpočtu zo CSV súboru
- Uloženie rozpočtu do CSV súboru
- Pridávanie nových položiek
- Odstraňovanie položiek
- Výpočet celkovej sumy výdavkov
- Výpočet súčtov podľa kategórií
- Filtrovanie položiek podľa kategórie
- Grafické používateľské rozhranie JavaFX
- Viacero vzorových profilov rozpočtu

## Použitie

1. Otvorte projekt v IntelliJ IDEA.
2. Načítajte Maven závislosti.
3. Spustite triedu:

```java
main.java.Main
```

4. Vyberte profil rozpočtu.
5. Kliknite na tlačidlo **Načítať**.
6. Pridávajte, odstraňujte alebo upravujte položky.
7. Zmeny uložte tlačidlom **Uložiť**.

## Popis tried

### BudgetController
Riadi používateľské rozhranie, spracúva udalosti tlačidiel a aktualizuje zobrazené údaje.

### CsvRozpocetRepository
Zabezpečuje načítanie a uloženie rozpočtu vo formáte CSV.

### Main
Hlavný vstupný bod aplikácie.

### RozpocetApp
Spúšťa JavaFX aplikáciu a načítava FXML rozhranie.

### Polozka
Základná trieda obsahujúca názov a kategóriu položky.

### RozpocetPolozka
Rozširuje triedu Polozka o finančnú hodnotu položky.

### Rozpocet
Obsahuje zoznam položiek a vykonáva výpočty nad rozpočtom.

## Grafické súbory

### primary.fxml
Definuje používateľské rozhranie aplikácie.

### styles.css
Hlavná farebná téma aplikácie.

### styles-secondary.css
Alternatívna farebná téma aplikácie.

## Dátové súbory

Adresár `data/` obsahuje vzorové rozpočty:

- `rodina.csv`
- `student.csv`
- `dochodca.csv`
- `slobodny_muz.csv`

Každý súbor obsahuje položky vo formáte:

```text
kategoria,nazov,suma
```

Príklad:

```text
Byvanie,Najom,450
Potraviny,Nakup,180
Doprava,Palivo,80
```

## Diagramy

- Vývojový diagram pridania položky (Mermaid)

## Požiadavky

- JDK 21 alebo novší
- Maven
- JavaFX 21

## Autor

Tento projekt bol vytvorený nasledujúcimi členmi: M.Duranik, N.Lipták, Š.Kordiak.