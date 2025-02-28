package org.poo.cb.Commands;

import org.poo.cb.Accounts.Account;
import org.poo.cb.Database;
import org.poo.cb.User;

import java.util.List;

public class ExchangeMoneyCommand implements Command{
    private Database database;
    private String user;
    private String src;
    private String dst;
    private float amount;
    private float srcVal;
    private float dstVal;
    private List<String[]> exchangeRates;
    private CommandHistory history;

    public ExchangeMoneyCommand(Database database, String user, String src, String dst, float amount, List<String[]> exchangeRates, CommandHistory history) {
        this.database = database;
        this.user = user;
        this.src = src;
        this.dst = dst;
        this.amount = amount;
        this.exchangeRates = exchangeRates;
        this.history = history;
    }

    @Override
    public void execute() {
        User user = this.database.getUsers().get(this.user);
        Account srcAcc = (Account) user.searchPortolio(src);
        Account dstAcc = (Account) user.searchPortolio(dst);

        // Get exchange rate value
        float exchangeRate = 0;
        int idx;
        String[] strings = this.exchangeRates.get(0);
        for (idx = 0; idx < strings.length; idx++)
            if (strings[idx].compareTo(src) == 0)
                break;

        for (int i = 1; i < this.exchangeRates.size(); i++)
            if (this.exchangeRates.get(i)[0].compareTo(dst) == 0) {
                exchangeRate = Float.parseFloat(this.exchangeRates.get(i)[idx]);
                break;
            }
        float excVal = amount * exchangeRate;

        // check if enough funds
        if (excVal > srcAcc.getSum() || (excVal > srcAcc.getSum() / 2) && (excVal + excVal / 100 > srcAcc.getSum())) {
            System.out.println("Insufficient amount in account " + src + " for exchange");
            return;
        }

        // check for bank commission
        if ((excVal > srcAcc.getSum() / 2) && !user.isPremium()) {
            srcAcc.setSum(srcAcc.getSum() - (excVal + excVal / 100));
            this.srcVal = excVal + excVal / 100;
        } else {
            srcAcc.setSum(srcAcc.getSum() - excVal);
            this.srcVal = excVal;
        }

        dstAcc.setSum(dstAcc.getSum() + amount);
        this.dstVal = amount;

        history.push(this);
    }

    @Override
    public void undo() {
        User user = this.database.getUsers().get(this.user);
        Account srcAcc = (Account) user.searchPortolio(src);
        Account dstAcc = (Account) user.searchPortolio(dst);

        srcAcc.setSum(srcAcc.getSum() + this.srcVal);
        dstAcc.setSum(dstAcc.getSum() - this.dstVal);
    }
}
