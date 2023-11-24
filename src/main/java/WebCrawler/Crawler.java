package WebCrawler;

import helpers.Strings;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import us.codecraft.xsoup.Xsoup;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Crawler {
    final Actions actions;
    final WebDriver driver;
    final WebDriverWait webDriverWait;
    final String url;
    String html;
    Document document;

    Crawler(WebDriver driver, Strings url, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.url = url.value;
        this.actions = new Actions(driver);
        this.webDriverWait = webDriverWait;
    }

    void createDirectory(Strings htmlFilePath) {
        try {
            Files.createDirectory(Paths.get(Strings.SourceParsedHTMLDirectory.value));
            Files.createDirectory(Paths.get(htmlFilePath.value));
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
