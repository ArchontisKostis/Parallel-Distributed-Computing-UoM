import java.awt.*;

public class MyColorUtils {
    public static int RED_SHIFT = 100;
    public static int GREEN_SHIFT = 100;
    public static int BLUE_SHIFT = 100;

    // Private constructor ωστε να μην μπορεί να δημιουργηθεί αντικείμενο της κλάσης
    private MyColorUtils() {}

    public static Color saturateColor(Color color) {
        int red = saturate(color.getRed(), RED_SHIFT);
        int green = saturate(color.getGreen(), GREEN_SHIFT);
        int blue = saturate(color.getBlue(), BLUE_SHIFT);

        return new Color(red, green, blue);
    }

    public static int saturate(int color, int shift) {
        return (color + shift) % 256;
    }
}
