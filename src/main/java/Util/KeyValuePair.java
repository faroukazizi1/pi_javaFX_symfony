package Util;

public class KeyValuePair<T> {
    private String displayValue;
    private T actualValue;

    public KeyValuePair(String displayValue, T actualValue) {
        this.displayValue = displayValue;
        this.actualValue = actualValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public T getActualValue() {
        return actualValue;
    }

    @Override
    public String toString() {
        return displayValue;
    }
}
