package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import helpers.Strings;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import us.codecraft.xsoup.Xsoup;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.regex.Pattern;

public class Crawler {
    Actions actions;
    WebDriver driver;
    WebDriverWait webDriverWait;
    final String url;
    String html;
    Document document;
    FitnessDataModel fitnessDataModel;
    boolean isDirectoryEmpty;
    int numberOfFiles;
    Pattern pricePattern;
    JavascriptExecutor javascriptExecutor;

    Crawler(Strings url) {
        this.url = url.value;
        this.fitnessDataModel = new FitnessDataModel();
        this.isDirectoryEmpty = true;
        this.numberOfFiles = 0;
        this.pricePattern = Pattern.compile(Strings.RegexPrice.value);
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
    }

    void createDirectory(Strings htmlFilePath) {
        isDirectoryEmpty = checkIfDirectoryIsEmpty(htmlFilePath); // TODO: Call checkIfDirectoryIsEmpty
        if (isDirectoryEmpty) {
            initDriver();
            try {
                Files.createDirectory(Paths.get(Strings.SourceParsedHTMLDirectory.value));
            } catch (Exception ignored) {}
            try {
                Files.createDirectory(Paths.get(htmlFilePath.value));
            } catch (Exception ignored) {}
        }
    }

    void initDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(Strings.ArgumentRemoteAllowOrigins.value);
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        chromeOptions.merge(desiredCapabilities);
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(100));
        actions = new Actions(driver);
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    boolean checkIfDirectoryIsEmpty(Strings path) {
        File directory = new File(path.value);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            assert files != null;
            numberOfFiles = files.length;
            return files.length == 0;
        } else {
            return true;
        }
    }

    @SuppressWarnings("All")
    void parseAndStoreHTMLFile(Strings directory) {
        String fileName = this.document.title();
        fileName = fileName.replaceAll(Strings.RegexSpecialCharacters.value, Strings.Empty.value);
        storeFileUsing(directory, fileName);
    }

    void parseAndStoreHTMLFile(Strings directory, String fileName) {
        storeFileUsing(directory, fileName);
    }

    private void storeFileUsing(Strings directory, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(
                    directory.value + fileName + Strings.HTMLExtension.value
            );
            fileWriter.write(document.outerHtml());
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    String getTextFrom(String xpath) {
        return Xsoup.compile(xpath).evaluate(document).getElements().text();
    }
}
