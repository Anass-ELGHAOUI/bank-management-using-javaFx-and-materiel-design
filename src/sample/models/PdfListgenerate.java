package sample.models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;

public class PdfListgenerate{
    private static String FILE = "C:\\Users\\Anass\\Documents\\releve_banquaire.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public static void generateReleve(String code){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaDataReleve(document);
            addTitlePageReleve(document);
            addContentReleve(document,code);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generate(){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("Liste of clients");
        document.addSubject("export clients");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Chef d'agence");
        document.addCreator("EL GHAOUI Anas");
    }
    private static void addMetaDataReleve(Document document) {
        document.addTitle("Liste of clients");
        document.addSubject("export clients");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Chef d'agence");
        document.addCreator("EL GHAOUI Anas");
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Table of clients", catFont));

        addEmptyLine(preface, 1);
        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addTitlePageReleve(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Table of clients", catFont));

        addEmptyLine(preface, 1);
        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("Transactions", catFont);
        anchor.setName("List of clients");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("", subFont);

        Section subCatPart = catPart.addSection(subPara);

        createTable(subCatPart);


        // now add all this to the document
        document.add(catPart);


    }

    private static void addContentReleve(Document document,String code) throws DocumentException {
        Anchor anchor = new Anchor("Transactions", catFont);
        anchor.setName("List of clients");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("", subFont);

        Section subCatPart = catPart.addSection(subPara);

        createTableReleve(subCatPart,code);


        // now add all this to the document
        document.add(catPart);


    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(5);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Numéro de compte"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Type de transaction"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Montant"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CIN du proprietaire"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        ObservableList<ListTransactionTableModel> liste= null;
        try {
            liste = ListTransactionTableModel.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<liste.size();++i){
            table.addCell(liste.get(i).getCodecompte());
            table.addCell(liste.get(i).getType());
            table.addCell(liste.get(i).getDateTran()+"");
            table.addCell(liste.get(i).getMontant()+"");
            table.addCell(liste.get(i).getCin()+"");
        }

        subCatPart.add(table);

    }

    private static void createTableReleve(Section subCatPart,String code)
            throws BadElementException {
        PdfPTable table = new PdfPTable(5);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Numéro de compte"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Type de transaction"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Montant"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CIN du proprietaire"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        ObservableList<ListTransactionTableModel> liste= null;
        try {
            liste = ListTransactionTableModel.getDataReleve(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<liste.size();++i){
            table.addCell(liste.get(i).getCodecompte());
            table.addCell(liste.get(i).getType());
            table.addCell(liste.get(i).getDateTran()+"");
            table.addCell(liste.get(i).getMontant()+"");
            table.addCell(liste.get(i).getCin()+"");
        }

        subCatPart.add(table);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
