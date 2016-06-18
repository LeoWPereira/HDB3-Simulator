/*
 * HDB3 Encoder
 * @author: Leonardo Winter Pereira
 * @authot: Caio Cesar
 * @author: Mauro Vialich
 * 
 * Function. Last modified: 18.06.2016
 */

package clienteEServidor;

public interface Function<T>
{	
	public boolean apply(T object);
}