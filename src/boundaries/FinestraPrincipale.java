package boundaries;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import control.ControllerMedia;
import control.ControllerPlaylist;
import control.ControllerRiproduzione;
import control.ControllerUtente;
import db.ElementoMultimedialeDAOImpl;
import db.PlaylistDAOImpl;
import db.StatisticheRiproduzioneDAOImpl;
import db.UtenteDAOImpl;
import entity.ElementoMultimediale;
import entity.Playlist;
import entity.Utente;

public class FinestraPrincipale extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final String CARTA_HOME = "HOME";
	public static final String CARTA_PROFILO = "PROFILO";
	public static final String CARTA_PLAYLIST = "PLAYLIST";
	public static final String CARTA_DETTAGLIO_PLAYLIST = "DETTAGLIO_PLAYLIST";
	public static final String CARTA_LISTA_AMICI = "LISTA_AMICI";
	public static final String CARTA_CREAZIONE_PLAYLIST = "CREAZIONE_PLAYLIST";
	public static final String CARTA_CREAZIONE_BRANO = "CREAZIONE_BRANO";

	private ControllerUtente controllerUtente;
	private ControllerRiproduzione controllerRiproduzione;
	private ControllerMedia controllerMedia;
	private ControllerPlaylist controllerPlaylist;

	private JPanel contentPane;
	private JLabel etichettaFotoProfilo;
	private JLabel etichettaNomeUtente;
	private JButton btnProfilo;
	private JButton btnHome;
	private JButton btnPlaylist;
	private JButton btnListaAmici;
	private JButton btnLogout;
	private JButton btnCreaBrano;
	private JLabel etichettaBranoCorrente;
	private JButton btnPrecedente;
	private JButton btnPlayPausa;
	private JButton btnSuccessivo;
	private JSlider barraAvanzamento;
	private JLabel etichettaTempo;

	private CardLayout gestoreCarte;
	private JPanel pannelloCentrale;
	private PannelloHome pannelloHome;
	private PannelloProfilo pannelloProfilo;
	private PannelloPlaylist pannelloPlaylist;
	private PannelloDettaglioPlaylist pannelloDettaglioPlaylist;
	private PannelloListaAmici pannelloListaAmici;
	private PannelloCreazionePlaylist pannelloCreazionePlaylist;
	private PannelloCreazioneBrano pannelloCreazioneBrano;

	
	public FinestraPrincipale() {
		this(new ControllerUtente(new UtenteDAOImpl()), null);
	}

	public FinestraPrincipale(ControllerUtente controllerUtente, ControllerRiproduzione controllerRiproduzione) {
		this.controllerUtente = controllerUtente;
		this.controllerRiproduzione = (controllerRiproduzione != null) ? controllerRiproduzione
				: new ControllerRiproduzione(controllerUtente, new StatisticheRiproduzioneDAOImpl());
		this.controllerMedia = new ControllerMedia(controllerUtente, new ElementoMultimedialeDAOImpl(),
				new StatisticheRiproduzioneDAOImpl());
		this.controllerPlaylist = new ControllerPlaylist(controllerUtente, new PlaylistDAOImpl(),
				new ElementoMultimedialeDAOImpl());

		inizializzaComponentiFissi();
		inizializzaMazzoDiCarte();
		inizializzaEventiNavigazione();
		inizializzaEventiPlayer();
		caricaIntestazioneUtente();
	}

	private void inizializzaComponentiFissi() {
		setTitle("UninaMultiCloud");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 956, 647);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pannelloLaterale = new JPanel();
		pannelloLaterale.setBackground(new Color(248, 249, 250));
		pannelloLaterale.setBorder(new LineBorder(new Color(222, 226, 230)));
		pannelloLaterale.setBounds(0, 0, 200, 518);
		pannelloLaterale.setLayout(null);
		contentPane.add(pannelloLaterale);

		etichettaFotoProfilo = new JLabel("foto");
		etichettaFotoProfilo.setHorizontalAlignment(SwingConstants.CENTER);
		etichettaFotoProfilo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		etichettaFotoProfilo.setForeground(new Color(108, 117, 125));
		etichettaFotoProfilo.setBackground(new Color(222, 226, 230));
		etichettaFotoProfilo.setBorder(new LineBorder(new Color(173, 181, 189)));
		etichettaFotoProfilo.setOpaque(true);
		etichettaFotoProfilo.setBounds(62, 18, 76, 76);
		pannelloLaterale.add(etichettaFotoProfilo);

		etichettaNomeUtente = new JLabel("");
		etichettaNomeUtente.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaNomeUtente.setHorizontalAlignment(SwingConstants.CENTER);
		etichettaNomeUtente.setBounds(10, 100, 180, 20);
		pannelloLaterale.add(etichettaNomeUtente);

		btnHome = new JButton("Home");
		btnHome.setHorizontalAlignment(SwingConstants.LEFT);
		btnHome.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnHome.setBackground(new Color(13, 110, 253));
		btnHome.setForeground(Color.WHITE);
		btnHome.setFocusPainted(false);
		btnHome.setBounds(14, 132, 172, 36);
		pannelloLaterale.add(btnHome);

		btnPlaylist = new JButton("Le mie playlist");
		btnPlaylist.setHorizontalAlignment(SwingConstants.LEFT);
		btnPlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnPlaylist.setBackground(Color.WHITE);
		btnPlaylist.setFocusPainted(false);
		btnPlaylist.setBounds(14, 176, 172, 36);
		pannelloLaterale.add(btnPlaylist);

		btnListaAmici = new JButton("Amici");
		btnListaAmici.setHorizontalAlignment(SwingConstants.LEFT);
		btnListaAmici.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnListaAmici.setBackground(Color.WHITE);
		btnListaAmici.setFocusPainted(false);
		btnListaAmici.setBounds(14, 220, 172, 36);
		pannelloLaterale.add(btnListaAmici);

		btnProfilo = new JButton("Profilo");
		btnProfilo.setHorizontalAlignment(SwingConstants.LEFT);
		btnProfilo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnProfilo.setBackground(Color.WHITE);
		btnProfilo.setFocusPainted(false);
		btnProfilo.setBounds(14, 264, 172, 36);
		pannelloLaterale.add(btnProfilo);

		btnLogout = new JButton("Esci");
		btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnLogout.setBackground(new Color(233, 236, 239));
		btnLogout.setFocusPainted(false);
		btnLogout.setBounds(14, 468, 172, 36);
		pannelloLaterale.add(btnLogout);

		JPanel pannelloPlayer = new JPanel();
		pannelloPlayer.setBackground(new Color(52, 58, 64));
		pannelloPlayer.setBounds(0, 518, 940, 90);
		pannelloPlayer.setLayout(null);
		contentPane.add(pannelloPlayer);

		etichettaBranoCorrente = new JLabel("Nessun brano in riproduzione");
		etichettaBranoCorrente.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaBranoCorrente.setForeground(Color.WHITE);
		etichettaBranoCorrente.setBounds(16, 22, 300, 44);
		pannelloPlayer.add(etichettaBranoCorrente);

		btnPrecedente = new JButton("<<");
		btnPrecedente.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnPrecedente.setBackground(new Color(233, 236, 239));
		btnPrecedente.setFocusPainted(false);
		btnPrecedente.setBounds(340, 16, 40, 30);
		pannelloPlayer.add(btnPrecedente);

		btnPlayPausa = new JButton(">");
		btnPlayPausa.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnPlayPausa.setBackground(new Color(13, 110, 253));
		btnPlayPausa.setForeground(Color.WHITE);
		btnPlayPausa.setFocusPainted(false);
		btnPlayPausa.setBounds(388, 12, 52, 38);
		pannelloPlayer.add(btnPlayPausa);

		btnSuccessivo = new JButton(">>");
		btnSuccessivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnSuccessivo.setBackground(new Color(233, 236, 239));
		btnSuccessivo.setFocusPainted(false);
		btnSuccessivo.setBounds(448, 16, 40, 30);
		pannelloPlayer.add(btnSuccessivo);

		barraAvanzamento = new JSlider();
		barraAvanzamento.setBackground(new Color(52, 58, 64));
		barraAvanzamento.setValue(0);
		barraAvanzamento.setBounds(300, 56, 260, 20);
		pannelloPlayer.add(barraAvanzamento);

		etichettaTempo = new JLabel("0:00 / 0:00");
		etichettaTempo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		etichettaTempo.setForeground(new Color(173, 181, 189));
		etichettaTempo.setBounds(572, 56, 110, 20);
		pannelloPlayer.add(etichettaTempo);

		btnCreaBrano = new JButton("+ Carica brano");
		btnCreaBrano.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCreaBrano.setBackground(new Color(13, 110, 253));
		btnCreaBrano.setForeground(Color.WHITE);
		btnCreaBrano.setFocusPainted(false);
		btnCreaBrano.setBounds(770, 26, 150, 36);
		pannelloPlayer.add(btnCreaBrano);
	}

	private void inizializzaMazzoDiCarte() {
		gestoreCarte = new CardLayout(0, 0);
		pannelloCentrale = new JPanel();
		pannelloCentrale.setBackground(Color.WHITE);
		pannelloCentrale.setBounds(200, 0, 740, 518);
		pannelloCentrale.setLayout(gestoreCarte);
		contentPane.add(pannelloCentrale);

		pannelloHome = new PannelloHome(controllerMedia, controllerRiproduzione);
		pannelloProfilo = new PannelloProfilo(controllerUtente);
		pannelloPlaylist = new PannelloPlaylist(controllerPlaylist, this);
		pannelloDettaglioPlaylist = new PannelloDettaglioPlaylist(controllerPlaylist, controllerRiproduzione);
		pannelloListaAmici = new PannelloListaAmici(controllerUtente);
		pannelloCreazionePlaylist = new PannelloCreazionePlaylist(controllerPlaylist, this);
		pannelloCreazioneBrano = new PannelloCreazioneBrano(controllerMedia, this);

		pannelloCentrale.add(pannelloHome, CARTA_HOME);
		pannelloCentrale.add(pannelloProfilo, CARTA_PROFILO);
		pannelloCentrale.add(pannelloPlaylist, CARTA_PLAYLIST);
		pannelloCentrale.add(pannelloDettaglioPlaylist, CARTA_DETTAGLIO_PLAYLIST);
		pannelloCentrale.add(pannelloListaAmici, CARTA_LISTA_AMICI);
		pannelloCentrale.add(pannelloCreazionePlaylist, CARTA_CREAZIONE_PLAYLIST);
		pannelloCentrale.add(pannelloCreazioneBrano, CARTA_CREAZIONE_BRANO);

		cambiaSchermataCentrale(CARTA_HOME);
	}

	private void inizializzaEventiNavigazione() {
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannelloHome.caricaRaccomandati();
				cambiaSchermataCentrale(CARTA_HOME);
			}
		});

		btnPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannelloPlaylist.mostraLeMiePlaylist();
				cambiaSchermataCentrale(CARTA_PLAYLIST);
			}
		});

		btnListaAmici.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannelloListaAmici.caricaListaAmici();
				cambiaSchermataCentrale(CARTA_LISTA_AMICI);
			}
		});

		btnProfilo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannelloProfilo.caricaDatiUtente();
				cambiaSchermataCentrale(CARTA_PROFILO);
			}
		});

		btnCreaBrano.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannelloCreazioneBrano.svuotaCampi();
				cambiaSchermataCentrale(CARTA_CREAZIONE_BRANO);
			}
		});

		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciLogout();
			}
		});
	}

	private void inizializzaEventiPlayer() {
		btnPlayPausa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controllerRiproduzione.playPausa();
				aggiornaDalControllerRiproduzione();
			}
		});

		btnPrecedente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controllerRiproduzione.elementoPrecedente();
				aggiornaDalControllerRiproduzione();
			}
		});

		btnSuccessivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controllerRiproduzione.elementoSuccessivo();
				aggiornaDalControllerRiproduzione();
			}
		});

		barraAvanzamento.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (barraAvanzamento.getValueIsAdjusting()) {
					return;
				}
				ElementoMultimediale corrente = controllerRiproduzione.getElementoCorrente();
				if (corrente == null || corrente.getDurata() == null) {
					return;
				}
				long secondi = corrente.getDurata().getSeconds() * barraAvanzamento.getValue() / 100;
				controllerRiproduzione.spostaPosizione(Duration.ofSeconds(secondi));
				aggiornaAvanzamento(controllerRiproduzione.getPosizioneCorrente(), corrente.getDurata());
			}
		});
	}

	private void gestisciLogout() {
		controllerUtente.eseguiLogout();
		SchermataBenvenuto schermataBenvenuto = new SchermataBenvenuto(controllerUtente);
		schermataBenvenuto.setVisible(true);
		dispose();
	}

	public void cambiaSchermataCentrale(String nomeCarta) {
		gestoreCarte.show(pannelloCentrale, nomeCarta);
	}

	public void apriDettaglioPlaylist(Playlist playlist) {
		pannelloDettaglioPlaylist.caricaDatiPlaylist(playlist);
		cambiaSchermataCentrale(CARTA_DETTAGLIO_PLAYLIST);
	}

	public void apriCreazionePlaylist() {
		pannelloCreazionePlaylist.svuotaCampi();
		cambiaSchermataCentrale(CARTA_CREAZIONE_PLAYLIST);
	}

	public void aggiornaInterfacciaPlayer(String titolo, boolean inRiproduzione) {
		etichettaBranoCorrente.setText(titolo);
		btnPlayPausa.setText(inRiproduzione ? "||" : ">");
	}

	public void aggiornaAvanzamento(Duration posizione, Duration durata) {
		if (posizione == null || durata == null || durata.isZero()) {
			barraAvanzamento.setValue(0);
			etichettaTempo.setText("0:00 / 0:00");
			return;
		}
		int percentuale = (int) (posizione.getSeconds() * 100 / durata.getSeconds());
		barraAvanzamento.setValue(percentuale);
		etichettaTempo.setText(formattaDurata(posizione) + " / " + formattaDurata(durata));
	}

	public void aggiornaDalControllerRiproduzione() {
		ElementoMultimediale corrente = controllerRiproduzione.getElementoCorrente();
		if (corrente == null) {
			aggiornaInterfacciaPlayer("Nessun brano in riproduzione", false);
			aggiornaAvanzamento(null, null);
			return;
		}
		String autore = (corrente.getAutore() == null) ? "" : corrente.getAutore().getNomeCompleto();
		aggiornaInterfacciaPlayer("<html>" + corrente.getTitolo() + "<br><font size='2' color='#ADB5BD'>" + autore
				+ "</font></html>", controllerRiproduzione.isInRiproduzione());
		aggiornaAvanzamento(controllerRiproduzione.getPosizioneCorrente(), corrente.getDurata());
	}

	private void caricaIntestazioneUtente() {
		Utente utente = (controllerUtente == null) ? null : controllerUtente.getUtenteInSessione();
		if (utente == null) {
			return;
		}
		etichettaNomeUtente.setText(utente.getNomeCompleto());
		pannelloHome.caricaRaccomandati();
	}

	private String formattaDurata(Duration durata) {
		long secondi = durata.getSeconds();
		return String.format("%d:%02d", secondi / 60, secondi % 60);
	}

	public PannelloHome getPannelloHome() {
		return pannelloHome;
	}

	public PannelloPlaylist getPannelloPlaylist() {
		return pannelloPlaylist;
	}

	public ControllerMedia getControllerMedia() {
		return controllerMedia;
	}

	public ControllerUtente getControllerUtente() {
		return controllerUtente;
	}
}
