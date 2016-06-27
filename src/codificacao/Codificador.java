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

// Classe básica que (de)codifica uma determinada mensagem, além de algumas outras funções extras
public final class Codificador
{
	public static final String 	ID 			= "HDB3";
	public static boolean		DEBUG;
	
	private Codificador() { }

	public static String codifica(String message)
	{
		println("Gostaria de verificar a codificação passo-a-passo? ['true' para sim ou 'false' para não]");
		
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
			println("Entrada desconhecida. Passo-a-passo não será realizado");
			
			DEBUG = false;
		}
		
		if (DEBUG)
		{
			println("Início da codificação\n\n");
			println("Etapa de busca por 0000\n");
		}
			
		
		String hdb3stream = message;

		int index0 = hdb3stream.indexOf("0000"); // Procura pela primeira sequência 0000
		
		int index1 = 0;

		// Testa até o final da String por 0000
		while (index0 != -1)
		{
			if ((index0 - index1) % 2 == 1) // Se a operação resultar em 1
			{
				hdb3stream = hdb3stream.substring(0, index0) + "000V" + hdb3stream.substring(index0 + 4);
				
				if (DEBUG)
				{
					println("Encontrada sequência 0000 ímpar. Será substituído por '" + "000V"  + "'");
					println(hdb3stream + "\n");
				}
				
			}
			
			else // Se a operação resultar em 0
			{
				hdb3stream = hdb3stream.substring(0, index0) + "B00V" + hdb3stream.substring(index0 + 4);
				
				if (DEBUG)
				{
					println("Encontrada sequência 0000 par. Será substituído por '" + "B00V"  + "'");
					println(hdb3stream + "\n");
				}
			}
			
			index1 = index0 + 4; // index1 recebe um novo index

			index0 = hdb3stream.indexOf("0000"); // index0 recebe a posição da última sequência
		}
		
		if (DEBUG)
			println("Fim da mensagem. Início da próxima etapa\n");

		int signal = 0;

		char last1bit = '0';
		char lastbit = '0';

		for (int pos = 0; pos < message.length(); pos++)
		{
			if (hdb3stream.charAt(pos) == '1')
			{
				if (signal % 2 == 0) // Se a operação resultar em 0
					last1bit = '+';
				
				else // Caso contrário
					last1bit = '-';

				lastbit = last1bit;
				
				hdb3stream = hdb3stream.substring(0, pos) + lastbit + hdb3stream.substring(pos + 1);
				
				signal++;
				
				if (DEBUG)
				{
					println("Encontrado valor '1'. Será substituído por '" + lastbit + "'");
					println(hdb3stream + "\n");
				}
			}
			
			else if (hdb3stream.charAt(pos) == 'V')
			{
				hdb3stream = hdb3stream.substring(0, pos) + lastbit + hdb3stream.substring(pos + 1);
				
				if (DEBUG)
				{
					println("Encontrado valor 'V'. Será substituído por '" + lastbit + "'");
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
					println("Encontrado valor 'B'. Será substituído por '" + lastbit + "'");
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