package boundaries;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import control.ControllerPlaylist;
import entity.Playlist;

public class PannelloPlaylist extends JPanel {

	private static final long serialVersionUID = 1L;

	private ControllerPlaylist controllerPlaylist;
	private FinestraPrincipale finestraPrincipale;

	private JLabel etichettaTitolo;
	private JTabbedPane schedeVisualizzazione;
	private JButton btnCreaPlaylist;
	private JPanel grigliaPlaylist;
	private JLabel etichettaMessaggio;


	public PannelloPlaylist() {
		this(null, null);
	}

	public PannelloPlaylist(ControllerPlaylist controllerPlaylist, FinestraPrincipale finestraPrincipale) {
		this.controllerPlaylist = controllerPlaylist;
		this.finestraPrincipale = finestraPrincipale;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setBackground(Color.WHITE);
		setBounds(0, 0, 740, 518);
		setLayout(null);

		etichettaTitolo = new JLabel("Le mie playlist");
		etichettaTitolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaTitolo.setBounds(24, 20, 300, 32);
		add(etichettaTitolo);

		btnCreaPlaylist = new JButton("+ Crea playlist");
		btnCreaPlaylist.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCreaPlaylist.setBackground(new Color(13, 110, 253));
		btnCreaPlaylist.setForeground(Color.WHITE);
		btnCreaPlaylist.setFocusPainted(false);
		btnCreaPlaylist.setBounds(536, 20, 180, 38);
		add(btnCreaPlaylist);

		schedeVisualizzazione = new JTabbedPane(JTabbedPane.TOP);
		schedeVisualizzazione.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		schedeVisualizzazione.setBounds(24, 72, 692, 40);
		schedeVisualizzazione.addTab("Private", null, new JPanel(), null);
		schedeVisualizzazione.addTab("Condivise con me", null, new JPanel(), null);
		schedeVisualizzazione.addTab("Pubbliche", null, new JPanel(), null);
		add(schedeVisualizzazione);

		grigliaPlaylist = new JPanel();
		grigliaPlaylist.setBackground(Color.WHITE);
		grigliaPlaylist.setLayout(new GridLayout(0, 4, 18, 18));

		JScrollPane scorrimentoGriglia = new JScrollPane(grigliaPlaylist);
		scorrimentoGriglia.setBorder(BorderFactory.createDashedBorder(new Color(222, 226, 230)));
		scorrimentoGriglia.setBounds(24, 116, 692, 260);
		add(scorrimentoGriglia);

		etichettaMessaggio = new JLabel("");
		etichettaMessaggio.setHorizontalAlignment(SwingConstants.CENTER);
		etichettaMessaggio.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaMessaggio.setForeground(new Color(108, 117, 125));
		etichettaMessaggio.setBounds(24, 390, 692, 24);
		add(etichettaMessaggio);
	}

	private void inizializzaEventi() {
		btnCreaPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriCreazionePlaylist();
			}
		});

		schedeVisualizzazione.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (schedeVisualizzazione.getSelectedIndex()) {
					case 1:
						mostraPlaylistCondivise();
						break;
					case 2:
						mostraPlaylistPubbliche();
						break;
					default:
						mostraLeMiePlaylist();
						break;
				}
			}
		});
	}

	private JPanel creaSchedaPlaylist(final Playlist playlist) {
		JPanel scheda = new JPanel();
		scheda.setBackground(new Color(248, 249, 250));
		scheda.setBorder(new LineBorder(new Color(222, 226, 230)));
		scheda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scheda.setLayout(null);

		JLabel etichettaNome = new JLabel("<html><b>" + playlist.getNome() + "</b></html>");
		etichettaNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		etichettaNome.setBounds(8, 96, 134, 20);
		scheda.add(etichettaNome);

		JLabel etichettaConteggio = new JLabel(playlist.getNumeroBrani() + " brani");
		etichettaConteggio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		etichettaConteggio.setForeground(new Color(108, 117, 125));
		etichettaConteggio.setBounds(8, 118, 134, 20);
		scheda.add(etichettaConteggio);

		scheda.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (finestraPrincipale != null) {
					finestraPrincipale.apriDettaglioPlaylist(playlist);
				}
			}
		});
		return scheda;
	}

	private void apriCreazionePlaylist() {
		if (finestraPrincipale != null) {
			finestraPrincipale.apriCreazionePlaylist();
		}
	}

	public void mostraLeMiePlaylist() {
		etichettaTitolo.setText("Le mie playlist");
		popolaGriglia(controllerPlaylist == null ? null : controllerPlaylist.getLeMiePlaylist(),
				"Non hai ancora nessuna playlist");
	}

	public void mostraPlaylistCondivise() {
		etichettaTitolo.setText("Condivise con me");
		popolaGriglia(controllerPlaylist == null ? null : controllerPlaylist.getPlaylistCondivise(),
				"Nessuna playlist condivisa con te");
	}

	public void mostraPlaylistPubbliche() {
		etichettaTitolo.setText("Playlist pubbliche");
		popolaGriglia(controllerPlaylist == null ? null : controllerPlaylist.getPlaylistPubbliche(),
				"Nessuna playlist pubblica disponibile");
	}

	private void popolaGriglia(List<Playlist> playlist, String messaggioVuoto) {
		grigliaPlaylist.removeAll();

		if (playlist == null || playlist.isEmpty()) {
			etichettaMessaggio.setText(messaggioVuoto);
		} else {
			etichettaMessaggio.setText("");
			for (Playlist singola : playlist) {
				grigliaPlaylist.add(creaSchedaPlaylist(singola));
			}
		}
		grigliaPlaylist.revalidate();
		grigliaPlaylist.repaint();
	}
}
