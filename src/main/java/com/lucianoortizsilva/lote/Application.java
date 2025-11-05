package com.lucianoortizsilva.lote;

import javax.swing.SwingUtilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lucianoortizsilva.lote.ui.UISwing;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		new Thread(() -> SpringApplication.run(Application.class, args)).start();
		SwingUtilities.invokeLater(() -> new UISwing().setVisible(true));
	}

}