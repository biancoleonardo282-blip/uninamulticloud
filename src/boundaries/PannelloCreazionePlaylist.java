package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import control.ControllerPlaylist;
import entity.TipoPlaylist;

public class PannelloCreazionePlaylist extends JPanel {

	private static final long serialVersionUID = 1L;

	private ControllerPlaylist controllerPlaylist;
	private FinestraPrincipale finestraPrincipale;

	private JLabel etichettaNomePlaylist;
	private JTextField campoNomePlaylist;
	private JLabel etichettaTipo;
	private JComboBox<TipoPlaylist> comboTipo;
	private JLabel etichettaDescrizione;
	private JTextArea campoDescrizione;
	private JLabel etichettaTagRicerca;
	private JTextField campoTagRicerca;
	private JButton btnCrea;
	private JButton btnAnnulla;
	private JLabel etichettaErrore;

	
	public PannelloCreazionePlaylist() {
		this(null, null);
	}

	public PannelloCreazionePlaylist(ControllerPlaylist controllerPlaylist, FinestraPrincipale finestraPrincipale) {
		this.controllerPlaylist = controllerPlaylist;
		this.finestraPrincipale = finestraPrincipale;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setBackground(Color.WHITE);
		setBounds(0, 0, 740, 518);
		setLayout(null);

		JLabel etichettaTitolo = new JLabel("Crea una nuova playlist");
		etichettaTitolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaTitolo.setBounds(24, 20, 400, 32);
		add(etichettaTitolo);

		etichettaNomePlaylist = new JLabel("Nome");
		etichettaNomePlaylist.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaNomePlaylist.setBounds(24, 68, 200, 20);
		add(etichettaNomePlaylist);

		campoNomePlaylist = new JTextField();
		campoNomePlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoNomePlaylist.setColumns(10);
		campoNomePlaylist.setBounds(24, 90, 480, 34);
		add(campoNomePlaylist);

		etichettaTipo = new JLabel("Visibilita'");
		etichettaTipo.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaTipo.setBounds(24, 136, 200, 20);
		add(etichettaTipo);

		comboTipo = new JComboBox<TipoPlaylist>();
		comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		comboTipo.setBackground(Color.WHITE);
		for (TipoPlaylist tipo : TipoPlaylist.values()) {
			comboTipo.addItem(tipo);
		}
		comboTipo.setBounds(24, 158, 300, 34);
		add(comboTipo);

		etichettaDescrizione = new JLabel("Descrizione");
		etichettaDescrizione.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaDescrizione.setBounds(24, 204, 200, 20);
		add(etichettaDescrizione);

		campoDescrizione = new JTextArea();
		campoDescrizione.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoDescrizione.setLineWrap(true);
		campoDescrizione.setWrapStyleWord(true);

		JScrollPane scorrimentoDescrizione = new JScrollPane(campoDescrizione);
		scorrimentoDescrizione.setBounds(24, 226, 480, 80);
		add(scorrimentoDescrizione);

		etichettaTagRicerca = new JLabel("Tag di ricerca");
		etichettaTagRicerca.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaTagRicerca.setBounds(24, 318, 260, 20);
		add(etichettaTagRicerca);

		campoTagRicerca = new JTextField();
		campoTagRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoTagRicerca.setColumns(10);
		campoTagRicerca.setBounds(24, 340, 480, 34);
		add(campoTagRicerca);

		etichettaErrore = new JLabel("");
		etichettaErrore.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaErrore.setForeground(new Color(220, 53, 69));
		etichettaErrore.setBounds(24, 384, 480, 20);
		add(etichettaErrore);

		btnCrea = new JButton("Crea playlist");
		btnCrea.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCrea.setBackground(new Color(13, 110, 253));
		btnCrea.setForeground(Color.WHITE);
		btnCrea.setFocusPainted(false);
		btnCrea.setBounds(24, 412, 300, 42);
		add(btnCrea);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(336, 412, 168, 42);
		add(btnAnnulla);
	}

	private void inizializzaEventi() {
		btnCrea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciCreazione();
			}
		});

		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				svuotaCampi();
				if (finestraPrincipale != null) {
					finestraPrincipale.cambiaSchermataCentrale(FinestraPrincipale.CARTA_PLAYLIST);
				}
			}
		});
	}

	private void gestisciCreazione() {
		if (!validaCampi() || controllerPlaylist == null) {
			return;
		}
		TipoPlaylist tipo = (TipoPlaylist) comboTipo.getSelectedItem();

		boolean esito = controllerPlaylist.creaNuovaPlaylist(campoNomePlaylist.getText().trim(),
				campoDescrizione.getText().trim(), tipo, campoTagRicerca.getText().trim());

		if (esito) {
			svuotaCampi();
			if (finestraPrincipale != null) {
				finestraPrincipale.getPannelloPlaylist().mostraLeMiePlaylist();
				finestraPrincipale.cambiaSchermataCentrale(FinestraPrincipale.CARTA_PLAYLIST);
			}
		} else {
			etichettaErrore.setText("Creazione della playlist non riuscita");
		}
	}

	private boolean validaCampi() {
		if (campoNomePlaylist.getText().trim().isEmpty()) {
			etichettaErrore.setText("Il nome della playlist e' obbligatorio");
			return false;
		}
		if (comboTipo.getSelectedItem() == TipoPlaylist.PUBBLICA && campoTagRicerca.getText().trim().isEmpty()) {
			etichettaErrore.setText("Le playlist pubbliche richiedono un tag di ricerca");
			return false;
		}
		etichettaErrore.setText("");
		return true;
	}

	public void svuotaCampi() {
		campoNomePlaylist.setText("");
		campoDescrizione.setText("");
		campoTagRicerca.setText("");
		comboTipo.setSelectedItem(TipoPlaylist.PRIVATA);
		etichettaErrore.setText("");
	}
}
