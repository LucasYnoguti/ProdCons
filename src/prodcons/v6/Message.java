package prodcons.v6;

public class Message {
    private static int idCounter = 1;
    private final int id;
    private final long producerId;
    private int remainingCopies;

    public Message(long producerId) {
        this.producerId = producerId;
        this.id = getNextId();
    }

    private static synchronized int getNextId() {
        return idCounter++;
    }

    public synchronized void setPendingCopies(int n) {
        this.remainingCopies = n;
    }

    public synchronized boolean decrement() {
        remainingCopies--;
        return (remainingCopies == 0);
    }

    public synchronized void waitUntilFinished() throws InterruptedException {
        while (remainingCopies > 0) {
            wait();
        }
    }

    public synchronized void signalFinished() {
        notifyAll();
    }

    public int getId() {
        return id;
    }

    public long getProducerId() {
        return producerId;
    }
}