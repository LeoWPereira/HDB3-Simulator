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

public class CodificadorDeEntrada extends Endpoint
{
	public static final String 	ID 			= "Cliente";
	
	private 			String 	hostname;
	private 			int 	portNumber;

	public CodificadorDeEntrada(String hostname, int portNumber)
	{
		super(ID);
		this.hostname = hostname;
		this.portNumber = portNumber;
	}

	public void run()
	{	
		abreSocket(this.hostname, this.portNumber, new Runnable()
		{
			@Override
			public void run()
			{
				println("Enviando pedido para enviar mensagem\n");
				enviar("pedido para enviar");
				
				// Aguarda permissão para enviar
				ouvindo(new Function<String>()
				{
					public boolean apply(String message)
					{
						if (!message.equals("confirmação de recebimento"))
						{
							println("O sistema não está pronto para enviar\n");
							return false;
						}
						
						println("Confirmação de recebimento recebida\n");
						
						return true;
					}
				});
				
				// Recebe entrada de dado
				Scanner keyboard = new Scanner(System.in);
				String 	line;
				
				println("Aguardando mensagem para envio:");
				
				do
				{
					line = keyboard.nextLine();
					String copia = line;

					if (!validateBinaryInput(line))
						line = Codificador.textToBin(line).toString();
					
					println("Texto para envio (binário): " + line + "\n");
					
					if (!validateBinaryInput(copia))
						println("Texto para envio (ASCII): " + Codificador.binToText(line) + "\n");
					
					String codificado = Codificador.codifica(line);
					
					println("Mensagem codificada para: " + codificado + "\n");
					
					enviar(codificado);
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