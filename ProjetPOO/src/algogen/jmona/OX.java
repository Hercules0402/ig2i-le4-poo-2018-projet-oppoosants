package algogen.jmona;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OX {
    public static Map<Integer, ArrayList<Integer>> crossover(final List<Integer> p1, final List<Integer> p2) {
        int size = p1.size();

        int r1 = new Random().nextInt(size);
        int r2 = new Random().nextInt(size-1);

        int i = Math.min(r1, r2);
        int j = Math.max(r1, r2);

        ArrayList<Integer> c1 = new ArrayList<>();
        c1.addAll(p1.subList(i, j));
        
        ArrayList<Integer> c2 = new ArrayList<>();
        c2.addAll(p2.subList(i, j));

        int currentIndex = 0, currentInT1 = 0, currentInT2 = 0;
        for(int n=0; n<size; n++) {
            currentIndex = (j + n) % size;
            
            currentInT1 = p1.get(currentIndex);
            currentInT2 = p2.get(currentIndex);
            
            if (!c1.contains(currentInT2))
                c1.add(currentInT2);
            if (!c2.contains(currentInT1))
                c2.add(currentInT1);
        }

        Collections.rotate(c1, i);
        Collections.rotate(c2, i);
        
        Map<Integer, ArrayList<Integer>> cs = new HashMap();
        cs.put(0, c1);
        cs.put(1, c2);
        return cs;
    }
}