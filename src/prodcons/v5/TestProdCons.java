package prodcons.v5;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class            TestProdCons {
    public static void main(String[] args) {

        Properties properties = new Properties();
        try {
            properties.loadFromXML(
                    TestProdCons.class.getClassLoader().getResourceAsStream("prodcons/v5/options.xml")
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

        for (int i = 0; i < nCons + nProd; i++) {
            r = rand.nextInt(2);
            if ((r == 0 && curCons < nCons) || curProd == nProd) {
                //1 to 3 messages
                int k = rand.nextInt(3) + 1;
                new Consumer(prodConsBuffer, consTime, k).start();
                curCons++;
            } else if (curProd < nProd || curCons == nCons) {
                new Producer(prodConsBuffer, minProd, maxProd, prodTime).start();
                curProd++;
            }
        }
    }
}