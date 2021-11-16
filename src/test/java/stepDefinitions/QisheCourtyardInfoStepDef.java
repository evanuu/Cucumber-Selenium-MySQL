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
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class QisheCourtyardInfoStepDef {

    WebDriver driver;
    String projectURL;
    String name;
    boolean namePresent;

    @Before("@QisheCourtyard")
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "webdrivers/chromedriver95.exe");

        ChromeOptions option =  new ChromeOptions();
        option.setPageLoadStrategy(PageLoadStrategy.NONE); //can set to options: normal, eager, none

        driver = new ChromeDriver(option);
        projectURL = "https://www.archdaily.com/931216/qishe-courtyard-archstudio";
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Given("project title {string} exists")
    public void project_title_exists(String expected) {
        driver.get(projectURL);
        WebElement t = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/header[2]/h1"));
        String actualTitle = t.getText();
        //String expected = "Qishe Courtyard / ARCHSTUDIO";
        Assert.assertEquals(expected, actualTitle);
    }

    @Then("check if title exists in db")
    public void checkIfTitleExistsInDb() {

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

    @Then("check if architect exists in db")
    public void checkIfArchitectExistsInDb() {

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

    @Then("check if year exists in db")
    public void checkIfYearExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement y = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[2]/ul/li[3]/span[2]/a")));
        String year = y.getText();

        String query = "select * from projectpages where year = " + year + " and name = " + "'" + name + "'";
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

    @And("dropdown is clicked")
    public void dropdownIsClicked() {
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

    @Then("check if city exists in db")
    public void checkIfCityExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement c = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[11]/div/span[2]")));
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

    @Then("check if country exists in db")
    public void checkIfCountryExistsInDb() {
        mySQLConnection mySQLConnection = new mySQLConnection();
        Statement statement;
        ResultSet result;

        boolean isPresent = driver.findElements(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[12]/div/span[2]/a")).size() > 0;
        if (!isPresent) {
            System.out.println("country was not found on page");
            throw new AssertionFailedException("country was not found on page");
        } else {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            WebElement co = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[12]/div/span[2]/a")));
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

    @After("@QisheCourtyard")
    public void teardown() {
        driver.quit();
    }

}
