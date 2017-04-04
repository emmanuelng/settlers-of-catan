package catan.settlers.server.view;

import java.awt.Font;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class LogPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTextPane textPane;

	public LogPanel() {
		init();
	}

	private void init() {
		scrollPane = new javax.swing.JScrollPane();
		textPane = new JTextPane();

		textPane.setEditable(false);
		textPane.setFont(new Font("Consolas", 0, 16)); // NOI18N
		scrollPane.setViewportView(textPane);

		javax.swing.GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
	}

	public void writeToLog(String msg) {
		String currentText = textPane.getText();
		String newLine = "[" + new Date() + "] " + msg;

		if (currentText.length() > 0) {
			textPane.setText(currentText + "\n" + newLine);
		} else {
			textPane.setText(newLine);
		}
	}

}
