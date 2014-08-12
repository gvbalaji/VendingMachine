package com.vmexample.vendingmachine.internal.product;

public enum ProductCode {
	COKE("Coke"), PEPSI("Pepsi"), DRPEPPER("DrPepper");
	ProductCode(String value) { this.value = value; }
    private final String value;
    public String value() { return value; }

}
