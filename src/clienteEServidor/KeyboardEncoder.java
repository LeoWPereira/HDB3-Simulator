/*
 * HDB3 Encoder
 * @author: Leonardo Winter Pereira
 * @authot: Caio Cesar
 * @author: Mauro Vialich
 * 
 * EntryPoint. Last modified: 18.06.2016
 */

package clienteEServidor;

import java.util.Scanner;

import codificacao.Codificador;

public class KeyboardEncoder extends Endpoint
{
	public static final String ID = "Cliente";
	
	private String hostname;
	private int portNumber;

	public KeyboardEncoder(String hostname, int portNumber)
	{
		super(ID);
		this.hostname = hostname;
		this.portNumber = portNumber;
	}

	public void run()
	{	
		openSocket(this.hostname, this.portNumber, new Runnable(){

			@Override
			public void run()
			{
				println("Enviando pedido para enviar mensagem");
				send("pedido para enviar");
				
				// Handshaking: waiting for clear-to-send
				listen(new Function<String>()
				{
					public boolean apply(String message)
					{
						if (!message.equals("confirmação de recebimento"))
						{
							println("O sistema não está pronto para enviar");
							return false;
						}
						
						println("Confirmação de recebimento recebida");
						
						return true;
					}
				});
				
				//Listen for data
				Scanner keyboard = new Scanner(System.in);
				String line;
				
				println("Aguardando mensagem para envio:");
				
				do
				{
					line = keyboard.nextLine();

					if (!validateBinaryInput(line))
					{
						line = Codificador.textToBin(line).toString();
						//println("Please input a binary string");
						//continue;
					}

					println("Texto para envio (binário): " + line);
					println("Texto para envio (ASCII): " + Codificador.binToText(line));
					
					String codificado = Codificador.codifica(line);
					
					println("Mensagem codificada para: " + codificado);
					
					send(codificado);
				} while(line != null);
				
				keyboard.close();
			}
		});
	}

	private static boolean validateBinaryInput(String input)
	{
		int n = input.length();
		
		for (int i = 0; i < n; i++)
			if (input.charAt(i) != '0' && input.charAt(i) != '1')
				return false;
		
		return true;
	}
}