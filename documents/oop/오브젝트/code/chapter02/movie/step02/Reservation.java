package chapter02.movie.step02;


import chapter02.money.Money;

public class Reservation {
    private Customer customer;
    private chapter02.movie.step02.Screening Screening;
    private Money fee;
    private int audienceCount;

    public Reservation(Customer customer, Screening Screening, Money fee, int audienceCount) {
        this.customer = customer;
        this.Screening = Screening;
        this.fee = fee;
        this.audienceCount = audienceCount;
    }
}
