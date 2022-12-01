package chapter02.movie.step02;

import chapter02.money.Money;

public interface DiscountPolicy {
    Money calculateDiscountAmount(Screening screening);
}
