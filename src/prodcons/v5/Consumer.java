package prodcons.v5;

public class Consumer extends   Thread {
    private final IProdConsBuffer buffer;
    private final int consTime;
    private final int batchSize;

    public Consumer(IProdConsBuffer buffer, int consTime, int batchSize) {
        this.buffer = buffer;
        this.consTime = consTime;
        this.batchSize = batchSize;
    }
    @Override
    public void run() {
        try {
            while (true) {
                if (batchSize == 1) {
                    Message m = buffer.get();
                    if (m == null) break;
                    Thread.sleep(consTime);
                } else {
                    Message[] messages = buffer.get(batchSize);
                    if (messages == null || messages.length == 0 || messages[0] == null) {
                        break;
                    }
                    Thread.sleep(consTime * messages.length);
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Consumer " + getName() + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}