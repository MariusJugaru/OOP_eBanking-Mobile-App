package org.poo.cb.Accounts;

public class AccountFactory {
    private static AccountFactory factoryInstance;
    AccountFactory() { }
    public static AccountFactory Instance() {
        if (factoryInstance == null)
            factoryInstance = new AccountFactory();
        return factoryInstance;
    }

    public Account createAccount(String currencyType) {
        switch (currencyType) {
            case "CAD":
                return new Account_CAD();
            case "EUR":
                return new Account_EUR();
            case "GBP":
                return new Account_GBP();
            case "JPY":
                return new Account_JPY();
            case "USD":
                return new Account_USD();
            default:
                return null;
        }
    }
}
