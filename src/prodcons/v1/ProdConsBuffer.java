package prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {
    private final Message[] buffer;
    private final int bufferSz;
    private int nmsg;
    private int totmsg;
    private int in;
    private int out;

    public ProdConsBuffer(int bufferSz) {
        this.bufferSz = bufferSz;
        this.buffer = new Message[bufferSz];
        this.nmsg = 0;
        this.totmsg = 0;
        this.in = 0;
        this.out = 0;
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
        while (!(nmsg != 0)) {
            wait();
        }

        // POST-ACTION
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
}

/*
Opération      |    Pre-action   |          Garde       |               Post-action
put(Message m) |                 |  nmsg != bufferSz    | buffer[in] = m; in = (in + 1) % bufferSz; nmsg++; totmsg++; notifyAll();
Message get()  |                 |  nmsg != 0           | Message m = buffer[out]; out = (out + 1) % bufferSz; nmsg--; notifyAll();
*/