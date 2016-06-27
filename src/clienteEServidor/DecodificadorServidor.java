/*
 * HDB3 Encoder
 * @author: Leonardo Winter Pereira
 * @authot: Caio Cesar
 * @author: Mauro Vialich
 * 
 * EntryPoint. Last modified: 18.06.2016
 */

package clienteEServidor;

import codificacao.Codificador;

public class DecodificadorServidor extends Endpoint
{
	public static final String 	ID 			= "Servidor";
	private 			int		portNumber;

	public DecodificadorServidor(int portNumber)
	{
		super(ID);
		this.portNumber = portNumber;
	}

	public void run()
	{		
		abreSocketServidor(this.portNumber, new Runnable()
		{
			@Override
			public void run()
			{
				// Aguardando pelo pedido para enviar
				ouvindo(new Function<String>()
				{
					public boolean apply(String message)
					{
						if (!message.equals("pedido para enviar"))
						{
							println("O codificador não está pronto para enviar\n");
							return false;
						}
						
						else
						{
							println("Pedido recebido\n");
							println("Enviando confirmação de recebimento\n");
							enviar("confirmação de recebimento");
							return true;
						}
					}

				});

				// Aguardando a mensagem ser decodificada
				ouvindo(new Function<String>()
				{

					public boolean apply(String message)
					{
						println("Mensagem recebida: " + message + "\n");
						
						println("Mensagem decodificada para (binário): " + Codificador.decodifica(message) + "\n");
						
						try
						{
							println("Mensagem decodificada para (ASCII): " + Codificador.binToText(Codificador.decodifica(message)) + "\n");
						}
						catch(Exception e)
						{
							println(e.toString());
						}
						
						return false;
					}
				});
			}
		});
	}
}