package hepl.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
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
                String[] values = line.split(","); // séparateur CSV
                data.add(values);
            }
        }
        return data;
    }

    // Échappe les caractères spéciaux XML
    private static String xmlEscape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    // Écriture du XML
    private static void writeXml(String filename, List<String[]> data) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!DOCTYPE dataset SYSTEM \"dataset.dtd\">\n");
            writer.write("<dataset source=\"csv\">\n"); // élément racine debut

            if (!data.isEmpty()) {
                String[] headers = data.get(0); // première ligne = entêtes
                for (int i = 1; i < data.size(); i++) {
                    String[] row = data.get(i);
                    writer.write("  <record id=\"" + i + "\">\n"); //record debut contients des attribut id =1 , id=2, ...

                    for (int j = 0; j < headers.length; j++) {
                        String colName = headers[j].trim();

                        if (colName.isEmpty()) {
                            colName = "Col0";
                        }
                        String value = j < row.length ? row[j] : "";
                        writer.write("    <" + colName + ">" + xmlEscape(value) + "</" + colName + ">\n");
                    }

                    writer.write("  </record>\n"); //fin record
                }
            }

            writer.write("</dataset>\n"); //élément racine fin
        }
    }
}
