package org.example.pdf;

public record Seller(String companyName, String address, String TaxIdentificationNumber, String phoneNumber, String email) implements CompanyInfo{


}
