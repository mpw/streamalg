package algebras;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import streams.algebras.ExecIterateStreamAlg;
import streams.factories.ExecPullFactory;
import streams.factories.ExecPullWithIterateFactory;
import streams.factories.PullFactory;
import streams.higher.App;
import streams.higher.Id;
import streams.higher.Pull;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Authors:
 * Aggelos Biboudis (@biboudis)
 * Nick Palladinos (@NickPalladinos)
 */
public class TestAlgebrasFlatMapAlgebras extends BaseTest {

    public Long[] v, v_small;

    @Before
    public void setUp() {
        v = IntStream.range(0, 15).mapToObj(Long::new).toArray(Long[]::new);
        v_small = IntStream.range(0, 5).mapToObj(Long::new).toArray(Long[]::new);
    }

    @Test
    public void testPull() {
        PullFactory algebra = new PullFactory();

        App<Pull.t, Long> map = algebra.map(y -> {
            System.out.print("inner: " + y + '\n');
            return (long) 10 * y;
        }, algebra.source(v_small));

        App<Pull.t, Long> flatMap = algebra.flatMap(y -> map, algebra.source(v));

        Pull<Long> prj = Pull.prj(flatMap);

        prj.hasNext();

        System.out.print(prj.next());

        Assert.assertEquals(
                "inner: 0\n" +
                        "0",
                outContent.toString());
    }

    @Test
    public void testJava8StreamsPushWithPull() {
        Iterator<Long> iterator = Stream.of(v)
                .flatMap(x -> {
                    return Stream.of(v_small).map(y -> {
                        System.out.print("inner: " + y + '\n');
                        return x * y;
                    });
                }).iterator();

        iterator.hasNext();

        System.out.print(iterator.next());

        Assert.assertEquals(
                "inner: 0\n" +
                        "inner: 1\n" +
                        "inner: 2\n" +
                        "inner: 3\n" +
                        "inner: 4\n" +
                        "0",
                outContent.toString());
    }

    @Test
    public void testPullAlgebraWithInfinite() {
        ExecIterateStreamAlg<Id.t, Pull.t> algebra = new ExecPullWithIterateFactory<>(new ExecPullFactory());

        App<Pull.t, Long> flatMap = algebra.flatMap(x -> algebra.map(y -> {
            System.out.print("inner: " + y + '\n');
            return x * y;
        }, algebra.iterate(0L, i -> i + 2)), algebra.source(v));

        Pull<Long> prj = Pull.prj(flatMap);

        prj.hasNext();

        System.out.print(prj.next() + "\n");

        Assert.assertEquals(
                "inner: 0\n" +
                        "0\n",
                outContent.toString());
    }

    @Test(expected = TimeoutException.class)
    public void testJava8StreamsTimeoutWithInfinite() throws ExecutionException, InterruptedException, TimeoutException {
        runWithExpectedException(() -> {
            Stream<Long> longStream = Stream
                    .iterate(0L, i -> i + 2);

            Iterator<Long> iterator = Stream.of(v)
                    .flatMap(x -> longStream.map(y -> x * y))
                    .iterator();

            iterator.hasNext();

            return iterator.next();
        });

        assert false;
    }

    @Test(expected = TimeoutException.class)
    public void testJava8TimeoutFlatMapWithAny() throws ExecutionException, InterruptedException, TimeoutException {
        runWithExpectedException(() -> Stream.iterate(0, i -> i + 1).flatMap(j -> Stream.iterate(0, i -> i + 1)).anyMatch(x -> true));

        assert false;
    }

    @Test(expected = TimeoutException.class)
    public void testJava8TimeoutFlatMapWithNone() throws ExecutionException, InterruptedException, TimeoutException {
        runWithExpectedException(() -> Stream.iterate(0, i -> i + 1).flatMap(j -> Stream.iterate(0, i -> i + 1)).noneMatch(x -> false));

        assert false;
    }
}
