package v2;

public class ProdConsBuffer implements IProdConsBuffer {
    private final Message[] buffer;
    private final int bufferSz;
    private int nmsg;
    private int totmsg;
    private int in;
    private int out;
    private int activeProducers;

    public ProdConsBuffer(int bufferSz, int nProd) {
        this.bufferSz = bufferSz;
        this.buffer = new Message[bufferSz];
        this.nmsg = 0;
        this.totmsg = 0;
        this.in = 0;
        this.out = 0;
        this.activeProducers = nProd;
    }
    @Override
    public synchronized void put(Message m) throws InterruptedException {
        // GARDE
        while (!(nmsg != bufferSz)) {
            wait();
        }

        //POST-ACTION
        buffer[in] = m;
        in = (in + 1) % bufferSz;
        nmsg++;
        totmsg++;

        System.out.println("Producer #" + m.getProducerId() + " produced message #" + m.getId());
        notifyAll();
    }

    @Override
    public synchronized Message get() throws InterruptedException {
        // GARDE
        while (!(nmsg != 0) && activeProducers > 0) {
            wait();
        }
        // POST-ACTION

        //if the buffer is empty and the producers are finished, end
        if (nmsg == 0 && activeProducers == 0) {
            return null; // termination sign
        }

        Message m = buffer[out];
        out = (out + 1) % bufferSz;
        nmsg--;
        System.out.println("Consumer #" + Thread.currentThread().getId() + " consumed message #" + m.getId());
        notifyAll();
        return m;
    }

    @Override
    public int nmsg() {
        return nmsg;
    }

    @Override
    public synchronized int totmsg() {
        return totmsg;
    }

    @Override
    public synchronized void producerDone() {
        this.activeProducers--;
        if (this.activeProducers == 0) {
            notifyAll();
        }
    }
}