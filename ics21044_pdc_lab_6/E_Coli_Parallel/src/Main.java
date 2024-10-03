import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;

// Για να παραλληλοποιήσουμε το πρόγραμμα θα χωρίσουμε το κείμενο σε κομμάτια και θα
// επεξεργαστούμε κάθε κομμάτι παράλληλα. Κάθε νήμα θα χειρίζεται ένα συγκεκριμένο τμήμα του κειμένου και θα αναζητά αντιστοιχίες ανεξάρτητα από τα υπόλοιπα νήματα.
// Τα νήματα θα χρησιμοποιούν έναν μετρητή για να καταμετρήσουν τις αντιστοιχίες που βρίσκουν.
// Καθώς τα νήματα γράφουν όλα στον ίδιο μετρητή, υπάρχει πιθανότητα να υπάρξουν συνθήκες ανταγωνισμού και προβληματα συγχρονισμού.
// Για τον λόγο αυτό θα χρησιμοποιήσουμε synchronized blocks.
public class Main {
    public static final int TOTAL_ARGS = 2;

    public static void main(String[] args) throws IOException {
        if (args.length != TOTAL_ARGS) {
            System.out.println("Usage: java Main <file name> <pattern>");
            System.exit(1);
        }

        String fileName = args[0];
        String pattern = args[1];

        int numOfThreads = Runtime.getRuntime().availableProcessors() - 1;

        Path filePath = Path.of(fileName);
        String genomeData = new String(Files.readAllBytes(filePath));

        // Αρχικοποίηση SharedData and CountHolder
        int genomeDataLength = genomeData.length();
        int patternLength = pattern.length();
        SharedData sharedData = new SharedData(genomeData, pattern);
        CountHolder countHolder = new CountHolder(genomeDataLength - patternLength + 1);

        // Εκκίνηση Χρονομέτρου
        Timer timer = new Timer("Parrallel Execution");

        // Αρχικοποίηση των νημάτων
        PatternMatchThread[] threads = new PatternMatchThread[numOfThreads];
        int chunkSize = genomeDataLength / numOfThreads;

        for (int i = 0; i < numOfThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numOfThreads - 1) ? genomeDataLength : start + chunkSize;
            threads[i] = new PatternMatchThread(sharedData, countHolder, start, end);
            threads[i].start();
        }

        // Περιμένουμε όλα τα νήματα να τελειώσουν
        for (int i = 0; i < numOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Σταμάτημα του χρονομέτρου
        timer.stop();

        // Εκτύπωση των αποτελεσμάτων
        System.out.println("Matches found at positions: ");
        for (int i = 0; i < countHolder.getMatchArray().length; i++) {
            if (countHolder.getMatchArray()[i] == '1') {
                System.out.print(i + " ");
            }
        }

        System.out.println();
        System.out.println("Total matches: " + countHolder.getCount());
        System.out.println();

        timer.printTotalTime();
    }

    public static void copyFile(String source, int timesToCopy) throws IOException {
        // Αντιγράφει το αρχείο source timesToCopy φορές.
        // Αν source είναι "file.txt" και timesToCopy είναι 3, τότε το αρχείο "file.txt" θα αντιγραφεί 3 φορές.

        String destination = source.replace(".txt", "_x" + timesToCopy + ".txt");
        Path sourcePath = Path.of(source);
        Path destinationPath = Path.of(destination);

        List<String> lines = Files.readAllLines(sourcePath);

        // Open the destination file for appending
        try (var writer = Files.newBufferedWriter(destinationPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (int i = 0; i < timesToCopy; i++) {
                // Write the contents of the source file to the destination file
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        System.out.println("File copied successfully!");

    }
}