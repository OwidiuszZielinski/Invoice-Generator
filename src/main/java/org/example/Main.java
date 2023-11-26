package org.example;

import org.example.pdf.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<InvoiceData> invoiceDataList = createData();

        CompanyInfo buyer = new Buyer("Apex Innovations Ltd", "123 Tech, Suite 567, Silicon Valley, CA 94000",
                "US123456789", "+1 (555) 123-4567", "apex@gmail.com");
        CompanyInfo seller = new Seller("Global Solutions Inc", "456 Commerce Street, Floor 8, New York, NY 10001",
                "US987654321", "+1 (555) 987-6543", "global@gmail.com");

        InvoiceGenerator invoiceGenerator = new InvoiceGenerator(" USD", buyer, seller, invoiceDataList);
        invoiceGenerator.createInvoice();
    }
    private static List<InvoiceData> createData() {
        List<InvoiceData> invoiceDataList = new ArrayList<>();
        invoiceDataList.add(new InvoiceData(1, "Product A", "pcs", 10, 50.0f, 23.0f));
        invoiceDataList.add(new InvoiceData(2, "Service B", "pcs", 5, 30.0f, 23.0f));
        invoiceDataList.add(new InvoiceData(3, "Product C", "pcs", 8, 40.0f, 23.0f));
        return invoiceDataList;
    }
}