/*
 * HDB3 Encoder
 * @author: Leonardo Winter Pereira
 * @authot: Caio Cesar
 * @author: Mauro Vialich
 * 
 * EntryPoint. Last modified: 18.06.2016
 */

package clienteEServidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Endpoint extends Thread
{
	private String id;
	protected Socket socket;

	Endpoint(String id)
	{
		this.id = id;
	}

	void abreSocketServidor(int portNumber, Runnable process)
	{
		ServerSocket serverSocket = null;
		
		try 
		{
			serverSocket = new ServerSocket(portNumber);
			
			socket = serverSocket.accept();
			
			process.run();
		} 
		catch (IOException e) 
		{
			println("Socket do Servidor não pôde ser inicializado");
		} 
		finally
		{
			if (serverSocket != null) 
			{
				try
				{
					serverSocket.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			fechaSocket();
		}
	}

	void abreSocket(String hostname, int portNumber, Runnable process)
	{
		try 
		{
			socket = new Socket(hostname, portNumber);
			
			process.run();
		} 
		catch (IOException e) 
		{
			println("Socket não pôde ser inicializado");
		} 
		finally 
		{
			fechaSocket();
		}
	}
	
	private void fechaSocket()
	{
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	void ouvindo(Function<String> listener) 
	{
		if (socket == null) 
		{
			println("Socket Nulo");
		
			return;
		} 
		else if (listener == null) 
		{
			println("Variável 'listener' não pode ser nula");
		
			return;
		}

		DataInputStream input;
		
		String tmp;
		
		try 
		{
			input = new DataInputStream(socket.getInputStream());
		
			while ((tmp = input.readLine()) != null) 
			{
				if (listener.apply(tmp))
					break;
			}
		} 
		catch (IOException e) 
		{
			println("Exceção ao tentar ler o Socket: " + e.getMessage());
		}
	}

	void enviar(String data) 
	{
		if (socket == null) 
		{
			println("Socket Nulo");
			
			return;
		}

		PrintStream output;
		
		try 
		{
			output = new PrintStream(socket.getOutputStream());
		
			output.println(data);
		} 
		catch (IOException e) 
		{
			println("Exceção ao tentar enviar o Socket: " + e.getMessage());
		}
	}

	void println(String message)
	{
		System.out.println("[" + id + "] " + message);
	}
}