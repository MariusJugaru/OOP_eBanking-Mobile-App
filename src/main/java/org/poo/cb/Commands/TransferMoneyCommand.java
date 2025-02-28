package org.poo.cb.Commands;

import org.poo.cb.Accounts.Account;
import org.poo.cb.Database;
import org.poo.cb.User;

public class TransferMoneyCommand implements Command{
    private Database database;
    private String user;
    private String friend;
    private String currency;
    private float amount;
    private CommandHistory history;

    public TransferMoneyCommand(Database database, String user, String friend, String currency, float amount, CommandHistory history) {
        this.database = database;
        this.user = user;
        this.friend = friend;
        this.currency = currency;
        this.amount = amount;
        this.history = history;
    }

    @Override
    public void execute() {
        User thisUser = this.database.getUsers().get(this.user);
        User thatUser = this.database.getUsers().get(this.friend);

        Account srcAcc = (Account) thisUser.searchPortolio(this.currency);
        Account dstAcc = (Account) thatUser.searchPortolio(this.currency);

        // check for funds
        if (this.amount > srcAcc.getSum()) {
            System.out.println("Insufficient amount in account " + this.currency + " for transfer");
            return;
        }

        // check if friends
        if (!thisUser.getFriends().contains(thatUser)) {
            System.out.println("You are not allowed to transfer money to " + this.friend);
            return;
        }

        // send money
        srcAcc.setSum(srcAcc.getSum() - this.amount);
        dstAcc.setSum(dstAcc.getSum() + this.amount);

        history.push(this);
    }

    @Override
    public void undo() {
        User thisUser = this.database.getUsers().get(this.user);
        User thatUser = this.database.getUsers().get(this.friend);

        Account srcAcc = (Account) thisUser.searchPortolio(this.currency);
        Account dstAcc = (Account) thatUser.searchPortolio(this.currency);

        srcAcc.setSum(srcAcc.getSum() + this.amount);
        dstAcc.setSum(dstAcc.getSum() - this.amount);
    }
}
