package br.edu.iftm.gartic;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import br.edu.iftm.selenium.webdriver.WebDriverSettings;
import net.coobird.thumbnailator.Thumbnails;

public class GarticDrawer {
	private ChromeDriver driver;
	private WebDriverWait wait;
	
	public GarticDrawer(String roomLink, String roomPassword, String nickname, boolean custom)
	{
		WebDriverSettings.inicializar();
		this.driver = new ChromeDriver();
		//this.driver.manage().window().maximize();
		this.wait = new WebDriverWait(driver, 120);
		WebDriverSettings.esperarPorComponentes(driver, 20);
		nickname = nickname.replaceAll(" ", "");
		joinRoom(roomLink, roomPassword, nickname, custom);
	}
	
	
	
	private void joinRoom(String roomLink, String roomPassword, String nickname, boolean custom)
	{
		driver.navigate().to(roomLink);
		driver.findElement(By.id("nick")).sendKeys(nickname);
		if(!roomPassword.isEmpty())
			driver.findElement(By.id("senha")).sendKeys(roomPassword);
		driver.findElement(By.xpath("//*[@id='botoes']/input[4]")).submit();
		if(custom)
		{
			// Botão para clicar em sala custom
			WebDriverSettings.esperarPorComponente(driver, "popupBt1", 20);
			driver.findElement(By.id("popupBt1")).click();
		}else
		{
			// Botão para clicar em sala pública
			WebDriverSettings.esperarPorComponente(driver, "btn2", 20);
			//wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn2")));
			driver.findElement(By.className("btn2")).click();
		}
		try {
			play();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void play() throws MalformedURLException, IOException, InterruptedException
	{
		boolean exit = false;
		WebDriver driverGetImage = new ChromeDriver();
		driverGetImage.get("https://www.google.com.br/imghp?as_st=y&tbm=isch&as_q=&as_epq=&as_oq=&as_eq=&cr=&as_sitesearch=&safe=images&tbs=isz:i");
		
		while(!exit)
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("desenhar")));
			System.out.println("Pronto pra desenhar");
			draw(driverGetImage, "");
		}
	}
	
	
	private void draw(WebDriver driverGetImage, String imageLink) throws MalformedURLException, IOException, InterruptedException
	{
		String word =driver.findElement(By.className("texto2")).getText();
		driver.findElement(By.id("desenhar")).click();
		System.out.println("Desenhar: " + word);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		URL urlImagem;
		double espacamentoPixel = 2.5; // 1 o menor
		
		int comecarDesenharX = 150, 
			comecarDesenharY = 100, 
			pularnPixel = 2,
			redimensionarImagemXY = 200,
			tamanhoPincel = 2;
		
		if(imageLink == "")
			urlImagem = new URL(getImageLink(word + " -.gif", driverGetImage));
		else
			urlImagem = new URL(word);
		
		BufferedImage image = ImageIO.read(urlImagem);
		//image = resize(image, redimensionarImagemXY, redimensionarImagemXY);
		
		//Definir tamanho do pincel
		if(tamanhoPincel < 3) {
			for (int i = 0; i < 3 - tamanhoPincel; i++) {
				driver.findElement(By.id("tamanho")).sendKeys(Keys.DOWN);
			}
		}else if(tamanhoPincel>=3){
			for (int i = 0; i < 3 - tamanhoPincel; i++) {
				driver.findElement(By.id("tamanho")).sendKeys(Keys.UP);
			}
		}
		
		WebElement canvas = driver.findElement(By.id("telaCanvas"));
	
		Actions builder = new Actions(driver);
		
		String[][] result = convertTo2DWithoutUsingGetRGB(image);
		
		WebElement clickquadradoCor = driver.findElement(By.xpath("//*[@id=\"cores\"]/div[1]"));
		String temp = "";
		
		for (int x = 0; x < image.getWidth(); x = x + pularnPixel) {
			for (int y = 0; y < image.getHeight(); y = y + pularnPixel) {
				
				//Se cor do pixel for branco skip
				if(result[y][x] != "branco") {
				
					//Define a cor do quadrado e clica
					if(temp != result[y][x]) {
						js.executeScript("document.getElementsByClassName('cor')[0].setAttribute('codigo', '" + result[y][x]+"')");
						clickquadradoCor.click();
					}
					
					//Desenha no canvas
					builder.moveToElement(canvas, 0, 0)
					.moveByOffset((int) (comecarDesenharX + (x * espacamentoPixel)),
						          (int) (comecarDesenharY + (y * espacamentoPixel))).
								  click().build().perform();
					
					temp = result[y][x];
				}

			}
		}
	}

	
	public static String getImageLink(String pesquisa, WebDriver driver) throws IOException, InterruptedException {
		driver.findElement(By.id("lst-ib")).sendKeys(pesquisa);
		driver.findElement(By.id("lst-ib")).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("//*[@id=\"rg_s\"]/div[1]")).click();
		
		Thread.sleep(2000);;
		
		List <WebElement> elementOption = driver.findElements(By.className("irc_mi"));
		
		String link = elementOption.get(1).getAttribute("src");
		System.out.println(link);
		
		return link;
		
	}
	
	
	public static String cordoPixel(int clr) {
		int  red   = (clr & 0x00ff0000) >> 16;
		int  green = (clr & 0x0000ff00) >> 8;
		int  blue  =  clr & 0x000000ff;
		
		double luminance = 0.2126*red + 0.7152*green + 0.0722*blue;
		
		String hex = "";
		
		if(luminance>230){
			 hex = "branco";
		}else {
			hex = String.format("%02x%02x%02x", red, green, blue);  
		
		}
		return hex;
	}
	
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) throws IOException 
	{
		return Thumbnails.of(img).size(newW, newH).asBufferedImage();
	}
	
	private static String[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

	      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

	      String[][] result = new String[height][width];
	      if (hasAlphaChannel) {
	         final int pixelLength = 4;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
	            argb += ((int) pixels[pixel + 1] & 0xff); // blue
	            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row][col] = cordoPixel(argb);
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      } else {
	         final int pixelLength = 3;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row][col] = cordoPixel(argb);
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      }

	      return result;
	   }
	
	private void login(String user, String password)
	{}
}
