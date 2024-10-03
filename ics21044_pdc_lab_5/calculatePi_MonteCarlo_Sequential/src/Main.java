import java.util.Random;

// Η μέθοδος Μόντε Κάρλο για την εκτίμηση του π βασίζεται στην ιδέα ότι αν "σκορπίσουμε" τυχαία σημεία μέσα σε ένα τετράγωνο
// και μετρήσουμε τον αριθμό των σημείων που εμπίπτουν σε έναν κύκλο εγγεγραμμένο μέσα στο τετράγωνο, τότε μπορούμε να
// εκτιμήσουμε την τιμή του π.
//
// Η προσέγγιση του π μπορεί να υπολογιστεί ως εξής:
// π = 4 * (Αριθμός σημείων που εμπίπτουν στον κύκλο) / (Συνολικός αριθμός σημείων)
//
// Η μέθοδος αυτή δουλεύει γιατί η πιθανότητα να βρεθεί ένα σημείο μέσα στον κύκλο είναι ανάλογη του εμβαδού του κύκλου, που είναι το π.
// Επομένως, μετρώντας τον αριθμό των σημείων που πέφτουν μέσα στον κύκλο, μπορούμε να εκτιμήσουμε την τιμή του pi.
//
// Πηγή: https://medium.com/the-modern-scientist/estimating-pi-using-monte-carlo-methods-dbdf26c888d6

// ΠΑΡΑΛΛΗΛΟΠΟΙΗΣΗ:
// Σε αυτό το πρόγραμμα, ο βρόχος που δημιουργεί τυχαία σημεία και ελέγχει αν βρίσκονται μέσα στον κύκλο μπορεί να εκτελεστεί παράλληλα.
// Στην πράξη, κάθε επανάληψη του βρόχου είναι ανεξάρτητη από άλλες επαναλήψεις. Ο υπολογισμός της θέσης κάθε σημείου και του κατά πόσον
// βρίσκεται εντός του κύκλου δεν εξαρτάται από τα αποτελέσματα άλλων σημείων. Επομένως, μπορούμε να χωρίσουμε τον συνολικό αριθμό των σημείων
// σε μικρότερα κομμάτια και να κατανείμουμε αυτά τα κομμάτια σε πολλά νήματα για να εκτελέσουμε τους υπολογισμούς ταυτόχρονα.

public class Main {
    public static void main(String[] args) {
        // Number of points inside the circle
        int insideCircle = 0;
        // Total number of points
        int totalPoints = 1000000; // You can increase this value for better accuracy

        // Create a Random object
        Random rand = new Random();

        // Loop to generate random points and check if they are inside the circle
        for (int i = 0; i < totalPoints; i++) {
            double x = rand.nextDouble(); // Generate random x-coordinate between 0 and 1
            double y = rand.nextDouble(); // Generate random y-coordinate between 0 and 1

            // Check if the point is inside the circle (x^2 + y^2 <= 1)
            if (x * x + y * y <= 1) {
                insideCircle++;
            }
        }

        // Calculate the approximation of pi using the ratio of points inside the circle to total points
        double piApproximation = 4.0 * insideCircle / totalPoints;

        // Print the approximation of pi
        System.out.println("Approximation of pi using Monte Carlo method: " + piApproximation);
    }
}