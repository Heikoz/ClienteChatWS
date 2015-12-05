package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JPasswordField;

import br.com.restful.bean.Cliente;
import br.com.restful.bean.Mensagem;
import br.com.restful.desktop.app.ClienteWS;

import java.awt.FlowLayout;
import javax.swing.SwingConstants;


public class MainWindow extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField txtUserName;
	private JButton btnConectar = new JButton();
	private JLabel lblNoConectado = new JLabel();
	private JTextArea textConsole = new JTextArea();
	private JTextArea txtAreaMsgs = new JTextArea();
	private JTextField txtMsg;
	private JButton btnEnviar;
	private JPasswordField txtPassword = new JPasswordField();
	private ClienteWS cws;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the this.
	 */
	private void initialize() {
		this.setBounds(100, 100, 543, 418);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		this.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNome = new JLabel("User: ");
		panel.add(lblNome);

		txtUserName = new JTextField();
		panel.add(txtUserName);
		txtUserName.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		panel.add(lblSenha);

		txtPassword = new JPasswordField();
		panel.add(txtPassword);
		txtPassword.setColumns(10);

		btnConectar = new JButton("Conectar");
		panel.add(btnConectar);
		btnConectar.addActionListener(this);

		lblNoConectado = new JLabel("Não conectado   ");
		panel.add(lblNoConectado);
		lblNoConectado.setFont(new Font("Arial", Font.PLAIN, 11));
		lblNoConectado.setForeground(Color.RED);

		JPanel panel_1 = new JPanel();
		this.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		textConsole = new JTextArea();
		panel_1.add(textConsole);

		JPanel panel_5 = new JPanel();
		getContentPane().add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));

		txtAreaMsgs = new JTextArea();
		txtAreaMsgs.setEditable(false);
		panel_5.add(txtAreaMsgs);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Mensagem:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		txtMsg = new JTextField();
		txtMsg.setToolTipText("Para enviar uma mensagem voc\u00EA precisa estar conectado ao servidor.");
		txtMsg.setHorizontalAlignment(SwingConstants.LEFT);
		txtMsg.setText("\"Digite aqui sua mensagem.\"");
		txtMsg.setEditable(false);
		panel_4.add(txtMsg);
		txtMsg.setColumns(10);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setEnabled(false);
		btnEnviar.addActionListener(this);
		panel_4.add(btnEnviar, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() ==  btnConectar){
			if (btnConectar.getText().equals("Conectar")){
				if (txtUserName.getText().isEmpty()){
					textConsole.setText("Entre com um nome para tentar se conectar ao servidor.");
				}
				else{
					String aux = new String(txtPassword.getPassword());
					if (aux.isEmpty())
						textConsole.setText("Entre com uma senha para tentar se conectar ao servidor.");
					else{
						textConsole.setText("");
						cws = new ClienteWS();

						if (cws.connect(new Cliente(txtUserName.getText(), aux)))
							setConnected(true);
						else{
							textConsole.setText("Não foi possível se conectar com o servidor.");
						}
					}
				}
			}else
				setConnected(false);

		}else
			if (e.getSource() == btnEnviar){
				if (txtMsg.getText().isEmpty() || txtMsg.getText().equals(""))
					textConsole.setText("Entre com um texto para enviar a mensagem.");
				else
					cws.sendMessage(txtMsg.getText());
			}
	}

	private void setConnected(boolean b) {
		if (b){
			btnConectar.setText("Desconectar");
			lblNoConectado.setText("Conectado");
			lblNoConectado.setFont(new Font("Arial", Font.PLAIN, 11));
			lblNoConectado.setForeground(Color.GREEN);
			btnEnviar.setEnabled(true);
			txtMsg.setEnabled(true);
		}else{
			txtMsg.setEnabled(false);
			btnEnviar.setEnabled(false);
			btnConectar.setText("Conectar");
			lblNoConectado.setText("Desconectado");
			lblNoConectado.setFont(new Font("Arial", Font.PLAIN, 11));
			lblNoConectado.setForeground(Color.RED);
		}

	}

	public void atualiza(){
		this.repaint();
	}

	public JTextArea getTxtAreaMsgs() {
		return txtAreaMsgs;
	}

	public JTextArea getTextConsole() {
		return textConsole;
	}

	public String getNomeCliente() {
		return txtUserName.getText();
	}
}
