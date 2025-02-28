package org.poo.cb;

import org.poo.cb.Accounts.Account;

import java.util.ArrayList;

public class User {
    private String email;
    private String firstname;
    private String lastname;
    private String address;
    private boolean isPremium = false;
    private final ArrayList<Object> portfolio = new ArrayList<>();
    private final ArrayList<User> friends = new ArrayList<>();

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Object> getPortfolio() {
        return portfolio;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public Object searchPortolio(String name) {
        for (Object obj : this.portfolio) {
            if (obj instanceof Account) {
                Account account = (Account) obj;
                if (account.getType().compareTo(name) == 0)
                    return account;
            }
            if (obj instanceof Stocks) {
                Stocks stock = (Stocks) obj;
                if (stock.getName().compareTo(name) == 0)
                    return stock;
            }
        }

        return null;
    }


}
