/*
 * HDB3 Encoder
 * @author: Leonardo Winter Pereira
 * @authot: Caio Cesar
 * @author: Mauro Vialich
 * 
 * Codificador. Last modified: 18.06.2016
 */

package codificacao;

import java.util.Scanner;

// Classe b�sica que (de)codifica uma determinada mensagem, al�m de algumas outras fun��es extras
public final class Codificador
{
	public static final String 	ID 			= "HDB3";
	public static boolean		DEBUG;
	
	private Codificador() { }

	public static String codifica(String message)
	{
		println("Gostaria de verificar a codifica��o passo-a-passo? ['true' para sim ou 'false' para n�o]");
		
		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		String	line;
		
		line = keyboard.nextLine();
		
		if (line.equalsIgnoreCase("true"))
			DEBUG = true;
		
		else if (line.equalsIgnoreCase("false"))
			DEBUG = false;
		
		else
		{
			println("Entrada desconhecida. Passo-a-passo n�o ser� realizado");
			
			DEBUG = false;
		}
		
		if (DEBUG)
		{
			println("In�cio da codifica��o\n\n");
			println("Etapa de busca por 0000\n");
		}
			
		
		String hdb3stream = message;

		int index0 = hdb3stream.indexOf("0000"); // Procura pela primeira sequ�ncia 0000
		
		int index1 = 0;

		// Testa at� o final da String por 0000
		while (index0 != -1)
		{
			if ((index0 - index1) % 2 == 1) // Se a opera��o resultar em 1
			{
				hdb3stream = hdb3stream.substring(0, index0) + "000V" + hdb3stream.substring(index0 + 4);
				
				if (DEBUG)
				{
					println("Encontrada sequ�ncia 0000 �mpar. Ser� substitu�do por '" + "000V"  + "'");
					println(hdb3stream + "\n");
				}
				
			}
			
			else // Se a opera��o resultar em 0
			{
				hdb3stream = hdb3stream.substring(0, index0) + "B00V" + hdb3stream.substring(index0 + 4);
				
				if (DEBUG)
				{
					println("Encontrada sequ�ncia 0000 par. Ser� substitu�do por '" + "B00V"  + "'");
					println(hdb3stream + "\n");
				}
			}
			
			index1 = index0 + 4; // index1 recebe um novo index

			index0 = hdb3stream.indexOf("0000"); // index0 recebe a posi��o da �ltima sequ�ncia
		}
		
		if (DEBUG)
			println("Fim da mensagem. In�cio da pr�xima etapa\n");

		int signal = 0;

		char last1bit = '0';
		char lastbit = '0';

		for (int pos = 0; pos < message.length(); pos++)
		{
			if (hdb3stream.charAt(pos) == '1')
			{
				if (signal % 2 == 0) // Se a opera��o resultar em 0
					last1bit = '+';
				
				else // Caso contr�rio
					last1bit = '-';

				lastbit = last1bit;
				
				hdb3stream = hdb3stream.substring(0, pos) + lastbit + hdb3stream.substring(pos + 1);
				
				signal++;
				
				if (DEBUG)
				{
					println("Encontrado valor '1'. Ser� substitu�do por '" + lastbit + "'");
					println(hdb3stream + "\n");
				}
			}
			
			else if (hdb3stream.charAt(pos) == 'V')
			{
				hdb3stream = hdb3stream.substring(0, pos) + lastbit + hdb3stream.substring(pos + 1);
				
				if (DEBUG)
				{
					println("Encontrado valor 'V'. Ser� substitu�do por '" + lastbit + "'");
					println(hdb3stream + "\n");
				}
			}
			
			else if (hdb3stream.charAt(pos) == 'B')
			{
				if (last1bit == '+')
					lastbit = '-';
				
				else
					lastbit = '+';
				
				signal++;
				
				hdb3stream = hdb3stream.substring(0, pos) + lastbit	+ hdb3stream.substring(pos + 1);
				
				if (DEBUG)
				{
					println("Encontrado valor 'B'. Ser� substitu�do por '" + lastbit + "'");
					println(hdb3stream + "\n");
				}
			}
		}
		return hdb3stream;
	}

	public static String decodifica(String data)
	{
		String message = data;

		char lastpo1 = '0';
		char tempc = '0';

		for (int pos = 0; pos < message.length(); pos++)
		{
			if (message.charAt(pos) != '0')
			{
				tempc = message.charAt(pos);

				if (lastpo1 == message.charAt(pos))
					message = message.substring(0, pos - 3) + "0000" + message.substring(pos + 1);
					
				lastpo1 = tempc;
			}
		}

		for (int pos = 0; pos < message.length(); pos++)
		{
			if (message.charAt(pos) != '0')
				message = message.substring(0, pos) + '1' + message.substring(pos + 1);
		}
		return message;
	}
	
	public static StringBuilder textToBin(String data)
	{
		byte[] bytes = data.getBytes();
		
		StringBuilder binary = new StringBuilder();
		
		for (byte b : bytes)
		{
			int val = b;
			
			for (int i = 0; i < 8; i++)
			{
				binary.append((val & 128) == 0 ? 0 : 1); // similar a -> if (val & 128) == 0) binary.append(0); else binary.append(1);
				val <<= 1;
			}
     
			//binary.append(' ');
		}
	  
		return binary;
	}

	public static String binToText(String data)
	{
		StringBuilder sb = new StringBuilder();
		
	    char[] chars = data.replaceAll("\\s", "").toCharArray();
	    
	    int [] mapping = {1,2,4,8,16,32,64,128};

	    for (int j = 0; j < chars.length; j+=8)
	    {
	        int idx = 0;
	        int sum = 0;
	        
	        for (int i = 7; i >= 0; i--)
	        {
	            if (chars[i + j] == '1')
	                sum += mapping[idx];
	            
	            idx++;
	        }
	        
	        sb.append(Character.toChars(sum));
	    }
	    return sb.toString();
	}
	
	static void println(String message)
	{
		System.out.println("[" + ID + "] " + message);
	}
}