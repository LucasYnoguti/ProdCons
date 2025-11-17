package v2;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class TestProdCons {
    public static void main(String[] args) {

        Properties properties = new Properties();
        try {
            properties.loadFromXML(
                    TestProdCons.class.getClassLoader().getResourceAsStream("prodcons/v1/options.xml")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        int nProd = Integer.parseInt(properties.getProperty("nProd"));
        int nCons = Integer.parseInt(properties.getProperty("nCons"));
        int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
        int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
        int consTime = Integer.parseInt(properties.getProperty("consTime"));
        int minProd = Integer.parseInt(properties.getProperty("minProd"));
        int maxProd = Integer.parseInt(properties.getProperty("maxProd"));

        ProdConsBuffer prodConsBuffer = new ProdConsBuffer(bufSz, nProd);
        Random rand = new Random();
        int r, curCons= 0, curProd=0;

        //create producers and consumers randomly
        for (int i = 0; i < nCons + nProd; i++) {
            r = rand.nextInt(2);
            if((r == 0 && curCons < nCons) || curProd == nProd) {
                new Consumer(prodConsBuffer, consTime).start();
                curCons++;
            }
            else if(curProd < nProd || curCons == nCons) {
                new Producer(prodConsBuffer, minProd, maxProd, prodTime).start();
                curProd++;
            }
        }
    }
}
/*
Opération      |    Pre-action   |          Garde                               |               Post-action
put(Message m) |                 |  nmsg != bufferSz & || activeProducers <= 0  | buffer[in] = m; in = (in + 1) % bufferSz; nmsg++; totmsg++; notifyAll();
Message get()  |                 |  nmsg != 0                                   |    Message m = buffer[out]; out = (out + 1) % bufferSz; nmsg--; notifyAll();
*/