package org.poo.cb;

import java.util.ArrayList;
import java.util.List;

public class Stocks {
    private String name;
    private Float amount;
    private List<String[]> allStocks = Database.Instance().getStockValues();
    private final List<String[]> stockValues = new ArrayList<String[]>();

    public Stocks(String name, Float amount) {
        this.name = name;
        this.amount = amount;

        this.stockValues.add(this.allStocks.get(0));
        for (int i = 1; i < this.allStocks.size(); i++)
            if (this.allStocks.get(i)[0].compareTo(this.name) == 0) {
                this.stockValues.add(this.allStocks.get(i));
                break;
            }
    }

    public String getName() {
        return name;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("\"stockName\":\"%s\",\"amount\":%.0f", this.name, this.amount);
    }
}
