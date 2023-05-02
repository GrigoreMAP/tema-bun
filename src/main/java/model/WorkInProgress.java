package model;

public enum WorkInProgress {

    PAPERBACK, HARD_COVER;

    public static WorkInProgress safeValueOf(String value) {
        try {
            return WorkInProgress.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
