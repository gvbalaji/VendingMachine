package com.vmexample.vendingmachine.internal.cash.service;

import java.util.List;
import java.util.Map;

import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.Money;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;

public interface CashService {
	
	public void depositCash (MoneyEntity money) throws UnableToAddCashException;
	public void depositCash (List<MoneyEntity> money) throws UnableToAddCashException;
	public Boolean checkIfChangeAvailable(Money actualPrice, Money inputMoney);
	public Map<CurrencyCode,List<MoneyEntity>> depositAndDispatchChange (Money actualPrice,
			                   Map<CurrencyCode,List<MoneyEntity>> cashMap) throws NotEnoughMoneyLeftException, UnableToAddCashException;
	

}
