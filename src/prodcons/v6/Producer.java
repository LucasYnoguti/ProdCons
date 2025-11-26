package prodcons.v6;

import java.util.Random;

public class Producer extends Thread {
    private final ProdConsBuffer buffer;
    private final int minProd;
    private final int maxProd;
    private final int prodTime;
    private final int nCopies;
    private final Random rand = new Random();
    public Producer(ProdConsBuffer buffer, int minProd, int maxProd, int prodTime, int nCopies) {
        this.buffer = buffer;
        this.minProd = minProd;
        this.maxProd = maxProd;
        this.prodTime = prodTime;
        this.nCopies = nCopies;
    }
    @Override
    public void run() {
        try {
            int noMsgs = minProd + rand.nextInt(maxProd - minProd + 1);
            for (int i = 1; i <= noMsgs; i++) {
                Message m = new Message(getId());
                buffer.put(m, nCopies);
                Thread.sleep(prodTime);
            }
        } catch (InterruptedException e) {
            System.err.println("Producer " + getId() + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}