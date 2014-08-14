package com.vmexample.vendingmachine.internal.cash.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmexample.vendingmachine.internal.cash.CurrencyCode;
import com.vmexample.vendingmachine.internal.cash.Money;
import com.vmexample.vendingmachine.internal.cash.MoneyEntity;
import com.vmexample.vendingmachine.internal.cash.NotEnoughMoneyLeftException;
import com.vmexample.vendingmachine.internal.cash.UnableToAddCashException;
import com.vmexample.vendingmachine.internal.cash.repository.CashRepository;

@Service
public class DefaultCashService implements CashService {

	
	private final CashRepository cashRepository;
	private static final Object lock = new Object();
	
	@Autowired
	public DefaultCashService(CashRepository cashRepository) {
		super();
		this.cashRepository = cashRepository;
	}



	@Override
	public void depositCash(MoneyEntity money) throws UnableToAddCashException{
		cashRepository.depositCash(money);

	}

	@Override
	public void depositCash(List<MoneyEntity> money) throws UnableToAddCashException {
		cashRepository.depositCash(money);

	}

	@Override
	public Boolean checkIfChangeAvailable(Money actualPrice, Money inputMoney) {
		
		Money difference = inputMoney.subtract(actualPrice);
		Map<CurrencyCode, Integer> cashChangeMap = generateCashChangeMap(difference);
		Boolean result = true;
		for(CurrencyCode currencyCode : cashChangeMap.keySet())
		{
			if( cashRepository.getNumberOfCurrrencyEntities(currencyCode) < cashChangeMap.get(currencyCode))
			{
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	private Map<CurrencyCode, Integer> generateCashChangeMap(Money money)
	{
		double change = money.getMoneyValue().doubleValue();
		Map<CurrencyCode, Integer> cashChangeMap = new EnumMap<>(CurrencyCode.class);
		change = computeMoneyEntitiesForChange(CurrencyCode.DOLLAR,change,cashChangeMap);
		change = computeMoneyEntitiesForChange(CurrencyCode.QUARTER,change,cashChangeMap);
		change = computeMoneyEntitiesForChange(CurrencyCode.DIME,change,cashChangeMap);
		change = computeMoneyEntitiesForChange(CurrencyCode.NICKEL,change,cashChangeMap);
		change = computeMoneyEntitiesForChange(CurrencyCode.PENNY,change,cashChangeMap);
		return cashChangeMap;
	}
	
	private double computeMoneyEntitiesForChange(CurrencyCode currencyCode, double change, Map<CurrencyCode, Integer> cashChangeMap)
	{
        if ( change / currencyCode.value() >= 1 ) { 
            int numMoneyEntities = (int)change / (int)currencyCode.value();
            change = change - (numMoneyEntities * currencyCode.value());
            cashChangeMap.put(currencyCode, numMoneyEntities);
        }
		return change;
	}
	
	@Override
	public Map<CurrencyCode,List<MoneyEntity>> depositAndDispatchChange (
			Money actualPrice, Map<CurrencyCode, List<MoneyEntity>> cashMap) throws NotEnoughMoneyLeftException,  UnableToAddCashException  {
		
		Money userInputMoney = computeMoneyInput(cashMap);
		Money difference = userInputMoney.subtract(actualPrice);
		Map<CurrencyCode,List<MoneyEntity>> cashDebited = null;
		
		List<MoneyEntity> moneyEntityList = createMoneyEntityListFromCashMap(cashMap);

		synchronized(lock)
		{
			if(checkIfChangeAvailable(actualPrice,userInputMoney))
			{
				cashRepository.depositCash(moneyEntityList);
				cashDebited = cashRepository.debitCash(generateCashChangeMap(difference));
			}
		}
		return cashDebited;
	}
	
	private List<MoneyEntity> createMoneyEntityListFromCashMap(Map<CurrencyCode, List<MoneyEntity>> cashMap)
	{
		List<MoneyEntity> moneyEntityList = new ArrayList<>();
		for(CurrencyCode currencyCode : cashMap.keySet())
		{
			moneyEntityList.addAll(cashMap.get(currencyCode));
		}
		return moneyEntityList;
	}
	
	private Money computeMoneyInput(Map<CurrencyCode, List<MoneyEntity>> cashMap)
	{
		double sum = 0;
		for(CurrencyCode currencyCode : cashMap.keySet())
		{
			sum = sum + currencyCode.value()*cashMap.get(currencyCode).size();
		}
		return new Money( new BigDecimal(sum), Currency.getInstance(Locale.getDefault()));
	}

}
