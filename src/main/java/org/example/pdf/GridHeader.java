package org.example.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

public enum GridHeader {
    LP(50, 480, 20, 30, "Lp.", Color.decode("#f0f0f0")),
    NAME(70, 480, 130, 30, "Name of the service or product", Color.decode("#f0f0f0")),
    PCS(200, 480, 25, 30, "Pcs.", Color.decode("#f0f0f0")),
    QTY(225, 480, 25, 30, "Qty.", Color.decode("#f0f0f0")),
    NET_PRICE(250, 480, 50, 30, "Unit price", Color.decode("#f0f0f0")),
    GROSS_PRICE(300, 480, 50, 30, "Net value", Color.decode("#f0f0f0")),
    VAT_PERCENT(350, 480, 30, 30, "Vat %", Color.decode("#f0f0f0")),
    AMOUNT_OF_VAT(380, 480, 85, 30, "Amount of vat", Color.decode("#f0f0f0")),
    GROSS_VALUE(465, 480, 85, 30, "Gross value", Color.decode("#f0f0f0"));

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final String text;
    private final Color color;

    GridHeader(float x, float y, float width, float height, String text, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;
    }

    public void createCell(PDPageContentStream contentStream) throws IOException {
        InvoiceGenerator.createCell(contentStream, x, y, width, height, text, color);
    }
}
