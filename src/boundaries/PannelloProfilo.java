package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import control.ControllerUtente;
import entity.CampoProfilo;
import entity.Utente;

public class PannelloProfilo extends JPanel {

	private static final long serialVersionUID = 1L;

	private ControllerUtente controllerUtente;

	private JLabel etichettaTitolo;
	private JLabel etichettaUsername;
	private JLabel etichettaNome;
	private JLabel etichettaCognome;
	private JLabel etichettaEmail;
	private JButton btnModificaUsername;
	private JButton btnModificaNome;
	private JButton btnModificaCognome;
	private JButton btnModificaEmail;
	private JButton btnModificaPassword;

	
	public PannelloProfilo() {
		this(null);
	}

	public PannelloProfilo(ControllerUtente controllerUtente) {
		this.controllerUtente = controllerUtente;
		inizializzaGrafica();
		inizializzaEventi();
		caricaDatiUtente();
	}

	private void inizializzaGrafica() {
		setBackground(Color.WHITE);
		setBounds(0, 0, 740, 518);
		setLayout(null);

		etichettaTitolo = new JLabel("Il mio profilo");
		etichettaTitolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaTitolo.setBounds(24, 20, 300, 32);
		add(etichettaTitolo);

		etichettaUsername = new JLabel("<html><b>Username:</b> </html>");
		etichettaUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		etichettaUsername.setBounds(24, 80, 420, 26);
		add(etichettaUsername);

		btnModificaUsername = new JButton("Modifica");
		btnModificaUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnModificaUsername.setBackground(new Color(233, 236, 239));
		btnModificaUsername.setFocusPainted(false);
		btnModificaUsername.setBounds(464, 76, 150, 32);
		add(btnModificaUsername);

		etichettaNome = new JLabel("<html><b>Nome:</b> </html>");
		etichettaNome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		etichettaNome.setBounds(24, 124, 420, 26);
		add(etichettaNome);

		btnModificaNome = new JButton("Modifica");
		btnModificaNome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnModificaNome.setBackground(new Color(233, 236, 239));
		btnModificaNome.setFocusPainted(false);
		btnModificaNome.setBounds(464, 120, 150, 32);
		add(btnModificaNome);

		etichettaCognome = new JLabel("<html><b>Cognome:</b> </html>");
		etichettaCognome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		etichettaCognome.setBounds(24, 168, 420, 26);
		add(etichettaCognome);

		btnModificaCognome = new JButton("Modifica");
		btnModificaCognome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnModificaCognome.setBackground(new Color(233, 236, 239));
		btnModificaCognome.setFocusPainted(false);
		btnModificaCognome.setBounds(464, 164, 150, 32);
		add(btnModificaCognome);

		etichettaEmail = new JLabel("<html><b>Email:</b> </html>");
		etichettaEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		etichettaEmail.setBounds(24, 212, 420, 26);
		add(etichettaEmail);

		btnModificaEmail = new JButton("Modifica");
		btnModificaEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnModificaEmail.setBackground(new Color(233, 236, 239));
		btnModificaEmail.setFocusPainted(false);
		btnModificaEmail.setBounds(464, 208, 150, 32);
		add(btnModificaEmail);

		btnModificaPassword = new JButton("Cambia password");
		btnModificaPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnModificaPassword.setBackground(new Color(233, 236, 239));
		btnModificaPassword.setFocusPainted(false);
		btnModificaPassword.setBounds(24, 256, 220, 36);
		add(btnModificaPassword);
	}

	private void inizializzaEventi() {
		btnModificaUsername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogModifica(CampoProfilo.USERNAME);
			}
		});

		btnModificaNome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogModifica(CampoProfilo.NOME);
			}
		});

		btnModificaCognome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogModifica(CampoProfilo.COGNOME);
			}
		});

		btnModificaEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogModifica(CampoProfilo.EMAIL);
			}
		});

		btnModificaPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogModifica(CampoProfilo.PASSWORD);
			}
		});
	}

	private void apriDialogModifica(CampoProfilo campo) {
		FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities.getWindowAncestor(this);
		DialogModificaDatoProfilo dialog = new DialogModificaDatoProfilo(finestra, controllerUtente, campo);
		dialog.setVisible(true);
		if (dialog.isModificaRiuscita()) {
			caricaDatiUtente();
		}
	}

	public void caricaDatiUtente() {
		Utente utente = (controllerUtente == null) ? null : controllerUtente.getUtenteInSessione();
		if (utente == null) {
			return;
		}

		etichettaUsername.setText("<html><b>Username:</b> " + testo(utente.getUsername()) + "</html>");
		etichettaNome.setText("<html><b>Nome:</b> " + testo(utente.getNome()) + "</html>");
		etichettaCognome.setText("<html><b>Cognome:</b> " + testo(utente.getCognome()) + "</html>");
		etichettaEmail.setText("<html><b>Email:</b> " + testo(utente.getEmail()) + "</html>");
	}

	private String testo(String valore) {
		return (valore == null) ? "" : valore;
	}
}
