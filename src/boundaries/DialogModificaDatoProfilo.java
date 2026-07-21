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
import entity.CampoProfilo;
import entity.Utente;

public class DialogModificaDatoProfilo extends JDialog {

	private static final long serialVersionUID = 1L;

	private ControllerUtente controllerUtente;
	private CampoProfilo campoModificato;
	private boolean modificaRiuscita;

	private JPanel contentPanel;
	private JLabel etichettaCampo;
	private JTextField campoNuovoValore;
	private JPasswordField campoNuovaPassword;
	private JButton btnSalva;
	private JButton btnAnnulla;
	private JLabel etichettaErrore;

	
	public DialogModificaDatoProfilo() {
		this(null, null, CampoProfilo.USERNAME);
	}

	public DialogModificaDatoProfilo(JFrame padre, ControllerUtente controllerUtente, CampoProfilo campo) {
		super(padre, "Modifica dato profilo", true);
		this.controllerUtente = controllerUtente;
		this.campoModificato = campo;
		inizializzaGrafica();
		inizializzaEventi();
		precompilaCampo();
	}

	private void inizializzaGrafica() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 496, 369);
		setLocationRelativeTo(getParent());
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		etichettaCampo = new JLabel("Nuovo valore");
		etichettaCampo.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaCampo.setBounds(30, 18, 420, 22);
		contentPanel.add(etichettaCampo);

		campoNuovoValore = new JTextField();
		campoNuovoValore.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoNuovoValore.setColumns(10);
		campoNuovoValore.setBounds(30, 46, 420, 34);
		contentPanel.add(campoNuovoValore);

		JLabel etichettaConferma = new JLabel("Password attuale (per conferma)");
		etichettaConferma.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		etichettaConferma.setBounds(30, 90, 420, 18);
		contentPanel.add(etichettaConferma);

		campoNuovaPassword = new JPasswordField();
		campoNuovaPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoNuovaPassword.setBounds(30, 112, 420, 34);
		contentPanel.add(campoNuovaPassword);

		etichettaErrore = new JLabel("");
		etichettaErrore.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaErrore.setForeground(new Color(220, 53, 69));
		etichettaErrore.setBounds(30, 154, 420, 20);
		contentPanel.add(etichettaErrore);

		btnSalva = new JButton("Salva");
		btnSalva.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnSalva.setBackground(new Color(13, 110, 253));
		btnSalva.setForeground(Color.WHITE);
		btnSalva.setFocusPainted(false);
		btnSalva.setBounds(30, 182, 280, 40);
		contentPanel.add(btnSalva);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(322, 182, 128, 40);
		contentPanel.add(btnAnnulla);

		getRootPane().setDefaultButton(btnSalva);
	}

	private void inizializzaEventi() {
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciSalvataggio();
			}
		});

		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modificaRiuscita = false;
				dispose();
			}
		});
	}

	private void precompilaCampo() {
		if (campoModificato != null) {
			etichettaCampo.setText(campoModificato.getEtichetta());
		}
		Utente utente = (controllerUtente == null) ? null : controllerUtente.getUtenteInSessione();
		if (utente == null || campoModificato == null) {
			return;
		}

		switch (campoModificato) {
			case USERNAME:
				campoNuovoValore.setText(utente.getUsername());
				break;
			case NOME:
				campoNuovoValore.setText(utente.getNome());
				break;
			case COGNOME:
				campoNuovoValore.setText(utente.getCognome());
				break;
			case EMAIL:
				campoNuovoValore.setText(utente.getEmail());
				break;
			case PASSWORD:
				campoNuovoValore.setText("");
				break;
			default:
				break;
		}
	}

	private void gestisciSalvataggio() {
		if (controllerUtente == null || controllerUtente.getUtenteInSessione() == null) {
			etichettaErrore.setText("Sessione non disponibile");
			return;
		}
		String nuovoValore = campoNuovoValore.getText().trim();
		if (nuovoValore.isEmpty()) {
			etichettaErrore.setText("Il nuovo valore non puo' essere vuoto");
			return;
		}

		String passwordAttuale = new String(campoNuovaPassword.getPassword());
		Utente utente = controllerUtente.getUtenteInSessione();
		if (utente.getPassword() != null && !utente.getPassword().equals(passwordAttuale)) {
			etichettaErrore.setText("Password attuale non corretta");
			return;
		}
		if (campoModificato == CampoProfilo.EMAIL && !controllerUtente.formatoEmailValido(nuovoValore)) {
			etichettaErrore.setText("Indirizzo email non valido");
			return;
		}

		if (controllerUtente.aggiornaDatoProfilo(campoModificato, nuovoValore)) {
			modificaRiuscita = true;
			dispose();
		} else {
			modificaRiuscita = false;
			if (campoModificato == CampoProfilo.USERNAME) {
				etichettaErrore.setText("Username gia' in uso");
			} else if (campoModificato == CampoProfilo.EMAIL) {
				etichettaErrore.setText("Email gia' in uso");
			} else {
				etichettaErrore.setText("Aggiornamento non riuscito");
			}
		}
	}

	public boolean isModificaRiuscita() {
		return modificaRiuscita;
	}
}
