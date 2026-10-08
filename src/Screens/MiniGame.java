package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiniGame {	
	private JFrame frame;
	private JLabel loseLabel, winLabel, keyLabel, blueLabel, yellowLabel, greenLabel, magentaLabel, orangeLabel;
	private JPanel losePanel, winPanel, submitPanel, mainPanel, keyPanel, clickCountPanel, clickCountPanel2, clickCountPanel3, clickCountPanel4, clickCountPanel5, clickCountPanel6;
	private JButton button1, button2, button3, button4, button5, button6;
    
    public MiniGame(String frameTitle, int frameWidth, int frameHeight) {

    	frame = new JFrame(frameTitle);
		frame.setSize(frameWidth, frameHeight);
        
        frame.setLayout(new GridBagLayout());
		
		mainPanel = new JPanel();
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
		button1.setBackground(Color.BLUE);
		button1.setOpaque(true);
		button1.setBorderPainted(false);
		
		button2 = new JButton ("2");
		button2.setBackground(Color.BLUE);
		button2.setOpaque(true);
		button2.setBorderPainted(false);
		     
		button3 = new JButton ("3");
		button3.setBackground(Color.BLUE);
		button3.setOpaque(true);
		button3.setBorderPainted(false);     
		
		button4 = new JButton ("4");
		button4.setBackground(Color.BLUE);
		button4.setOpaque(true);
		button4.setBorderPainted(false);
		
		button5 = new JButton ("5");
		button5.setBackground(Color.BLUE);
		button5.setOpaque(true);
		button5.setBorderPainted(false);
		
		button6 = new JButton ("Submit!");
		
		
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
       
       
        // labels for the keys
        keyLabel = new JLabel("Key:", SwingConstants.CENTER);
        keyLabel.setForeground(Color.BLACK);
        keyLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        blueLabel = new JLabel("1", SwingConstants.CENTER);
        blueLabel.setForeground(Color.WHITE);
        blueLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        yellowLabel = new JLabel("2", SwingConstants.CENTER);
        yellowLabel.setForeground(Color.WHITE);
        yellowLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        greenLabel = new JLabel("3", SwingConstants.CENTER);
        greenLabel.setForeground(Color.WHITE);
        greenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		magentaLabel = new JLabel("4", SwingConstants.CENTER);
        magentaLabel.setForeground(Color.WHITE);
        magentaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        orangeLabel = new JLabel("5", SwingConstants.CENTER);
        orangeLabel.setForeground(Color.WHITE);
        orangeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // background color for keys
    	clickCountPanel6 = new JPanel();
    	clickCountPanel6.setBackground(Color.WHITE);
        clickCountPanel6.setMaximumSize(new Dimension(60, 40));        
        clickCountPanel6.add(keyLabel);
         
        clickCountPanel = new JPanel();
		clickCountPanel.setBackground(Color.BLUE);
        clickCountPanel.setMaximumSize(new Dimension(60, 40));
        clickCountPanel.add(blueLabel);

        clickCountPanel2 = new JPanel();
		clickCountPanel2.setBackground(Color.YELLOW);
        clickCountPanel2.setMaximumSize(new Dimension(60, 40));
        clickCountPanel2.add(yellowLabel);

        clickCountPanel3 = new JPanel();
		clickCountPanel3.setBackground(Color.GREEN);
        clickCountPanel3.setMaximumSize(new Dimension(60, 40));
        clickCountPanel3.add(greenLabel);

        clickCountPanel4 = new JPanel();
		clickCountPanel4.setBackground(Color.MAGENTA);
        clickCountPanel4.setMaximumSize(new Dimension(60, 40));
        clickCountPanel4.add(magentaLabel);

        clickCountPanel5 = new JPanel();
		clickCountPanel5.setBackground(Color.ORANGE);
        clickCountPanel5.setMaximumSize(new Dimension(60, 40));
        clickCountPanel5.add(orangeLabel);


		keyPanel.add(clickCountPanel6);
		//keyPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
		keyPanel.add(clickCountPanel);
		//keyPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
		keyPanel.add(clickCountPanel2);
		//keyPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
		keyPanel.add(clickCountPanel3);
		//keyPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
		keyPanel.add(clickCountPanel4);
		//keyPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
		keyPanel.add(clickCountPanel5);
	
		mainPanel.add(button1);
		mainPanel.add(button2);
		mainPanel.add(button3);
		mainPanel.add(button4);
		mainPanel.add(button5);
		submitPanel.add(button6);
     //   mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
   	
		frame.add(mainPanel, l);
		frame.add(keyPanel, k);
		frame.add(submitPanel, m);
        addButtonListener(button1);
        addButtonListener(button2);
        addButtonListener(button3);
        addButtonListener(button4);
        addButtonListener(button5); 
        addButtonListener(button6); 
        
        frame.setVisible(true);
    }

    private void addButtonListener(JButton button) {
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
    }

	private void checkCorrect(){ 
		int correct = 0;
		boolean isCorrect = false;
		
		GridBagConstraints k = new GridBagConstraints();
		k.gridx = 0;
		k.gridy = 3;
		k.anchor = GridBagConstraints.CENTER;
			
			if (button1.getBackground() == Color.BLUE){
				correct++;
			} 
			if (button2.getBackground() == Color.YELLOW){
				correct++;
			}
			if (button3.getBackground() == Color.GREEN){
				correct++;
			} 
			if (button4.getBackground() == Color.MAGENTA){
				correct++;
			}	
			if (button5.getBackground() == Color.ORANGE){
				correct++;
			} 
			
			
			if (correct == 5) {
				isCorrect = true;
			} else {
				isCorrect = false;
			}
			
			if (isCorrect == true) {
				winPanel = new JPanel();  
				winPanel.setBackground(Color.WHITE); 
				winLabel = new JLabel("You win! Congrats", SwingConstants.CENTER);
				winLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
				winPanel.add(winLabel);
				
				frame.getContentPane().removeAll();
				frame.add(winPanel);
				frame.revalidate();
				frame.repaint();
			} else {
				losePanel = new JPanel(); 
				losePanel.setLayout(new BoxLayout(losePanel, BoxLayout.X_AXIS));
				loseLabel = new JLabel("You lost, try again!", SwingConstants.CENTER);
				loseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				losePanel.add(loseLabel);
				frame.add(losePanel, k);
				frame.revalidate();
				frame.repaint();
			}
			
			if (correct == 5) {
				isCorrect = true;
			} else {
				isCorrect = false;
			}

	}


    private void changeBackgroundColor(JButton button) {
		Color[] colorChoices = {Color.MAGENTA, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.BLUE};
		int min = 0;
		int max = 4;
		int randomNum = (int) (Math.random() * (max - min + 1) + min);
		Color newColor = Color.WHITE;

		for (int i = 0; i<colorChoices.length; i++) {
			newColor = colorChoices[randomNum] ;
		    button.setBackground(newColor);
		}
        
    }

    public static void main(String[] args) {
    	new MiniGame("Minigame 1", 500, 500);
    }
}
