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
import java.util.List;

public class InvoiceGenerator {
    private static final LocalDate date = LocalDate.now();
    private static final PDType1Font normalFont = PDType1Font.HELVETICA;
    private static final PDType1Font boldFont = PDType1Font.HELVETICA_BOLD_OBLIQUE;
    private static int invoiceCounter;
    private final String currency;
    private final CompanyInfo buyer;
    private final CompanyInfo seller;
    private final List<InvoiceData> data;

    public InvoiceGenerator(String currency, CompanyInfo buyer, CompanyInfo seller, List<InvoiceData> data) {
        this.currency = currency;
        this.buyer = buyer;
        this.seller = seller;
        this.data = data;
    }

    public void createInvoice() {
        try {

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            generateInvoice(document, contentStream);
            document.save(getPath());
            document.close();
            System.out.println("Invoice Created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPath() {
        return date + ".pdf";
    }

    private void generateInvoice(PDDocument document, PDPageContentStream contentStream) throws IOException {
        //Add image
        createInvoiceImage(contentStream, document);
        writeBoldText(contentStream, 24f, 405, 750, "Invoice");
        createCellWithInvoiceNumber(contentStream);
        addText_12(contentStream, "Invoice created in day : " + date, 350, 680);
        addText_14_With_Line(contentStream, "Seller", 50, 630, 200);
        addCompanyInfo(contentStream, seller, 50, 600);
        addText_14_With_Line(contentStream, "Buyer", 350, 630, 200);
        addCompanyInfo(contentStream, buyer, 350, 600);


        for (GridHeader cell : GridHeader.values()) {
            cell.createCell(contentStream);
        }
        int startY = 450;
        float sumOfUnitPrices = 0;
        float sumOfNetPrice = 0;
        float sumAmountOfVat = 0;
        float sumOfGrossValue = 0;

        for (InvoiceData x : data) {
            addInvoiceDataRow(contentStream, x, startY);

            sumOfUnitPrices += x.unitPrice();
            sumOfNetPrice += x.netValue();
            sumAmountOfVat += x.amountOfVat();
            sumOfGrossValue += x.grossValue();
            startY -= 30;
        }
        createSummaries(contentStream, startY, sumOfUnitPrices, sumOfNetPrice, sumAmountOfVat, sumOfGrossValue);
        createCell(contentStream, 300, 100, 250, 100, "Signature", Color.decode("#f0f0f0"));
        startY -= 30;
        createPaymentMethod(contentStream, startY, sumOfGrossValue);
        addText_9(contentStream, "The document was created using the Apache PDFBox library and had a learning purpose.", 50, 50);
        addText_9(contentStream, "The source code is available in the public repository at the following link: https://github.com/OwidiuszZielinski/Invoice-Generator ", 50, 38);

        contentStream.close();

    }

    private void createPaymentMethod(PDPageContentStream contentStream, int startY, float sumOfGrossValue) throws IOException {
        int y = startY;
        String payment = "Transfer";
        addText_12(contentStream, "Payment Method : " + payment, 50, y);
        y -= 30;
        addText_12_Bold(contentStream, "Sum to pay : " + stringWithCurrencyConverter(sumOfGrossValue), 50, y);
        y -= 30;
        String words = NumberToWordsConverter.convertPriceToWords(sumOfGrossValue);
        addText_12_Bold(contentStream, "In words : " + words, 50, y);

    }

    private void createSummaries(PDPageContentStream contentStream, int startY, float sumOfNetPrice, float sumOfGrossPrice, float sumAmountOfVat, float sumOfGrossValue) throws IOException {
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
        contentStream.showText("TIN : " + companyInfo.TaxIdentificationNumber());
        contentStream.newLine();
        contentStream.newLineAtOffset(0, fontSize * lineSpacing);
        contentStream.showText("tel. " + companyInfo.phoneNumber());
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

    private void addInvoiceDataRow(PDPageContentStream contentStream, InvoiceData data, int startY) throws IOException {
        addCellData(contentStream, 50, startY, 20, 30, String.valueOf(data.lp()));
        addCellData(contentStream, 70, startY, 130, 30, data.name());
        addCellData(contentStream, 200, startY, 25, 30, String.valueOf(data.unit()));
        addCellData(contentStream, 225, startY, 25, 30, String.valueOf(data.qty()));
        addCellData(contentStream, 250, startY, 50, 30, stringWithCurrencyConverter(data.unitPrice()));
        addCellData(contentStream, 300, startY, 50, 30, stringWithCurrencyConverter(data.netValue()));
        addCellData(contentStream, 350, startY, 30, 30, String.valueOf(data.vat()));
        addCellData(contentStream, 380, startY, 85, 30, stringWithCurrencyConverter(data.amountOfVat()));
        addCellData(contentStream, 465, startY, 85, 30, stringWithCurrencyConverter(data.grossValue()));
    }

    private String stringWithCurrencyConverter(Object object) {
        return object + currency;
    }


}