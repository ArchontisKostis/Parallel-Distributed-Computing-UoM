import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static String INPUT_FILE_NAME = "original.jpg";
    public static int NUM_THREADS = 4;

    public static void main(String[] args) {
        parallel();
//        sequential(args);
    }

    public static void parallel() {
        String OUTPUT_FILENAME = INPUT_FILE_NAME.replace(".jpg", "_grayscale.jpg");

        BufferedImage img = readImage(INPUT_FILE_NAME);

        long startTime = System.currentTimeMillis();

        // Πίνακας για αποθήκευση των Νημάτων
        GrayscaleThread[] threads = new GrayscaleThread[NUM_THREADS];

        // Υπολογισμός τμήματος που αναλαμβάνει κάθε νήμα με βάση το ύψος της εικόνας (ωστε καθε νήμα να αναλαμβάνει μια "γραμμή" της εικόνας)
        int chunkSize = img.getHeight() / NUM_THREADS;

        // Δημιουργία και εκκίνηση νημάτων
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new GrayscaleThread(img, i, chunkSize);
            threads[i].start();
        }

        // Αναμονή τερματισμού όλων των νημάτων
        for (int i = 0; i < NUM_THREADS; i++) {
            try { threads[i].join(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        long endTime = System.currentTimeMillis();

        saveImage(img, OUTPUT_FILENAME);

        System.out.println("Done...");
        System.out.println("Time: " + (endTime - startTime) + " ms");
    }

    public static BufferedImage readImage(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static boolean saveImage(BufferedImage img, String filename) {
        try {
            File file = new File(filename);
            ImageIO.write(img, "jpg", file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ------ ΑΚΟΛΟΥΘΙΑΚΗ ΕΚΤΕΛΕΣΗ ------ //
    public static void sequential(String[] args) {
        String OUTPUT_FILENAME = INPUT_FILE_NAME.replace(".jpg", "_grayscale.jpg");

        //Reading Input file to an image
        BufferedImage img = readImage(INPUT_FILE_NAME);

        //Start timing
        long start = System.currentTimeMillis();

        //Coefficinets of R G B to GrayScale
        double redCoefficient = 0.299;
        double greenCoefficient = 0.587;
        double blueCoefficient = 0.114;

        // Η επεξεργασία της εικόνας ανά εικονοστοιχείο είναι κατάλληλος υπολογισμός για παράλληλη εκτέλεση,
        // επειδή η τιμή της κλίμακας του γκρι για κάθε εικονοστοιχείο μπορεί να υπολογιστεί ανεξάρτητα από τις υπόλοιπες.
        // Στην πραξη μπορούμε να φτιάξουμε πολλά νήματα όπου κάθε νήμα θα υπολογίζει την τιμή του γκρι για ένα τμήμα της εικόνας.
        // Έτσι θα μπορούσαμε να επιτύχουμε ταχύτερη επεξεργασία της εικόνας αφού θα εκτελούνται παράλληλα οι υπολογισμοί.
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Retrieving contents of a pixel
                int pixel = img.getRGB(x,y);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                //Retrieving the R G B values, 8 bits per r,g,b
                //Calculating GrayScale
                int red = (int) (color.getRed() * redCoefficient);
                int green = (int) (color.getGreen() * greenCoefficient);
                int blue = (int) (color.getBlue() * blueCoefficient);
                //Creating new Color object
                color = new Color(red+green+blue,
                        red+green+blue,
                        red+green+blue);
                //Setting new Color object to the image
                img.setRGB(x, y, color.getRGB());
            }
        }

        //Stop timing
        long elapsedTimeMillis = System.currentTimeMillis()-start;

        //Saving the modified image to Output file
        try {
            File file = new File(OUTPUT_FILENAME);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {}

        System.out.println("Done...");
        System.out.println("time in ms = "+ elapsedTimeMillis);
    }
}