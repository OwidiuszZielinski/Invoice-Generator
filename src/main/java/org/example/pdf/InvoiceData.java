package org.example.pdf;

public record InvoiceData(int lp,String name,String unit,int qty,float netPrice,float grossPrice, float vat, float amountOfVat,float grossValue) {


}
