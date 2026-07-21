package boundaries;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.ControllerRiproduzione;
import control.ControllerUtente;
import db.StatisticheRiproduzioneDAOImpl;
import db.UtenteDAOImpl;

public class SchermataBenvenuto extends JFrame {

	private static final long serialVersionUID = 1L;

	private ControllerUtente controllerUtente;

	private JPanel contentPane;
	private JButton btnMostraLogin;
	private JButton btnMostraRegistrazione;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ControllerUtente controllerUtente = new ControllerUtente(new UtenteDAOImpl());
					SchermataBenvenuto finestra = new SchermataBenvenuto(controllerUtente);
					finestra.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public SchermataBenvenuto() {
		this(null);
	}

	public SchermataBenvenuto(ControllerUtente controllerUtente) {
		this.controllerUtente = controllerUtente;
		inizializzaGrafica();
		inizializzaEventi();
	}

	private void inizializzaGrafica() {
		setTitle("UninaMultiCloud");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 476, 337);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel etichettaLogo = new JLabel("UninaMultiCloud");
		etichettaLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		etichettaLogo.setForeground(new Color(33, 37, 41));
		etichettaLogo.setBounds(40, 26, 380, 34);
		contentPane.add(etichettaLogo);

		JLabel etichettaSlogan = new JLabel("La tua musica, ovunque.");
		etichettaSlogan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		etichettaSlogan.setForeground(new Color(33, 37, 41));
		etichettaSlogan.setBounds(40, 62, 380, 22);
		contentPane.add(etichettaSlogan);

		btnMostraLogin = new JButton("Accedi");
		btnMostraLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnMostraLogin.setBackground(new Color(13, 110, 253));
		btnMostraLogin.setForeground(Color.WHITE);
		btnMostraLogin.setFocusPainted(false);
		btnMostraLogin.setBounds(90, 110, 280, 44);
		contentPane.add(btnMostraLogin);

		btnMostraRegistrazione = new JButton("Registrati");
		btnMostraRegistrazione.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnMostraRegistrazione.setBackground(new Color(233, 236, 239));
		btnMostraRegistrazione.setForeground(new Color(33, 37, 41));
		btnMostraRegistrazione.setFocusPainted(false);
		btnMostraRegistrazione.setBounds(90, 166, 280, 44);
		contentPane.add(btnMostraRegistrazione);
	}

	private void inizializzaEventi() {
		btnMostraLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogLogin();
			}
		});

		btnMostraRegistrazione.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apriDialogSignUp();
			}
		});
	}

	private void apriDialogLogin() {
		DialogLogin dialogLogin = new DialogLogin(this, controllerUtente);
		dialogLogin.setVisible(true);
		if (dialogLogin.isLoginRiuscito()) {
			apriFinestraPrincipale();
		}
	}

	private void apriDialogSignUp() {
		DialogSignUp dialogSignUp = new DialogSignUp(this, controllerUtente);
		dialogSignUp.setVisible(true);
	}

	public void apriFinestraPrincipale() {
		ControllerRiproduzione controllerRiproduzione = new ControllerRiproduzione(controllerUtente,
				new StatisticheRiproduzioneDAOImpl());
		FinestraPrincipale finestraPrincipale = new FinestraPrincipale(controllerUtente, controllerRiproduzione);
		finestraPrincipale.setVisible(true);
		dispose();
	}
}
