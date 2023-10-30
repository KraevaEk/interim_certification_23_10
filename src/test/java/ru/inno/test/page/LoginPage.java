package ru.inno.test.page;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.inno.test.config.PropertyProvider;

import java.util.Properties;

import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

public class LoginPage extends Page {
    @FindBy(how = How.CSS, using = "#userName")
    private WebElement usernameInput;
    @FindBy(css = "#password")
    private WebElement passwordInput;
    @FindBy(css = "#login")
    private WebElement loginButton;
    @FindBy(css = ".rt-noData")
    private WebElement rowsTable;
    Properties properties = PropertyProvider.getInstance().getProps();
    private final String loginUrl = properties.getProperty("login.url");
    private final String ptofileUrl = properties.getProperty("profile.url");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void setLogin(String username) {
        usernameInput.sendKeys(username);
    }

    public void setPassword(String pass) {
        passwordInput.sendKeys(pass);
    }

    public void clickLogin() {
        loginButton.click();
    }

    @Step("Проверить, что таблица пустая")
    public String getRowsTable() {
        driver.manage().timeouts().implicitlyWait(of(4, SECONDS));
        String text = rowsTable.getText();
        getScreenshot();
        return text;
    }

    @Step("Ввести логин и пароль, переход в раздел профиль")
    public void authAs(String username, String pass) {
        this.setLogin(username);
        this.setPassword(pass);
        getScreenshot();
        this.clickLogin();
        WebDriverWait wait = new WebDriverWait(driver, of(40, SECONDS));
        wait.withMessage("Не удалось авторизоваться")
                .pollingEvery(of(1, MILLIS))
                .until(ExpectedConditions.urlToBe(ptofileUrl));
    }

    @Step("Открыть страницу")
    public void open() {
        driver.manage().window().maximize();
        driver.get(loginUrl);
    }

    @Attachment(value = "screen.png", type = "image/png")
    private byte[] getScreenshot(){
        return driver.findElement(By.cssSelector("body")).getScreenshotAs(OutputType.BYTES);
    }
}
