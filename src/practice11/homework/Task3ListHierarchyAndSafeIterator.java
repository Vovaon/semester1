package practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Task3ListHierarchyAndSafeIterator {

    public static void demonstrateFailFastCrash(List<String> logs) {
        System.out.println("  ❌ Спроба викликати logs.remove() всередині for-each...");
        try {
            for (String log : logs) {
                if (log != null && log.contains("CORRUPTED")) {
                    logs.remove(log);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ⚡ Перехоплено ConcurrentModificationException! "
                    + "Причина: пряма зміна списку порушила expectedModCount ітератора.");
        }
    }

    public static int purgeLogsByKeywordSafely(List<String> logs, String keyword) {
        int removed = 0;
        Iterator<String> it = logs.iterator();
        while (it.hasNext()) {
            String log = it.next();
            if (log == null || log.isEmpty() || log.contains(keyword)) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    public static void enrichCriticalLogsWithListIterator(List<String> logs) {
        ListIterator<String> listIt = logs.listIterator();
        while (listIt.hasNext()) {
            String trimmed = listIt.next().trim();
            listIt.set(trimmed);
            if (trimmed.startsWith("ERROR:")) {
                listIt.add("[ALERT-TRIGGERED]");
            }
        }
    }

    public static List<String> collectReverseOrder(List<String> logs) {
        List<String> reversed = new ArrayList<>();
        ListIterator<String> listIt = logs.listIterator(logs.size());
        while (listIt.hasPrevious()) {
            reversed.add(listIt.previous());
        }
        return reversed;
    }

    public static void main(String[] args) {
        System.out.println("================ ІЄРАРХІЯ LIST ТА БЕЗПЕЧНИЙ ITERATOR ================");

        List<String> rawLogs = Arrays.asList(
                "INFO: Auth OK",
                "CORRUPTED_PACKET_01",
                "  ERROR: DB Timeout  ",
                "DEBUG: Ping",
                "CORRUPTED_PACKET_02",
                null,
                "ERROR: Disk Full",
                "INFO: Logout"
        );

        System.out.println("Початковий потік логів (" + rawLogs.size() + " записів):");
        System.out.println("  " + rawLogs);

        System.out.println("\n[1. ДЕМОНСТРАЦІЯ ПАСТКИ FAIL-FAST У ЦИКЛІ FOR-EACH]:");
        demonstrateFailFastCrash(new ArrayList<>(rawLogs));

        List<String> arrayLogs = new ArrayList<>(rawLogs);
        List<String> linkedLogs = new LinkedList<>(rawLogs);

        System.out.println("\n[2. ПОЛІМОРФНА ОБРОБКА ARRAYLIST<STRING> ЧЕРЕЗ ITERATOR]:");
        int removedFromArray = purgeLogsByKeywordSafely(arrayLogs, "CORRUPTED");
        System.out.println("  -> Безпечно видалено пошкоджених/null записів через it.remove(): "
                + removedFromArray + " шт.");
        System.out.println("  -> Залишилося чистих логів: " + arrayLogs);
        enrichCriticalLogsWithListIterator(arrayLogs);
        System.out.println("  -> Модифікація через ListIterator (set + add після ERROR):");
        System.out.println("     " + arrayLogs);

        System.out.println("\n[3. ПОЛІМОРФНА ОБРОБКА LINKEDLIST<STRING> ТИМИ САМИМИ МЕТОДАМИ]:");
        System.out.println("  -> Передано LinkedList у ті самі методи List<String>...");
        purgeLogsByKeywordSafely(linkedLogs, "CORRUPTED");
        enrichCriticalLogsWithListIterator(linkedLogs);

        List<String> reversedArray = collectReverseOrder(arrayLogs);
        List<String> reversedLinked = collectReverseOrder(linkedLogs);
        boolean identical = arrayLogs.equals(linkedLogs) && reversedArray.equals(reversedLinked);

        System.out.println("  -> Результат LinkedList повністю збігається з ArrayList: "
                + identical + " (Розмір: " + linkedLogs.size() + ")");
        System.out.println("  -> Зворотний обхід через ListIterator (hasPrevious / previous):");
        System.out.println("     " + reversedLinked);

        System.out.println("====================================================================");
    }
}