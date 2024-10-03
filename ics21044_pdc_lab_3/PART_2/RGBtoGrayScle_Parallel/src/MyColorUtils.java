import java.awt.*;

public class MyColorUtils {
    public static double RED_COEFFICIENT = 0.299;
    public static double GREEN_COEFFICIENT = 0.587;
    public static double BLUE_COEFFICIENT = 0.114;

    // Private constructor ωστε να μην μπορεί να δημιουργηθεί αντικείμενο της κλάσης
    private MyColorUtils() {}

    public static Color getColorInGrayScale(Color color) {
        int red = calculateCoefficient(color.getRed(), RED_COEFFICIENT);
        int green = calculateCoefficient(color.getGreen(), GREEN_COEFFICIENT);
        int blue = calculateCoefficient(color.getBlue(), BLUE_COEFFICIENT);

        int grayScaleColor = red + green + blue;

        return new Color(grayScaleColor, grayScaleColor, grayScaleColor);
    }

    public static int calculateCoefficient(int color, double coefficient) {
        return (int) (color * coefficient);
    }
}
