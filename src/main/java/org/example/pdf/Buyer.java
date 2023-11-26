package org.example.pdf;

public record Buyer(String companyName, String address, String TaxIdentificationNumber, String phoneNumber, String email) implements CompanyInfo {


}
