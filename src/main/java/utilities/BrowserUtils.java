package utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BrowserUtils {

	private static String parentWindow;
	private static Logger console = LoggerFactory.getLogger(BrowserUtils.class);

	public static void sleep(double seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static WebElement getElement(String xpath, String value) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format(xpath, value))));
	}

	public static WebElement getElement(String xpath) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
	}

	public static List<WebElement> getElements(String xpath) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
	}

	public static WebElement findElement(String xpath) {
		return Driver.get().findElement(By.xpath(xpath));
	}

	public static WebElement findElement(String xpath, String value) {
		return Driver.get().findElement(By.xpath(String.format(xpath, value)));
	}

	public static void clickElement(WebElement element,String name) {
		scrollTo(element);
		waitUntilClickable(element).click();
		console.info("Clicked on element {} with locator {} successfully",name,element);
	}

	public static void clickAndSend(String xpath, String value) {
		WebElement element = getElement(xpath);
		clickElement(element,"");
		element.sendKeys(value);
	}

	public static void clickAndSend(WebElement wb, String value, String name) {
		scrollTo(wb);
		waitUntilClickable(wb);
		clickElement(wb,name);
		wb.sendKeys(value);
		console.info("text {} is entered in webelement {} with {} locator ", value, name, wb);
	}

	public static void clickElement(String xpath) {
		waitUntilClickable(getElement(xpath)).click();
	}

	public static void clickElement(String xpath, String value) {
		waitUntilClickable(getElement(xpath, value)).click();
	}

	public static boolean checkElement(WebElement wb, String elementName) throws ElementNotVisibleException {
		boolean bCheck = true;
		try {
			scrollTo(wb);
			waitUntilClickable(wb);
			if (wb.isSelected()) {
				console.info("{} with locator {} is already checked", elementName, wb);
			} else {
				wb.click();
				console.info("{} with locator {} is checked successfully", elementName, wb);
			}
		} catch (Exception e) {
			bCheck = false;
		}
		return bCheck;

	}

	public static String getChildWindowTitle() {
		switchToChildWindow();
		return Driver.get().getTitle();
	}

	public static String getChildWindowURL() {
		switchToChildWindow();
		return Driver.get().getCurrentUrl();
	}

	public static void scrollTo(String xpath, String value) {
		((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView(true);",
				getElement(xpath, value));
	}

	public static void scrollTo(String xpath) {
		((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView(true);", getElement(xpath));
	}

	public static void scrollTo(WebElement element) {
		((JavascriptExecutor) Driver.get()).executeScript("arguments[0].scrollIntoView(true);", element);
		console.info("scroll to element {}", element);
	}

	public static void scrollAndClick(String xpath, String value) {
		scrollTo(xpath, value);
		clickElement(xpath, value);
	}

	public static void setParentWindow() {
		parentWindow = Driver.get().getWindowHandle();
	}

	public static void switchToChildWindow() {
		setParentWindow();
		Set<String> windows = Driver.get().getWindowHandles();
		for (String each : windows) {
			if (!each.equals(parentWindow)) {
				Driver.get().switchTo().window(each);
			}
		}
	}

	public static void switchToParentWindow() {
		Driver.get().switchTo().window(parentWindow);
	}

	public static boolean elementDisplayed(String xpath) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).isDisplayed();
	}
	
	public static boolean elementDisplayed(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.visibilityOf(ele)).isDisplayed();
	}

	public static boolean elementDisplayed(String xpath, String value) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(xpath, value))))
				.isDisplayed();
	}

	public static boolean elementSelected(String xpath) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).isSelected();
	}

	public static boolean elementSelected(String xpath, String value) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.elementToBeSelected(By.xpath(String.format(xpath, value))));
	}

	public static String getElementText(WebElement element) {
		waitUntilVisible(element);
		return element.getText();
	}

	public static List<String> getTextOfElements(List<WebElement> elements) {
		List<String> texts = new ArrayList<>();
		for (WebElement eachElement : elements) {
			texts.add(eachElement.getText().trim());
		}
		return texts;
	}

	public static WebElement waitUntilClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		//wait.until(ExpectedConditions.visibilityOf(element));
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public static boolean waitUntilTextToBe(WebElement element, String text) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public static void waitUntilUrlToBe(String url) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 8);
		wait.until(ExpectedConditions.urlToBe(url));
	}

	public static void waitUntilTitleContains(String title) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		wait.until(ExpectedConditions.titleContains(title));
	}

	public static void waitUntilAttributeContains(String xpath, String attribute, String value) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		wait.until(ExpectedConditions.attributeContains(getElement(xpath), attribute, value));
	}

	public static void hoverOver(String xpath) {
		Actions act = new Actions(Driver.get());
		act.moveToElement(getElement(xpath)).perform();
	}

	public static void turnOnImplicitWait() {
		Driver.get().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public static void turnOffImplicitWait() {
		Driver.get().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	public static void getScreenshot(String name) throws IOException {
		// name the screenshot with the current date time to avoid duplicate name
		String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		// TakesScreenshot ---> interface from selenium which takes screenshots
		TakesScreenshot ts = (TakesScreenshot) Driver.get();
		File source = ts.getScreenshotAs(OutputType.FILE);
		// full path to the screenshot location
		String target = System.getProperty("user.dir") + "\\test-output\\Screenshots\\" + name + date + ".png";
		File finalDestination = new File(target);
		// save the screenshot to the path given
		FileUtils.copyFile(source, finalDestination);
	}

	public static String getCurrentPageTitle() {
		return Driver.get().getTitle();
	}

	public static String getCurrentPageURL() {
		return Driver.get().getCurrentUrl();
	}

	public static void closeCurrentPage() {
		Driver.get().close();
	}

	public static void navigateBack() {
		Driver.get().navigate().back();
	}

	public static void waitUntilVisibilityOfAllElements(List<WebElement> elements) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	public static void waitUntilVisible(WebElement element) {
		WebDriverWait wait = new WebDriverWait(Driver.get(), 5);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void takeScreenshot(String methodname) throws IOException {
		File srcFile = ((TakesScreenshot) Driver.get()).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir") + "/screenshots/" + methodname + ".PNG"));
	}

	public static String getRandomASCIICharacter(int length) {
		String data = "";
		SecureRandom secureRandom = new SecureRandom();
		String characters = "bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVW";
		StringBuilder generatedString = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int randonSequence = secureRandom.nextInt(characters.length());
			generatedString.append((int) characters.charAt(randonSequence));
		}
		data = generatedString.toString();
		return data;
	}
}
