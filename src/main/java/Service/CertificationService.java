package Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.time.LocalDate;

import Util.DBconnection;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class CertificationService {

    private Connection conn;

    public CertificationService() {
        this.conn = DBconnection.getInstance().getConn();
    }

    public boolean utilisateurExiste(int userId) {
        String query = "SELECT COUNT(*) FROM user WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void genererCertification(int userId, int formationId) {
        if (!utilisateurExiste(userId)) {
            System.out.println("❌ Utilisateur non trouvé !");
            return;
        }

        String query = "SELECT f.Titre, f.Duree, f.id_Formateur, f.Date_F FROM formation f WHERE f.id_form = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, formationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String titreFormation = rs.getString("Titre");
                int duree = rs.getInt("Duree");
                int formateurId = rs.getInt("id_Formateur");

                String formateurQuery = "SELECT Nom_F, email FROM formateur WHERE id_formateur = ?";
                try (PreparedStatement formateurPstmt = conn.prepareStatement(formateurQuery)) {
                    formateurPstmt.setInt(1, formateurId);
                    ResultSet formateurRs = formateurPstmt.executeQuery();
                    if (formateurRs.next()) {
                        String nomFormateur = formateurRs.getString("Nom_F");

                        String insertQuery = "INSERT INTO certification (user_id, formation_id, date_certification, titre_formation, duree, formateur_id) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertPstmt = conn.prepareStatement(insertQuery)) {
                            insertPstmt.setInt(1, userId);
                            insertPstmt.setInt(2, formationId);
                            insertPstmt.setDate(3, Date.valueOf(LocalDate.now()));
                            insertPstmt.setString(4, titreFormation);
                            insertPstmt.setInt(5, duree);
                            insertPstmt.setInt(6, formateurId);
                            insertPstmt.executeUpdate();
                            System.out.println("✅ Certification enregistrée dans la base !");

                            // Générer le certificat PDF
                            String pdfPath = genererCertificatAvecModele(userId, titreFormation, duree, nomFormateur);
                            System.out.println("📜 Certificat généré à : " + pdfPath);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String genererCertificatAvecModele(int userId, String titreFormation, int duree, String nomFormateur) {
        String certifDir = "C:/Users/menyarbg/Desktop/certif";
        File dir = new File(certifDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String templatePath = "C:/Users/menyarbg/Desktop/template/certificat-de-reussite.pdf";
        String outputPath = certifDir + "/certif_" + userId + ".pdf";
        File templateFile = new File(templatePath);

        if (templateFile.exists()) {
            try {
                PdfReader reader = new PdfReader(templatePath);
                FileOutputStream outputStream = new FileOutputStream(outputPath);
                PdfStamper stamper = new PdfStamper(reader, outputStream);

                AcroFields form = stamper.getAcroFields();

                if (!form.getFields().containsKey("userName") ||
                        !form.getFields().containsKey("titreFormation") ||
                        !form.getFields().containsKey("dateFormation")) {
                    System.out.println("⚠️ Les champs du PDF ne correspondent pas aux attentes !");
                } else {
                    form.setField("userName", "Utilisateur " + userId);
                    form.setField("titreFormation", titreFormation);
                    form.setField("dateFormation", LocalDate.now().toString());
                }

                stamper.setFormFlattening(true);
                stamper.close();
                reader.close();
                outputStream.close();

                return outputPath;

            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return genererCertificatPDF(userId, titreFormation, duree, nomFormateur);
        }
    }
    public String genererCertificatPDF(int userId, String titreFormation, int duree, String nomFormateur) {
        String certifDir = "C:/Users/menyarbg/Desktop/certif";
        File dir = new File(certifDir);
        String userName = getUserNameById(userId);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String certifPath = certifDir + "/certif_" + userId + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(certifPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);

            // 🔹 Ajouter l'image de bordure en arrière-plan (comme un background CSS)
            String borderPath = "C:/Users/menyarbg/Desktop/bordure/10335605-removebg-preview.png";
            File borderFile = new File(borderPath);
            if (borderFile.exists()) {
                ImageData borderImage = ImageDataFactory.create(borderFile.toURI().toURL());
                Image border = new Image(borderImage);

                // Ajuster l'image pour qu'elle couvre toute la page
                border.scaleToFit(pdfDoc.getDefaultPageSize().getWidth(), pdfDoc.getDefaultPageSize().getHeight());

                // Obtenir la largeur et la hauteur de l'image redimensionnée
                float scaledWidth = border.getImageWidth();  // Largeur redimensionnée de l'image
                float scaledHeight = border.getImageHeight();  // Hauteur redimensionnée de l'image

                // Récupérer la taille de la page
                float pageWidth = pdfDoc.getDefaultPageSize().getWidth();
                float pageHeight = pdfDoc.getDefaultPageSize().getHeight();

                // Exemple : Bordure en haut à gauche
                border.setFixedPosition(0, pageHeight - scaledHeight);  // Positionner en haut à gauche

                // Exemple : Bordure en bas à gauche
                // border.setFixedPosition(0, 0);  // Positionner en bas à gauche

                // Exemple : Bordure en haut à droite
                // border.setFixedPosition(pageWidth - scaledWidth, pageHeight - scaledHeight);

                // Exemple : Bordure en bas à droite
                // border.setFixedPosition(pageWidth - scaledWidth, 0);

                document.add(border);  // Ajouter l'image de fond sur toute la page
            } else {
                System.out.println("⚠️ Bordure non trouvée, génération sans bordure.");
            }




            // 🔹 Ajouter le titre (texte sur le fond)
            document.add(new Paragraph("🏆 CERTIFICAT DE FORMATION")
                    .setBold()
                    .setFontSize(24)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(50)
                    .setMarginBottom(10));

            // 🔹 Ajouter du texte principal (sur le fond)
            document.add(new Paragraph("Nous certifions que l'utilisateur " + userName)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(3));

            document.add(new Paragraph("a suivi avec succès la formation suivante :")
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            document.add(new Paragraph(titreFormation)
                    .setItalic()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            document.add(new Paragraph("Durée : " + duree + " Jours")
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(5));

            document.add(new Paragraph("Délivré par : " + nomFormateur)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(25));

            // 🔹 Ajouter une ligne de séparation
            document.add(new LineSeparator(new SolidLine(1)));

            // 🔹 Ajouter la date d'émission
            document.add(new Paragraph("Certificat émis le " + LocalDate.now().toString())
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(30));

            document.close(); // ❗ Fermer le document après avoir ajouté tous les éléments
            System.out.println("✅ Certificat PDF généré avec succès !");
            return certifPath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getUserNameById(int userId) {
        String query = "SELECT nom FROM user WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static final String URL = "jdbc:mysql://localhost:3306/3a20";  // Remplacez par votre URL de base de données
    private static final String USER = "root"; // Remplacez par votre utilisateur
    private static final String PASSWORD = ""; // Remplacez par votre mot de passe

    // Méthode pour obtenir une connexion à la base de données
    public Connection getConn() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur de connexion à la base de données", e);
        }
    }


}