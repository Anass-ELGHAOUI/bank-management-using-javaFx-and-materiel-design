package sample.models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import sample.Controllers.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Pdfgenerate{
    private static String FILE = "C:\\Users\\Anass\\Desktop\\Clients.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD,BaseColor.RED);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD,BaseColor.WHITE);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public static void generate(){
        try {
            Document document = new Document();
            FileOutputStream file=new FileOutputStream(FILE);
            PdfWriter.getInstance(document, file);
            document.open();
            addMetaData(document);
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


    private static void addContent(Document document) throws DocumentException, IOException {


        // Second parameter is the number of the chapter
        Image img=Image.getInstance("src/sample/images/logo.png");
        Paragraph p=new Paragraph();
        p.add(img);
        img.setAbsolutePosition(20,765);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);
        Paragraph pari=new Paragraph("Created By : EL GHAOUI Anass");
        pari.setAlignment(Element.ALIGN_RIGHT);
        document.add(pari);

        Paragraph par=new Paragraph(new Date().toString());
        par.setAlignment(Element.ALIGN_RIGHT);
        document.add(par);
        Paragraph lig=new Paragraph("==========================================================================");
        addEmptyLine(lig,1);
        document.add(lig);
        //Chapter catPart = new Chapter(new Paragraph(), 1);

        Paragraph subPara = new Paragraph("Table of clients",catFont);
        addEmptyLine(lig,1);


        //Section subCatPart = catPart.addSection(subPara);

        createTable(subPara);


        // now add all this to the document
        document.add(subPara);


    }

    private static void createTable(Paragraph subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        Phrase ph=new Phrase("CIN");
        ph.setFont(subFont);
        PdfPCell c1 = new PdfPCell(ph);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(c1);

        ph=new Phrase("Nom");
        ph.setFont(subFont);
        c1 = new PdfPCell(ph);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(c1);

        ph=new Phrase("Username");
        ph.setFont(subFont);
        c1 = new PdfPCell(ph);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(c1);
        table.setHeaderRows(1);

        ArrayList<Client> liste=Client.getClients();
        for(int i=0;i<liste.size();++i){
            table.addCell(String.valueOf(liste.get(i).getCIN()));
            table.addCell(liste.get(i).getNom());
            table.addCell(liste.get(i).getUsername());
        }

        subCatPart.add(table);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
