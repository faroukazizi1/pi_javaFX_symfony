package models;

public class User {
    // Attributes
    private int id;
    private String nom;
    private String role; // Added role field

    // Static user for testing
    public static final User CURRENT_USER = new User(1, "Raoua15Moyenne", "admin"); // Badel el Role lahne :)

    // Constructors
    public User() {}

    public User(int id, String nom, String role) {
        this.id = id;
        this.nom = nom;
        this.role = role;
    }

    public User(String nom) {
        this.nom = nom;
        this.role = "user"; // Default role
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Helper methods
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }

    // Display
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}