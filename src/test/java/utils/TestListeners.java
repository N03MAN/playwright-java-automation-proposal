package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestListeners implements ITestListener {
    public static ThreadLocal<Page> currentPage = new ThreadLocal<>();

    @Override
    public void onTestFailure(ITestResult result) {
        Page page = currentPage.get();
        if (page != null) {
            byte[] png = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment("screenshot", new ByteArrayInputStream(png));
        }
    }
}
