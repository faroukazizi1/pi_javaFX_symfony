package models;

public class penalite {
    private int id_pen;
    private String type;
    private int seuil_abs;
    private int cin;  // Remplacer l'id_absence par cin

    // Constructeur avec cin
    public penalite(int id_pen, String type, int seuil_abs, int cin) {
        this.id_pen = id_pen;
        this.type = type;
        this.seuil_abs = seuil_abs;
        this.cin = cin;
    }
    // Nouveau constructeur sans id_pen
    public penalite(String type, int seuil_abs, int cin) {
        this.type = type;
        this.seuil_abs = seuil_abs;
        this.cin = cin;
        this.id_pen = 0;  // Ou n'importe quelle valeur par défaut, si nécessaire
    }

    // Getters et Setters
    public int getId_pen() {
        return id_pen;
    }

    public void setId_pen(int id_pen) {
        this.id_pen = id_pen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeuil_abs() {
        return seuil_abs;
    }

    public void setSeuil_abs(int seuil_abs) {
        this.seuil_abs = seuil_abs;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    @Override
    public String toString() {
        return "Penalite{" +
                "id_pen=" + id_pen +
                ", type='" + type + '\'' +
                ", seuil_abs=" + seuil_abs +
                ", cin=" + cin +
                '}';
    }
}