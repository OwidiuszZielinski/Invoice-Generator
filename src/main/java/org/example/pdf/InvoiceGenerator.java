package org.example.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceGenerator {
    private static final LocalDate date = LocalDate.now();
    private static final PDType1Font normalFont = PDType1Font.HELVETICA;
    private static final PDType1Font boldFont = PDType1Font.HELVETICA_BOLD_OBLIQUE;

    private static final String currency = " USD ";

    private static int invoiceCounter;

    public static void main(String[] args) {


        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            String path = date + ".pdf";
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            generateInvoice(document, contentStream);
            document.save(path);
            document.close();
            System.out.println("Invoice Created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateInvoice(PDDocument document, PDPageContentStream contentStream) throws IOException {
        //Add image
        createInvoiceImage(contentStream, document);
        writeBoldText(contentStream, 24f, 405, 750, "Invoice");
        createCellWithInvoiceNumber(contentStream);
        addText_12(contentStream, "Invoice created in day : " + date, 350, 680);
        addText_14_With_Line(contentStream, "Seller", 50, 630, 200);
        addCompanyInfo(contentStream, exampleSeller(), 50, 600);
        addText_14_With_Line(contentStream, "Buyer", 350, 630, 200);
        addCompanyInfo(contentStream, exampleBuyer(), 350, 600);


        for (GridHeader cell : GridHeader.values()) {
            cell.createCell(contentStream);
        }
        int startY = 450;
        float sumOfGrossPrice = 0;
        float sumOfNetPrice = 0;
        float sumAmountOfVat = 0;
        float sumOfGrossValue = 0;

        for (InvoiceData x : generateSampleData()) {
            addInvoiceDataRow(contentStream, x, startY);
            sumOfGrossPrice += x.grossPrice();
            sumOfNetPrice += x.netPrice();
            sumAmountOfVat += x.amountOfVat();
            sumOfGrossValue += x.grossValue();
            startY -= 30;
        }
        createSummaries(contentStream, startY, sumOfNetPrice, sumOfGrossPrice, sumAmountOfVat, sumOfGrossValue);
        createCell(contentStream, 300, 100, 250, 100, "Signature", Color.decode("#f0f0f0"));
        startY -= 30;
        createPaymentMethod(contentStream, startY, sumOfGrossValue);
        addText_9(contentStream, "The document was created using the Apache PDFBox library and had a learning purpose.", 50, 50);
        addText_9(contentStream, "The source code is available in the public repository at the following link: https://github.com/OwidiuszZielinski/Invoice-Generator ", 50, 38);

        contentStream.close();

    }

    private static void createPaymentMethod(PDPageContentStream contentStream, int startY, float sumOfGrossValue) throws IOException {
        int y = startY;
        String payment = "Transfer";
        addText_12(contentStream, "Payment Method : " + payment, 50, y);
        y -= 30;
        addText_12_Bold(contentStream, "Sum to pay : " + stringWithCurrencyConverter(sumOfGrossValue), 50, y);
        y -= 30;
        String words = NumberToWordsConverter.convertPriceToWords(sumOfGrossValue);
        addText_12_Bold(contentStream, "In words : " + words, 50, y);

    }

    private static void createSummaries(PDPageContentStream contentStream, int startY, float sumOfNetPrice, float sumOfGrossPrice, float sumAmountOfVat, float sumOfGrossValue) throws IOException {
        addCellData(contentStream, 250, startY, 50, 30, stringWithCurrencyConverter(sumOfNetPrice));
        addCellData(contentStream, 300, startY, 50, 30, stringWithCurrencyConverter(sumOfGrossPrice));
        addCellData(contentStream, 380, startY, 85, 30, stringWithCurrencyConverter(sumAmountOfVat));
        addCellData(contentStream, 465, startY, 85, 30, stringWithCurrencyConverter(sumOfGrossValue));
        writeBoldText(contentStream, 14f, 150, startY + 14, "Summary : ");
    }

    private static void createCellWithInvoiceNumber(PDPageContentStream contentStream) throws IOException {
        invoiceCounter++;
        addCellWithOnlyTopAndBottomBorder(contentStream, 350, 700, 200, 40, Color.decode("#f0f0f0"));
        addText_24(contentStream, "nr: " + invoiceCounter + "/" + date.getYear(), 390, 710);
    }

    private static void createInvoiceImage(PDPageContentStream contentStream, PDDocument document) throws IOException {
        PDImageXObject image = PDImageXObject.createFromFile("src/main/resources/logo.png", document);
        contentStream.drawImage(image, 50, 700, 130, 65);
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

    private static void writeBoldText(PDPageContentStream contentStream, float fontSize, int tx, int ty, String text) throws IOException {
        contentStream.setFont(boldFont, fontSize);
        contentStream.setLeading(1.5f * fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(tx, ty);
        contentStream.showText(text);
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

    public static void addText_12_Bold(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        float fontSize = 12;
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(boldFont, fontSize);
        contentStream.showText(text);
        contentStream.endText();
    }

    public static void addText_9(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        float fontSize = 9;
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(normalFont, fontSize);
        contentStream.showText(text);
        contentStream.endText();
    }

    public static void addCompanyInfo(PDPageContentStream contentStream, CompanyInfo companyInfo, float x, float y) throws IOException {
        float fontSize = 10;
        float lineSpacing = 2.0f;
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.setFont(normalFont, fontSize);
        contentStream.newLineAtOffset(x, y + 15);
        contentStream.showText(companyInfo.companyName());
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText(companyInfo.address());
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText("TIN : " + companyInfo.getFormattedTax());
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText("tel. " + String.valueOf(companyInfo.phoneNumber()));
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText("email. " + companyInfo.email());
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
    }

    public static void createCell(
            PDPageContentStream contentStream,
            float x,
            float y,
            float cellWidth,
            float cellHeight,
            String text,
            Color borderColor) throws IOException {

        contentStream.setNonStrokingColor(borderColor);
        contentStream.fillRect(x, y, cellWidth, cellHeight);
        contentStream.setStrokingColor(Color.BLACK);

        // Linia górna komórki
        contentStream.moveTo(x, y + cellHeight);
        contentStream.lineTo(x + cellWidth, y + cellHeight);
        contentStream.stroke();

        // Linia dolna komórki
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + cellWidth, y);
        contentStream.stroke();
        // Linia lewa komórki
        contentStream.moveTo(x, y);
        contentStream.lineTo(x, y + cellHeight);
        contentStream.stroke();

        // Linia prawa komórki
        contentStream.moveTo(x + cellWidth, y);
        contentStream.lineTo(x + cellWidth, y + cellHeight);
        contentStream.stroke();
        addText_9(contentStream, text, x + 3, y + cellHeight / 2);


    }

    public static void addCellData(
            PDPageContentStream contentStream,
            float x,
            float y,
            float cellWidth,
            float cellHeight,
            String text) throws IOException {
        // Linia dolna komórki
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + cellWidth, y);
        contentStream.stroke();
        // Linia lewa komórki
        contentStream.moveTo(x, y);
        contentStream.lineTo(x, y + cellHeight);
        contentStream.stroke();

        // Linia prawa komórki
        contentStream.moveTo(x + cellWidth, y);
        contentStream.lineTo(x + cellWidth, y + cellHeight);
        contentStream.stroke();
        addText_9(contentStream, text, x + 3, y + cellHeight / 2);


    }


    public static void addInvoiceDataRow(PDPageContentStream contentStream, InvoiceData data, int startY) throws IOException {
        addCellData(contentStream, 50, startY, 20, 30, String.valueOf(data.lp()));
        addCellData(contentStream, 70, startY, 130, 30, data.name());
        addCellData(contentStream, 200, startY, 25, 30, String.valueOf(data.unit()));
        addCellData(contentStream, 225, startY, 25, 30, String.valueOf(data.qty()));
        addCellData(contentStream, 250, startY, 50, 30, stringWithCurrencyConverter(data.netPrice()));
        addCellData(contentStream, 300, startY, 50, 30, stringWithCurrencyConverter(data.grossPrice()));
        addCellData(contentStream, 350, startY, 30, 30, String.valueOf(data.vat()));
        addCellData(contentStream, 380, startY, 85, 30, stringWithCurrencyConverter(data.amountOfVat()));
        addCellData(contentStream, 465, startY, 85, 30, stringWithCurrencyConverter(data.grossValue()));
    }

    public static String stringWithCurrencyConverter(Object object) {
        return String.valueOf(object) + currency;
    }

    public static List<InvoiceData> generateSampleData() {
        List<InvoiceData> invoiceDataList = new ArrayList<>();

        InvoiceData invoice1 = new InvoiceData(1, "Product A", "pcs", 10, 50.0f, 60.0f, 23.0f, 13.0f, 73.0f);
        InvoiceData invoice2 = new InvoiceData(2, "Service B", "pcs", 5, 30.0f, 36.0f, 23.0f, 6.9f, 42.9f);
        InvoiceData invoice3 = new InvoiceData(3, "Product C", "pcs", 8, 40.0f, 48.0f, 23.0f, 11.0f, 59.0f);

        invoiceDataList.add(invoice1);
        invoiceDataList.add(invoice2);
        invoiceDataList.add(invoice3);

        return invoiceDataList;
    }


}