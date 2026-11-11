package practice10.homework;

import java.util.Locale;

interface CashbackCapable {
    double MAX_CASHBACK_UAH = 500.0;

    double getCashbackRate();

    default double calculateCashback(double baseAmount) {
        return Math.min(MAX_CASHBACK_UAH, baseAmount * getCashbackRate());
    }
}

interface Refundable {
    boolean requestRefund(String reason);

    boolean isRefunded();
}

interface BlockchainVerifiable {
    String getTxHash();

    int getConfirmations();

    default boolean isFinalized() {
        return getConfirmations() >= 12;
    }
}
abstract class AbstractPaymentTransaction {
    private final String txId;
    private final double baseAmountUah;
    private boolean processed;

    protected AbstractPaymentTransaction(String txId, double baseAmountUah) {
        this.txId = txId;
        this.baseAmountUah = baseAmountUah;
        this.processed = false;
    }

    public String getTxId() { return txId; }
    public double getBaseAmountUah() { return baseAmountUah; }
    public boolean isProcessed() { return processed; }

    public abstract double calculateFee();

    public abstract String getPaymentChannel();

    public final boolean process() {
        if (baseAmountUah <= 0 || processed) {
            return false;
        }
        processed = true;
        return true;
    }

    public final double getTotalCharged() {
        return baseAmountUah + calculateFee();
    }
}

class PremiumCardPayment extends AbstractPaymentTransaction implements CashbackCapable, Refundable {
    private final String maskedPan;
    private final double cashbackRate = 0.03;
    private boolean refunded;

    public PremiumCardPayment(String txId, double baseAmountUah, String maskedPan) {
        super(txId, baseAmountUah);
        this.maskedPan = maskedPan;
    }

    public String getMaskedPan() { return maskedPan; }

    @Override
    public double calculateFee() { return getBaseAmountUah() * 0.01; }

    @Override
    public String getPaymentChannel() { return "Visa Infinite"; }

    @Override
    public double getCashbackRate() { return cashbackRate; }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() { return refunded; }
}
class CryptoUsdtPayment extends AbstractPaymentTransaction implements CashbackCapable, BlockchainVerifiable {
    private final String txHash;
    private final int confirmations;

    public CryptoUsdtPayment(String txId, double baseAmountUah, String txHash, int confirmations) {
        super(txId, baseAmountUah);
        this.txHash = txHash;
        this.confirmations = confirmations;
    }

    @Override
    public double calculateFee() { return 40.0; }

    @Override
    public String getPaymentChannel() { return "USDT TRC-20"; }

    @Override
    public double getCashbackRate() { return 0.01; }

    @Override
    public String getTxHash() { return txHash; }

    @Override
    public int getConfirmations() { return confirmations; }
}
final class TaxTreasuryPayment extends AbstractPaymentTransaction implements Refundable {
    private final String taxCode;
    private boolean refunded;

    public TaxTreasuryPayment(String txId, double baseAmountUah, String taxCode) {
        super(txId, baseAmountUah);
        this.taxCode = taxCode;
    }

    public String getTaxCode() { return taxCode; }

    @Override
    public double calculateFee() { return 0.0; }

    @Override
    public String getPaymentChannel() { return "ДержКазначейство"; }

    @Override
    public boolean requestRefund(String reason) {
        if (isProcessed() && !refunded) {
            refunded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRefunded() { return refunded; }
}

public class Task2SmartPaymentAndAuditGateway {

    private static String fmt(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    public static void runFinancialAudit(AbstractPaymentTransaction[] ledger) {
        double totalTurnover = 0;
        double totalFees = 0;
        double totalCashback = 0;

        System.out.println("[1. ПРОВЕДЕННЯ ПАКЕТУ ТРАНЗАКЦІЙ (final process())]:");
        for (AbstractPaymentTransaction tx : ledger) {
            String label = fmt("[%s | %s]", tx.getTxId(), tx.getPaymentChannel());
            if (tx.process()) {
                totalTurnover += tx.getBaseAmountUah();
                totalFees += tx.calculateFee();
                System.out.println(fmt("  ✅ %-27s Сума: %8.2f грн | Комісія: %7.2f грн | Всього: %8.2f грн",
                        label, tx.getBaseAmountUah(), tx.calculateFee(), tx.getTotalCharged()));
            } else {
                System.out.println("  ❌ " + label + " відхилено (некоректна сума або повторна обробка)");
            }
        }

        System.out.println();
        System.out.println("[2. АВТОМАТИЧНИЙ АУДИТ МОЖЛИВОСТЕЙ (Pattern Matching instanceof)]:");
        boolean refundTested = false;

        for (AbstractPaymentTransaction tx : ledger) {
            if (!tx.isProcessed()) {
                continue;
            }

            if (tx instanceof CashbackCapable c) {
                double raw = tx.getBaseAmountUah() * c.getCashbackRate();
                double paid = c.calculateCashback(tx.getBaseAmountUah());
                totalCashback += paid;
                double percent = c.getCashbackRate() * 100;
                if (raw > CashbackCapable.MAX_CASHBACK_UAH) {
                    System.out.println(fmt(
                            "  🎁 [CashbackCapable] %s: розрахунковий %.0f%% = %.0f грн -> нараховано %.2f грн (Ліміт MAX_CASHBACK_UAH!)",
                            tx.getTxId(), percent, raw, paid));
                } else {
                    String kind = (tx instanceof CryptoUsdtPayment) ? "крипто-бонусу" : "кешбеку";
                    System.out.println(fmt("  🎁 [CashbackCapable] %s: нараховано %.0f%% %s = %.2f грн",
                            tx.getTxId(), percent, kind, paid));
                }
            }

            if (tx instanceof BlockchainVerifiable b) {
                System.out.println(fmt("  ⛓️ [BlockchainVerifiable] %s: Hash=%s, Підтвердження=%d (Finalized: %b)",
                        tx.getTxId(), b.getTxHash(), b.getConfirmations(), b.isFinalized()));
            }

            if (tx instanceof Refundable r && !refundTested) {
                refundTested = true;
                String reason = "Подвійне списання";
                boolean ok = r.requestRefund(reason);
                System.out.println(fmt("  🔄 [Refundable] Спроба повернення %s ('%s'): %s",
                        tx.getTxId(), reason, ok ? "УСПІШНО ПОВЕРНЕНО!" : "ВІДХИЛЕНО"));
            }
        }

        System.out.println();
        System.out.println("[ПІДСУМКОВИЙ ЗВІТ ШЛЮЗУ]:");
        System.out.println(fmt("  Сумарний базовий оборот: %9.2f грн", totalTurnover));
        System.out.println(fmt("  Сумарна комісія шлюзу:   %9.2f грн", totalFees));
        System.out.println(fmt("  Сумарно виплачений кешбек: %7.2f грн", totalCashback));
    }

    public static void main(String[] args) {
        AbstractPaymentTransaction[] ledger = {
                new PremiumCardPayment("TX-101", 20000.0, "4141 **** **** 1234"),
                new CryptoUsdtPayment("TX-102", 8500.0, "0x9f8a...c4", 18),
                new TaxTreasuryPayment("TX-103", 5000.0, "TAX-2026-UA")
        };

        String title = " ПРОЦЕСИНГОВИЙ ЦЕНТР FINTECH (POLYMORPHISM & INTERFACES) ";
        System.out.println("================" + title + "================");
        runFinancialAudit(ledger);
        System.out.println("=".repeat(32 + title.length()));
    }
}
