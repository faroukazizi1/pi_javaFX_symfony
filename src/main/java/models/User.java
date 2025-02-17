package models;

public class User {
    // Attributes
    private int id;
    private String nom;

    // Constructors
    public User() {}

    public User(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public User(String nom) {
        this.nom = nom;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Display
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}
