import java.io.*;
import java.util.*;


public class MergeSorting {
    private static String mode = "a";
    private static String type = "string";

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguments. Usage: java MergeSorting [-a|-d] -s|-i output_file input_file1 [input_file2 ...]");
            System.exit(1);
        }
        String outputFile = args[2]; // обязательный параметр имени выходного файла

        // обработка флагов
        for (String arg : args) {
            if ("-a".equals(arg)) {
                mode = "a";
            } else if ("-d".equals(arg)) {
                mode = "d";
            } else if ("-i".equals(arg)) {
                type = "integer";
            } else if ("-s".equals(arg)) {
                type = "string";
            }
        }

        List<String> inputFiles = Arrays.asList(args).subList(3, args.length); // имена входных файлов

        List<Object> arrayFile1 = new ArrayList<>();
        List<Object> arrayFile2 = new ArrayList<>();

        try (InputStream inputStream1 = MergeSorting.class.getClassLoader().getResourceAsStream(inputFiles.get(0));
             BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
             InputStream inputStream2 = MergeSorting.class.getClassLoader().getResourceAsStream(inputFiles.get(1));
             BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2))) {

            readFile(bufferedReader1, arrayFile1, type);
            readFile(bufferedReader2, arrayFile2, type);

            Object[] array1 = arrayFile1.toArray(new Object[0]);
            Object[] array2 = arrayFile2.toArray(new Object[0]);

            mergeSort(array1, array1.length, type);
            mergeSort(array2, array2.length, type);

            Object[] mergedArray = new Object[array1.length + array2.length];

            int index1 = 0, index2 = 0, indexMerged = 0;

            if (mode.equals("d")) {
                while (index1 < array1.length && index2 < array2.length) {
                    if (type.equals("integer") && ((Integer) array1[index1]).compareTo((Integer) array2[index2]) <= 0) {
                        mergedArray[indexMerged++] = array1[index1++];
                    } else {
                        mergedArray[indexMerged++] = array2[index2++];
                    }
                }
                while (index1 < array1.length) {
                    mergedArray[indexMerged++] = array1[index1++];
                }
                while (index2 < array2.length) {
                    mergedArray[indexMerged++] = array2[index2++];
                }
                reverse(mergedArray);
            } else {
                while (index1 < array1.length && index2 < array2.length) {
                    if (type.equals("integer") && ((Integer) array1[index1]).compareTo((Integer) array2[index2]) <= 0) {
                        mergedArray[indexMerged++] = array1[index1++];
                    } else {
                        mergedArray[indexMerged++] = array2[index2++];
                    }
                }
                while (index1 < array1.length) {
                    mergedArray[indexMerged++] = array1[index1++];
                }
                while (index2 < array2.length) {
                    mergedArray[indexMerged++] = array2[index2++];
                }
            }

            System.out.println("Merged array: " + Arrays.toString(mergedArray));
            writeToFile(outputFile, mergedArray, mode, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFile(BufferedReader bufferedReader, List<Object> list, String type) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split(" ");
            for (String part : parts) {
                if ("integer".equals(type)) {
                    try {
                        list.add(Integer.parseInt(part));
                    } catch (NumberFormatException e) {

                    }
                } else if ("string".equals(type)) {
                    list.add(part);
                }
            }
        }
    }

    private static void mergeSort(Object[] array, int n, String type) {
        if (n < 2) {
            return;
        }

        int mid = n / 2;
        Object[] array1 = new Object[mid];
        Object[] array2 = new Object[n - mid];

        for (int i = 0; i < mid; i++) {
            array1[i] = array[i];
        }
        for (int i = mid; i < n; i++) {
            array2[i - mid] = array[i];
        }

        mergeSort(array1, mid, type);
        mergeSort(array2, n - mid, type);
        merge(array, array1, array2, mid, n - mid, type);
    }

    private static void reverse(Object[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            Object temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    private static void merge(Object[] mergedArray, Object[] array1, Object[] array2, int lengthArray1, int lengthArray2, String type) {
        int index1 = 0, index2 = 0, indexMerged = 0;
        while (index1 < lengthArray1 && index2 < lengthArray2) {
            if ("integer".equals(type)) {
                if (((Integer) array1[index1]).compareTo((Integer) array2[index2]) <= 0) {
                    mergedArray[indexMerged++] = array1[index1++];
                } else {
                    mergedArray[indexMerged++] = array2[index2++];
                }
            } else if ("string".equals(type)) {
                if (((String) array1[index1]).compareTo((String) array2[index2]) <= 0) {
                    mergedArray[indexMerged++] = array1[index1++];
                } else {
                    mergedArray[indexMerged++] = array2[index2++];
                }
            }
        }
        // Копирование оставшихся элементов из array1, если они есть
        while (index1 < lengthArray1) {
            mergedArray[indexMerged++] = array1[index1++];
        }
        // Копирование оставшихся элементов из array2, если они есть
        while (index2 < lengthArray2) {
            mergedArray[indexMerged++] = array2[index2++];
        }
    }

    private static void writeToFile(String filename, Object[] array, String mode, String type) throws IOException {
        try (BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename, false))) {
            for (Object obj : array) {
                if ("integer".equals(type)) {
                    outputWriter.write(((Integer) obj).toString() + "\n");
                } else if ("string".equals(type)) {
                    outputWriter.write(((String) obj) + "\n");
                }
            }
        }
    }
}