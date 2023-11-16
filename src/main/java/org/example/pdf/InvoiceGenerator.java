package org.example.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class InvoiceGenerator {
    private static final LocalDate date = LocalDate.now();
    private static final PDType1Font normalFont = PDType1Font.HELVETICA;
    private static final PDType1Font boldFont = PDType1Font.HELVETICA_BOLD_OBLIQUE;

    private static int invoiceCounter;

    public static void main(String[] args) {

        try {
            generateInvoice(date + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateInvoice(String filePath) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        document.addPage(page);

        createHeader(contentStream);
        invoiceCounter++;
        addCellWithOnlyTopAndBottomBorder(contentStream, 350, 700, 200, 40, Color.decode("#f0f0f0"));
        addText_24(contentStream, "nr: " + invoiceCounter + "/" + date.getYear(), 390, 710);
        addText_12(contentStream, "Invoice created in day : " + date, 350, 680);
        addText_14_With_Line(contentStream, "Seller", 50, 630, 200);
        addCompanyInfo(contentStream, exampleSeller(), 50, 600);
        addText_14_With_Line(contentStream, "Buyer", 350, 630, 200);
        addCompanyInfo(contentStream,exampleBuyer(),350,600);
        contentStream.close();
        document.save(filePath);
        document.close();
    }

    private static Seller exampleSeller() {
        return new Seller("P.W. CONSTRUCTOR", "Wall Street 12/4"
                , 9481428206L, 515515515L
                , "const@gmail.com");

    }
    private static Buyer exampleBuyer() {
        return new Buyer("SCAFF-DONE", "Rainly 3/4"
                , 9481428206L, 515515515L
                , "scaff@gmail.com");

    }

    private static void createHeader(PDPageContentStream contentStream) throws IOException {
        float fontSize = 24;
        contentStream.setFont(boldFont, fontSize);
        contentStream.setLeading(1.5f * fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(405, 750);
        contentStream.showText("Invoice");
        contentStream.endText();
    }

    public static void addText_24(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        float fontSize = 24;
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(normalFont, fontSize);
        contentStream.showText(text);
        contentStream.endText();
    }

    public static void addText_14_With_Line(PDPageContentStream contentStream, String text, float x, float y, float width) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        float fontSize = 14;
        contentStream.newLineAtOffset(x, y + 5);
        contentStream.setFont(normalFont, fontSize);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + width, y);
        contentStream.stroke();

    }

    public static void addText_12(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        float fontSize = 12;
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(normalFont, fontSize);
        contentStream.showText(text);
        contentStream.endText();
    }
    public static void addCompanyInfo(PDPageContentStream contentStream,CompanyInfo companyInfo, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        float fontSize = 12;
        contentStream.setFont(normalFont, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(companyInfo.companyName());
        contentStream.newLine();
        float lineSpacing = 1.5f;
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText(companyInfo.address());
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText(companyInfo.getFormattedTax());
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText(String.valueOf(companyInfo.phoneNumber()));
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText(companyInfo.email());
        contentStream.endText();
    }

    public static void addCellWithOnlyTopAndBottomBorder(
            PDPageContentStream contentStream,
            float x,
            float y,
            float width,
            float height,
            Color borderColor) throws IOException {

        // Ustawienia koloru linii
        contentStream.setNonStrokingColor(borderColor);
        contentStream.fillRect(x, y, width, height);
        contentStream.setStrokingColor(Color.BLACK);

        // Linia górna komórki
        contentStream.moveTo(x, y + height);
        contentStream.lineTo(x + width, y + height);
        contentStream.stroke();

        // Linia dolna komórki
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + width, y);
        contentStream.stroke();
//        // Linia lewa komórki
//        contentStream.moveTo(x, y);
//        contentStream.lineTo(x, y + height);
//        contentStream.stroke();
//
//        // Linia prawa komórki
//        contentStream.moveTo(x + width, y);
//        contentStream.lineTo(x + width, y + height);
//        contentStream.stroke();

        // Dodanie tekstu do komórki


    }
}