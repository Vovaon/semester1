package practice11.homework;

import java.util.ArrayList;
import java.util.Arrays;

public class Task2ArrayListCapacityAndDynamicGrowth {

    static class CustomArrayCapacityEmulator<E> {
        private static final int DEFAULT_CAPACITY = 10;

        private Object[] elementData;
        private int size = 0;
        private int resizeCount = 0;
        private int totalElementsCopied = 0;

        public CustomArrayCapacityEmulator() {
            this.elementData = new Object[DEFAULT_CAPACITY];
        }

        private void ensureCapacityInternal() {
            if (size == elementData.length) {
                int oldCapacity = elementData.length;
                int newCapacity = oldCapacity + (oldCapacity >> 1);
                if (newCapacity <= oldCapacity) {
                    newCapacity = oldCapacity + 1;
                }
                Object[] newBuffer = new Object[newCapacity];
                System.arraycopy(elementData, 0, newBuffer, 0, size);
                elementData = newBuffer;
                resizeCount++;
                totalElementsCopied += size;
            }
        }

        public void add(E element) {
            ensureCapacityInternal();
            elementData[size++] = element;
        }

        public void add(int index, E element) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            ensureCapacityInternal();
            System.arraycopy(elementData, index, elementData, index + 1, size - index);
            elementData[index] = element;
            size++;
        }

        @SuppressWarnings("unchecked")
        public E remove(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            E oldValue = (E) elementData[index];
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(elementData, index + 1, elementData, index, numMoved);
            }
            elementData[--size] = null;
            return oldValue;
        }

        @SuppressWarnings("unchecked")
        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return (E) elementData[index];
        }

        @SuppressWarnings("unchecked")
        public E set(int index, E element) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            E oldValue = (E) elementData[index];
            elementData[index] = element;
            return oldValue;
        }

        public int size() {
            return size;
        }

        public int getCapacity() {
            return elementData.length;
        }

        public int getResizeCount() {
            return resizeCount;
        }

        public int getTotalElementsCopied() {
            return totalElementsCopied;
        }
        public void trimToSize() {
            if (size < elementData.length) {
                Object[] trimmed = new Object[size];
                System.arraycopy(elementData, 0, trimmed, 0, size);
                elementData = trimmed;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("================ ЕМУЛЯТОР ВНУТРІШНЬОЇ БУДОВИ ARRAYLIST (ПІД КАПОТОМ) ================");
        var emulator = new CustomArrayCapacityEmulator<String>();
        var raw = new ArrayList<>();
        raw.add("рядок");
        raw.add(42);

        System.out.println("[ІНІЦІАЛІЗАЦІЯ]: Створено CustomArrayCapacityEmulator<String> | size = "
                + emulator.size() + " | capacity = " + emulator.getCapacity());
        System.out.println("\n--- Додавання 35 елементів у список ---");
        int growNumber = 0;
        for (int i = 1; i <= 35; i++) {
            int capBefore = emulator.getCapacity();
            int copiedBefore = emulator.getTotalElementsCopied();

            emulator.add("Block-" + i);

            int capAfter = emulator.getCapacity();
            if (capAfter != capBefore) {
                growNumber++;
                int copied = emulator.getTotalElementsCopied() - copiedBefore;
                System.out.printf("  ⚡ [GROW #%d] Переповнення при додаванні %d-го елемента! "
                                + "Ємність: %d -> %d (System.arraycopy скопіював %d ел.)%n",
                        growNumber, i, capBefore, capAfter, copied);
            }
        }
        System.out.printf("Стан після додавання 35 елементів: size = %d | capacity = %d "
                        + "(Вільний резерв: %d комірок null)%n",
                emulator.size(), emulator.getCapacity(), emulator.getCapacity() - emulator.size());
        System.out.println("\n--- Тестування вставки add(0, 'HEADER') та видалення remove(5) ---");
        int sizeBeforeInsert = emulator.size();
        emulator.add(0, "HEADER");
        System.out.println("  -> Вставлено 'HEADER' на індекс 0 (зсунуто праворуч "
                + sizeBeforeInsert + " елементів через System.arraycopy)");

        int sizeBeforeRemove = emulator.size();
        int removeIndex = 5;
        String removed = emulator.remove(removeIndex);
        int shifted = sizeBeforeRemove - removeIndex - 1;
        System.out.println("  -> Видалено елемент '" + removed + "' з індексу " + removeIndex
                + " (зсунуто ліворуч " + shifted + " елементів, хвіст занулено для GC)");
        System.out.printf("  Поточний стан: size = %d | capacity = %d | Перший: '%s' | Останній: '%s'%n",
                emulator.size(), emulator.getCapacity(),
                emulator.get(0), emulator.get(emulator.size() - 1));

        System.out.println("\n--- Оптимізація пам'яті trimToSize() ---");
        int capBeforeTrim = emulator.getCapacity();
        emulator.trimToSize();
        System.out.println("  -> Викликано trimToSize(): буфер Object[" + capBeforeTrim
                + "] стиснуто рівно до Object[" + emulator.getCapacity() + "]!");
        System.out.printf("  Фінальна телеметрія: size = %d | capacity = %d | Кількість розширень: %d%n",
                emulator.size(), emulator.getCapacity(), emulator.getResizeCount());

        System.out.println("  (Усього елементів перенесено під час розширень: "
                + emulator.getTotalElementsCopied() + ")");
        System.out.println("  (capacity == size: " + (emulator.getCapacity() == emulator.size()) + ")");
        System.out.println("================================================================================");
    }
}