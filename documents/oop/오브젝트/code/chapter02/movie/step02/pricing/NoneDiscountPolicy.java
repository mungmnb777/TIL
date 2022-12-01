package chapter02.movie.step02.pricing;


import chapter02.money.Money;
import chapter02.movie.step02.DefaultDiscountPolicy;
import chapter02.movie.step02.DiscountPolicy;
import chapter02.movie.step02.Screening;

public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public Money calculateDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
