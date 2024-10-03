import java.lang.Math;

class Main {
    public static int CIRCUIT_SIZE = 28;
    public static int NUM_THREADS = 4;

    public static void main(String[] args) {
        parallel();
    }

    public static void parallel() {
        int iterations = (int) Math.pow(2, CIRCUIT_SIZE);
        long startTime = System.currentTimeMillis();

        SimpleSatThread[] threads = new SimpleSatThread[NUM_THREADS];
        int chunkSize = iterations / NUM_THREADS;   // Κάθε νήμα αναλαμβάνει τον έλεγχο του κυκλώματος για ένα τμήμα των συνδυασμών

        // Δημιουργία και εκκίνηση νημάτων
        for (int i = 0; i < NUM_THREADS; i++) {
            int threadStart = i * chunkSize;
            int threadEnd = (i == NUM_THREADS - 1) ? iterations : threadStart + chunkSize;
            threads[i] = new SimpleSatThread(threadStart, threadEnd, CIRCUIT_SIZE);
            threads[i].start();
        }

        // Αναμονή τερματισμού όλων των νημάτων
        for (int i = 0; i < NUM_THREADS; i++) {
            try { threads[i].join(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("All done" + System.lineSeparator());

        System.out.println("Time: " + (endTime - startTime) + " ms");
    }

//    public static void sequential() {
//        // Circuit input size (number of bits)
//        int size = 28;
//        // Number of possible inputs (bit combinations)
//        int iterations = (int) Math.pow(2, size);
//
//        // Start timing
//        long start = System.currentTimeMillis();
//
//        // Check all possible inputs
//
//        // Το σημείο του κώδικα που μπορεί να παραλληλοποιηθεί
//        // είναι η επανάληψη που ελέγχει όλους τους συνδυασμούς
//        // των εισόδων του κυκλώματος. Κάθε νήμα μπορεί να αναλάβει
//        // τον έλεγχο ενός τμήματος των συνδυασμών αφου ο κάθε συνδυασμός
//        // είναι ανεξάρτητος από τους υπόλοιπους.
//        for (int i = 0; i < iterations; i++)
//            check_circuit (i, size);
//
//        // Stop timing
//        long elapsedTimeMillis = System.currentTimeMillis()-start;
//
//        System.out.println ("All done\n");
//        System.out.println("time in ms = "+ elapsedTimeMillis);
//    }

    public static void check_circuit (int z, int size) {

        // z: the combination number
        // v: the combination number in binary format

        boolean[] v = new boolean[size];

        for (int i = size-1; i >= 0; i--)
            v[i] = (z & (1 << i)) != 0;

        // Check the ouptut of the circuit for input v
        boolean value =
                (  v[0]  ||  v[1]  )
                        && ( !v[1]  || !v[3]  )
                        && (  v[2]  ||  v[3]  )
                        && ( !v[3]  || !v[4]  )
                        && (  v[4]  || !v[5]  )
                        && (  v[5]  || !v[6]  )
                        && (  v[5]  ||  v[6]  )
                        && (  v[6]  || !v[15] )
                        && (  v[7]  || !v[8]  )
                        && ( !v[7]  || !v[13] )
                        && (  v[8]  ||  v[9]  )
                        && (  v[8]  || !v[9]  )
                        && ( !v[9]  || !v[10] )
                        && (  v[9]  ||  v[11] )
                        && (  v[10] ||  v[11] )
                        && (  v[12] ||  v[13] )
                        && (  v[13] || !v[14] )
                        && (  v[14] ||  v[15] )
                        && (  v[14] ||  v[16] )
                        && (  v[17] ||  v[1]  )
                        && (  v[18] || !v[0]  )
                        && (  v[19] ||  v[1]  )
                        && (  v[19] || !v[18] )
                        && ( !v[19] || !v[9]  )
                        && (  v[0]  ||  v[17] )
                        && ( !v[1]  ||  v[20] )
                        && ( !v[21] ||  v[20] )
                        && ( !v[22] ||  v[20] )
                        && ( !v[21] || !v[20] )
                        && (  v[22] || !v[20] );

        // If output == 1 print v and z
        if (value) {
            printResult(v, size, z);
        }
    }

    // Printing utility
    static void printResult (boolean[] v, int size, int z) {

        String result = null;
        result = String.valueOf(z);

        for (int i=0; i< size; i++)
            if (v[i]) result += " 1";
            else result += " 0";

        System.out.println(result);
    }

}
