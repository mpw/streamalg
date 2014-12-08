package streams;

/**
 * Created by bibou on 12/8/14.
 */
public interface LogPushAlgebra extends StreamAlg<Push.t> {
    <T> App<Push.t, T> log(App<Push.t, T> app);
}
