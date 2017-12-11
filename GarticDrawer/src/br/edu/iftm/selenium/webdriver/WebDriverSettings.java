package br.edu.iftm.selenium.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverSettings {
	// http://stefanteixeira.com.br/2014/04/29/entendendo-os-tipos-de-esperas-no-selenium-webdriver/
	public static final void inicializar()
	{
		String pasta = "C:\\Users\\everis\\Downloads\\DRIVERS\\";
		System.setProperty("webdriver.chrome.driver", pasta + "chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", pasta + "geckodriver.exe");
		System.setProperty("webdriver.ie.driver", pasta + "IEDriverServer.exe");
	}
	
	public static void esperarPorComponentes(WebDriver driver, int tempoSegundos)
	{
		driver.manage().timeouts().implicitlyWait(tempoSegundos, TimeUnit.SECONDS);
	}
	
	public static void esperarPorComponente(WebDriver driver, String id, int tempoSegundos)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);  
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		driver.manage().timeouts().implicitlyWait(tempoSegundos, TimeUnit.SECONDS);
	}
}
