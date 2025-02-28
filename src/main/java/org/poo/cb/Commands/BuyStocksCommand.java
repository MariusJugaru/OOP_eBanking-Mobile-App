package org.poo.cb.Commands;

import org.poo.cb.Accounts.Account;
import org.poo.cb.Database;
import org.poo.cb.Stocks;
import org.poo.cb.User;

import java.util.ArrayList;
import java.util.List;

public class BuyStocksCommand implements Command{
    private Database database;
    private String user;
    private String company;
    private float amount;
    private float value;
    private List<String[]> stockValues;
    private CommandHistory history;

    public BuyStocksCommand(Database database, String user, String company, float amount, CommandHistory history) {
        this.database = database;
        this.user = user;
        this.company = company;
        this.amount = amount;
        this.history = history;
        this.stockValues = this.database.getStockValues();
    }

    @Override
    public void execute() {
        User user = this.database.getUsers().get(this.user);
        Account account = (Account) user.searchPortolio("USD");
        Stocks stock = (Stocks) user.searchPortolio(this.company);

        float stockValue = 0;
        for (int i = 1; i < this.stockValues.size(); i++){
            String[] strings = this.stockValues.get(i);

            if (strings[0].compareTo(company) == 0) {
                stockValue = Float.parseFloat(strings[strings.length - 1]);
                break;
            }
        }

        // check if enough funds
        if (this.amount * stockValue > account.getSum()) {
            System.out.println("Insufficient amount in account for buying stock");
            return;
        }

        if (user.isPremium() && database.recommendStocks().contains(company)) {
            account.setSum(account.getSum() - ((this.amount * stockValue) - 5 * (this.amount * stockValue) / 100));
            this.value = (this.amount * stockValue) - 5 * (this.amount * stockValue) / 100;
        } else {
            account.setSum(account.getSum() - this.amount * stockValue);
            this.value = this.amount * stockValue;
        }

        // Already has stocks in company
        if (stock != null) {
            stock.setAmount(stock.getAmount() + this.amount);
            return;
        }

        ArrayList<Object> portfolio = user.getPortfolio();
        stock = new Stocks(company, this.amount);
        portfolio.add(stock);

        history.push(this);
    }

    @Override
    public void undo() {
        User user = this.database.getUsers().get(this.user);
        Account account = (Account) user.searchPortolio("USD");
        Stocks stock = (Stocks) user.searchPortolio(this.company);

        stock.setAmount(stock.getAmount() - this.amount);
        account.setSum(account.getSum() + this.value);

        if (stock.getAmount() == 0)
            user.getPortfolio().remove(stock);
    }
}
