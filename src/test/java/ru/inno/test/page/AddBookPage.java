package ru.inno.test.page;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.inno.test.config.PropertyProvider;

import java.util.List;
import java.util.Properties;

import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

public class AddBookPage extends Page {
    @FindBy(css = ".text-right.fullButton")
    private WebElement addButton;
    @FindBy(css = ".text-left.fullButton")
    private WebElement backButton;
    @FindBy(css = ".text-right.button.di>*:first-child")
    private WebElement deleteAllButton;
    @FindBy(css = "#closeSmallModal-ok")
    private WebElement deleteOkButton;
    @FindBy(css = ".-next")
    private WebElement nextButton;
    @FindBy(css = ".modal-body")
    private WebElement modal;
    Properties properties = PropertyProvider.getInstance().getProps();
    private final String loginUrl = properties.getProperty("login.url");
    private final String ptofileUrl = properties.getProperty("profile.url");
    private final String bookUrl = properties.getProperty("book.url");


    public AddBookPage(WebDriver driver) {
        super(driver);
    }

    public void clickAddButton() {
        addButton.click();
    }
    public void clickDeleteOkButton() {
        deleteOkButton.click();
    }
    public void clickBackButton() {
        backButton.click();
    }
    public void clickNextButton() {
        nextButton.click();
    }

    @Step("Очистить коллекцию книг")
    public void clickDeleteAllButton() throws InterruptedException {
        deleteAllButton.click();
        WebDriverWait wait =  new WebDriverWait(driver, of(5, SECONDS));
        wait.withMessage("Не удалось очистить коллекцию")
                .pollingEvery(of(1, MILLIS))
                .until(ExpectedConditions.visibilityOf(modal));
        this.clickDeleteOkButton();
        wait.withMessage("Не удалось очистить коллекцию")
                .pollingEvery(of(1, MILLIS))
                .until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    @Step("Открыть каталог книг")
    @Override
    public void open() {
        driver.manage().window().maximize();
        driver.get(bookUrl);
        getScreenshot();
    }

    @Step("Добавить шесть книг в коллекцию")
    public void addSixBooks() {
        List<WebElement> link;
        for (int i = 0; i < 6; i++) {
            link = driver.findElements(By.cssSelector("a[href*=book]"));
            link.get(i).click();
            this.clickAddButton();
            this.clickBackButton();
        }
    }

    @Step("Проверить, что в коллекции отображается шесть книг")
    public int sixBooksProfile() {
        getScreenshot();
        List<WebElement> books = driver.findElements(By.cssSelector("a[href*=book]"));
        this.clickNextButton();
        getScreenshot();
        books.add(driver.findElement(By.cssSelector("a[href*=book]")));
        return books.size();
    }

    @Step("Добавить две книги в коллекцию")
    public void addTwoBooks() {
        List<WebElement> link;
        for (int i = 0; i < 2; i++) {
            link = driver.findElements(By.cssSelector("a[href*=book]"));
            link.get(i).click();
            this.clickAddButton();
            this.clickBackButton();
        }
    }

    @Step("Проверить, что в коллекции отображается две книги")
    public int twoBooksProfile() {
        getScreenshot();
        List<WebElement> books = driver.findElements(By.cssSelector("a[href*=book]"));
        return books.size();
    }

    @Step("Открыть раздел профиль")
    public void openProfile() {
        driver.manage().window().maximize();
        driver.get(ptofileUrl);
        getScreenshot();
    }

    @Attachment(value = "screen.png", type = "image/png")
    private byte[] getScreenshot(){
        return driver.findElement(By.cssSelector("body")).getScreenshotAs(OutputType.BYTES);
    }
}