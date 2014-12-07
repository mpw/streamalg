package gadt;

/**
 * Created by bibou on 12/5/14.
 */
public class EvalVisitor implements Visitor<Expr.t> {

    @Override
    public App<Expr.t, NumberHigh> caseIntLit(IntLit expr) {
        return expr.value;
    }

    @Override
    public App<Expr.t, BooleanHigh> caseBoolLit(BoolLit expr) {
        return expr.value;
    }

    @Override
    public <A> App<Expr.t, A> casePlus(Plus expr) {

        NumberHigh left = NumberHigh.prj(expr.left.accept( (Visitor) this));
        NumberHigh right = NumberHigh.prj(expr.right.accept( (Visitor) this));

        return new NumberHigh(left.value + right.value);
    }

    @Override
    public <Y, R> App<Expr.t, R> caseIf(If<Y> expr) {
        return null;
    }
}