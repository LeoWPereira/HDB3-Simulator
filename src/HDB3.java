/*
 * HDB3 Encoder
 * @author: Leonardo Winter Pereira
 * @authot: Caio Cesar
 * @author: Mauro Vialich
 * 
 * HDB3. Last modified: 18.06.2016
 */

import clienteEServidor.CodificadorDeEntrada;
import clienteEServidor.DecodificadorServidor;

public class HDB3
{
	private static final String HOSTNAME 	= "localhost";

	private static final int 	PORT_NUMBER = 65234;

	public static void main(String[] args)
	{
		System.out.println("Caio Cesar, Leonardo Pereira e Mauro Vialich\n");
		System.out.println("******CODIFICADOR / DECODIFICADOR HDB3******\n\n");
		
		// Inicialmente precisamos iniciar o Servidor
		DecodificadorServidor decodificador = new DecodificadorServidor(PORT_NUMBER);
		decodificador.start();

		// Por fim, iniciamos o cliente
		CodificadorDeEntrada codificador = new CodificadorDeEntrada(HOSTNAME, PORT_NUMBER);
		codificador.start();
	}
}