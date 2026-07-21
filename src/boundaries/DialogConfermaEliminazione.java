package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DialogConfermaEliminazione extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel;
	private JLabel etichettaMessaggio;
	private JButton btnConferma;
	private JButton btnAnnulla;
	private boolean confermato;

	
	public DialogConfermaEliminazione() {
		this(null, "Vuoi procedere con l'eliminazione?");
	}

	public DialogConfermaEliminazione(JFrame padre, String messaggio) {
		super(padre, "Conferma eliminazione", true);
		inizializzaGrafica();
		inizializzaEventi();
		etichettaMessaggio.setText(messaggio);
	}

	private void inizializzaGrafica() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 496, 279);
		setLocationRelativeTo(getParent());
		setResizable(false);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		etichettaMessaggio = new JLabel("");
		etichettaMessaggio.setVerticalAlignment(SwingConstants.TOP);
		etichettaMessaggio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		etichettaMessaggio.setBounds(30, 24, 420, 60);
		contentPanel.add(etichettaMessaggio);

		btnConferma = new JButton("Elimina");
		btnConferma.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnConferma.setBackground(new Color(220, 53, 69));
		btnConferma.setForeground(Color.WHITE);
		btnConferma.setFocusPainted(false);
		btnConferma.setBounds(30, 106, 250, 40);
		contentPanel.add(btnConferma);

		btnAnnulla = new JButton("Annulla");
		btnAnnulla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnAnnulla.setBackground(new Color(233, 236, 239));
		btnAnnulla.setFocusPainted(false);
		btnAnnulla.setBounds(292, 106, 158, 40);
		contentPanel.add(btnAnnulla);

		getRootPane().setDefaultButton(btnAnnulla);
	}

	private void inizializzaEventi() {
		btnConferma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confermato = true;
				dispose();
			}
		});

		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confermato = false;
				dispose();
			}
		});
	}

	public boolean isConfermato() {
		return confermato;
	}
}
