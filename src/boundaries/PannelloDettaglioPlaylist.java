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

import control.ControllerPlaylist;
import control.ControllerRiproduzione;
import entity.ElementoMultimediale;
import entity.Playlist;

public class PannelloDettaglioPlaylist extends JPanel {

	private static final long serialVersionUID = 1L;

	private ControllerPlaylist controllerPlaylist;
	private ControllerRiproduzione controllerRiproduzione;
	private Playlist playlistCorrente;

	private JLabel etichettaNomePlaylist;
	private JButton btnModificaDettagli;
	private JButton btnCondividi;
	private JButton btnEliminaPlaylist;
	private JTable tabellaBrani;
	private DefaultTableModel modelloTabellaBrani;
	private JButton btnRiproduciSelezionato;
	private JButton btnApriAggiuntaBrano;
	private JButton btnRimuoviBrano;

	
	private List<ElementoMultimediale> braniVisualizzati = new ArrayList<ElementoMultimediale>();

	
	public PannelloDettaglioPlaylist() {
		this(null, null);
	}

	public PannelloDettaglioPlaylist(ControllerPlaylist controllerPlaylist,
			ControllerRiproduzione controllerRiproduzione) {
		this.controllerPlaylist = controllerPlaylist;
		this.controllerRiproduzione = controllerRiproduzione;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setBackground(Color.WHITE);
		setBounds(0, 0, 740, 518);
		setLayout(null);

		etichettaNomePlaylist = new JLabel("Playlist");
		etichettaNomePlaylist.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaNomePlaylist.setBounds(24, 20, 320, 32);
		add(etichettaNomePlaylist);

		btnModificaDettagli = new JButton("Modifica");
		btnModificaDettagli.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnModificaDettagli.setBackground(new Color(233, 236, 239));
		btnModificaDettagli.setFocusPainted(false);
		btnModificaDettagli.setBounds(348, 20, 140, 34);
		add(btnModificaDettagli);

		btnCondividi = new JButton("Condividi");
		btnCondividi.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCondividi.setBackground(new Color(13, 110, 253));
		btnCondividi.setForeground(Color.WHITE);
		btnCondividi.setFocusPainted(false);
		btnCondividi.setBounds(494, 20, 120, 34);
		add(btnCondividi);

		btnEliminaPlaylist = new JButton("Elimina");
		btnEliminaPlaylist.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnEliminaPlaylist.setBackground(new Color(220, 53, 69));
		btnEliminaPlaylist.setForeground(Color.WHITE);
		btnEliminaPlaylist.setFocusPainted(false);
		btnEliminaPlaylist.setBounds(620, 20, 100, 34);
		add(btnEliminaPlaylist);

		modelloTabellaBrani = new DefaultTableModel(new Object[][] {},
				new String[] { "#", "Titolo", "Autore", "Tipo", "Durata" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int riga, int colonna) {
				return false;
			}
		};

		tabellaBrani = new JTable(modelloTabellaBrani);
		tabellaBrani.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tabellaBrani.setRowHeight(30);
		tabellaBrani.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scorrimentoTabella = new JScrollPane(tabellaBrani);
		scorrimentoTabella.setBounds(24, 68, 692, 250);
		add(scorrimentoTabella);

		btnRiproduciSelezionato = new JButton("> Riproduci");
		btnRiproduciSelezionato.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnRiproduciSelezionato.setBackground(new Color(13, 110, 253));
		btnRiproduciSelezionato.setForeground(Color.WHITE);
		btnRiproduciSelezionato.setFocusPainted(false);
		btnRiproduciSelezionato.setBounds(24, 334, 190, 38);
		add(btnRiproduciSelezionato);

		btnApriAggiuntaBrano = new JButton("+ Aggiungi brano");
		btnApriAggiuntaBrano.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnApriAggiuntaBrano.setBackground(new Color(233, 236, 239));
		btnApriAggiuntaBrano.setFocusPainted(false);
		btnApriAggiuntaBrano.setBounds(214, 334, 180, 38);
		add(btnApriAggiuntaBrano);

		btnRimuoviBrano = new JButton("Rimuovi brano");
		btnRimuoviBrano.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnRimuoviBrano.setBackground(new Color(220, 53, 69));
		btnRimuoviBrano.setForeground(Color.WHITE);
		btnRimuoviBrano.setFocusPainted(false);
		btnRimuoviBrano.setBounds(404, 334, 160, 38);
		add(btnRimuoviBrano);
	}

