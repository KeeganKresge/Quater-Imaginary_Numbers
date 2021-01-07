public class ThreadSleep extends Thread {
    public static final int NUM_THREADS = 5;
    public static int counter = 0;
    private int num;

    public ThreadSleep( int num ) {
        this.num = num;
    }

    public void run() {
        while ( this.num != counter ) {
            try {
                sleep( 1 );
            } catch (InterruptedException e) {}
        }
        System.out.println( "Thread " + num + " running!" );
        counter = ( counter + 2 ) % NUM_THREADS;
    }

    public static void main( String args [ ] ) {
        Thread[] threads = new Thread[NUM_THREADS];
        for ( int i = 0 ; i < NUM_THREADS; i++ ) {
            threads[i] = new ThreadSleep( i );
            threads[i].start();
        }
        for ( int i = 0 ; i < NUM_THREADS; i++ ) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }
    }
}