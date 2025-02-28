package org.poo.cb.Accounts;

public abstract class Account {
    private String type;
    private float sum;

    public Account() {
        this.sum = 0;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return String.format("\"currencyName\":\"%s\",\"amount\":\"%.2f\"", this.type, this.sum);
    }
}
