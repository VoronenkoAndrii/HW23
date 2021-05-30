import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SearchByModels {
    WebDriver driver;
    WebDriverWait wait;
    final int waitTime = 500;
    Robot robot = new Robot();

    public SearchByModels() throws AWTException {
    }

    @BeforeClass
    public void actionBeforeTest() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @AfterClass
    public void closeChrome() {
        driver.quit();
    }


    @Test
    public void search() {
        driver.get("https://rozetka.com.ua/");
        driver.findElement(By.xpath("//input[@name='search']")).sendKeys("samsung" + Keys.ENTER);
        driver.manage().window().maximize();
        robot.delay(waitTime);
        WebElement elem = driver.findElement(By.xpath("//span[contains(text(),'Мобильные телефоны')]"));
        elem.click();
        robot.delay(waitTime);
        driver.findElement(By.cssSelector("label[for='Apple']")).click();
        robot.delay(waitTime);
        driver.findElement(By.cssSelector("label[for='Huawei']")).click();
        robot.delay(waitTime);
        List<String> names = getNames(driver);
        for (String name: names){
            assertTrue(name.contains("Apple") || name.contains("Huawei") ||name.contains("Samsung"));
        }
    }
    public List<String> getNames(WebDriver driver) {
        List<String> names = new ArrayList<>();
        List<WebElement> elementList = driver.findElements(By.cssSelector("span.goods-tile__title"));
        for (WebElement e : elementList) {
            names.add(e.getText());
        }
        return names;
    }
}