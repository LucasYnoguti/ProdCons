package prodcons.v4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProdConsBuffer implements IProdConsBuffer {
    private final Message[] buffer;
    private final int bufferSz;
    private int in;
    private int out;
    private int nmsg;
    private int totmsg;
    private final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public ProdConsBuffer(int bufferSz) {
        this.bufferSz = bufferSz;
        this.buffer = new Message[bufferSz];
        this.in = 0;
        this.out = 0;
        this.nmsg = 0;
        this.totmsg = 0;
    }

    @Override
    public void put(Message m) throws InterruptedException {
        lock.lock();
        try {
            while (nmsg == bufferSz) {
                notFull.await();
            }

            buffer[in] = m;
            in = (in + 1) % bufferSz;
            nmsg++;
            totmsg++;

            System.out.println("Producer #" + m.getProducerId() + " produced message #" + m.getId());

            notEmpty.signal();

        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message get() throws InterruptedException {
        lock.lock();
        try {
            while (nmsg == 0) {
                notEmpty.await();
            }

            Message m = buffer[out];
            out = (out + 1) % bufferSz;
            nmsg--;

            System.out.println("Consumer #" + Thread.currentThread().getId() + " consumed message #" + m.getId());

            notFull.signal();

            return m;

        } finally {
            lock.unlock();
        }
    }

    @Override
    public int nmsg() {
        lock.lock();
        try {
            return nmsg;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int totmsg() {
        lock.lock();
        try {
            return totmsg;
        } finally {
            lock.unlock();
        }
    }
}