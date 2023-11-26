package org.example.pdf;

public record InvoiceData(int lp, String name, String unit, int qty, float unitPrice, float netValue, float vat,
                          float amountOfVat, float grossValue){

    public InvoiceData(int lp, String name, String unit, int qty, float unitPrice, float vat) {
        this(lp, name, unit, qty, unitPrice, qty * unitPrice, vat, (qty * unitPrice) * (vat / 100), qty * unitPrice + ((qty * unitPrice) * (vat / 100)));
    }
}
