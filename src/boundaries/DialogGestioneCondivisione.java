package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import control.ControllerPlaylist;
import entity.Condivisione;
import entity.Playlist;
import entity.Utente;

public class DialogGestioneCondivisione extends JDialog {

	private static final long serialVersionUID = 1L;

	private ControllerPlaylist controllerPlaylist;
	private Playlist playlistCorrente;

	private JPanel contentPanel;
	private JTextField campoCercaAmico;
	private JList<Utente> listaAmici;
	private DefaultListModel<Utente> modelloAmici;
	private JButton btnAggiungiCollaboratore;
	private JTable tabellaCollaboratori;
	private DefaultTableModel modelloTabellaCollaboratori;
	private JButton btnRimuoviCollaboratore;
	private JButton btnChiudi;

	
	private List<Condivisione> collaboratoriVisualizzati = new ArrayList<Condivisione>();


	public DialogGestioneCondivisione() {
		this(null, null, null);
	}

	public DialogGestioneCondivisione(FinestraPrincipale padre, ControllerPlaylist controllerPlaylist,
			Playlist playlist) {
		super(padre, "Gestione condivisione", true);
		this.controllerPlaylist = controllerPlaylist;
		this.playlistCorrente = playlist;
		inizializzaGrafica();
		inizializzaEventi();
		caricaAmiciDisponibili(padre);
		caricaCollaboratoriAttuali();
	}

	private void inizializzaGrafica() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 576, 519);
		setLocationRelativeTo(getParent());
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		JLabel etichettaAggiungi = new JLabel("Aggiungi un collaboratore");
		etichettaAggiungi.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaAggiungi.setBounds(30, 16, 400, 18);
		contentPanel.add(etichettaAggiungi);

		campoCercaAmico = new JTextField();
		campoCercaAmico.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoCercaAmico.setColumns(10);
		campoCercaAmico.setBounds(30, 38, 380, 34);
		contentPanel.add(campoCercaAmico);

		modelloAmici = new DefaultListModel<Utente>();
		listaAmici = new JList<Utente>(modelloAmici);
		listaAmici.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		listaAmici.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scorrimentoAmici = new JScrollPane(listaAmici);
		scorrimentoAmici.setBounds(30, 82, 500, 110);
		contentPanel.add(scorrimentoAmici);

		btnAggiungiCollaboratore = new JButton("Aggiungi");
		btnAggiungiCollaboratore.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnAggiungiCollaboratore.setBackground(new Color(13, 110, 253));
		btnAggiungiCollaboratore.setForeground(Color.WHITE);
		btnAggiungiCollaboratore.setFocusPainted(false);
		btnAggiungiCollaboratore.setBounds(340, 200, 190, 34);
		contentPanel.add(btnAggiungiCollaboratore);

		JLabel etichettaCollaboratori = new JLabel("Collaboratori attuali");
		etichettaCollaboratori.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaCollaboratori.setBounds(30, 244, 400, 18);
		contentPanel.add(etichettaCollaboratori);

		modelloTabellaCollaboratori = new DefaultTableModel(new Object[][] {}, new String[] { "Utente", "Link" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int riga, int colonna) {
				return false;
			}
		};

		tabellaCollaboratori = new JTable(modelloTabellaCollaboratori);
		tabellaCollaboratori.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tabellaCollaboratori.setRowHeight(28);
		tabellaCollaboratori.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scorrimentoTabella = new JScrollPane(tabellaCollaboratori);
		scorrimentoTabella.setBounds(30, 266, 500, 116);
		contentPanel.add(scorrimentoTabella);

		btnRimuoviCollaboratore = new JButton("Revoca accesso");
		btnRimuoviCollaboratore.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnRimuoviCollaboratore.setBackground(new Color(220, 53, 69));
		btnRimuoviCollaboratore.setForeground(Color.WHITE);
		btnRimuoviCollaboratore.setFocusPainted(false);
		btnRimuoviCollaboratore.setBounds(30, 396, 250, 40);
		contentPanel.add(btnRimuoviCollaboratore);

		btnChiudi = new JButton("Chiudi");
		btnChiudi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnChiudi.setBackground(new Color(233, 236, 239));
		btnChiudi.setFocusPainted(false);
		btnChiudi.setBounds(292, 396, 238, 40);
		contentPanel.add(btnChiudi);
	}

	private void inizializzaEventi() {
		btnAggiungiCollaboratore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciAggiuntaCollaboratore();
			}
		});

		btnRimuoviCollaboratore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciRevocaCollaboratore();
			}
		});

		btnChiudi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void caricaAmiciDisponibili(FinestraPrincipale padre) {
		modelloAmici.clear();
		if (padre == null || padre.getControllerUtente() == null) {
			return;
		}
		for (Utente amico : padre.getControllerUtente().getListaAmici()) {
			modelloAmici.addElement(amico);
		}
	}

	private void gestisciAggiuntaCollaboratore() {
		Utente amico = listaAmici.getSelectedValue();
		if (amico == null || playlistCorrente == null || controllerPlaylist == null) {
			JOptionPane.showMessageDialog(this, "Seleziona prima un amico dalla lista", "UninaMultiCloud",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean esito = controllerPlaylist.condividiPlaylist(playlistCorrente.getCodicePlaylist(), amico.getUsername());

		if (esito) {
			caricaCollaboratoriAttuali();
		} else {
			JOptionPane.showMessageDialog(this, "Condivisione non riuscita: l'utente potrebbe essere gia' collaboratore",
					"UninaMultiCloud", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void gestisciRevocaCollaboratore() {
		int riga = tabellaCollaboratori.getSelectedRow();
		if (riga < 0 || riga >= collaboratoriVisualizzati.size() || playlistCorrente == null
				|| controllerPlaylist == null) {
			return;
		}
		Utente destinatario = collaboratoriVisualizzati.get(riga).getDestinatario();
		if (destinatario == null) {
			return;
		}
		if (controllerPlaylist.revocaCondivisione(playlistCorrente.getCodicePlaylist(), destinatario.getCodiceUtente())) {
			caricaCollaboratoriAttuali();
		} else {
			JOptionPane.showMessageDialog(this, "Revoca non riuscita", "UninaMultiCloud", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void caricaCollaboratoriAttuali() {
		modelloTabellaCollaboratori.setRowCount(0);
		if (playlistCorrente == null || controllerPlaylist == null) {
			return;
		}
		collaboratoriVisualizzati = controllerPlaylist.getCollaboratori(playlistCorrente.getCodicePlaylist());

		for (Condivisione condivisione : collaboratoriVisualizzati) {
			String nome = (condivisione.getDestinatario() == null) ? "" : condivisione.getDestinatario().getNomeCompleto();
			modelloTabellaCollaboratori.addRow(new Object[] { nome, condivisione.getLinkCondivisione() });
		}
	}
}
