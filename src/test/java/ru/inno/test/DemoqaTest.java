package ru.inno.test;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import ru.inno.test.config.PropertyProvider;
import ru.inno.test.page.AddBookPage;
import ru.inno.test.page.LoginPage;
import java.util.Properties;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Промежуточная аттестация")
@ExtendWith(SeleniumJupiter.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoqaTest {

    private WebDriver driver;
    Properties properties = PropertyProvider.getInstance().getProps();
    private final String username = properties.getProperty("username");
    private final String password = properties.getProperty("password");
    @BeforeEach
    public void setUp() {
        step("Открыть драйвер", () -> {
            driver = new ChromeDriver();
        });
    }

    @AfterEach
    @Step("Закрыть драйвер")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Первый сценарий")
    @Description("Проверить, что коллекция пустая")
    @Order(1)
    public void FirstScenario(){
        LoginPage page = PageFactory.initElements(driver, LoginPage.class);
        page.open();
        page.authAs(username, password);
        String text = page.getRowsTable();
        assertEquals("No rows found", text);
    }

    @Test
    @DisplayName("Второй сценарий")
    @Description("Добавить в коллекцию шесть книг")
    @Order(2)
    public void SecondScenario() throws InterruptedException {
        LoginPage page = PageFactory.initElements(driver, LoginPage.class);
        AddBookPage bookPage = PageFactory.initElements(driver, AddBookPage.class);
        page.open();
        page.authAs(username, password);
        bookPage.open();
        bookPage.addSixBooks();
        bookPage.openProfile();
        int countBook = bookPage.sixBooksProfile();
        bookPage.clickDeleteAllButton();
        assertEquals(6, countBook);
    }

    @DisplayName("Третий сценарий")
    @Description("Добавить в коллекцию две книги; очистить коллекцию; проверить, что коллекция пустая")
    @Test
    @Order(3)
    public void ThirdScenario() throws InterruptedException {
        LoginPage page = PageFactory.initElements(driver, LoginPage.class);
        AddBookPage bookPage = PageFactory.initElements(driver, AddBookPage.class);
        page.open();
        page.authAs(username, password);
        bookPage.open();
        bookPage.addTwoBooks();
        bookPage.openProfile();
        int countBook = bookPage.twoBooksProfile();
        assertEquals(2, countBook);
        bookPage.clickDeleteAllButton();
        String text = page.getRowsTable();
        assertEquals("No rows found", text);
    }

}
