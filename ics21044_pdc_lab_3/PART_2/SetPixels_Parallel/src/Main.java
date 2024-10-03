import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static String INPUT_FILE_NAME = "UOMLOGOGR.png";
    public static int NUM_THREADS = 4;

    public static void main(String[] args) {
        parallel();
    }

    public static void parallel() {
        String fileNameR = INPUT_FILE_NAME;
        String fileNameW = INPUT_FILE_NAME.replace(".jpg", "_saturate.jpg");

        BufferedImage img = readImage(fileNameR);

        long start = System.currentTimeMillis();

        SaturationThread[] threads = new SaturationThread[NUM_THREADS];

        int chunkSize = img.getHeight() / NUM_THREADS;

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new SaturationThread(img, i, chunkSize);
            threads[i].start();
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            try { threads[i].join(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        long elapsedTimeMillis = System.currentTimeMillis()-start;

        saveImage(img, fileNameW);

        System.out.println("Done...");
        System.out.println("time in ms = "+ elapsedTimeMillis);
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
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void sequential() {
        System.out.println("Sequential");

        String fileNameR = INPUT_FILE_NAME;
        String fileNameW = INPUT_FILE_NAME.replace(".jpg", "_saturate.jpg");

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileNameR));
        } catch (IOException e) {}

        long start = System.currentTimeMillis();

        int redShift = 100;
        int greenShift = 100;
        int blueShift = 100;

        // Όπως και στο πρόγραμμα RGBtoGrayscale το κομμάτι που επωφελείται από την παραλληλοποίηση είναι η επεξεργασία των pixels της εικόνας.
        // Στο παρόν πρόγραμμα η επεξεργασία των pixels γίνεται σειριακά, δηλαδή ένα προς ένα.
        // Για να παραλληλοποιήσουμε τον κώδικα επεξεργασίας του saturation της εικόνας μπορούμε να κατανείμουμε την εικόνα σε τμήματα (chunks) και να αναθέσουμε
        // σε κάθε νήμα την επεξεργασία ενός τμήματος. Στην πράξη θα χωρίσουμε την εικόνα σε "γραμμές" και θα αναθέσουμε σε κάθε νήμα την επεξεργασία μιας γραμμής.
        // Ο λόγος που θα παραλληλοποιήσουμε αυτό το τμήμα του κώδικα είναι γιατι στην πραγματικότητα η επεξεργασία και ο υπολογισμός του saturation ενός pixel
        // ή ενός τμήματος της εικόνας είναι ανεξάρτητος από την επεξεργασία ενός άλλου. Έτσι μπορούμε να επωφεληθούμε από την παραλληλοποίηση του κώδικα.
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Retrieving contents of a pixel
                int pixel = img.getRGB(x,y);
                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                //Retrieving the R G B values, 8 bits per r,g,b
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                //Modifying the RGB values
                red = (red + redShift)%256;
                green = (green + greenShift)%256;
                blue = (blue + blueShift)%256;
                //Creating new Color object
                color = new Color(red, green, blue);
                //Setting new Color object to the image
                img.setRGB(x, y, color.getRGB());
            }
        }

        long elapsedTimeMillis = System.currentTimeMillis()-start;

        try {
            File file = new File(fileNameW);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {}

        System.out.println("Done...");
        System.out.println("time in ms = "+ elapsedTimeMillis);
    }
}