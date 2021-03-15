package practica;


import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;


import java.awt.Font;

public class ClienteLogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static JTextField textField0;
	public Cliente cliente;
	
	
	Socket socket;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteLogin frame = new ClienteLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClienteLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 226, 137);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField0 = new JTextField();
		textField0.setBounds(10, 30, 188, 23);
		contentPane.add(textField0);
		textField0.setColumns(10);
		
		JButton btnNewButton = new JButton("Aceptar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField0.getText().equals("")) {
					Label err = new Label("Debe introducir un nombre");
					JDialog aviso = new JDialog();
					aviso.setSize(200, 80);
					aviso.setTitle("ERROR");
					aviso.getContentPane().setLayout(new FlowLayout());
					aviso.getContentPane().add(err);
					aviso.setLocationRelativeTo(null);
					aviso.setResizable(false);
					aviso.setVisible(true);
					
					
				}
				else {
					int puerto = 44444;
					try {
						socket = new Socket("localhost", puerto);
					} catch (UnknownHostException e1) {
						
						e1.printStackTrace();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					String nombre = textField0.getText();
					cliente = new Cliente(socket, nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase());
					cliente.setVisible(true);
					Cliente.lblNewLabel.setText(nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase());
					setVisible(false);
					cliente.ejecutar();
				}
			}
		});
		btnNewButton.setBounds(10, 64, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Salir");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton_1.setBounds(109, 64, 89, 23);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel = new JLabel("Introduce tu nombre");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(10, 5, 188, 23);
		contentPane.add(lblNewLabel);
		
	}
	
}