package view;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;

class WindowFrame extends JFrame
{
	private Menu menu_panel = new Menu();
	private Game game_panel = new Game();
	private CardLayout layout = new CardLayout();
	
	protected WindowFrame()
	{
		super("Xadrez");
		
		getContentPane().setLayout(layout);
		
		menu_panel.setUp();
		game_panel.setUp();
		
		menu_panel.setBounds(0, 0, 512, 512);
		game_panel.setBounds(0, 0, 512, 512);
		
		showPanel(menu_panel);
		
		setUp();
	}
	
	protected void showPanel(Component target_component)
	{
		getContentPane().removeAll();
		getContentPane().add(target_component);

		target_component.setVisible(true);

		revalidate();
		repaint();
	}

	private void setUp()
	{
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	 
	// Métodos get()
	
	protected Menu getMenuPanel() { return menu_panel; } 
	
	protected Game getGamePanel() { return game_panel; } 
}