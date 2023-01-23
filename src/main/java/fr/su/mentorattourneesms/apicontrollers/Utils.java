package fr.su.mentorattourneesms.apicontrollers;

public class Utils {

    private Utils() {
    }

    public static Boolean emptyInput(String input) {
        return input == null || input.isEmpty() || input.trim().isEmpty();
    }

    public static String validInputString(String input) {
        return emptyInput(input) ? " " : input;
    }

    public static String validInputNumeric(String input) {
        return emptyInput(input) || !input.matches("-?(\\d+)((\\,|\\.)\\d+)?") ? "0" : input;
    }

    public static String validInputBoolean(String input) {
        return (!emptyInput(input) && ("true".equals(input) || "1".equals(input))) ? "1" : "0";
    }

    public static String validInputOptionalBoolean(String input) {
        String sInput = input.trim().toLowerCase();

        if (!emptyInput(input)) {
            switch (sInput) {
                case "true":
                case "1":
                    return "1";
                case "false":
                case "0":
                    return "0";
                default:
            }
        }

        return " ";
    }
}
