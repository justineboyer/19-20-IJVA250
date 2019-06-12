package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;

public class End2EndTest {

    private static WebDriver driver;

    @BeforeClass
    public static void initWebDriver() {
        // télécharger sur http://chromedriver.chromium.org/downloads
        System.setProperty("webdriver.chrome.driver", "C:/alex/chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterClass
    public static void shutDownDriver() {
        driver.quit();
    }

    @Test
    public void demo() {
        driver.get("http://localhost:8080");
        int nbFactures = driver.findElements(By.cssSelector("h2")).size();

        driver.findElement(By.partialLinkText("achats")).click();
        Assertions.assertThat(driver.getCurrentUrl()).isEqualTo("http://localhost:8080/acheter");

        WebElement firstInput = driver.findElements(By.cssSelector("input")).get(0);
        WebElement secondInput = driver.findElements(By.cssSelector("input")).get(1);
        firstInput.sendKeys("3");
        secondInput.sendKeys("6");
        driver.findElement(By.cssSelector("button")).click();

        Assertions.assertThat(driver.getCurrentUrl()).isEqualTo("http://localhost:8080/");

        int nbFacturesApres = driver.findElements(By.cssSelector("h2")).size();
        Assertions.assertThat(nbFacturesApres).isEqualTo(nbFactures + 1);

        // vérifier que le stock a été mis à jour
    }
}