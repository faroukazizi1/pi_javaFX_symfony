package Model;

public class user {

    private int id;
    private int cin;
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String password;
    private String role;
    private String sexe;
    private String adresse;
    private int numero;

    public user() {
    }


    public user(int id, int cin, String nom, String email, String prenom, String username, String password, String role, String sexe, String adresse, int numero) {
        this.id = id;
        this.cin = cin;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.sexe = sexe;
        this.adresse = adresse;
        this.numero = numero;
    }

    public user(int cin, String nom, String email, String prenom, String username, String password, String role, String sexe, String adresse, int numero) {
        this.cin = cin;
        this.nom = nom;
        this.email = email;
        this.prenom = prenom;
        this.username = username;
        this.password = password;
        this.role = role;
        this.sexe = sexe;
        this.adresse = adresse;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", cin=" + cin +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", sexe='" + sexe + '\'' +
                ", adresse='" + adresse + '\'' +
                ", numero='" + numero + '\'' +
                '}';
    }
}
