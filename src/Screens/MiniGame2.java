package Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Sound.SoundEffect;
import Sound.SoundManager;

public class MiniGame2 {	
	private JFrame frame;
    private JLabel loseLabel, winLabel, sLabel;
	private JPanel losePanel, winPanel, sPanel, topPanel, midPanel, bottomPanel;
	private JButton button, button2, button3, button4, button5, button6, button7, button8, button9;    
    private ImageIcon icon, icon2, icon3, icon4, icon5, icon6, icon7, icon8, icon9;
	private int sCounter = 1;
	private int clickCounter = 1;
	private int[] placeHolder = new int[3];
	private JButton[] buttonChoices;
	private static boolean winGame;

    public MiniGame2(String frameTitle, int frameWidth, int frameHeight) {
	    frame = new JFrame(frameTitle);
		frame.setSize(frameWidth, frameHeight);
        frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setOpaque(true);

		midPanel = new JPanel();
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        midPanel.setOpaque(true);
        midPanel.setBackground(Color.WHITE);

		bottomPanel = new JPanel();    
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(Color.WHITE);

		sPanel = new JPanel();  
		sPanel.setBackground(Color.WHITE); 
		sLabel = new JLabel("Stage: " + sCounter, SwingConstants.CENTER);

		sLabel.setAlignmentY(Component.LEFT_ALIGNMENT);
		sPanel.add(sLabel);

		losePanel = new JPanel(); 
		losePanel.setLayout(new BoxLayout(losePanel, BoxLayout.X_AXIS));
		loseLabel = new JLabel("You lost, try again!", SwingConstants.CENTER);
		loseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		losePanel.add(loseLabel);

		GridBagConstraints k = new GridBagConstraints();
		k.gridx = 0;
		k.gridy = 0;
		k.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints l = new GridBagConstraints();
		l.gridx = 0;
		l.gridy = 1;
		l.anchor = GridBagConstraints.CENTER;
	
		GridBagConstraints m = new GridBagConstraints();
		m.gridx = 0;
		m.gridy = 2;
		m.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints s = new GridBagConstraints();
		s.gridx = 0;
		s.gridy = 3;
		s.anchor = GridBagConstraints.CENTER;

		icon = new ImageIcon("Resources/Tile1.png");
		button = new JButton (icon);

		icon2 = new ImageIcon("Resources/Triangle.png");
		button2 = new JButton (icon2);

		icon3 = new ImageIcon("Resources/Circle.png");
		button3 = new JButton (icon3);

		icon4 = new ImageIcon("Resources/Arrow.png");
		button4 = new JButton (icon4);

		icon5 = new ImageIcon("Resources/Swirl.png"); 
		button5 = new JButton (icon5);	
		
		icon6 = new ImageIcon("Resources/Umbrella.png"); 
		button6 = new JButton (icon6);

		icon7 = new ImageIcon("Resources/Mountains.png");
		button7 = new JButton (icon7);

		icon8 = new ImageIcon("Resources/Idk.png"); 
		button8 = new JButton (icon8);

		icon9 = new ImageIcon("Resources/Hourglass.png");
		button9 = new JButton (icon9);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(80, 80));
        button.setMaximumSize(new Dimension(80, 80));
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
		button6.setAlignmentX(Component.CENTER_ALIGNMENT);
        button6.setPreferredSize(new Dimension(80, 80));
        button6.setMaximumSize(new Dimension(80, 80));		
        button7.setAlignmentX(Component.CENTER_ALIGNMENT);
        button7.setPreferredSize(new Dimension(80, 80));
        button7.setMaximumSize(new Dimension(80, 80));
        button8.setAlignmentX(Component.CENTER_ALIGNMENT);
        button8.setPreferredSize(new Dimension(80, 80));
        button8.setMaximumSize(new Dimension(80, 80));		
		button9.setAlignmentX(Component.CENTER_ALIGNMENT);
        button9.setPreferredSize(new Dimension(80, 80));
        button9.setMaximumSize(new Dimension(80, 80));	

		topPanel.add(button);
		topPanel.add(button2);
		topPanel.add(button3);
		midPanel.add(button4);
		midPanel.add(button5);
		midPanel.add(button6);
		bottomPanel.add(button7);
		bottomPanel.add(button8);
		bottomPanel.add(button9);
		frame.add(sPanel, k);
		frame.add(topPanel, l);
		frame.add(midPanel, m);
		frame.add(bottomPanel, s);

		buttonChoices = new JButton[] {button, button2, button3, button4, button5, button6, button7, button8, button9};
    	addButtonListener(button);
        addButtonListener(button2);
        addButtonListener(button3);
        addButtonListener(button4);
        addButtonListener(button5); 
        addButtonListener(button6); 
        addButtonListener(button7);
        addButtonListener(button8);
        addButtonListener(button9);

