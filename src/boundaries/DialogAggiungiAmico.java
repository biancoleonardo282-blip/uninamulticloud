package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import control.ControllerUtente;
import entity.Utente;

public class DialogAggiungiAmico extends JDialog {

	private static final long serialVersionUID = 1L;

	private ControllerUtente controllerUtente;

	private JPanel contentPanel;
	private JTextField campoUsernameRicerca;
	private JButton btnCerca;
	private JList<Utente> listaRisultati;
	private DefaultListModel<Utente> modelloRisultati;
	private JButton btnConferma;
	private JButton btnAnnulla;
	private JLabel etichettaErrore;

	
	public DialogAggiungiAmico() {
		this(null, null);
	}

	public DialogAggiungiAmico(JFrame padre, ControllerUtente controllerUtente) {
		super(padre, "Aggiungi amico", true);
		this.controllerUtente = controllerUtente;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 496, 439);
		setLocationRelativeTo(getParent());
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		JLabel etichettaRicerca = new JLabel("Cerca per username");
		etichettaRicerca.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaRicerca.setBounds(30, 18, 300, 18);
		contentPanel.add(etichettaRicerca);

		campoUsernameRicerca = new JTextField();
		campoUsernameRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoUsernameRicerca.setColumns(10);
		campoUsernameRicerca.setBounds(30, 40, 300, 34);
		contentPanel.add(campoUsernameRicerca);

		btnCerca = new JButton("Cerca");
		btnCerca.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCerca.setBackground(new Color(13, 110, 253));
		btnCerca.setForeground(Color.WHITE);
		btnCerca.setFocusPainted(false);
		btnCerca.setBounds(342, 40, 108, 34);
		contentPanel.add(btnCerca);

		modelloRisultati = new DefaultListModel<Utente>();
		listaRisultati = new JList<Utente>(modelloRisultati);
		listaRisultati.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		listaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scorrimentoRisultati = new JScrollPane(listaRisultati);
		scorrimentoRisultati.setBounds(30, 88, 420, 130);
		contentPanel.add(scorrimentoRisultati);

		etichettaErrore = new JLabel("");
		etichettaErrore.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaErrore.setForeground(new Color(220, 53, 69));
		etichettaErrore.setBounds(30, 228, 420, 20);
		contentPanel.add(etichettaErrore);

		btnConferma = new JButton("Aggiungi");
		btnConferma.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnConferma.setBackground(new Color(13, 110, 253));
		btnConferma.setForeground(Color.WHITE);
		btnConferma.setFocusPainted(false);
		btnConferma.setBounds(30, 256, 280, 40);
		contentPanel.add(btnConferma);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(322, 256, 128, 40);
		contentPanel.add(btnAnnulla);
	}

	private void inizializzaEventi() {
		btnCerca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciRicerca();
			}
		});

		campoUsernameRicerca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciRicerca();
			}
		});

		btnConferma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciAggiunta();
			}
		});

		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void gestisciRicerca() {
		if (controllerUtente == null) {
			return;
		}
		String chiave = campoUsernameRicerca.getText().trim();
		if (chiave.isEmpty()) {
			etichettaErrore.setText("Inserisci un username da cercare");
			return;
		}
		etichettaErrore.setText("");
		mostraRisultati(controllerUtente.cercaUtenti(chiave));
	}

	private void mostraRisultati(List<Utente> risultati) {
		modelloRisultati.clear();
		if (risultati == null || risultati.isEmpty()) {
			etichettaErrore.setText("Nessun utente trovato");
			return;
		}
		for (Utente utente : risultati) {
			modelloRisultati.addElement(utente);
		}
	}

	private void gestisciAggiunta() {
		Utente selezionato = listaRisultati.getSelectedValue();
		if (selezionato == null) {
			etichettaErrore.setText("Seleziona un utente dalla lista");
			return;
		}
		if (controllerUtente == null) {
			return;
		}
		if (controllerUtente.aggiungiAmico(selezionato.getUsername())) {
			dispose();
		} else {
			etichettaErrore.setText("Utente gia' presente fra i tuoi amici");
		}
	}
}
