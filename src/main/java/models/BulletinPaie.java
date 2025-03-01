package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BulletinPaie {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty employeId;
    private SimpleStringProperty mois;
    private SimpleIntegerProperty annee;
    private SimpleObjectProperty<BigDecimal> salaireBrut;
    private SimpleObjectProperty<BigDecimal> deductions;
    private SimpleObjectProperty<BigDecimal> salaireNet;
    private SimpleObjectProperty<LocalDateTime> dateGeneration;

    public BulletinPaie(int id, int employeId, String mois, int annee, BigDecimal salaireBrut, BigDecimal deductions, BigDecimal salaireNet, LocalDateTime dateGeneration) {
        this.id = new SimpleIntegerProperty(id);
        this.employeId = new SimpleIntegerProperty(employeId);
        this.mois = new SimpleStringProperty(mois);
        this.annee = new SimpleIntegerProperty(annee);
        this.salaireBrut = new SimpleObjectProperty<>(salaireBrut);
        this.deductions = new SimpleObjectProperty<>(deductions);
        this.salaireNet = new SimpleObjectProperty<>(salaireNet);
        this.dateGeneration = new SimpleObjectProperty<>(dateGeneration);
    }

    public BulletinPaie() {
        this.id = new SimpleIntegerProperty(0);
        this.employeId = new SimpleIntegerProperty(0);
        this.mois = new SimpleStringProperty("");
        this.annee = new SimpleIntegerProperty(0);
        this.salaireBrut = new SimpleObjectProperty<>(BigDecimal.ZERO);
        this.deductions = new SimpleObjectProperty<>(BigDecimal.ZERO);
        this.salaireNet = new SimpleObjectProperty<>(BigDecimal.ZERO);
        this.dateGeneration = new SimpleObjectProperty<>(LocalDateTime.now());
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleIntegerProperty employeIdProperty() {
        return employeId;
    }

    public SimpleStringProperty moisProperty() {
        return mois;
    }

    public SimpleIntegerProperty anneeProperty() {
        return annee;
    }

    public SimpleObjectProperty<BigDecimal> salaireBrutProperty() {
        return salaireBrut;
    }

    public SimpleObjectProperty<BigDecimal> deductionsProperty() {
        return deductions;
    }

    public SimpleObjectProperty<BigDecimal> salaireNetProperty() {
        return salaireNet;
    }

    public SimpleObjectProperty<LocalDateTime> dateGenerationProperty() {
        return dateGeneration;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getEmployeId() {
        return employeId.get();
    }

    public void setEmployeId(int employeId) {
        this.employeId.set(employeId);
    }

    public String getMois() {
        return mois.get();
    }

    public void setMois(String mois) {
        this.mois.set(mois);
    }

    public int getAnnee() {
        return annee.get();
    }

    public void setAnnee(int annee) {
        this.annee.set(annee);
    }

    public BigDecimal getSalaireBrut() {
        return salaireBrut.get();
    }

    public void setSalaireBrut(BigDecimal salaireBrut) {
        this.salaireBrut.set(salaireBrut);
    }

    public BigDecimal getDeductions() {
        return deductions.get();
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions.set(deductions);
    }

    public BigDecimal getSalaireNet() {
        return salaireNet.get();
    }

    public void setSalaireNet(BigDecimal salaireNet) {
        this.salaireNet.set(salaireNet);
    }

    public LocalDateTime getDateGeneration() {
        return dateGeneration.get();
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration.set(dateGeneration);
    }
}
