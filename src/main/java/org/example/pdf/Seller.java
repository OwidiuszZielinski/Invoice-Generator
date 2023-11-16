package org.example.pdf;

public record Seller(String companyName, String address, long TaxIdentificationNumber, long phoneNumber, String email) implements CompanyInfo{

    @Override
    public String getFormattedTax() {
        String longString = String.valueOf(TaxIdentificationNumber);
        int length = longString.length();

        if (length < 2) {
            return longString; // Nothing to format
        }

        StringBuilder formatted = new StringBuilder();
        int groupCount = 0;

        for (int i = length - 1; i >= 0; i--) {
            formatted.insert(0, longString.charAt(i));

            if (++groupCount % 2 == 0 && i != 0) {
                formatted.insert(0, "-");
            }
        }

        return formatted.toString();
    }
}
