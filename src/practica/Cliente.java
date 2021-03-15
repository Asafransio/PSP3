package practica;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.TextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.awt.event.ActionEvent;


public class Cliente extends JFrame {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 1L;
	JPanel contentPane;
	JTextField textField;
	static JLabel lblNewLabel;
	DataOutputStream fsalida;
	DataInputStream fentrada;
	boolean repetir = true;
	Socket socket;
	String nombre;
	TextArea textArea;
	

	/**
	 * Create the frame.
	 */
	public Cliente(Socket socket, String nombre) {
		
		this.socket = socket;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 42, 246, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 11, 246, 20);
		contentPane.add(lblNewLabel);
		
		Button button = new Button("Enviar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (1<=Integer.parseInt(textField.getText()) && Integer.parseInt(textField.getText())<=100) {
					String texto = lblNewLabel.getText() + "> " + textField.getText();
					try {
						textField.setText("");
						fsalida.writeUTF(texto);
						ejecutar();
						
					}
					catch (IOException ex){
						ex.printStackTrace();
					}
				}
				else {
					
				}
			}
		});
		button.setBounds(311, 9, 100, 22);
		contentPane.add(button);
		
		Button button_1 = new Button("Desconectar");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String texto = "SERVIDOR> "+nombre+" abandona la partida.";
				try
				{
					
					fsalida.writeUTF(texto);
					fsalida.writeUTF("*");
				
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
				System.exit(0);
			}
		});
		button_1.setBounds(311, 40, 100, 22);
		contentPane.add(button_1);
		
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 78, 414, 173);
		contentPane.add(textArea);
		
		try
		{
			fentrada = new DataInputStream(socket.getInputStream());
			fsalida = new DataOutputStream(socket.getOutputStream());
			String texto = "SERVIDOR> "+nombre+" entra en la partida.";
			fsalida.writeUTF(texto);
		}
		catch (IOException ex)
		{
			System.out.println("Error de E/S");
			ex.printStackTrace();
			System.exit(0);
		}
		
	}
	
	public void ejecutar()
	{
		String texto = "";
		while(repetir)
		{

			try 
			{
				texto = fentrada.readUTF();
				textArea.setText(texto);
			}
			catch (IOException ex)
			{
				JOptionPane.showMessageDialog(null, "Imposible conectar con el servidor \n" + ex.getMessage(), "<<Mensaje de Error:2>>", JOptionPane.ERROR_MESSAGE);
				repetir = false;
			}
		}
		try
		{
			socket.close();
			System.exit(0);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
}
