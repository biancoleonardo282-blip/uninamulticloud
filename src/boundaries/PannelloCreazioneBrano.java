package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.ControllerMedia;


public class PannelloCreazioneBrano extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String FORMATO_AUDIO_DEFAULT = "MP3";
	private static final String CODEC_AUDIO_DEFAULT = "MPEG-3";
	private static final int BITRATE_AUDIO_DEFAULT = 320;
	private static final int FRAMERATE_VIDEO_DEFAULT = 30;
	private static final String RISOLUZIONE_VIDEO_DEFAULT = "1920x1080";

	private ControllerMedia controllerMedia;
	private FinestraPrincipale finestraPrincipale;

	private JLabel etichettaTitoloBrano;
	private JTextField campoTitoloBrano;
	private JLabel etichettaDescrizioneBrano;
	private JTextArea campoDescrizioneBrano;
	private JLabel etichettaTipoBrano;
	private JRadioButton radioAudio;
	private JRadioButton radioVideo;
	private ButtonGroup gruppoTipoBrano;
	private JLabel etichettaDurata;
	private JSpinner campoDurata;
	private JLabel etichettaCopertina;
	private JButton btnScegliCopertina;
	private String percorsoCopertina;
	private JButton btnCarica;
	private JButton btnAnnulla;
	private JLabel etichettaErrore;

	
	public PannelloCreazioneBrano() {
		this(null, null);
	}

	public PannelloCreazioneBrano(ControllerMedia controllerMedia, FinestraPrincipale finestraPrincipale) {
		this.controllerMedia = controllerMedia;
		this.finestraPrincipale = finestraPrincipale;
		inizializzaGrafica();
		raggruppaSceltaTipo();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setBackground(Color.WHITE);
		setBounds(0, 0, 740, 518);
		setLayout(null);

		JLabel etichettaTitoloSchermata = new JLabel("Carica un nuovo brano");
		etichettaTitoloSchermata.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaTitoloSchermata.setBounds(24, 14, 400, 32);
		add(etichettaTitoloSchermata);

		etichettaTitoloBrano = new JLabel("Titolo");
		etichettaTitoloBrano.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaTitoloBrano.setBounds(24, 56, 200, 20);
		add(etichettaTitoloBrano);

		campoTitoloBrano = new JTextField();
		campoTitoloBrano.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoTitoloBrano.setColumns(10);
		campoTitoloBrano.setBounds(24, 78, 480, 34);
		add(campoTitoloBrano);

		etichettaDescrizioneBrano = new JLabel("Descrizione");
		etichettaDescrizioneBrano.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaDescrizioneBrano.setBounds(24, 122, 200, 20);
		add(etichettaDescrizioneBrano);

		campoDescrizioneBrano = new JTextArea();
		campoDescrizioneBrano.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoDescrizioneBrano.setLineWrap(true);
		campoDescrizioneBrano.setWrapStyleWord(true);

		JScrollPane scorrimentoDescrizione = new JScrollPane(campoDescrizioneBrano);
		scorrimentoDescrizione.setBounds(24, 144, 480, 70);
		add(scorrimentoDescrizione);

		etichettaTipoBrano = new JLabel("Tipo di contenuto");
		etichettaTipoBrano.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaTipoBrano.setBounds(24, 226, 200, 20);
		add(etichettaTipoBrano);

		radioAudio = new JRadioButton("Audio");
		radioAudio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		radioAudio.setBackground(Color.WHITE);
		radioAudio.setSelected(true);
		radioAudio.setBounds(20, 248, 110, 24);
		add(radioAudio);

		radioVideo = new JRadioButton("Video");
		radioVideo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		radioVideo.setBackground(Color.WHITE);
		radioVideo.setBounds(138, 248, 110, 24);
		add(radioVideo);

		etichettaDurata = new JLabel("Durata (secondi)");
		etichettaDurata.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaDurata.setBounds(24, 292, 200, 20);
		add(etichettaDurata);

		campoDurata = new JSpinner();
		campoDurata.setModel(new SpinnerNumberModel(180, 1, 36000, 1));
		campoDurata.setBounds(24, 314, 140, 32);
		add(campoDurata);

		etichettaCopertina = new JLabel("Nessuna copertina selezionata");
		etichettaCopertina.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		etichettaCopertina.setForeground(new Color(108, 117, 125));
		etichettaCopertina.setBounds(210, 358, 294, 20);
		add(etichettaCopertina);

		btnScegliCopertina = new JButton("Scegli copertina...");
		btnScegliCopertina.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnScegliCopertina.setBackground(new Color(233, 236, 239));
		btnScegliCopertina.setFocusPainted(false);
		btnScegliCopertina.setBounds(24, 354, 160, 34);
		add(btnScegliCopertina);

		etichettaErrore = new JLabel("");
		etichettaErrore.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaErrore.setForeground(new Color(220, 53, 69));
		etichettaErrore.setBounds(24, 400, 480, 20);
		add(etichettaErrore);

		btnCarica = new JButton("Carica");
		btnCarica.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCarica.setBackground(new Color(13, 110, 253));
		btnCarica.setForeground(Color.WHITE);
		btnCarica.setFocusPainted(false);
		btnCarica.setBounds(24, 428, 300, 42);
		add(btnCarica);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(336, 428, 168, 42);
		add(btnAnnulla);
	}

	private void raggruppaSceltaTipo() {
		gruppoTipoBrano = new ButtonGroup();
		gruppoTipoBrano.add(radioAudio);
		gruppoTipoBrano.add(radioVideo);
	}

	private void inizializzaEventi() {
		btnScegliCopertina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciScegliCopertina();
			}
		});

		btnCarica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestisciCaricamento();
			}
		});

		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				svuotaCampi();
				if (finestraPrincipale != null) {
					finestraPrincipale.cambiaSchermataCentrale(FinestraPrincipale.CARTA_HOME);
				}
			}
		});
	}

	private void gestisciScegliCopertina() {
		JFileChooser selettoreFile = new JFileChooser();
		selettoreFile.setDialogTitle("Scegli la copertina");
		selettoreFile.setFileFilter(new FileNameExtensionFilter("Immagini", "png", "jpg", "jpeg"));

		if (selettoreFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			percorsoCopertina = selettoreFile.getSelectedFile().getAbsolutePath();
			etichettaCopertina.setText(selettoreFile.getSelectedFile().getName());
		}
	}

	private void gestisciCaricamento() {
		if (!validaCampi() || controllerMedia == null) {
			return;
		}
		Duration durata = Duration.ofSeconds(((Integer) campoDurata.getValue()).intValue());
		boolean esito;

		if (radioVideo.isSelected()) {
			esito = controllerMedia.pubblicaVideo(campoTitoloBrano.getText().trim(),
					campoDescrizioneBrano.getText().trim(), durata, percorsoCopertina, FRAMERATE_VIDEO_DEFAULT,
					RISOLUZIONE_VIDEO_DEFAULT);
		} else {
			esito = controllerMedia.pubblicaBrano(campoTitoloBrano.getText().trim(),
					campoDescrizioneBrano.getText().trim(), durata, percorsoCopertina, BITRATE_AUDIO_DEFAULT,
					FORMATO_AUDIO_DEFAULT, CODEC_AUDIO_DEFAULT, "");
		}

		if (esito) {
			JOptionPane.showMessageDialog(this, "Contenuto pubblicato correttamente", "UninaMultiCloud",
					JOptionPane.INFORMATION_MESSAGE);
			svuotaCampi();
			if (finestraPrincipale != null) {
				finestraPrincipale.getPannelloHome().caricaRaccomandati();
				finestraPrincipale.cambiaSchermataCentrale(FinestraPrincipale.CARTA_HOME);
			}
		} else {
			etichettaErrore.setText("Caricamento non riuscito");
		}
	}

	private boolean validaCampi() {
		if (campoTitoloBrano.getText().trim().isEmpty()) {
			etichettaErrore.setText("Il titolo e' obbligatorio");
			return false;
		}
		etichettaErrore.setText("");
		return true;
	}

	public void svuotaCampi() {
		campoTitoloBrano.setText("");
		campoDescrizioneBrano.setText("");
		campoDurata.setValue(Integer.valueOf(180));
		radioAudio.setSelected(true);
		percorsoCopertina = null;
		etichettaCopertina.setText("Nessuna copertina selezionata");
		etichettaErrore.setText("");
	}
}
