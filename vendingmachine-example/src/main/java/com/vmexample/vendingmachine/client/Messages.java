package com.vmexample.vendingmachine.client;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "com.vmexample.vendingmachine.client.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static final String NO_PRODUCT_FOUND = "InMemoryProductRepository.NoProductFound";

	public static final String UNABLE_TO_ADD_PRODUCT = "InMemoryProductRepository.UnableToAddProduct";

	public static final String NOT_ENOUGH_MONEY_LEFT = "InMemoryCashRepository.NotEnoughMoneyLeft";

	public static final String UNABLE_TO_DEPOSIT_CASH = "InMemoryCashRepository.UnableToDepositCash";

	public static final String MESSAGE_DELIMITTER = "VendingMachine.MessageDelimitter";

	public static final String VENDING_MACHINE_CHANGE_TITLE = "VendingMachine.ChangeTitle";

	public static final String VENDING_MACHINE_PRODUCT_TITLE = "VendingMachine.ProductTitle";

	public static final String VENDING_MACHINE_PRODUCT_BOUGHT = "VendingMachine.ProductBought";

	public static final String PRODUCT_ADDED = "VendingMachine.ProductAdded";

	public static final String MONEY_ADDED = "VendingMachine.MoneyAdded";

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
