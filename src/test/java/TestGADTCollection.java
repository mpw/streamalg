import org.junit.Test;
import collection.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bibou on 12/9/14.
 */
public class TestGADTCollection {

    @Test
    public void testFilter(){
        List<Integer> list = new List<>();

        list.add(3);

        list.add(5);

        List<Integer> list2 = List.prj(list.remove(x -> x>3));

        assertEquals(list2.count(), 1);
        assertEquals(list2.get(0).intValue(), 3);
    }
}