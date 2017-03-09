package catan.settlers.server.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;

import catan.settlers.network.server.Server;

public class ServerWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabPane;
	private JButton startStopButton;

	private Server server;
	private LogPanel log;
	private SettingsPanel settings;

	public ServerWindow() {
		initialize();

		Server.getInstance().setGui(this);
		server = Server.getInstance();

		log = new LogPanel();
		settings = new SettingsPanel(server);

		addTab("Log", log);
		addTab("Settings", settings);
	}

	private void initialize() {
		this.setTitle("Settlers of Catan - Server");
		this.setResizable(false);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Ignore
		}

		tabPane = new JTabbedPane();
		startStopButton = new JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		startStopButton.setText("Start");
		startStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				startStopButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabPane)
				.addGroup(GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(948, Short.MAX_VALUE).addComponent(startStopButton,
										GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(tabPane, GroupLayout.PREFERRED_SIZE, 642, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(startStopButton, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
						.addContainerGap()));

		this.pack();
		this.setVisible(true);
	}

	protected void startStopButtonActionPerformed(ActionEvent evt) {
		if (server.isRunning()) {
			server.close();
			startStopButton.setText("Start");
		} else {
			try {
				Server.resetInstance();
				server = Server.getInstance();
				server.setGui(this);
				server.launch();
				startStopButton.setText("Stop");
			} catch (Exception e) {
				// Ignore
				e.printStackTrace();
			}
		}
	}

	public void addTab(String title, JPanel panel) {
		tabPane.addTab(title, panel);
		this.pack();
	}

	public void writeToLog(String msg) {
		log.writeToLog(msg);
	}

}
