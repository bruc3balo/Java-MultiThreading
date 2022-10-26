import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadPools {

    public static void main(String[] args) {
        //as many as possible
        Executor cachedThreadPool = Executors.newCachedThreadPool();

        //one thread
        Executor singleThreadPool = Executors.newSingleThreadExecutor();

        //creates n thread pools
        Executor numberedThreadPool = Executors.newFixedThreadPool(5);


        cachedThreadPool.execute(() -> {});
        singleThreadPool.execute(() -> {});
        numberedThreadPool.execute(() -> {});
    }
}
