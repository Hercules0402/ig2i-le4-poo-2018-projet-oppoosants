package algogen;

import java.util.ArrayList;
import java.util.List;
import util.Reader;
import metier.Instance;

public class TestGA {
    public static void main(String[] args) throws Exception {
        String fileName = "instance_40000.txt";

        /*Reader*/
        Instance inst = Reader.read(fileName, false); 

        /*GA*/
        //List<Trolley> trolleys = AlgoGen.run(inst);
        List<ArrayList<Integer>> products = GAColis.runTest(inst.getOrders().get(0), inst);
    }
}