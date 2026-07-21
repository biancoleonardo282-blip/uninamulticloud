package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.ControllerUtente;

public class DialogLogin extends JDialog {

	private static final long serialVersionUID = 1L;

	private ControllerUtente controllerUtente;
	private boolean loginRiuscito;

	private JPanel contentPanel;
	private JTextField campoUsername;
	private JPasswordField campoPassword;
	private JButton btnLogIn;
	private JButton btnAnnulla;
	private JLabel etichettaErrore;


	public DialogLogin() {
		this(null, null);
	}

	public DialogLogin(JFrame padre, ControllerUtente controllerUtente) {
		super(padre, "Accedi", true);
		this.controllerUtente = controllerUtente;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 476, 357);
		setLocationRelativeTo(getParent());
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		JLabel etichettaUsername = new JLabel("Username");
		etichettaUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaUsername.setBounds(30, 20, 200, 20);
		contentPanel.add(etichettaUsername);

		campoUsername = new JTextField();
		campoUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoUsername.setColumns(10);
		campoUsername.setBounds(30, 44, 400, 36);
		contentPanel.add(campoUsername);

		JLabel etichettaPassword = new JLabel("Password");
		etichettaPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaPassword.setBounds(30, 92, 200, 20);
		contentPanel.add(etichettaPassword);

		campoPassword = new JPasswordField();
		campoPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoPassword.setBounds(30, 116, 400, 36);
		contentPanel.add(campoPassword);

		etichettaErrore = new JLabel("");
		etichettaErrore.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaErrore.setForeground(new Color(220, 53, 69));
		etichettaErrore.setBounds(30, 160, 400, 20);
		contentPanel.add(etichettaErrore);

		btnLogIn = new JButton("Accedi");
		btnLogIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnLogIn.setBackground(new Color(13, 110, 253));
		btnLogIn.setForeground(Color.WHITE);
		btnLogIn.setFocusPainted(false);
		btnLogIn.setBounds(30, 190, 250, 42);
		contentPanel.add(btnLogIn);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(292, 190, 138, 42);
		contentPanel.add(btnAnnulla);

		getRootPane().setDefaultButton(btnLogIn);
	}

	private void inizializzaEventi() {
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciLogin();
			}
		});

		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginRiuscito = false;
				dispose();
			}
		});
	}

	private void gestisciLogin() {
		String username = campoUsername.getText().trim();
		String password = new String(campoPassword.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			mostraErrore("Compila username e password");
			return;
		}
		if (controllerUtente == null) {
			mostraErrore("Controller non disponibile");
			return;
		}
		if (controllerUtente.effettuaLogin(username, password)) {
			loginRiuscito = true;
			dispose();
		} else {
			loginRiuscito = false;
			mostraErrore("Username o password non corretti");
			campoPassword.setText("");
		}
	}

	public void svuotaCampi() {
		campoUsername.setText("");
		campoPassword.setText("");
		etichettaErrore.setText("");
	}

	public void mostraErrore(String messaggio) {
		etichettaErrore.setText(messaggio);
	}

	public boolean isLoginRiuscito() {
		return loginRiuscito;
	}
}
