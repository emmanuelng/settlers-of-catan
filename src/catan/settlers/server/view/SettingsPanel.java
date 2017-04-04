package catan.settlers.server.view;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import catan.settlers.network.server.Server;
import catan.settlers.network.server.ServerSettings;

public class SettingsPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel jLabel1;
	private JLabel jLabel2;
	private JTextField jTextField1;
	private JButton jButton1;
	private JButton jButton2;

	private Server server;

	public SettingsPanel(Server server) {
		this.server = server;

		init();
		updateSettings();
	}

	private void updateSettings() {
		jTextField1.setText("" + server.getSettings().getPort());
	}

	private void init() {
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jTextField1 = new JTextField();
		jButton1 = new JButton();
		jButton2 = new JButton();

		jLabel1.setFont(new Font("Tahoma", 1, 14));
		jLabel1.setText("Server settings");

		jLabel2.setText("Port number");

		jButton1.setText("Save");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});

		jButton2.setText("Cancel");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabel1)
						.addGroup(layout.createSequentialGroup().addComponent(jLabel2).addGap(18, 18, 18).addComponent(
								jTextField1, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(847, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jButton2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jButton1).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(
						jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 483, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton1)
						.addComponent(jButton2))
				.addContainerGap()));
	}

	protected void saveButtonActionPerformed(ActionEvent evt) {
		ServerSettings newSettings = new ServerSettings();

		newSettings.setPort(Integer.parseInt(jTextField1.getText()));

		server.setSettings(newSettings);
		updateSettings();
	}

	protected void cancelButtonActionPerformed(ActionEvent evt) {
		updateSettings();
	}

}
