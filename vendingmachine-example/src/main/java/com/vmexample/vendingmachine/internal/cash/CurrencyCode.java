package com.vmexample.vendingmachine.internal.cash;

public enum CurrencyCode {
	PENNY(1.0), NICKEL(5.0), DIME(10.0), QUARTER(25.0), DOLLAR(100.0);
	CurrencyCode(double value) { this.value = value; }
    private final double value;
    public double value() { return value; }

}