        frame.setVisible(true);
		stageController(buttonChoices);
    }
	
    private void addButtonListener(JButton playerButton) {
        playerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				checkCorrect(playerButton, placeHolder);
            }
        });
    }

	public void checkCorrect (JButton playerButton, int[] placeHolder) {
		GridBagConstraints n = new GridBagConstraints();
		n.gridx = 0;
		n.gridy = 4;
		n.anchor = GridBagConstraints.CENTER;

			if (clickCounter < sCounter && playerButton == buttonChoices[placeHolder[clickCounter -1]]) {
				clickCounter++;
			} else if (clickCounter == 3 && playerButton == buttonChoices[placeHolder[clickCounter-1]]){ //&& playerButton == buttonChoices[placeHolder[clickCounter-1]]){
				SoundManager.play(SoundEffect.PUZZLE_COMPLETE);
				winPanel = new JPanel();  
				winPanel.setBackground(Color.WHITE); 
				winLabel = new JLabel("You win! Congrats", SwingConstants.CENTER);
				winLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
				winPanel.add(winLabel);
				setWinGame(true);
				frame.getContentPane().removeAll();
				frame.add(winPanel);
				frame.revalidate();
				frame.repaint();
			} else if (clickCounter == sCounter && playerButton == buttonChoices[placeHolder[clickCounter-1]]) {
				sCounter++;
				sLabel.setText("Stage: " + sCounter);
				clickCounter = 1;
				stageController(buttonChoices);
			} else {
				frame.add(losePanel, n);
				setWinGame(false);
				frame.revalidate();
				frame.repaint();
				sCounter = 1;
				clickCounter = 1;
				sLabel.setText("Stage: " + sCounter);
				stageController(buttonChoices);
			} 
	}

	public void stageController(JButton[] buttonChoices) {
		if (sCounter == 1) {
			int randomButton = (int) (Math.random() * (8 - 0 + 1) + 0);
			placeHolder[0] = randomButton;
				Timer timer = new Timer(1000, e -> { 
					buttonChoices[randomButton].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				});
				timer.start();
				timer.setRepeats(false);

				Timer timer2 = new Timer(2000, e -> { 
					buttonChoices[randomButton].setBorder(BorderFactory.createEmptyBorder());
				});
				timer2.start();
				timer2.setRepeats(false);
		}

		if (sCounter == 2) {
			int randomButton = (int) (Math.random() * (8 - 0 + 1) + 0);
			placeHolder[0] = randomButton;
				Timer timer = new Timer(1000, e -> { 
					buttonChoices[randomButton].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				});
				timer.start();
				timer.setRepeats(false);

				Timer timer2 = new Timer(2000, e -> { 
					buttonChoices[randomButton].setBorder(BorderFactory.createEmptyBorder());
				});
				timer2.start();
				timer2.setRepeats(false);

			int randomButton2 = (int) (Math.random() * (8 - 0 + 1) + 0);
			placeHolder[1] = randomButton2;
				Timer timer3 = new Timer(2350, e -> { 
					buttonChoices[randomButton2].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				});
				timer3.start();
				timer3.setRepeats(false);

				Timer timer4 = new Timer(3350, e -> { 
					buttonChoices[randomButton2].setBorder(BorderFactory.createEmptyBorder());
				});
				timer4.start();
				timer4.setRepeats(false);

		}

		if (sCounter == 3) {
			int randomButton = (int) (Math.random() * (8 - 0 + 1) + 0);
			placeHolder[0] = randomButton;
				Timer timer = new Timer(1000, e -> { 
					buttonChoices[randomButton].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				});
				timer.start();
				timer.setRepeats(false);

				Timer timer2 = new Timer(2000, e -> { 
					buttonChoices[randomButton].setBorder(BorderFactory.createEmptyBorder());
				});
				timer2.start();
				timer2.setRepeats(false);


			int randomButton2 = (int) (Math.random() * (8 - 0 + 1) + 0);
			placeHolder[1] = randomButton2;
				Timer timer3 = new Timer(2350, e -> { 
					buttonChoices[randomButton2].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				});
				timer3.start();
				timer3.setRepeats(false);

				Timer timer4 = new Timer(3350, e -> { 
					buttonChoices[randomButton2].setBorder(BorderFactory.createEmptyBorder());
				});
				timer4.start();
				timer4.setRepeats(false);
									

			int randomButton3 = (int) (Math.random() * (8 - 0 + 1) + 0);
			placeHolder[2] = randomButton3;
				Timer timer5 = new Timer(3700, e -> { 
					buttonChoices[randomButton3].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
				});
				timer5.start();
				timer5.setRepeats(false);

				Timer timer6 = new Timer(4700, e -> { 
					buttonChoices[randomButton3].setBorder(BorderFactory.createEmptyBorder());
				});
				timer6.start();
				timer6.setRepeats(false);
		}
	}

	public static void setWinGame (boolean value) {
		winGame = value;
	}

	public static boolean getWinGame () {
		return winGame;
	}

	public static void main(String[] args) {
        new MiniGame2("Minigame 2", 500, 500);
    }
}