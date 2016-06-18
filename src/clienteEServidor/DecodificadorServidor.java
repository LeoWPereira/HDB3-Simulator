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
	public static final String ID = "Servidor";
	private int portNumber;

	public DecodificadorServidor(int portNumber)
	{
		super(ID);
		this.portNumber = portNumber;
	}

	public void run()
	{		
		openServerSocket(this.portNumber, new Runnable()
		{
			@Override
			public void run()
			{
				// Aguardando pelo pedido para enviar
				listen(new Function<String>()
				{
					public boolean apply(String message)
					{
						if (!message.equals("pedido para enviar"))
						{
							println("O codificador não está pronto para enviar");
							return false;
						}
						
						else
						{
							println("Pedido recebido");
							println("Enviando confirmação de recebimento");
							send("confirmação de recebimento");
							return true;
						}
					}

				});

				// Aguardando a mensagem a ser decodificada
				listen(new Function<String>()
				{

					public boolean apply(String message)
					{
						println("Mensagem decodificada para (binário): " + Codificador.decodifica(message));
						println("Mensagem decodificada para (ASCII): " + Codificador.binToText(Codificador.decodifica(message)));
						
						return false;
					}
				});
			}
		});
	}
}