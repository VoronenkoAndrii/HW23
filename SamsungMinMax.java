import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SamsungMinMax {
    WebDriver driver;
    WebDriverWait wait;
    final int waitTime = 500;
    Robot robot = new Robot();

    public SamsungMinMax() throws AWTException {
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
    public void searchPrice() {
        driver.get("https://rozetka.com.ua/");
        driver.findElement(By.xpath("//input[@name='search']")).sendKeys("samsung" + Keys.ENTER);
        driver.manage().window().maximize();
        robot.delay(waitTime);
        WebElement elem = driver.findElement(By.xpath("//span[contains(text(),'Мобильные телефоны')]"));
        elem.click();
        driver.findElement(By.cssSelector("input[formcontrolname=min]")).clear();
        driver.findElement(By.cssSelector("input[formcontrolname=min]")).sendKeys("5000");
        driver.findElement(By.cssSelector("input[formcontrolname=max]")).clear();
        driver.findElement(By.cssSelector("input[formcontrolname=max]")).sendKeys("15000");
        driver.findElement(By.xpath("(//button[@type='submit'])[1]")).click();
        robot.delay(waitTime);
        List<WebElement> elementList= driver.findElements(By.cssSelector("span.good-tile__price-value"));
        for (WebElement element: elementList){
            int goodPrice = Integer.parseInt(element.getText().replaceAll("&nbsp", "").replaceAll(" ", ""));
            assertTrue(goodPrice> 5000 && goodPrice<15000);
        }
    }
}