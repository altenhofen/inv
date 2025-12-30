package io.github.altenhofen.inv.core.domain.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

// TODO: reconsider a lot of the choices here, naming too
public final class Invoice {
    private final String number;
    private final String series;
    private final Issuer issuer;
    private final Customer customer;
    private final List<Item> items;
    private final LocalDate issueDate;
    private final BigDecimal valuePaid;

    public Invoice(String number, String series, Issuer issuer, Customer customer, List<Item> items,
                   LocalDate issueDate, BigDecimal valuePaid) {
        this.number = number;
        this.series = series;
        this.issuer = issuer;
        this.customer = customer;
        this.items = items;
        this.issueDate = issueDate;
        this.valuePaid = valuePaid;
    }

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO;
        for (final Item item : items) {
            total = total.add(item.totalPrice());
        }

        return total;
    }

    public BigDecimal discount() {
        // for now returning flat
        return new BigDecimal("8.00");
    }

    public BigDecimal totalWithDiscount() {
        return this.total().subtract(this.discount());
    }

    public BigDecimal exchange() {
        if (valuePaid.compareTo(this.totalWithDiscount()) < 0) {
            // TODO: type, create exception etc
            throw new IllegalStateException();
        }
        return valuePaid.subtract(this.totalWithDiscount());
    }

    public String getNumber() {
        return number;
    }

    public String getSeries() {
        return series;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public BigDecimal getValuePaid() {
        return valuePaid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Invoice) obj;
        return Objects.equals(this.number, that.number) &&
                Objects.equals(this.series, that.series) &&
                Objects.equals(this.issuer, that.issuer) &&
                Objects.equals(this.customer, that.customer) &&
                Objects.equals(this.items, that.items) &&
                Objects.equals(this.issueDate, that.issueDate) &&
                Objects.equals(this.valuePaid, that.valuePaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, series, issuer, customer, items, issueDate, valuePaid);
    }

    @Override
    public String toString() {
        return "Invoice[" +
                "number=" + number + ", " +
                "series=" + series + ", " +
                "issuer=" + issuer + ", " +
                "customer=" + customer + ", " +
                "items=" + items + ", " +
                "issueDate=" + issueDate + ", " +
                "valuePaid=" + valuePaid + ']';
    }

}

