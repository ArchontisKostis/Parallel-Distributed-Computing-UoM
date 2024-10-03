class Sequential
{
    public static void main(String[] args)
    {
        System.out.println("Sequential Sieve Running");

        int size = 0;
        if (args.length != 1) {
            System.out.println("Usage: java SieveOfEratosthenes <size>");
            System.exit(1);
        }

        try {
            size = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }
        if (size <= 0) {
            System.out.println("size should be positive integer");
            System.exit(1);
        }

        boolean[] prime = new boolean[size+1];

        for(int i = 2; i <= size; i++)
            prime[i] = true;

        // get current time
        Timer timer = new Timer("Sequential Sieve");

        //for (int p = 2; p*p <= size; p++)
        int limit = (int)Math.sqrt(size) + 1;
        for (int p = 2; p <= limit; p++)
        {
            // If prime[p] is not changed, then it is a prime
            if(prime[p] == true)
            {
                // Update all multiples of p
                for (int i = p*p; i <= size; i += p)
                    prime[i] = false;
            }
        }

        // get current time and calculate elapsed time
        timer.stop();

        int count = 0;
        for(int i = 2; i <= size; i++)
            if (prime[i] == true) {
                //System.out.println(i);
                count++;
            }

        System.out.println("number of primes "+count);
        timer.printTotalTime();
    }

}
