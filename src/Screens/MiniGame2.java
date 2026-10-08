package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiniGame2 {	
	private JFrame frame;
	//private JLabel ;
	//private JPanel mainPanel, ;
	//private JButton button, button2, button3, button4, button5, button6, button7, button8, button9;    
    
    public MiniGame2(String frameTitle, int frameWidth, int frameHeight) {
	    frame = new JFrame(frameTitle);
		frame.setSize(frameWidth, frameHeight);
        frame.setLayout(new GridBagLayout());
		
		/*mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setOpaque(true);

		keyPanel = new JPanel();
        keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.X_AXIS));
        keyPanel.setOpaque(true);
        keyPanel.setBackground(Color.WHITE);

		submitPanel = new JPanel();    

		GridBagConstraints k = new GridBagConstraints();
		k.gridx = 0;
		k.gridy = 0;
		k.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints l = new GridBagConstraints();
		l.gridx = 0;
		l.gridy = 1;
		l.anchor = GridBagConstraints.FIRST_LINE_START;
	
		GridBagConstraints m = new GridBagConstraints();
		m.gridx = 0;
		m.gridy = 2;
		m.anchor = GridBagConstraints.CENTER;
		
        // Create button
		button1 = new JButton ("1");
		button1.setBackground(Color.WHITE);
		button1.setOpaque(true);
		button1.setBorderPainted(false);
		
		button2 = new JButton ("2");
		button2.setBackground(Color.WHITE);
		button2.setOpaque(true);
		button2.setBorderPainted(false);
		     
		button3 = new JButton ("3");
		button3.setBackground(Color.WHITE);
		button3.setOpaque(true);
		button3.setBorderPainted(false);     
		
		button4 = new JButton ("4");
		button4.setBackground(Color.WHITE);
		button4.setOpaque(true);
		button4.setBorderPainted(false);
		
		button5 = new JButton ("5");
		button5.setBackground(Color.WHITE);
		button5.setOpaque(true);
		button5.setBorderPainted(false);
		
		button6 = new JButton ("6");
		button6.setBackground(Color.WHITE);
		button6.setOpaque(true);
		button6.setBorderPainted(false);
		
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        button1.setPreferredSize(new Dimension(80, 80));
        button1.setMaximumSize(new Dimension(80, 80));
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.setPreferredSize(new Dimension(80, 80));
        button2.setMaximumSize(new Dimension(80, 80));
        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
        button3.setPreferredSize(new Dimension(80, 80));
        button3.setMaximumSize(new Dimension(80, 80));
        button4.setAlignmentX(Component.CENTER_ALIGNMENT);
        button4.setPreferredSize(new Dimension(80, 80));
        button4.setMaximumSize(new Dimension(80, 80));
        button5.setAlignmentX(Component.CENTER_ALIGNMENT);
        button5.setPreferredSize(new Dimension(80, 80));
        button5.setMaximumSize(new Dimension(80, 80));		
		
		frame.add(mainPanel, l);
		frame.add(keyPanel, k);
		frame.add(submitPanel, m);
        addButtonListener(button1);
        addButtonListener(button2);
        addButtonListener(button3);
        addButtonListener(button4);
        addButtonListener(button5); 
        addButtonListener(button6); */
        
        frame.setVisible(true);
    }
	
  /*  private void addButtonListener(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

				if (button == button6) {
					checkCorrect();
				} else {
					changeBackgroundColor(button);
				}
            }
        });
    }*/

	
	
	public static void main(String[] args) {
    	new MiniGame2("Minigame 2", 500, 500);
    }
	
}
	
