package jocVida;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//©Daniel Garcia (daniieelgs@gmail.com)

public class jocVida {

	private static JFrame frame;
	final static String folder = "conf";
	final static String file = "data.config";
	
	public static void main(String[] args) {
		
		initTerminal();
		
		if(!configurationFolder(folder, file)) JOptionPane.showMessageDialog(null, "No se puedo crear la configuración", "Conf Error", JOptionPane.ERROR_MESSAGE);
		
		initGUI("Joc de la Vida", folder+"/"+file);
				
	}
	
	private static boolean configurationFolder(String folder, String file) {
		
		System.out.println("[log] Checking conf: " + folder + "/" + file);
		
		File source = new File(folder);
		
		if(!source.exists()) source.mkdir();
		
		File conf = new File(source, file);
		
		if(!conf.exists()) config.saveConfigurationFile(conf.getAbsolutePath(), config.createDefaultConfig());
		
		return conf.exists();
	}
	
	private static boolean deleteConfiguration(String folder, String file) {

		System.out.println("[log] Deleting conf: " + folder + "/" + file);
		
		File source = new File(folder);
		
		if(source.exists()) {
		
			File conf = new File(source, file);
			
			if(conf.exists()) conf.delete();
			source.delete();
		}
		
		return new File(source, file).exists();
		
	}
	
	private static void initGUI(String title, String pathConf) {
		
		System.out.println("[log] Starting '" + title + "' on conf: " + pathConf);
		
		frame = createFrame(title);

		createComponents(frame, pathConf);
		
	}
	
	private static JFrame createFrame(String title) {
		
		JFrame frame = new JFrame(title);
		
		frame.setIconImage(new ImageIcon(jocVida.class.getResource("Images/icon.png")).getImage());
		
		int ancho=Toolkit.getDefaultToolkit().getScreenSize().width;
		int heigh=Toolkit.getDefaultToolkit().getScreenSize().height;
		
		frame.setBounds(ancho/4, heigh/2-(ancho/4), ancho/2, ancho/2);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		return frame;
		
	}
	
	private static void createComponents(JFrame frame, String pathConf) {
		
		dataConfig conf = config.readConfigurationFile(pathConf);
		
		barOptions top=new barOptions();
		
		map map=new map(conf.getSizeX(), conf.getSizeY(), conf.getRuleLive(), conf.getRuleDie(), new Dimension(frame.getSize().width, (int)(frame.getSize().height-top.getPreferredSize().getHeight())));

		top.setMinimumSize(new Dimension(frame.getWidth(), 48));
		top.setPreferredSize(new Dimension(frame.getWidth(), 48));
		//top.setBackground(Color.BLACK);
		frame.add(top, BorderLayout.NORTH);
		
		
		frame.add(map, BorderLayout.CENTER);
		
		frame.setVisible(true);
		
		game game;
		
		if(conf.getMode()==dataConfig.MODE_AUTOMATIC)
			game = new game(map, conf.getVel(), conf.getInitialGroup());
		else
			game = new game(map, conf.getVel());
		
		top.setGame(game);
		top.start(pathConf);
		
	}
	
	public static void restart(String pathConf) {
		
		System.out.println("[log] Restarting...");
		
		frame.dispose();
		
		initGUI("Joc de la Vida", pathConf);
		
	}

	private static void initTerminal() {
		
		new Thread(new Runnable() {
			
			@SuppressWarnings("resource")
			public void run() {
			
				System.out.println("[log] Terminal: ON");
				
				Scanner scan = new Scanner(System.in);				
							
				while(true) {
				
					String command = scan.nextLine();
					
					String[] param = command.split(" ");
					
					String ex = param[0];
					
					if(ex.equals("recover")) {
						deleteConfiguration(folder, file);
						if(!configurationFolder(folder, file)) JOptionPane.showMessageDialog(null, "No se puedo crear la configuración", "Conf Error", JOptionPane.ERROR_MESSAGE);
						restart(folder+"/"+file);
						System.out.println("recovering...");
					}else if(ex.equals("close")) frame.dispose();
					else if(ex.equals("restart")) restart(param[1]+"/"+param[2]);
					else if(ex.equals("exit")) System.exit(0);
					else if(ex.equals("start")) {
												
						String title="";
						
						for(int i=3; i<param.length; i++) title+=param[i] + " ";
												
						initGUI(title, param[1]+"/"+param[2]);
					}else if(ex.equals("delete-config")) {
												
						if(!deleteConfiguration(param[1], param[2])) System.out.println("Deleted");
						else System.out.println("Can not delete " + param[1] + "/" + param[2]);
						
					}else if(ex.equals("create-config")) {
												
						if(configurationFolder(param[1], param[2])) System.out.println("Created");
						else System.out.println("Can not create " + param[1] + "/" + param[2]);

					}else if(ex.equals("develop")) System.out.println("//©Daniel Garcia (daniieelgs@gmail.com)");
						
					else {
						
						System.out.println("recover");
						System.out.println("close");
						System.out.println("restart");
						System.out.println("exit");
						System.out.println("start [folder] [file] [title]");
						System.out.println("delete-config [file] [title]");
						System.out.println("create-config [file] [title]");

					}
					
				}
				
			}
			
		}).start();
		
	}
	
}
