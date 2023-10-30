package ru.inno.test.page;

import org.openqa.selenium.WebDriver;

public abstract class Page {
    protected final WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    public abstract void  open();
}
