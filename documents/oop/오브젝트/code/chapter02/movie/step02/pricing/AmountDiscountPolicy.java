package chapter02.movie.step02.pricing;


import chapter02.money.Money;
import chapter02.movie.step02.DiscountCondition;
import chapter02.movie.step02.DefaultDiscountPolicy;
import chapter02.movie.step02.Screening;

public class AmountDiscountPolicy extends DefaultDiscountPolicy {
    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DiscountCondition... conditions) {
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}
