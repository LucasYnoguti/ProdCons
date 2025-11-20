package prodcons.v3;

public class Consumer extends   Thread {
    private final IProdConsBuffer buffer;
    private final int consTime;

    public Consumer(IProdConsBuffer buffer, int consTime) {
        this.buffer = buffer;
        this.consTime = consTime;
    }
    @Override
    public void run() {
        try {
            while (true) {
                Message m = buffer.get();
                if (m == null) {
                    break;
                }
                Thread.sleep(consTime);
            }
        } catch (InterruptedException e) {
            System.err.println("Consumer " + getName() + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}