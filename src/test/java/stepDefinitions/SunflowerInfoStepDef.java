package stepDefinitions;

import SQLtest.mySQLConnection;
import com.mysql.cj.exceptions.AssertionFailedException;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class SunflowerInfoStepDef {

    WebDriver driver;
    String projectURL;
    String name;
    boolean namePresent;

    @Before("@SunflowerHouse")
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "webdrivers/chromedriver95.exe");

        ChromeOptions option =  new ChromeOptions();
        option.setPageLoadStrategy(PageLoadStrategy.NONE); //can set to options: normal, eager, none

        driver = new ChromeDriver(option);
        projectURL = "https://www.archdaily.com/638145/sunflower-house-cadaval-and-sola-morales";
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(projectURL);
    }

    @Given("sf title exists in db")
    public void sfTitleExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement n = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"content\"]/div/div[2]/header[2]/h1")));
        name = n.getText();

        boolean namePresent;
        try {
            String query = "select * from projectpages where name = " + "'" + name + "'";
            statement = mySQLConnection.connect().createStatement();
            result = statement.executeQuery(query);

            if (result.next()) {
                System.out.println("Name : " + name + " : exists in db");
                namePresent = true;
            } else {
                System.out.println("Name : " + name + " : not in db");
                String queryAdd = "insert into projectpages (name) values (" + "'" + name + "'" + ")";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with : name: " + name);
                namePresent = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mySQLConnection.disconnect();
        }
    }

    @Then("check if sf architect exists in db")
    public void checkIfSfArchitectExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement a = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[2]/ul/li[1]/div/span[2]/a")));
        String architect = a.getText();

        String query = "select * from projectpages where architect = " + "'" + architect + "'" + " and name = " + "'" + name + "'";
        try {
            statement = mySQLConnection.connect().createStatement();
            result = statement.executeQuery(query);

            if (result.next()) {
                System.out.println("Architect : " + architect + " : exists in db");
            } else {
                System.out.println("-----");
                System.out.println("Architect : " + architect + " : not in db");
                String queryAdd = "update projectpages set architect = " + "'" + architect + "'" + "where name = " + "'" + name + "'";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with : Architect: " + architect);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("check if sf year exists in db")
    public void checkIfSfYearExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        boolean isPresent = driver.findElements(By.xpath("//*[@id=\"single-content\"]/div[2]/ul/li[3]/span[2]/a")).size() > 0;
        if (!isPresent) {
            System.out.println("year was not found on page");
            throw new AssertionFailedException("year was not found on page");
        } else {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            WebElement y = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[2]/ul/li[3]/span[2]/a")));
            String year = y.getText();

            String query = "select * from projectpages where year = " + "'" + year + "'" + " and name = " + "'" + name + "'";
            try {
                statement = mySQLConnection.connect().createStatement();
                result = statement.executeQuery(query);

                if (result.next()) {
                    System.out.println("Year : " + year + " : exists in db");
                } else {
                    System.out.println("-----");
                    System.out.println("Year : " + year + " : not in db");
                    String queryAdd = "update projectpages set year = " + year + " where name = " + "'" + name + "'";
                    statement.executeUpdate(queryAdd);
                    System.out.println("db updated with : Year: " + year);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @And("dropdown sf is clicked")
    public void dropdownSfIsClicked() {
        //found this loop + "(PageLoadStrategy.NONE)" together loads faster than NORMAL
        String pageLoadStatus = null;
        do {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            pageLoadStatus = (String)jse.executeScript("return document.readyState");
        } while (!pageLoadStatus.equals("complete")); {
            //System.out.println("Page Loaded");
        }

        //close popup
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"gdpr-consent\"]"))).click();
        //click btn to show all details
        WebElement moreBtn = driver.findElement(By.xpath("//*[@id=\"js-toggle-extra-specs\"]"));
        Actions action = new Actions(driver);
        action.moveToElement(moreBtn).build().perform();
        moreBtn.click();
    }

    @Then("check if sf city exists in db")
    public void checkIfSfCityExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        boolean isPresent = driver.findElements(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[5]/div/span[2]")).size() > 0;
        if (!isPresent) {
            System.out.println("city is not found on page");
            throw new AssertionFailedException("city was not found on page");
        } else {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            WebElement c = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[5]/div/span[2]")));
            String city = c.getText();

            String query = "select * from projectpages where city = " + "'" + city + "'" + " and name = " + "'" + name + "'";
            try {
                statement = mySQLConnection.connect().createStatement();
                result = statement.executeQuery(query);

                if (result.next()) {
                    System.out.println("City : " + city + " : exists in db");
                } else {
                    System.out.println("-----");
                    System.out.println("City : " + city + " : not in db");
                    String queryAdd = "update projectpages set city = " + "'" + city + "'" + " where name = " + "'" + name + "'";
                    statement.executeUpdate(queryAdd);
                    System.out.println("db updated with : city: " + city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //This test will fail unless removing "WILLFAIL" from the end of 'boolean isPresent' xpath
    @Then("check if sf country exists in db")
    public void checkIfSfCountryExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        boolean isPresent = driver.findElements(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[6]/div/span[2]/a/WILLFAIL")).size() > 0; //delete WILLFAIL from end to make test pass
        if (!isPresent) {
            System.out.println("country was not found on page");
            throw new AssertionFailedException("country was not found on page");
        } else {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            WebElement co = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[6]/div/span[2]/a")));
            String country = co.getText();

            String query = "select * from projectpages where country = " + "'" + country + "'" + " and name = " + "'" + name + "'";
            try {
                statement = mySQLConnection.connect().createStatement();
                result = statement.executeQuery(query);

                if (result.next()) {
                    System.out.println("Country : " + country + " : exists in db");
                } else {
                    System.out.println("-----");
                    System.out.println("Country : " + country + " : not in db");
                    String queryAdd = "update projectpages set country = " + "'" + country + "'" + "where name = " + "'" + name + "'";
                    statement.executeUpdate(queryAdd);
                    System.out.println("db updated with : country : " + country);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @After("@SunflowerHouse")
    public void after() {
        driver.quit();
    }

}
