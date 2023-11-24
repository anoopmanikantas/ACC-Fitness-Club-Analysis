package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import helpers.Strings;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import us.codecraft.xsoup.Xsoup;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

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

    Crawler(Strings url) {
        this.url = url.value;
        this.fitnessDataModel = new FitnessDataModel();
        this.isDirectoryEmpty = true;
        this.numberOfFiles = 0;
    }

    void createDirectory(Strings htmlFilePath) {
        isDirectoryEmpty = checkIfDirectoryIsEmpty(htmlFilePath); // TODO: Call checkIfDirectoryIsEmpty
        if (isDirectoryEmpty) {
            initDriver();
            try {
                Files.createDirectory(Paths.get(Strings.SourceParsedHTMLDirectory.value));
                Files.createDirectory(Paths.get(htmlFilePath.value));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void initDriver() {
        EdgeOptions edgeOptions = new EdgeOptions();
        driver = new EdgeDriver(edgeOptions);
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
            if (files.length == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    void parseAndStoreHTMLFile() {
        String fileName = this.document.title();
        fileName = fileName.replaceAll(Strings.RegexSpecialCharacters.value, Strings.Empty.value);
        try {
            FileWriter fileWriter = new FileWriter(
                    Strings.PlanetFitnessParsedHTMLDirectory.value + fileName + Strings.HTMLExtension.value
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
