package com.vanlang.webbanhang.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {

    public static String formatPrice(int price) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return numberFormat.format(price);
    }
}
