package org.example.records;

import java.util.Date;

public record TotalPriceRental(Date startDate, Date endDate, double totalPrice, int days) {
}
