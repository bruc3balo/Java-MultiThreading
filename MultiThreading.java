
public class MultiThreading {
    public static void main(String[] args) throws InterruptedException {

        int n = 15;

        Count count = new Count("Count", n);
        count.start();

        //++ count
        //  fizz
        //  wait
        //  buzz
        //  wait
        //  fizzbuzz
        //  loop

    }

    public static class Count extends Thread {
        int count = 1;
        final int n;

        public Count(String name, int n) {
            super(name);
            this.n = n;
        }

        @Override
        public void run() {

            while (count <= n) {


                try {
                    Fizz fizz = new Fizz("Fizz", count);
                    fizz.start();
                    fizz.join();

                    Buzz buzz = new Buzz("Buzz", count);
                    buzz.start();
                    buzz.join();

                    FizzBuzz fizzBuzz = new FizzBuzz("Fizzbuzz", count, fizz.fizz, buzz.buzz);
                    fizzBuzz.start();
                    fizzBuzz.join();

                    count++;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Finish execution");
        }
    }

    public static class Fizz extends Thread {
        public Integer count;
        public boolean fizz;

        public Fizz(String name, Integer count) {
            super(name);
            this.count = count;
        }

        @Override
        public void run() {
            fizz = count % 3 == 0;
        }
    }

    public static class Buzz extends Thread {
        public Integer count;
        public boolean buzz;

        public Buzz(String name, Integer count) {
            super(name);
            this.count = count;
        }

        @Override
        public void run() {
            buzz = count % 5 == 0;
        }
    }

    public static final class FizzBuzz extends Thread {
        public Integer count;
        public boolean fizz;
        public boolean buzz;

        public FizzBuzz(String name, Integer count, boolean fizz, boolean buzz) {
            super(name);
            this.count = count;
            this.fizz = fizz;
            this.buzz = buzz;
        }

        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            if (fizz) sb.append("Fizz : ");
            if (buzz) sb.append("Buzz : ");
            sb.append(count);
            if (!sb.isEmpty()) System.out.println(sb);
        }
    }

}
