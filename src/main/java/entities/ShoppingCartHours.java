package entities;

public enum ShoppingCartHours {
    HOURS_12(12),
    HOURS_24(24),
    HOURS_36(36);

    private final int hours;

    ShoppingCartHours(int hours) {
        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }

    public boolean isOpenForHours(int hours) {
        return hours % this.hours == 0;
    }
}