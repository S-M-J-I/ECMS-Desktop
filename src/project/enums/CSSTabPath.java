package project.enums;

public enum CSSTabPath {
    ACTIVE,
    INACTIVE;

    public static String getCSSPath(CSSTabPath state) {
        switch (state) {
            case ACTIVE -> {
                return "-fx-background-color: orange; -fx-cursor: hand; -fx-font-weight: bolder; -fx-focus-color: transparent; -fx-border-width: 0.1px; -fx-border-radius: 0px; -fx-text-fill: white;";
            }
            case INACTIVE -> {
                return "-fx-background-color: white; -fx-cursor: hand; -fx-font-weight: normal; -fx-border-color: black; -fx-border-width: 0.1px;";
            }
            default -> {
                return null;
            }
        }
    }
}
