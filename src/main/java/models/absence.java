package models;

import java.util.Date;

public class absence {
    private int id_abs;
    private int cin;
    private Date date;
    private int nbr_abs;
    private String type;
    private String image_path;  // Ajout de l'attribut image_path

    // Constructeur
    public absence(int id_abs, int cin, Date date, int nbr_abs, String type, String image_path) {
        this.id_abs = id_abs;
        this.cin = cin;
        this.date = date;
        this.nbr_abs = nbr_abs;
        this.type = type;
        this.image_path = image_path;
    }
    public absence(int id_abs, int cin, Date date, int nbr_abs, String type) {
        this.id_abs = id_abs;
        this.cin = cin;
        this.date = date;
        this.nbr_abs = nbr_abs;
        this.type = type;
        this.image_path = null;  // Si aucune image n'est nécessaire, on met null.
    }
    // Getters et setters
    public int getId_abs() {
        return id_abs;
    }

    public void setId_abs(int id_abs) {
        this.id_abs = id_abs;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "absence{" +
                "id_abs=" + id_abs +
                ", cin=" + cin +
                ", date=" + date +
                ", nbr_abs=" + nbr_abs +
                ", type='" + type + '\'' +
                ", image_path='" + image_path + '\'' +
                '}';
    }
}