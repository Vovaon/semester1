package semester1.practice02.homework;

import java.util.Locale;

public class Task1CurrencyExchange {
    public static void main(String[] args) {
        double usdAmount = 250.0;
        final double EXCHANGE_RATE = 41.25;
        final double COMMISSION_PERCENT = 1.5;

        double uahAmountBeforeCommission = usdAmount * EXCHANGE_RATE;
        double bankCommission = uahAmountBeforeCommission * COMMISSION_PERCENT / 100.0;
        double netAmountToReceive = uahAmountBeforeCommission - bankCommission;
        long cashAmountToReceive = Math.round(netAmountToReceive);

        System.out.println("================ КВИТАНЦІЯ ОБМІНУ ================");
        System.out.println("Сума до обміну:             " + String.format(Locale.US, "%.2f", usdAmount) + " USD");
        System.out.println("Курс валют:                 " + String.format(Locale.US, "%.2f", EXCHANGE_RATE) + " UAH/USD");
        System.out.println("Нараховано до комісії:      " + String.format(Locale.US, "%.2f", uahAmountBeforeCommission) + " UAH");
        System.out.println("Комісія банку (" + COMMISSION_PERCENT + "%):       " + String.format(Locale.US, "%.2f", bankCommission) + " UAH");
        System.out.println("--------------------------------------------------");
        System.out.println("Чиста сума до видачі:       " + String.format(Locale.US, "%.2f", netAmountToReceive) + " UAH");
        System.out.println("До видачі готівкою:         " + cashAmountToReceive + " UAH");
        System.out.println("==================================================");
    }
}
