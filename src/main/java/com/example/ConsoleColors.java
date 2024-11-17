package com.example;

public class ConsoleColors {
    //colori 
    public static final String RED_TEXT = "\033[31m";
    public static final String GREEN_TEXT = "\033[32m";
    public static final String YELLOW_TEXT = "\033[33m";
    public static final String MAGENTA_TEXT = "\033[35m";
    public static final String BLUE_TEXT = "\033[36m";
    public static final String BRIGHT_CYAN = "\033[96m";


    //stili di testo
    public static final String BOLD_TEXT = "\033[1m";
    public static final String ITALIC = "\033[3m";
    public static final String UNDERLINE = "\033[4m";
    public static final String STRIKETHROUGH = "\033[9m";

    // Colori per lo sfondo del testo
    public static final String BG_RED = "\033[41m";
    public static final String BG_GREEN = "\033[42m";
    public static final String BG_YELLOW = "\033[43m";
    public static final String BG_BLUE = "\033[44m";
    public static final String BG_MAGENTA = "\033[45m";
    public static final String BG_CYAN = "\033[46m";
    public static final String BG_WHITE = "\033[47m";

    //per riportare il testo ai valori di default
    public static final String RESET_TEXT = "\033[0m";
}
