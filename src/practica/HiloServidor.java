package practica;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class HiloServidor extends Thread
{
	DataInputStream fentrada;
	Socket socket;
	static boolean fin = false;
	DataOutputStream fsalida;
	Random rng = new Random();

	public HiloServidor(Socket socket)
	{
		this.socket = socket;
		try
		{
			fentrada = new DataInputStream(socket.getInputStream());
			fsalida = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException e)
		{
			System.out.println("Error de E/S");
			e.printStackTrace();
		}
	}
	// En el m�todo run() lo primero que hacemos
	// es enviar todos los mensajes actuales al cliente que se
	// acaba de incorporar
	public void run()
	{
		Servidor.mensaje.setText("N�mero de conexiones actuales: " +
				Servidor.ACTUALES);
		String texto = Servidor.textarea.getText();
		EnviarMensajes(texto);
		// Seguidamente, se crea un bucle en el que se recibe lo que el cliente escribe en el chat.
		// Cuando un cliente finaliza con el bot�n Salir, se env�a un * al servidor del Chat,
		// entonces se sale del bucle while, ya que termina el proceso del cliente,
		// de esta manera se controlan las conexiones actuales
		while(!fin)
		{
			String cadena = "";
			try
			{
				cadena = fentrada.readUTF();
				if(cadena.trim().equals("*"))
				{
					Servidor.ACTUALES--;
					Servidor.mensaje.setText("N�mero de conexiones actuales: "
							+ Servidor.ACTUALES);
					fin=true;
					socket.close();
				}
				// El texto que el cliente escribe en el chat,
				// se a�ade al textarea del servidor y se reenv�a a todos los clientes
				else
				{
					Servidor.textarea.append(cadena + "\n");
					
					if(fin!=true) {
						try {
							int num = Integer.parseInt(cadena.split("> ")[1]);
							String jugador = cadena.split("> ")[0];
							if (num == Servidor.premio) {
								Servidor.textarea.append("SERVIDOR> " + jugador + 
										" piensa que el n�mero es el " + num + ". "
										+ "\nSERVIDOR> Y HA ACERTADOOOO!!!!" 
										+ "\nSERVIDOR> Fin de la partida. \n");
								
								EnviarMensajes(texto);
								
								fin = true;
							} else if (num > 100 || num < 1) {
								texto="SERVIDOR> " + jugador + " piensa que el n�mero es el " 
										+ num + ".\nSERVIDOR> El n�mero puede ser cualquiera entra 1 y 100.\n";
								Servidor.textarea.append(texto);
								EnviarMensajes(texto);
								
							} else if (num > Servidor.premio) {
								texto="SERVIDOR> " + jugador + " piensa que el n�mero es el " 
										+ num + ".\nSERVIDOR> El n�mero es menor a " + num + ".\n";
								Servidor.textarea.append(texto);
								EnviarMensajes(texto);
							} else if (num < Servidor.premio) {
								texto="SERVIDOR> " + jugador + " piensa que el n�mero es el " 
										+ num + ".\nSERVIDOR> El n�mero es mayor a " + num + ".\n";
								Servidor.textarea.append(texto);
								EnviarMensajes(texto);
							}
						} catch (NumberFormatException excepcion) {
							if(cadena.contains("SERVIDOR>")==false) {
								Servidor.textarea.append("SERVIDOR> ���ERROR!!! S�lo es posible introducir n�meros.\n");
							}
						}
						texto = Servidor.textarea.getText();
						EnviarMensajes(texto);
				}
			}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				fin=true;
			}
		}
	
	}
	// El m�todo EnviarMensajes() env�a el texto del textarea a
	// todos los sockets que est�n en la tabla de sockets,
	// de esta forma todos ven la conversaci�n.
	// El programa abre un stream de salida para escribir el texto en el socket
	private void EnviarMensajes(String texto)
	{
		for(int i=0; i<Servidor.CONEXIONES; i++)
		{
			Socket socket = Servidor.tabla[i];
			try
			{
				fsalida = new DataOutputStream(socket.getOutputStream());
				fsalida.writeUTF(texto);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}