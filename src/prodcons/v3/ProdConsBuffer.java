package prodcons.v3;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {
    private final Message[] buffer;
    private final int bufferSz;
    private int in;
    private int out;
    private int nmsg;
    private int totmsg;
    private int activeProducers;

    private final Semaphore notFull;
    private final Semaphore notEmpty;
    private final Semaphore mutex;

    public ProdConsBuffer(int bufferSz, int nProd) {
        this.bufferSz = bufferSz;
        this.buffer = new Message[bufferSz];
        this.in = 0;
        this.out = 0;
        this.nmsg = 0;
        this.totmsg = 0;
        this.activeProducers = nProd;

        this.notFull = new Semaphore(bufferSz);
        this.notEmpty = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    @Override
    public void put(Message m) throws InterruptedException {
        notFull.acquire();
        mutex.acquire();
        try {
            buffer[in] = m;
            in = (in + 1) % bufferSz;
            nmsg++;
            totmsg++;

            System.out.println("Producer #" + m.getProducerId() + " produced message #" + m.getId());
        } finally {
            mutex.release();
        }

        notEmpty.release();
    }

    @Override
    public Message get() throws InterruptedException {
        notEmpty.acquire();
        Message m = null;
        mutex.acquire();
        try {
            if (nmsg == 0 && activeProducers == 0) {
                notEmpty.release();
                return null;
            }
            m = buffer[out];
            out = (out + 1) % bufferSz;
            nmsg--;

            System.out.println(
                    "Consumer #" + Thread.currentThread().getId() + " consumed message #" + m.getId()
            );
        } finally {
            mutex.release();
        }

        notFull.release();
        return m;
    }

    @Override
    public void producerDone() throws InterruptedException {
        mutex.acquire();
        try {
            activeProducers--;
            if (activeProducers == 0) {
                notEmpty.release();
            }
        } finally {
            mutex.release();
        }
    }

    @Override
    public int nmsg() {
        int val = 0;
        try {
            mutex.acquire();
            val = nmsg;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
        return val;
    }

    @Override
    public int totmsg() {
        int val = 0;
        try {
            mutex.acquire();
            val = totmsg;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
        return val;
    }
}