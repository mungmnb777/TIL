package chapter02.movie.step01.pricing;


import chapter02.money.Money;
import chapter02.movie.step01.DiscountPolicy;
import chapter02.movie.step01.Screening;

public class NoneDiscountPolicy extends DiscountPolicy {
    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
