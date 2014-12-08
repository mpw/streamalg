import org.junit.Before;
import org.junit.Test;
import streams.*;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;

public class TestAlgebrasPush {

    public Long[] v, v_inner;

    @Before
    public void setUp() {
        v = IntStream.range(0, 10).mapToObj(i -> new Long(i % 5)).toArray(Long[]::new);
        v_inner = IntStream.range(0, 5).mapToObj(i -> new Long(i % 5)).toArray(Long[]::new);
    }

    @Test
    public void testFilterCountPush(){

        PushAlg alg = new PushAlg();

        Long actual = alg.count(alg.filter(x -> (long) x % 2L == 0, alg.source(v)));

        Long expected = java.util.stream.Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .count();

        assertEquals(expected, actual);
    }

    @Test
    public void testMapPush(){
        PushAlg alg = new PushAlg();

        Long actual = alg.count(alg.map(x -> (long) x ^ 2, alg.source(v)));

        Long expected = java.util.stream.Stream.of(v)
                .map(x -> x^2)
                .count();

        assertEquals(expected, actual);
    }

    @Test
    public void testFlatMapPush(){
        PushAlg alg = new PushAlg();

        Long actual = alg.count(alg.flatMap(x -> {
            PushAlg inner = new PushAlg();
            return inner.map(y -> (long) x * (long) y, alg.source(v_inner));
        }, alg.source(v)));

        Long expected = java.util.stream.Stream.of(v)
                .flatMap(x -> java.util.stream.Stream.of(v_inner).map(y -> x * y))
                .count();

        assertEquals(expected, actual);
    }

    @Test
    public void testReducePush(){
        PushAlg alg = new PushAlg();

        Long actual = (Long) alg.<Long>reduce(0L, (a,x)-> (long) a+ (long) x, alg.map(x -> (long) x ^ 2, alg.source(v)));

        Long expected = java.util.stream.Stream.of(v)
                .map(x -> x^2)
                .reduce(0L, Long::sum);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakePush(){
        PushWithTakeAlg alg = new PushWithTakeAlg();

        Long actual = alg.count(alg.take(5, alg.source(v)));

        Long expected = java.util.stream.Stream.of(v)
                .limit(5)
                .count();

        assertEquals(expected, actual);
    }
}