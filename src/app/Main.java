package app;

import ui.LoginForm;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        new LoginForm();
    }
}