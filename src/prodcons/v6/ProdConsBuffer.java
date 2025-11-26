package prodcons.v6;

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
    public void put(Message m, int nCopies) throws InterruptedException {
        notFull.acquire();
        mutex.acquire();
        try {
            m.setPendingCopies(nCopies);
            buffer[in] = m;
            in = (in + 1) % bufferSz;
            nmsg++;
            totmsg++;

            System.out.println("Producer #" + m.getProducerId() + " produced " + nCopies + " copies of message #" + m.getId());
        } finally {
            mutex.release();
        }
        //releasing n times so that n producers enter and get the instances of the msg
        notEmpty.release(nCopies);
        //blocking producer until all messages are consumed
        m.waitUntilFinished();
    }

    @Override
    public Message get() throws InterruptedException {
        notEmpty.acquire();
        Message m = null;
        boolean isLastConsumer = false;
        mutex.acquire();
        try {
            m = buffer[out];

            isLastConsumer = m.decrement();

            if (isLastConsumer) {
                out = (out + 1) % bufferSz;
                nmsg--;
                notFull.release();
                m.signalFinished();
            }

            System.out.println("Consumer #" + Thread.currentThread().getId() +
                    " taking copy of message #" + m.getId());

        } finally {
            mutex.release();
        }
        m.waitUntilFinished();
        System.out.println("Consumer #" + Thread.currentThread().getId() + " finished message #" + m.getId());

        return m;
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