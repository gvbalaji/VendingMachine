package com.vmexample.vendingmachine.internal.cash;

import java.math.BigDecimal;
import java.util.Currency;

public class Money {

	private final BigDecimal moneyValue;
	private final Currency currency;
	public Money(BigDecimal moneyValue, Currency currency) {
		super();
		this.moneyValue = moneyValue;
		this.currency = currency;
	}
	public BigDecimal getMoneyValue() {
		return moneyValue;
	}
	public Currency getCurrency() {
		return currency;
	}
    public Money add(Money money)
    {
    	return new Money(this.getMoneyValue().add(money.getMoneyValue()) , this.getCurrency());
    }
    public Money subtract(Money money)
    {
    	return new Money(this.getMoneyValue().subtract(money.getMoneyValue()) , this.getCurrency());
    }
	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		result = prime * result
				+ ((moneyValue == null) ? 0 : moneyValue.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (moneyValue == null) {
			if (other.moneyValue != null)
				return false;
		} else if (!moneyValue.equals(other.moneyValue))
			return false;
		return true;
	}
	
    

}
