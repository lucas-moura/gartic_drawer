package br.edu.iftm.gartic;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.edu.iftm.selenium.webdriver.WebDriverSettings;

public class GarticDrawer {
	private ChromeDriver driver;
	private WebDriverWait wait;
	
	public GarticDrawer(String roomLink, String roomPassword, String nickname)
	{
		WebDriverSettings.inicializar();
		this.driver = new ChromeDriver();
		this.wait = new WebDriverWait(driver, 120);
		WebDriverSettings.esperarPorComponentes(driver, 20);
		nickname = nickname.replaceAll(" ", "");
		joinRoom(roomLink, roomPassword, nickname);
	}
	
	private void joinRoom(String roomLink, String roomPassword, String nickname)
	{
		driver.navigate().to(roomLink);
		driver.findElement(By.id("nick")).sendKeys(nickname);
		driver.findElement(By.id("senha")).sendKeys(roomPassword);
		driver.findElement(By.xpath("//*[@id='botoes']/input[4]")).submit();
		WebDriverSettings.esperarPorComponente(driver, "popupBt1", 20);
		driver.findElement(By.id("popupBt1")).click(); // Pula o alerta inicial
		play();
	}
	
	private void play()
	{
		boolean exit = false;
		
		while(!exit)
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("desenhar")));
			System.out.println("Pronto pra desenhar");
			draw();
		}
	}
	
	private void draw()
	{
		String palavra = driver.findElement(By.xpath("//*[@id='alerta']/div[3]")).getText();
		System.out.println("Desenhar: " + palavra);
		//*[@id="alerta"]/div[3]
		// Palavra pra ser desenhada: //*[@id="alerta"]/div[3]
		// Clicka em desenhar (id = desenhar)
		// Para escolher a cor:
		// Cor para clickar: //*[@id="cores"]/div[1]
		// Muda o atributo(codigo) do elemento //*[@id="cores"]/div[1]  para o hexcolor
		// E depois clicka
		// Definir tamanho do pincel (elmento id = tamanho) para o value = 1
		
		// Canvas: //*[@id="telaCanvas"]/canvas[1] 
	}
	
	private void login(String user, String password)
	{}
	
}
