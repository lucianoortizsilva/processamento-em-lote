package com.lucianoortizsilva.lote.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import com.lucianoortizsilva.lote.rabbitmq.Payload;
import com.lucianoortizsilva.lote.rabbitmq.RabbitMQProducer;

public class UISwing extends JFrame {

	private static final long serialVersionUID = 7168314514179629668L;
	private final GridBagConstraints gridBagConstraintsPrincipal = new GridBagConstraints();
	private final JPanel panelPrincipal = new JPanel(new GridBagLayout());
	private final JPanel panelButtonOrLoading = new JPanel(new GridBagLayout());
	private final JComboBox<JobName> comboBox = new JComboBox<>();
	private final JButton button = new JButton("Mensagem enviada");
	private final JLabel labelLoading = new JLabel();

	public UISwing() {
		initJFramePrincipal();
		initGridBagConstraintsPrincipal();
		loadDocTypes();
		addComboBox();
		addLine();
		addLine();
		addButtonAndLoadingGif();
		addLine();
		add(panelPrincipal);
		button.addActionListener(e -> send());
		setLocationRelativeTo(null);
	}

	private void initJFramePrincipal() {
		setTitle("Mensagem RabbitMQ");
		setSize(300, 240);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
	}

	private void initGridBagConstraintsPrincipal() {
		gridBagConstraintsPrincipal.insets = new Insets(8, 8, 8, 8);
		gridBagConstraintsPrincipal.gridx = 0;
		gridBagConstraintsPrincipal.gridy = 0;
		gridBagConstraintsPrincipal.anchor = GridBagConstraints.WEST;
	}

	private void loadDocTypes() {
		try {
			comboBox.setPreferredSize(new Dimension(200, 30));
			comboBox.setName("jobName");
			final JobName livraria = new JobName("livraria");
			final JobName netflix = new JobName("netflix");
			final List<JobName> docTypes = List.of(livraria, netflix);
			comboBox.removeAllItems();
			for (final JobName job : docTypes) {
				comboBox.addItem(job);
			}
		} catch (final Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading job names: " + ex.getMessage());
			disableButtonValidate();
		}
	}

	private void send() {
		final JobName selectedDocType = (JobName) comboBox.getSelectedItem();
		disableAllFields();
		loading();
		sendMessageToRabbitMQ(selectedDocType.description());
	}

	private void sendMessageToRabbitMQ(final String jobName) {
		final SwingWorker<Void, Void> worker = new SwingWorker<>() {

			@Override
			protected Void doInBackground() throws Exception {
				final RabbitMQProducer rabbitMQProducer = new RabbitMQProducer();
				final Payload payload = new Payload();
				payload.setName(jobName);
				rabbitMQProducer.send(payload);
				return null;
			}

			@Override
			protected void done() {
				try {
					get();
					showAutoCloseDialog(UISwing.this, "Info", "Mensagem enviada", 3000);
				} catch (final Exception ex) {
					showAutoCloseDialog(UISwing.this, "Error", ex.getMessage(), 3000);
				} finally {
					enableFields();
					disableGifLoading();
				}
			}
		};
		worker.execute();
	}

	private void loading() {
		labelLoading.setVisible(true);
	}

	private void disableAllFields() {
		comboBox.setEditable(false);
		comboBox.setEnabled(false);
		button.setEnabled(false);
		button.setVisible(false);
	}

	private void enableFields() {
		button.setVisible(true);
		button.setEnabled(true);
		comboBox.setEnabled(true);
		comboBox.setEditable(false);
	}

	private void addLine() {
		gridBagConstraintsPrincipal.gridy++;
	}

	private void addComboBox() {
		panelPrincipal.add(new JLabel("Selecione o Job:*"), gridBagConstraintsPrincipal);
		addLine();
		panelPrincipal.add(comboBox, gridBagConstraintsPrincipal);
	}

	private void addButtonAndLoadingGif() {
		panelButtonOrLoading.removeAll();
		panelButtonOrLoading.setOpaque(false);
		final GridBagConstraints gridBagConstraintsInterno = new GridBagConstraints();
		initButtonCenterAndEnable(gridBagConstraintsInterno, gridBagConstraintsPrincipal);
		initLoadingCenterAndEnable(gridBagConstraintsInterno);
	}

	private void initLoadingCenterAndEnable(final GridBagConstraints gridBagConstraintsInterno) {
		labelLoading.setIcon(new ImageIcon(getClass().getResource("/static/rabbitmq.gif")));
		labelLoading.setVisible(false);
		gridBagConstraintsInterno.gridx = 0;
		gridBagConstraintsInterno.gridy = 0;
		gridBagConstraintsInterno.anchor = GridBagConstraints.CENTER;
		panelButtonOrLoading.add(labelLoading, gridBagConstraintsInterno);
		labelLoading.setVisible(false);
		button.setVisible(true);
		panelPrincipal.add(panelButtonOrLoading, gridBagConstraintsPrincipal);
		gridBagConstraintsPrincipal.gridwidth = 1;
		addLine();
	}

	private void initButtonCenterAndEnable(final GridBagConstraints gridBagConstraintsInterno, final GridBagConstraints gridBagConstraintsPrincipal) {
		gridBagConstraintsInterno.gridx = 0;
		gridBagConstraintsInterno.gridy = 0;
		gridBagConstraintsInterno.anchor = GridBagConstraints.CENTER;
		panelButtonOrLoading.add(button, gridBagConstraintsInterno);
		gridBagConstraintsPrincipal.gridx = 0;
		gridBagConstraintsPrincipal.gridwidth = 2;
		gridBagConstraintsPrincipal.anchor = GridBagConstraints.CENTER;
	}

	private void disableGifLoading() {
		labelLoading.setVisible(false);
	}

	private void disableButtonValidate() {
		button.setVisible(false);
		button.setEnabled(false);
	}

	public static void showAutoCloseDialog(final JFrame parent, final String type, final String message, final int durationMillis) {
		final JDialog dialog = new JDialog(parent, type, true);
		final JLabel label = new JLabel(message, SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(200, 50));
		dialog.getContentPane().add(label);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				dialog.dispose();
			}
		}, durationMillis);
		new Thread(() -> dialog.setVisible(true)).start();
	}

	public record JobName(String description) {

		@Override
		public final String toString() {
			return description;
		}
	}
}