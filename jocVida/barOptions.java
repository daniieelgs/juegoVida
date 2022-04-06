package jocVida;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//©Daniel Garcia (daniieelgs@gmail.com)

public class barOptions extends JPanel{

	private static final long serialVersionUID = 1L;
	private game game;
		
	public barOptions(game game) {
	
		super(new BorderLayout());
								
		setGame(game);
		
	}
	
	public barOptions() {
		
		super(new BorderLayout());
		
	}
	
	public void setGame(game game) {
		this.game = game;
	}
	
	public void start(String pathConf) {
		add(new buttonsPlay(), BorderLayout.WEST);
		setGeneration();
		setConfigButton(pathConf);	
		updateUI();
	}
	
	private void setConfigButton(String pathConf) {
		
		JLabel config = new JLabel();
		
		config.setIcon(cutImge("Images/config.png", 20));
		
		config.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));	
		
		add(config, BorderLayout.EAST);
		
		config.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		config.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				
				openModalConfig(pathConf);
				
			}
			
			public void mouseEntered(MouseEvent e) {
				
				config.setIcon(cutImge("Images/config_hover.png", 20));
				
			}
			
			public void mouseExited(MouseEvent e) {
				
				config.setIcon(cutImge("Images/config.png", 20));
				
			}
			
		});
		
	}
	
	private void openModalConfig(String pathConf) {
		
		
		JDialog modal = new JDialog();
		
		int ancho=Toolkit.getDefaultToolkit().getScreenSize().width;
		int alto=Toolkit.getDefaultToolkit().getScreenSize().height;
		
		modal.setBounds(ancho/2-ancho/4/2, alto/4, ancho/4, alto/2);
		
		JScrollPane scroll=new JScrollPane(new panelConfig(pathConf, modal));
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		scroll.getHorizontalScrollBar().setUnitIncrement(10);
		
		modal.add(scroll);
				
		modal.setModal(true);
		modal.setVisible(true);
		
	}
	
	private void setGeneration() {
		
		JLabel gen = new JLabel("Generation: " + game.getGeneration());
		
		add(gen, BorderLayout.CENTER);
		
		new Thread(new Runnable() {
			
			public void run() {

				while(true) gen.setText("Generation: " + game.getGeneration());
				
			}
		}).start();
		
	}
	
	private class buttonsPlay extends JPanel{
		
		private static final long serialVersionUID = 1L;

		public buttonsPlay() {
			
			super(new GridLayout(2, 1, 2, 2));
			
			JLabel play = new JLabel();
			JLabel reboot = new JLabel();
			
			final int sizeIcon = 20;
			
			play.setIcon(getImagePlaying(sizeIcon)[0]);

			reboot.setIcon(cutImge("Images/reboot.png", sizeIcon));
			
			play.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			reboot.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			play.setCursor(new Cursor(Cursor.HAND_CURSOR));
			reboot.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			add(play);
			add(reboot);
			
			updateUI();
			
			play.addMouseListener(new MouseAdapter() {
				
				public void mouseClicked(MouseEvent e) {
					
					if(game.isPlaying()) game.pause();
					else game.play(game.getGeneration());
					
					play.setIcon(getImagePlaying(sizeIcon)[1]);
					
				}
				
				public void mouseEntered(MouseEvent e) {
					
					play.setIcon(getImagePlaying(sizeIcon)[1]);

				}
				
				public void mouseExited(MouseEvent e) {
					
					play.setIcon(getImagePlaying(sizeIcon)[0]);
					
				}
				
			});

			reboot.addMouseListener(new MouseAdapter() {
				
				public void mouseClicked(MouseEvent e) {
					
					game.reboot();
					
					play.setIcon(getImagePlaying(sizeIcon)[0]);
					
				}
				
				public void mouseEntered(MouseEvent e) {
					
					reboot.setIcon(cutImge("Images/reboot_hover.png", sizeIcon));
					
				}
				
				public void mouseExited(MouseEvent e) {
					
					reboot.setIcon(cutImge("Images/reboot.png", sizeIcon));
					
				}
				
			});

			
		}
		
	}
	          
	private Icon[] getImagePlaying(int sizeIcon) {
				
		Icon icon[] = new Icon[2];
		
		try {
			BufferedImage buffer = ImageIO.read(barOptions.class.getResource(game.isPlaying() ? "Images/pause.png" : "Images/play.png"));
			icon[0] = new ImageIcon(buffer.getScaledInstance(sizeIcon, sizeIcon, Image.SCALE_DEFAULT));
			
			buffer = ImageIO.read(barOptions.class.getResource(game.isPlaying() ? "Images/pause_hover.png" : "Images/play_hover.png"));
			icon[1] = new ImageIcon(buffer.getScaledInstance(sizeIcon, sizeIcon, Image.SCALE_DEFAULT));

			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return icon;
		
	}
	
	private ImageIcon cutImge(String image, int sizeIcon) {
		
		try {
			BufferedImage buffer = ImageIO.read(barOptions.class.getResource(image));
			return new ImageIcon(buffer.getScaledInstance(sizeIcon, sizeIcon, Image.SCALE_DEFAULT));

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
