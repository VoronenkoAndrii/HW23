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

public class ComparisonOfMonitors {
    WebDriver driver;
    WebDriverWait wait;
    final int waitTime = 500;
    Robot robot = new Robot();

    public ComparisonOfMonitors() throws AWTException {
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
    public void comparison() {
        driver.get("https://rozetka.com.ua/");
        robot.delay(waitTime);
        driver.findElement(By.linkText("Ноутбуки и компьютеры")).click();
        robot.delay(waitTime);
        driver.findElement(By.linkText("Мониторы")).click();
        robot.delay(waitTime);

        Integer firstMonitorPrice = findElemetPrice(driver, 4000);
        System.out.println("firstMonitorPrice " + firstMonitorPrice);
        robot.delay(waitTime);
        String firstTitle = driver.findElement(By.xpath("//h1[@class='product__title']")).getText();
        driver.findElement(By.xpath("//button[@class='compare-button ng-star-inserted']")).click();
        robot.delay(waitTime);
        driver.navigate().back();
        Integer secondMonitorPrice = findElemetPrice(driver, firstMonitorPrice);
        System.out.println("secondMonitorPrice " + secondMonitorPrice);
        robot.delay(waitTime);
        String secondTitle = driver.findElement(By.xpath("//h1[@class='product__title']")).getText();
        driver.findElement(By.xpath("//button[@class='compare-button ng-star-inserted']")).click();
        robot.delay(waitTime);
        driver.findElement(By.xpath("//button[@aria-label='Списки сравнения']")).click();
        robot.delay(waitTime);
        driver.findElement(By.xpath("//a[@class='comparison-modal__link']")).click();
        robot.delay(waitTime);

        List<WebElement> selectedPrices = driver.findElements(By.xpath("//div[@class='product__price product__price--red ng-star-inserted']"));
        System.out.println("selectedPrices size = " + selectedPrices.size());
        assertTrue(selectedPrices.size() == 2);
        for (WebElement e : selectedPrices) {
            int currentPrice = parsePrice(e.getText());
            System.out.println("currentPrice = " + currentPrice);
            assertTrue(currentPrice == firstMonitorPrice || currentPrice == secondMonitorPrice);
        }

        List<WebElement> selectedTitles = driver.findElements(By.xpath("//a[@class='product__heading']"));
        System.out.println("selectedTitles size = " + selectedTitles.size());
        for (WebElement e : selectedTitles) {
            String currentTitle = e.getText();
            System.out.println("currentTitle = " + currentTitle);
            assertTrue(currentTitle.equals(firstTitle) || currentTitle.equals(secondTitle));
        }
    }

    private int parsePrice(String text) {
        String[] prices = text.replaceAll("\\s", "").split("₴");
        return Integer.parseInt(prices[1]);
    }

    public Integer findElemetPrice(WebDriver driver, int price) {
        List<WebElement> elementList = driver.findElements(By.cssSelector("span.goods-tile__price-value"));
        int counter = 1;
        for (WebElement e : elementList) {
            int currPrice = Integer.parseInt(e.getText().replace("&nbsp", "").replaceAll(" ", ""));
            if (currPrice < price) {
                driver.findElement(By.xpath("(//span[@class='goods-tile__price-value']//preceding::a[2])" + "[" + counter + "]")).click();
                return currPrice;
            }
            counter++;
        }
        return null;
    }

}
