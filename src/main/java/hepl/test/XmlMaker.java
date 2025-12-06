package hepl.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlMaker {

    public static void main(String[] args) {
        // Chemin vers ton CSV
        String csvFile = "src/main/resources/csv.csv";

        // Le nom du XML sera basé sur le nom du CSV
        String xmlFile = csvFile.replaceFirst("(?i)\\.csv$", ".xml");

        try {
            List<String[]> csvData = readCsv(csvFile);
            writeXml(xmlFile, csvData);
            System.out.println("Conversion terminée avec succès !");
            System.out.println("Fichier généré : " + xmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lecture du CSV
    private static List<String[]> readCsv(String filename) throws IOException {
        List<String[]> data = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                // Si ton CSV a des virgules "simples" (pas de guillemets avec des virgules dedans)
                String[] values = line.split(",");
                data.add(values);
            }
        }
        return data;
    }

    // Échappe les caractères spéciaux XML et empecher erreur taille trop longue
    private static String xmlEscape(String text) {
        if (text == null) return "";

        text = text.replaceAll("\\[.*?\\]", "");

        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    // Trouve l'index d'une colonne dans les headers (insensible à la casse)
    private static int findIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i] != null &&
                    headers[i].trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1; // pas trouvé
    }

    // Récupère la valeur d'une cellule avec sécurité
    private static String getCell(String[] row, int idx) {
        if (idx < 0 || idx >= row.length) return "";
        return row[idx];
    }

    // Découpe une chaîne de type ['a', 'b', 'c'] en morceaux individuels
    private static String[] splitList(String raw) {
        if (raw == null) return new String[0];

        // On enlève les crochets
        String cleaned = raw.replace("[", "").replace("]", "").trim();
        if (cleaned.isEmpty()) return new String[0];

        // On découpe sur les virgules
        String[] parts = cleaned.split(",");

        // On trim chaque élément
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        return parts;
    }

    // Écriture du XML structuré selon le DTD
    private static void writeXml(String filename, List<String[]> data) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"xslt.xslt\"?>\n");
            writer.write("<!DOCTYPE dataset SYSTEM \"dtd.dtd\">\n");
            writer.write("<dataset source=\"csv\">\n");

            if (!data.isEmpty()) {
                String[] headers = data.get(0); // première ligne = entêtes

                // 1) On récupère les index des colonnes utiles (à adapter à ton CSV)
                int idxImageID                       = findIndex(headers, "ImageID");
                int idxImageDir                      = findIndex(headers, "ImageDir");
                int idxStudyID                       = findIndex(headers, "StudyID"); // ou "StudyID_DICOM"
                int idxPatientID                     = findIndex(headers, "PatientID");
                int idxPatientBirth                  = findIndex(headers, "PatientBirth");
                int idxProjection                    = findIndex(headers, "Projection");
                int idxPediatric                     = findIndex(headers, "Pediatric");
                int idxMethodProjection              = findIndex(headers, "MethodProjection");
                int idxReportID                      = findIndex(headers, "ReportID");
                int idxReportText                    = findIndex(headers, "Report"); // texte du rapport
                int idxMethodLabel                   = findIndex(headers, "MethodLabel");
                int idxLabels                        = findIndex(headers, "Labels");
                int idxLabelsLocalizationsBySentence = findIndex(headers, "LabelsLocalizationsBySentence");
                int idxLabelCUIS                     = findIndex(headers, "labelCUIS");
                int idxLocalizations                 = findIndex(headers, "Localizations");
                int idxLocalizationsCUIS             = findIndex(headers, "LocalizationsCUIS");

                // 2) On parcourt les lignes de données
                for (int i = 1; i < data.size(); i++) {
                    String[] row = data.get(i);

                    // Récupération des valeurs utiles
                    String imageID        = getCell(row, idxImageID);
                    String imageDir       = getCell(row, idxImageDir);
                    String studyID        = getCell(row, idxStudyID);
                    String patientID      = getCell(row, idxPatientID);
                    String patientBirth   = getCell(row, idxPatientBirth);
                    String projection     = getCell(row, idxProjection);
                    String pediatric      = getCell(row, idxPediatric);
                    String methodProj     = getCell(row, idxMethodProjection);
                    String reportID       = getCell(row, idxReportID);
                    String reportText     = getCell(row, idxReportText);
                    String methodLabel    = getCell(row, idxMethodLabel);
                    String labels         = getCell(row, idxLabels);
                    String labelsLocSent  = getCell(row, idxLabelsLocalizationsBySentence);
                    String labelCUIS      = getCell(row, idxLabelCUIS);
                    String localizations  = getCell(row, idxLocalizations);
                    String localizationsCUIS = getCell(row, idxLocalizationsCUIS);

                    // 3) On génère un record
                    writer.write("  <record id=\"r" + i + "\">\n");

                    // Image
                    writer.write("    <Image");
                    writer.write(" ImageID=\"" + xmlEscape(imageID) + "\"");
                    writer.write(" ImageDir=\"" + xmlEscape(imageDir) + "\"");
                    writer.write("/>\n");

                    // StudyID
                    writer.write("    <StudyID>" + xmlEscape(studyID) + "</StudyID>\n");

                    // Patient
                    writer.write("    <Patient");
                    writer.write(" PatientID=\"" + xmlEscape(patientID) + "\"");
                    writer.write(" PatientBirth=\"" + xmlEscape(patientBirth) + "\"");
                    writer.write(">\n");
                    writer.write("    </Patient>\n");

                    // Projection
                    writer.write("    <Projection>" + xmlEscape(projection) + "</Projection>\n");

                    // Pediatric
                    writer.write("    <Pediatric>" + xmlEscape(pediatric) + "</Pediatric>\n");

                    // MethodProjection
                    writer.write("    <MethodProjection>" + xmlEscape(methodProj) + "</MethodProjection>\n");

                    // Report
                    writer.write("    <Report");
                    writer.write(" ReportID=\"" + xmlEscape(reportID) + "\"");
                    if (!reportText.isEmpty()) {
                        writer.write(" Report=\"" + xmlEscape(reportText) + "\"");
                    }
                    writer.write(">");
                    writer.write(xmlEscape(reportText));
                    writer.write("</Report>\n");

                    // Label (avec sous-éléments LabelItem pour pouvoir boucler en XSLT)
                    writer.write("    <Label");
                    if (!methodLabel.isEmpty()) {
                        writer.write(" MethodLabel=\"" + xmlEscape(methodLabel) + "\"");
                    }
                    // Labels est #REQUIRED dans la DTD → on garde l'attribut complet
                    writer.write(" Labels=\"" + xmlEscape(labels) + "\"");
                    if (!labelsLocSent.isEmpty()) {
                        writer.write(" LabelsLocalizationsBySentence=\"" + xmlEscape(labelsLocSent) + "\"");
                    }
                    if (!labelCUIS.isEmpty()) {
                        writer.write(" labelCUIS=\"" + xmlEscape(labelCUIS) + "\"");
                    }
                    writer.write(">\n");

                    // Sous-éléments LabelItem
                    for (String item : splitList(labels)) {
                        if (!item.isEmpty()) {
                            writer.write("      <LabelItem>" + xmlEscape(item) + "</LabelItem>\n");
                        }
                    }

                    writer.write("    </Label>\n");

                    // Localizations (avec sous-éléments LocItem)
                    writer.write("    <Localizations");
                    if (!localizationsCUIS.isEmpty()) {
                        writer.write(" LocalizationsCUIS=\"" + xmlEscape(localizationsCUIS) + "\"");
                    }
                    writer.write(">\n");

                    for (String loc : splitList(localizations)) {
                        if (!loc.isEmpty()) {
                            writer.write("      <LocItem>" + xmlEscape(loc) + "</LocItem>\n");
                        }
                    }

                    writer.write("    </Localizations>\n");

                    writer.write("  </record>\n");
                }
            }

            writer.write("</dataset>\n");
        }
    }
}
