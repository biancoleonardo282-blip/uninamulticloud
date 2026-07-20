package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import control.ControllerUtente;
import entity.Utente;

public class PannelloListaAmici extends JPanel {

	private static final long serialVersionUID = 1L;

	private ControllerUtente controllerUtente;

	private JLabel etichettaTitolo;
	private JTable tabellaAmici;
	private DefaultTableModel modelloTabellaAmici;
	private JButton btnAggiungiAmico;
	private JButton btnRimuoviAmico;

	
	private List<Utente> amiciVisualizzati = new ArrayList<Utente>();

	
	public PannelloListaAmici() {
		this(null);
	}

	public PannelloListaAmici(ControllerUtente controllerUtente) {
		this.controllerUtente = controllerUtente;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setBackground(Color.WHITE);
		setBounds(0, 0, 740, 518);
		setLayout(null);

		etichettaTitolo = new JLabel("I miei amici");
		etichettaTitolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaTitolo.setBounds(24, 20, 300, 32);
		add(etichettaTitolo);

		btnAggiungiAmico = new JButton("+ Aggiungi amico");
		btnAggiungiAmico.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnAggiungiAmico.setBackground(new Color(13, 110, 253));
		btnAggiungiAmico.setForeground(Color.WHITE);
		btnAggiungiAmico.setFocusPainted(false);
		btnAggiungiAmico.setBounds(536, 20, 180, 38);
		add(btnAggiungiAmico);

		modelloTabellaAmici = new DefaultTableModel(new Object[][] {},
				new String[] { "Username", "Nome e cognome" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int riga, int colonna) {
				return false;
			}
		};

		tabellaAmici = new JTable(modelloTabellaAmici);
		tabellaAmici.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tabellaAmici.setRowHeight(30);
		tabellaAmici.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scorrimentoTabella = new JScrollPane(tabellaAmici);
		scorrimentoTabella.setBounds(24, 72, 692, 260);
		add(scorrimentoTabella);

		btnRimuoviAmico = new JButton("Rimuovi selezionato");
		btnRimuoviAmico.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnRimuoviAmico.setBackground(new Color(220, 53, 69));
		btnRimuoviAmico.setForeground(Color.WHITE);
		btnRimuoviAmico.setFocusPainted(false);
		btnRimuoviAmico.setBounds(24, 348, 220, 38);
		add(btnRimuoviAmico);
	}

	private void inizializzaEventi() {
		btnAggiungiAmico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogAggiungiAmico();
			}
		});

		btnRimuoviAmico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confermaRimozioneAmico();
			}
		});
	}

	private void apriDialogAggiungiAmico() {
		FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities.getWindowAncestor(this);
		DialogAggiungiAmico dialog = new DialogAggiungiAmico(finestra, controllerUtente);
		dialog.setVisible(true);
		caricaListaAmici();
	}

	private void confermaRimozioneAmico() {
		int rigaSelezionata = tabellaAmici.getSelectedRow();
		if (rigaSelezionata < 0 || rigaSelezionata >= amiciVisualizzati.size()) {
			JOptionPane.showMessageDialog(this, "Seleziona prima un amico dalla tabella", "UninaMultiCloud",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		Utente amico = amiciVisualizzati.get(rigaSelezionata);

		FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities.getWindowAncestor(this);
		DialogConfermaEliminazione conferma = new DialogConfermaEliminazione(finestra,
				"Vuoi rimuovere " + amico.getNomeCompleto() + " dalla tua lista amici?");
		conferma.setVisible(true);

		if (conferma.isConfermato() && controllerUtente != null) {
			if (controllerUtente.rimuoviAmico(amico.getCodiceUtente())) {
				caricaListaAmici();
			} else {
				JOptionPane.showMessageDialog(this, "Rimozione non riuscita", "UninaMultiCloud",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void caricaListaAmici() {
		modelloTabellaAmici.setRowCount(0);
		amiciVisualizzati = (controllerUtente == null) ? new ArrayList<Utente>() : controllerUtente.getListaAmici();

		for (Utente amico : amiciVisualizzati) {
			modelloTabellaAmici.addRow(new Object[] { amico.getUsername(), amico.getNomeCompleto() });
		}
	}
}
