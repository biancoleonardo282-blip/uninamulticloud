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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import control.ControllerMedia;
import control.ControllerRiproduzione;
import entity.ElementoMultimediale;

public class PannelloHome extends JPanel {

	private static final long serialVersionUID = 1L;

	private ControllerMedia controllerMedia;
	private ControllerRiproduzione controllerRiproduzione;

	private JLabel etichettaTitolo;
	private JTextField campoRicerca;
	private JButton btnCerca;
	private JPanel pannelloGrigliaRisultati;
	private JLabel etichettaMessaggio;


	public PannelloHome() {
		this(null, null);
	}

	public PannelloHome(ControllerMedia controllerMedia, ControllerRiproduzione controllerRiproduzione) {
		this.controllerMedia = controllerMedia;
		this.controllerRiproduzione = controllerRiproduzione;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setBackground(Color.WHITE);
		setBounds(0, 0, 740, 518);
		setLayout(null);

		etichettaTitolo = new JLabel("Home");
		etichettaTitolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaTitolo.setForeground(new Color(33, 37, 41));
		etichettaTitolo.setBounds(24, 20, 300, 32);
		add(etichettaTitolo);

		campoRicerca = new JTextField();
		campoRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		campoRicerca.setColumns(10);
		campoRicerca.setBounds(24, 64, 440, 38);
		add(campoRicerca);

		btnCerca = new JButton("Cerca");
		btnCerca.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnCerca.setBackground(new Color(13, 110, 253));
		btnCerca.setForeground(Color.WHITE);
		btnCerca.setFocusPainted(false);
		btnCerca.setBounds(476, 64, 110, 38);
		add(btnCerca);

		JLabel etichettaSezione = new JLabel("Consigliati per te");
		etichettaSezione.setFont(new Font("Segoe UI", Font.BOLD, 13));
		etichettaSezione.setBounds(24, 120, 300, 22);
		add(etichettaSezione);

		pannelloGrigliaRisultati = new JPanel();
		pannelloGrigliaRisultati.setBackground(Color.WHITE);
		pannelloGrigliaRisultati.setLayout(new GridLayout(0, 4, 18, 18));

		JScrollPane scorrimentoGriglia = new JScrollPane(pannelloGrigliaRisultati);
		scorrimentoGriglia.setBorder(BorderFactory.createDashedBorder(new Color(222, 226, 230)));
		scorrimentoGriglia.setBounds(24, 146, 692, 240);
		add(scorrimentoGriglia);

		etichettaMessaggio = new JLabel("");
		etichettaMessaggio.setHorizontalAlignment(SwingConstants.CENTER);
		etichettaMessaggio.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		etichettaMessaggio.setForeground(new Color(108, 117, 125));
		etichettaMessaggio.setBounds(24, 400, 692, 24);
		add(etichettaMessaggio);
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
	}

	private void gestisciRicerca() {
		if (controllerMedia == null) {
			return;
		}
		String chiave = campoRicerca.getText().trim();
		if (chiave.isEmpty()) {
			caricaRaccomandati();
			return;
		}
		etichettaTitolo.setText("Risultati per \"" + chiave + "\"");
		mostraRisultatiRicerca(controllerMedia.cercaElementiPerTitolo(chiave));
	}

	private JPanel creaSchedaElemento(final ElementoMultimediale elemento) {
		JPanel scheda = new JPanel();
		scheda.setBackground(new Color(248, 249, 250));
		scheda.setBorder(new LineBorder(new Color(222, 226, 230)));
		scheda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		scheda.setLayout(null);

		JLabel etichettaTitoloScheda = new JLabel("<html><b>" + elemento.getTitolo() + "</b></html>");
		etichettaTitoloScheda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		etichettaTitoloScheda.setBounds(8, 96, 134, 20);
		scheda.add(etichettaTitoloScheda);

		String nomeAutore = (elemento.getAutore() == null) ? "" : elemento.getAutore().getNomeCompleto();
		JLabel etichettaAutore = new JLabel(nomeAutore);
		etichettaAutore.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		etichettaAutore.setForeground(new Color(108, 117, 125));
		etichettaAutore.setBounds(8, 118, 134, 20);
		scheda.add(etichettaAutore);

		scheda.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (controllerRiproduzione == null) {
					return;
				}
				controllerRiproduzione.avviaRiproduzione(elemento);
				FinestraPrincipale finestra = (FinestraPrincipale) SwingUtilities
						.getWindowAncestor(PannelloHome.this);
				if (finestra != null) {
					finestra.aggiornaDalControllerRiproduzione();
				}
			}
		});
		return scheda;
	}

	public void caricaRaccomandati() {
		if (controllerMedia == null) {
			return;
		}
		etichettaTitolo.setText("Home");
		campoRicerca.setText("");
		mostraRisultatiRicerca(controllerMedia.getElementiRaccomandati());
	}

	public void mostraRisultatiRicerca(List<ElementoMultimediale> risultati) {
		pannelloGrigliaRisultati.removeAll();

		if (risultati == null || risultati.isEmpty()) {
			etichettaMessaggio.setText("Nessun risultato per la ricerca");
		} else {
			etichettaMessaggio.setText("");
			for (ElementoMultimediale elemento : risultati) {
				pannelloGrigliaRisultati.add(creaSchedaElemento(elemento));
			}
		}
		pannelloGrigliaRisultati.revalidate();
		pannelloGrigliaRisultati.repaint();
	}
}
