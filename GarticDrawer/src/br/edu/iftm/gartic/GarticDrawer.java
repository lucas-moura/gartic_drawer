package br.edu.iftm.gartic;

import org.openqa.selenium.chrome.ChromeDriver;

public class GarticDrawer {
	private ChromeDriver driver;
	
	public GarticDrawer()
	{
		driver = new ChromeDriver();
		
	}
	
	private void login(String user, String password)
	{
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
	
	
}
