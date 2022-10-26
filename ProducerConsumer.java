import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
    public static void main(String[] args) {

        int n = 15;
        CountProducer countProducer = new CountProducer("Count producer", n);
        countProducer.start();

        FizzProducerConsumer fizzProducerConsumer = new FizzProducerConsumer("Fizz Producer Consumer", countProducer);
        fizzProducerConsumer.start();

        BuzzProducerConsumer buzzProducerConsumer = new BuzzProducerConsumer("Buzz Producer Consumer", fizzProducerConsumer);
        buzzProducerConsumer.start();

        FizzBuzzConsumer fizzBuzzConsumer = new FizzBuzzConsumer("FizzBuzz Consumer", buzzProducerConsumer);
        fizzBuzzConsumer.start();

    }
    public static class FizzBuzzObj {
        private int count;
        private boolean fizz;
        private boolean buzz;

        public FizzBuzzObj(int count, boolean fizz, boolean buzz) {
            this.count = count;
            this.fizz = fizz;
            this.buzz = buzz;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isFizz() {
            return fizz;
        }

        public void setFizz(boolean fizz) {
            this.fizz = fizz;
        }

        public boolean isBuzz() {
            return buzz;
        }

        public void setBuzz(boolean buzz) {
            this.buzz = buzz;
        }
    }
    public static class CountProducer extends Thread {
        int count = 1;
        final int n;

        final Queue<FizzBuzzObj> q = new LinkedList<>();

        public CountProducer(String name, int n) {
            super(name);
            this.n = n;
        }

        @Override
        public void run() {
            try {
                while (count <= n) produceCount(new FizzBuzzObj(count++, false, false));
            } catch (InterruptedException e) {
                System.out.println(e.getMessage() + "by "+getName());
            }
        }

        private synchronized void produceCount(FizzBuzzObj fizzBuzzObj) throws InterruptedException {
            q.offer(fizzBuzzObj);
//            System.out.println("Count produced");
            wait();
            notify();
        }

        public synchronized FizzBuzzObj consumeFizz() throws InterruptedException {
            notify();
            while (q.isEmpty()) wait();
            FizzBuzzObj data = q.poll();
//            System.out.println("Count consumed");
            data.setFizz(data.count % 3 == 0);
            return data;
        }
    }
    public static class FizzProducerConsumer extends Thread {
        private final CountProducer countProducer;
        final Queue<FizzBuzzObj> q = new LinkedList<>();


        public FizzProducerConsumer(String name, CountProducer countProducer) {
            super(name);
            this.countProducer = countProducer;
        }


        @Override
        public void run() {
            try {
                while (isAlive()) consumeCount();
            } catch (Exception e) {
                System.out.println(e.getMessage() + "by "+getName());
            }
        }


        private synchronized void consumeCount () throws InterruptedException {
            FizzBuzzObj fizzBuzzObj = countProducer.consumeFizz();
            produceBuzz(fizzBuzzObj);
        }

        private synchronized void produceBuzz(FizzBuzzObj fizzBuzzObj) throws InterruptedException {
            q.offer(fizzBuzzObj);
//            System.out.println("Fizz produced");
            wait();
            notify();
        }


        public synchronized FizzBuzzObj consumeBuzz() throws InterruptedException {
            notify();
            while (q.isEmpty()) wait();
            FizzBuzzObj fizzBuzzObj = q.poll();
//            System.out.println("Fizz consumed");
            fizzBuzzObj.setBuzz(fizzBuzzObj.count % 5 == 0);
            return fizzBuzzObj;
        }
    }
    public static class BuzzProducerConsumer extends Thread {
        private final FizzProducerConsumer fizzProducerConsumer;
        final Queue<FizzBuzzObj> q = new LinkedList<>();

        public BuzzProducerConsumer(String name, FizzProducerConsumer fizzProducerConsumer) {
            super(name);
            this.fizzProducerConsumer = fizzProducerConsumer;
        }

        @Override
        public void run() {
            try {
                while (isAlive()) consumeFizz();
            } catch (Exception e) {
                System.out.println(e.getMessage() + "by "+getName());
            }
        }

        private synchronized void consumeFizz () throws InterruptedException {
            FizzBuzzObj fizzBuzzObj = fizzProducerConsumer.consumeBuzz();
            produceFizzBuzz(fizzBuzzObj);
        }

        private synchronized void produceFizzBuzz (FizzBuzzObj fizzBuzzObj) throws InterruptedException {
            q.offer(fizzBuzzObj);
//            System.out.println("Buzz produced");
            wait();
            notify();
        }

        private synchronized FizzBuzzObj consumeFizzBuzz () throws InterruptedException {
            notify();
            while (q.isEmpty()) wait();
            FizzBuzzObj fizzBuzzObj = q.poll();
//            System.out.println("Fizz Buzz consumed");
            return fizzBuzzObj;
        }
    }
    public static class FizzBuzzConsumer extends Thread {
        private final BuzzProducerConsumer buzzProducerConsumer;

        public FizzBuzzConsumer(String name, BuzzProducerConsumer buzzProducerConsumer) {
            super(name);
            this.buzzProducerConsumer = buzzProducerConsumer;
        }

        @Override
        public void run() {
            try {
                while (isAlive()) consumeBuzz();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private synchronized void consumeBuzz () throws InterruptedException {
            FizzBuzzObj fizzBuzzObj = buzzProducerConsumer.consumeFizzBuzz();
            printFizzBuzz(fizzBuzzObj);
        }

        private synchronized void printFizzBuzz (FizzBuzzObj fizzBuzzObj) {
            StringBuilder sb = new StringBuilder();
            sb.append(fizzBuzzObj.count).append(" : ");

            if(fizzBuzzObj.isFizz()) sb.append("Fizz");
            if(fizzBuzzObj.isBuzz()) sb.append("Buzz");

            if(!sb.isEmpty()) System.out.println(sb);

            notify();
        }
    }
}
