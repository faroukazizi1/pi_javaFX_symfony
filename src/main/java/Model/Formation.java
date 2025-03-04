    package Model;

    import java.sql.Date;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;

    public class Formation {
        private int id_form, Duree, id_Formateur;
        private String Titre, Description, Image;
        private Date Date_D, Date_F;

        // Constructeurs
        public Formation() {}

        public Formation(int id_form, String Titre, String Description, Date Date_D, Date Date_F, int Duree, String Image, int id_Formateur) {
            this.id_form = id_form;
            this.Titre = Titre;
            this.Description = Description;
            this.Date_D = Date_D;
            this.Date_F = Date_F;
            this.Duree = Duree;
            this.Image = Image;
            this.id_Formateur = id_Formateur;
        }

        public Formation(String Titre, String Description, Date Date_D, Date Date_F, int Duree, String Image, int id_Formateur) {
            this.Titre = Titre;
            this.Description = Description;
            this.Date_D = Date_D;
            this.Date_F = Date_F;
            this.Duree = Duree;
            this.Image = Image;
            this.id_Formateur = id_Formateur;
        }

        // Getters et Setters
        public int getId_form() {
            return id_form;
        }

        public void setId_form(int id_form) {
            this.id_form = id_form;
        }

        public int getDuree() {
            return Duree;
        }



        public void setDuree(int duree) {
            this.Duree = duree;
        }

        public String getTitre() {
            return Titre;
        }

        public void setTitre(String titre) {
            this.Titre = titre;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            this.Description = description;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            this.Image = image;
        }

        public Date getDate_D() {
            return Date_D;
        }

        public void setDate_D(Date date_D) {
            this.Date_D = date_D;
        }

        public Date getDate_F() {
            return Date_F;
        }

        public void setDate_F(Date date_F) {
            this.Date_F = date_F;
        }

        public int getId_Formateur() {
            return id_Formateur;
        }

        public void setId_Formateur(int id_Formateur) {
            this.id_Formateur = id_Formateur;
        }

        // Méthodes pour convertir Date en String
        public String getFormattedDate_D() {
            return (Date_D != null) ? new SimpleDateFormat("yyyy-MM-dd").format(Date_D) : null;
        }

        public String getFormattedDate_F() {
            return (Date_F != null) ? new SimpleDateFormat("yyyy-MM-dd").format(Date_F) : null;
        }

        // Méthodes pour convertir String en Date
        public static Date convertStringToDate(String dateStr) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(dateStr);
                return new Date(utilDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String toString() {
            return "Formation{" +
                    "id_form=" + id_form +
                    ", Duree=" + Duree +
                    ", Titre='" + Titre + '\'' +
                    ", Description='" + Description + '\'' +
                    ", Image='" + Image + '\'' +
                    ", Date_D=" + Date_D +
                    ", Date_F=" + Date_F +
                    ", id_Formateur=" + id_Formateur +
                    '}';
        }
    }
