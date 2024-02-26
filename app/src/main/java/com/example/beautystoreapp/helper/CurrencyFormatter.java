package com.example.beautystoreapp.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatVND(double amount) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(vietnam);
        return format.format(amount);
    }
}
