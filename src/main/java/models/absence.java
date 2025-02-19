package models;

import java.util.Date;

public class absence {
    private int id_abs;
    private Date date;
    private int nbr_abs;
    private String type;

    public absence(int id_abs, Date date, int nbr_abs, String type) {
        this.id_abs = id_abs;
        this.date = date;
        this.nbr_abs = nbr_abs;
        this.type = type;

    }

    // Getters et Setters
    public int getId_abs() {
        return id_abs;
    }

    public void setId_abs(int id_abs) {
        this.id_abs = id_abs;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNbr_abs() {
        return nbr_abs;
    }

    public void setNbr_abs(int nbr_abs) {
        this.nbr_abs = nbr_abs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Absence{" +
                "id_abs=" + id_abs +
                ", date=" + date +
                ", nbr_abs=" + nbr_abs +
                ", type='" + type + '\'' +
                '}';
    }
}

