package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.ControllerUtente;

public class DialogSignUp extends JDialog {

	private static final long serialVersionUID = 1L;

	private ControllerUtente controllerUtente;

	private JPanel contentPanel;
	private JTextField campoUsername;
	private JPasswordField campoPassword;
	private JPasswordField campoConfermaPassword;
	private JTextField campoNome;
	private JTextField campoCognome;
	private JTextField campoEmail;
	private JButton btnSignUp;
	private JButton btnAnnulla;
	private JLabel etichettaErrore;


	public DialogSignUp() {
		this(null, null);
	}

	public DialogSignUp(JFrame padre, ControllerUtente controllerUtente) {
		super(padre, "Crea un account", true);
		this.controllerUtente = controllerUtente;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 536, 477);
		setLocationRelativeTo(getParent());
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		JLabel etichettaUsername = new JLabel("Username");
		etichettaUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaUsername.setBounds(30, 14, 200, 18);
		contentPanel.add(etichettaUsername);

		campoUsername = new JTextField();
		campoUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoUsername.setColumns(10);
		campoUsername.setBounds(30, 34, 460, 34);
		contentPanel.add(campoUsername);

		JLabel etichettaPassword = new JLabel("Password");
		etichettaPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaPassword.setBounds(30, 76, 220, 18);
		contentPanel.add(etichettaPassword);

		campoPassword = new JPasswordField();
		campoPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoPassword.setBounds(30, 96, 222, 34);
		contentPanel.add(campoPassword);

		JLabel etichettaConfermaPassword = new JLabel("Conferma password");
		etichettaConfermaPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaConfermaPassword.setBounds(268, 76, 222, 18);
		contentPanel.add(etichettaConfermaPassword);

		campoConfermaPassword = new JPasswordField();
		campoConfermaPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoConfermaPassword.setBounds(268, 96, 222, 34);
		contentPanel.add(campoConfermaPassword);

		JLabel etichettaNome = new JLabel("Nome");
		etichettaNome.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaNome.setBounds(30, 138, 220, 18);
		contentPanel.add(etichettaNome);

		campoNome = new JTextField();
		campoNome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoNome.setColumns(10);
		campoNome.setBounds(30, 158, 222, 34);
		contentPanel.add(campoNome);

		JLabel etichettaCognome = new JLabel("Cognome");
		etichettaCognome.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaCognome.setBounds(268, 138, 220, 18);
		contentPanel.add(etichettaCognome);

		campoCognome = new JTextField();
		campoCognome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoCognome.setColumns(10);
		campoCognome.setBounds(268, 158, 222, 34);
		contentPanel.add(campoCognome);

		JLabel etichettaEmail = new JLabel("Email");
		etichettaEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaEmail.setBounds(30, 200, 300, 18);
		contentPanel.add(etichettaEmail);

		campoEmail = new JTextField();
		campoEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoEmail.setColumns(10);
		campoEmail.setBounds(30, 220, 460, 34);
		contentPanel.add(campoEmail);

		etichettaErrore = new JLabel("");
		etichettaErrore.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaErrore.setForeground(new Color(220, 53, 69));
		etichettaErrore.setBounds(30, 272, 460, 20);
		contentPanel.add(etichettaErrore);

		btnSignUp = new JButton("Registrati");
		btnSignUp.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnSignUp.setBackground(new Color(13, 110, 253));
		btnSignUp.setForeground(Color.WHITE);
		btnSignUp.setFocusPainted(false);
		btnSignUp.setBounds(30, 300, 300, 42);
		contentPanel.add(btnSignUp);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(342, 300, 148, 42);
		contentPanel.add(btnAnnulla);
	}

	private void inizializzaEventi() {
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciRegistrazione();
			}
		});

		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void gestisciRegistrazione() {
		if (!validaCampi()) {
			return;
		}
		if (controllerUtente == null) {
			mostraErrore("Controller non disponibile");
			return;
		}

		boolean esito = controllerUtente.registraUtente(campoUsername.getText().trim(),
				new String(campoPassword.getPassword()), campoNome.getText().trim(), campoCognome.getText().trim(),
				campoEmail.getText().trim());

		if (esito) {
			JOptionPane.showMessageDialog(this, "Registrazione completata. Ora puoi accedere.", "UninaMultiCloud",
					JOptionPane.INFORMATION_MESSAGE);
			svuotaCampi();
			dispose();
		} else {
			mostraErrore("Username o email gia' in uso oppure registrazione non riuscita");
		}
	}

	private boolean validaCampi() {
		if (campoUsername.getText().trim().isEmpty() || campoNome.getText().trim().isEmpty()
				|| campoCognome.getText().trim().isEmpty() || campoEmail.getText().trim().isEmpty()) {
			mostraErrore("Compila tutti i campi obbligatori");
			return false;
		}
		String password = new String(campoPassword.getPassword());
		String conferma = new String(campoConfermaPassword.getPassword());

		if (password.isEmpty()) {
			mostraErrore("La password non puo' essere vuota");
			return false;
		}
		if (!password.equals(conferma)) {
			mostraErrore("Le due password non coincidono");
			return false;
		}
		if (!campoEmail.getText().trim().matches(".+@.+\\..+")) {
			mostraErrore("Indirizzo email non valido");
			return false;
		}
		mostraErrore("");
		return true;
	}

	public void svuotaCampi() {
		campoUsername.setText("");
		campoPassword.setText("");
		campoConfermaPassword.setText("");
		campoNome.setText("");
		campoCognome.setText("");
		campoEmail.setText("");
		etichettaErrore.setText("");
	}

	public void mostraErrore(String messaggio) {
		etichettaErrore.setText(messaggio);
	}
}
