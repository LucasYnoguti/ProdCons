package prodcons.v6;

public class Message {
    private static int idCounter = 1;
    private final int id;
    private final long producerId;
    private boolean isLast;
    private int pendingCopies;

    private static synchronized int getNextId() {
        return idCounter++;
    }

    public Message(long producerId) {
        isLast = false;
        this.producerId = producerId;
        this.id = getNextId();
    }

    public int getId() {
        return id;
    }

    public long getProducerId() {
        return producerId;
    }

    public synchronized void setPendingCopies(int n) {
        this.pendingCopies = n;
    }

    public synchronized boolean consumeSync() throws InterruptedException {
        pendingCopies--;

        if (pendingCopies > 0) {
            while (pendingCopies > 0) {
                wait();
            }
            return false; // Não sou o último
        } else {
            isLast = true;
            notifyAll();
            return true;
        }
    }

    public synchronized void waitUntilConsumed() throws InterruptedException {
        while (pendingCopies > 0) {
            wait();
        }
    }
}