	private void inizializzaEventi() {
		btnRiproduciSelezionato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int riga = tabellaBrani.getSelectedRow();
				if (riga < 0 || controllerRiproduzione == null) {
					return;
				}
				controllerRiproduzione.riproduciPlaylist(braniVisualizzati, riga);
				FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities
						.getWindowAncestor(PannelloDettaglioPlaylist.this);
				if (finestra != null) {
					finestra.aggiornaDalControllerRiproduzione();
				}
			}
		});

		btnApriAggiuntaBrano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogAggiungiBrano();
			}
		});

		btnCondividi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogCondivisione();
			}
		});

		btnEliminaPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confermaEliminazionePlaylist();
			}
		});

		btnRimuoviBrano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rimuoviBranoSelezionato();
			}
		});

		btnModificaDettagli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modificaNomePlaylist();
			}
		});
	}

	private void apriDialogAggiungiBrano() {
		if (playlistCorrente == null) {
			return;
		}
		FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities.getWindowAncestor(this);
		DialogAggiungiBrano dialog = new DialogAggiungiBrano(finestra, controllerPlaylist,
				finestra == null ? null : finestra.getControllerMedia(), playlistCorrente);
		dialog.setVisible(true);
		caricaDatiPlaylist(playlistCorrente);
	}

	private void apriDialogCondivisione() {
		if (playlistCorrente == null) {
			return;
		}
		FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities.getWindowAncestor(this);
		DialogGestioneCondivisione dialog = new DialogGestioneCondivisione(finestra, controllerPlaylist,
				playlistCorrente);
		dialog.setVisible(true);
	}

	private void confermaEliminazionePlaylist() {
		if (playlistCorrente == null || controllerPlaylist == null) {
			return;
		}
		FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities.getWindowAncestor(this);
		DialogConfermaEliminazione conferma = new DialogConfermaEliminazione(finestra,
				"<html>Vuoi eliminare definitivamente la playlist<br><b>\"" + playlistCorrente.getNome()
						+ "\"</b>? L'operazione non e' reversibile.</html>");
		conferma.setVisible(true);

		if (!conferma.isConfermato()) {
			return;
		}
		if (controllerPlaylist.eliminaPlaylist(playlistCorrente.getCodicePlaylist())) {
			playlistCorrente = null;
			if (finestra != null) {
				finestra.getPannelloPlaylist().mostraLeMiePlaylist();
				finestra.cambiaSchermataCentrale(FinestraPrincipale.CARTA_PLAYLIST);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Solo il proprietario puo' eliminare la playlist", "UninaMultiCloud",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void rimuoviBranoSelezionato() {
		int riga = tabellaBrani.getSelectedRow();
		if (riga < 0 || playlistCorrente == null || controllerPlaylist == null) {
			return;
		}
		ElementoMultimediale elemento = braniVisualizzati.get(riga);
		if (controllerPlaylist.rimuoviElementoDaPlaylist(playlistCorrente.getCodicePlaylist(),
				elemento.getCodiceElemento())) {
			caricaDatiPlaylist(playlistCorrente);
		} else {
			JOptionPane.showMessageDialog(this, "Non hai i permessi per modificare questa playlist", "UninaMultiCloud",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void modificaNomePlaylist() {
		if (playlistCorrente == null || controllerPlaylist == null) {
			return;
		}
		String nuovoNome = JOptionPane.showInputDialog(this, "Nuovo nome della playlist:", playlistCorrente.getNome());
		if (nuovoNome == null || nuovoNome.trim().isEmpty()) {
			return;
		}
		boolean esito = controllerPlaylist.aggiornaDettagliPlaylist(playlistCorrente.getCodicePlaylist(),
				nuovoNome.trim(), playlistCorrente.getDescrizione(), playlistCorrente.getTipo(),
				playlistCorrente.getTagRicerca());
		if (esito) {
			playlistCorrente.setNome(nuovoNome.trim());
			etichettaNomePlaylist.setText(nuovoNome.trim());
		} else {
			JOptionPane.showMessageDialog(this, "Non hai i permessi per modificare questa playlist", "UninaMultiCloud",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void aggiornaAbilitazioneControlli() {
		boolean modificabile = playlistCorrente != null && controllerPlaylist != null
				&& controllerPlaylist.puoModificare(playlistCorrente.getCodicePlaylist());

		btnModificaDettagli.setEnabled(modificabile);
		btnApriAggiuntaBrano.setEnabled(modificabile);
		btnRimuoviBrano.setEnabled(modificabile);
	}

	public void caricaDatiPlaylist(Playlist playlist) {
		this.playlistCorrente = playlist;
		if (playlist == null) {
			etichettaNomePlaylist.setText("Playlist");
			aggiornaListaBrani(new ArrayList<ElementoMultimediale>());
			return;
		}
		etichettaNomePlaylist.setText(playlist.getNome());

		List<ElementoMultimediale> brani = (controllerPlaylist == null) ? new ArrayList<ElementoMultimediale>()
				: controllerPlaylist.getElementiPlaylist(playlist.getCodicePlaylist());
		aggiornaListaBrani(brani);
		aggiornaAbilitazioneControlli();
	}

	public void aggiornaListaBrani(List<ElementoMultimediale> brani) {
		braniVisualizzati = (brani == null) ? new ArrayList<ElementoMultimediale>() : brani;
		modelloTabellaBrani.setRowCount(0);

		int posizione = 1;
		for (ElementoMultimediale elemento : braniVisualizzati) {
			String autore = (elemento.getAutore() == null) ? "" : elemento.getAutore().getNomeCompleto();
			modelloTabellaBrani.addRow(new Object[] { Integer.valueOf(posizione), elemento.getTitolo(), autore,
					elemento.getTipoMedia(), elemento.getDurataFormattata() });
			posizione++;
		}
	}
}
