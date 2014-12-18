package streams.valgebra;

import streams.algebras.StreamAlg;
import streams.factories.PullFactory;
import streams.factories.PushFactory;
import streams.higher.App;
import streams.higher.Pull;
import streams.higher.Push;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Authors:
 * Aggelos Biboudis (@biboudis)
 * Nick Palladinos (@NickPalladinos)
 */
public abstract class Stream<T> {

    long temp = 0L;

    public <R> Stream<R> map(Function<T, R> mapper) {
        return new Map<>(mapper, this);
    }

    public Stream<T> filter(Predicate<T> predicate) {
        return new Filter<>(predicate, this);
    }

    public <R> Stream<R> flatMap(Function<T, Stream<R>> mapper) {
        return new FlatMap<>(mapper, this);
    }

    public long count() {
        temp = 0;

        Consumer<T> k = i -> this.temp++;

        Push.prj(this.fold(new PushFactory())).invoke(k);

        return temp;
    }

    public Iterator<T> iterator() {
        return Pull.prj(this.fold(new PullFactory()));
    }

    abstract <C> App<C, T> fold(StreamAlg<C> algebra);
}
