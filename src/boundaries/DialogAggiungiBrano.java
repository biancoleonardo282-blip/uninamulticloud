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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import control.ControllerMedia;
import control.ControllerPlaylist;
import entity.ElementoMultimediale;
import entity.Playlist;

public class DialogAggiungiBrano extends JDialog {

	private static final long serialVersionUID = 1L;

	private ControllerPlaylist controllerPlaylist;
	private ControllerMedia controllerMedia;
	private Playlist playlistDestinazione;

	private JPanel contentPanel;
	private JTextField campoRicerca;
	private JButton btnCerca;
	private JList<ElementoMultimediale> listaRisultatiRicerca;
	private DefaultListModel<ElementoMultimediale> modelloRisultati;
	private JButton btnConfermaAggiunta;
	private JButton btnAnnulla;


	public DialogAggiungiBrano() {
		this(null, null, null, null);
	}

	public DialogAggiungiBrano(JFrame padre, ControllerPlaylist controllerPlaylist, ControllerMedia controllerMedia,
			Playlist playlistDestinazione) {
		super(padre, "Aggiungi brano alla playlist", true);
		this.controllerPlaylist = controllerPlaylist;
		this.controllerMedia = controllerMedia;
		this.playlistDestinazione = playlistDestinazione;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 536, 439);
		setLocationRelativeTo(getParent());
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		JLabel etichettaRicerca = new JLabel("Cerca un brano gia' pubblicato");
		etichettaRicerca.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaRicerca.setBounds(30, 18, 340, 18);
		contentPanel.add(etichettaRicerca);

		campoRicerca = new JTextField();
		campoRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoRicerca.setColumns(10);
		campoRicerca.setBounds(30, 40, 340, 34);
		contentPanel.add(campoRicerca);

		btnCerca = new JButton("Cerca");
		btnCerca.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCerca.setBackground(new Color(13, 110, 253));
		btnCerca.setForeground(Color.WHITE);
		btnCerca.setFocusPainted(false);
		btnCerca.setBounds(382, 40, 108, 34);
		contentPanel.add(btnCerca);

		modelloRisultati = new DefaultListModel<ElementoMultimediale>();
		listaRisultatiRicerca = new JList<ElementoMultimediale>(modelloRisultati);
		listaRisultatiRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		listaRisultatiRicerca.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scorrimentoRisultati = new JScrollPane(listaRisultatiRicerca);
		scorrimentoRisultati.setBounds(30, 88, 460, 160);
		contentPanel.add(scorrimentoRisultati);

		btnConfermaAggiunta = new JButton("Aggiungi alla playlist");
		btnConfermaAggiunta.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnConfermaAggiunta.setBackground(new Color(13, 110, 253));
		btnConfermaAggiunta.setForeground(Color.WHITE);
		btnConfermaAggiunta.setFocusPainted(false);
		btnConfermaAggiunta.setBounds(30, 262, 330, 40);
		contentPanel.add(btnConfermaAggiunta);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(372, 262, 118, 40);
		contentPanel.add(btnAnnulla);
	}

	private void inizializzaEventi() {
		btnCerca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciRicerca();
			}
		});

		campoRicerca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciRicerca();
			}
		});

		btnConfermaAggiunta.addActionListener(new ActionListener() {
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
		if (controllerMedia == null) {
			return;
		}
		mostraRisultati(controllerMedia.cercaElementiPerTitolo(campoRicerca.getText().trim()));
	}

	private void mostraRisultati(List<ElementoMultimediale> risultati) {
		modelloRisultati.clear();
		if (risultati == null) {
			return;
		}
		for (ElementoMultimediale elemento : risultati) {
			modelloRisultati.addElement(elemento);
		}
	}

	private void gestisciAggiunta() {
		ElementoMultimediale selezionato = listaRisultatiRicerca.getSelectedValue();
		if (selezionato == null || playlistDestinazione == null || controllerPlaylist == null) {
			JOptionPane.showMessageDialog(this, "Seleziona prima un brano dalla lista", "UninaMultiCloud",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean esito = controllerPlaylist.aggiungiElementoAPlaylist(playlistDestinazione.getCodicePlaylist(),
				selezionato.getCodiceElemento());

		if (esito) {
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Non hai i permessi per modificare questa playlist", "UninaMultiCloud",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
