import java.awt.*;
import java.awt.image.BufferedImage;

public class SaturationThread extends Thread {
    private final BufferedImage image;
    private final int startRow;
    private final int stopRow;

    public SaturationThread(BufferedImage image, int i, int chunkSize) {
        this.image = image;
        this.startRow = i * chunkSize;
        this.stopRow = (i == Main.NUM_THREADS - 1) ? image.getHeight() : (i + 1) * chunkSize;
    }

    // Κάθε νήμα αναλαμβάνει την επεξεργασία των pixels μιας γραμμής της εικόνας.
    @Override
    public void run() {
        for (int y = startRow; y < stopRow; y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);

                Color color = new Color(pixel, true);
                Color saturatedColor = MyColorUtils.saturateColor(color);

                image.setRGB(x, y, saturatedColor.getRGB());
            }
        }
    }
}
