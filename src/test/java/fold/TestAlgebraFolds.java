package fold;

import com.google.common.collect.Iterators;
import org.junit.Before;
import org.junit.Test;
import streams.fold.Stream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class TestAlgebraFolds {

    public Long[] v;

    @Before
    public void setUp() {
        v = IntStream.range(0, 100).mapToObj(i -> (long) (i % 5)).toArray(Long[]::new);
    }

    @Test
    public void testFilterPush() {
        long size = Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .count();

        long size2 = java.util.stream.Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .count();

        assert size == size2;
    }

    @Test
    public void testMapPush() {

        long size = Stream.of(v)
                .map(x -> x + 1L)
                .count();

        long size2 = java.util.stream.Stream.of(v)
                .map(x -> x + 1L)
                .count();

        assert size == size2;
    }

    @Test
    public void testFilterPull() {
        Iterator<Long> it1 = Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .iterator();

        Iterator<Long> it2 = java.util.stream.Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .iterator();

        ArrayList<Long> l1 = new ArrayList<>();
        ArrayList<Long> l2 = new ArrayList<>();

        Iterators.addAll(l1, it1);
        Iterators.addAll(l2, it2);

        assertEquals(l1, l2);
    }

    @Test
    public void testMapPull() {
        Iterator<Long> it1 = Stream.of(v)
                .map(x -> x + 1)
                .iterator();

        Iterator<Long> it2 = java.util.stream.Stream.of(v)
                .map(x -> x + 1)
                .iterator();

        ArrayList<Long> l1 = new ArrayList<>();
        ArrayList<Long> l2 = new ArrayList<>();

        Iterators.addAll(l1, it1);
        Iterators.addAll(l2, it2);

        assertEquals(l1, l2);
    }

    @Test
    public void testFlatMapPush() {

        long size = Stream.of(v)
                .flatMap(x -> Stream.of(v).map(y -> x * y))
                .count();

        long size2 = java.util.stream.Stream.of(v)
                .flatMap(x -> java.util.stream.Stream.of(v).map(y -> x * y))
                .count();

        assert size == size2;
    }
}