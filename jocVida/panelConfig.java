package jocVida;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//©Daniel Garcia (daniieelgs@gmail.com)

public class panelConfig extends JPanel{

	private static final long serialVersionUID = 1L;

	public panelConfig(String pathConf, java.awt.Window parent) {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		dataConfig conf = config.readConfigurationFile(pathConf);
		
		int ruleLive, ruleDie, mode, initialGroup, vel, sizeX, sizeY;
		
		ruleLive = conf.getRuleLive();
		ruleDie = conf.getRuleDie();
		mode = conf.getMode();
		initialGroup = conf.getInitialGroup();
		vel = conf.getVel();
		sizeX = conf.getSizeX();
		sizeY = conf.getSizeY();
		
		Map<String, Integer> data = new HashMap<String, Integer>();
		
		String[] keys = {"ruleLive", "ruleDie", "mode", "initialGroup", "vel", "sizeX", "sizeY"};
		
		add(createRulesPanel(ruleLive, ruleDie, data, keys[0], keys[1]));
		add(createModePanel(mode, initialGroup, data, keys[2], keys[3]));
		add(createVelPanel(vel, data, keys[4]));
		add(createGridPanel(sizeX, sizeY, data, keys[5], keys[6]));
		add(createButtonsActionPanel(parent, conf, pathConf, data, keys));
		
	}
	
	private JPanel createRulesPanel(int ruleLive, int ruleDie, Map<String, Integer> data, String keyRuleLive, String keyRuleDie) {
		
		JPanel rules = new JPanel();
		
		JTextField vivir = new JTextField(2);
		JLabel bar = new JLabel("/");
		JTextField morir = new JTextField(1);
		
		vivir.setText(String.valueOf(ruleLive));
		morir.setText(String.valueOf(ruleDie));
		
		rules.add(vivir);
		rules.add(bar);
		rules.add(morir);
		
		data.put(keyRuleLive, ruleLive);
		data.put(keyRuleDie, ruleDie);
		
		vivir.addFocusListener(new FocusListener() {
			
			public void focusLost(FocusEvent e) {
				
				String finalText = filterNumber(vivir.getText());
				
				vivir.setText(finalText);
				
				data.put(keyRuleLive, Integer.parseInt(finalText));
				
			}
			
			public void focusGained(FocusEvent e) {}
		});
		
		morir.addFocusListener(new FocusListener() {
			
			public void focusLost(FocusEvent e) {
				
				String finalText = filterNumber(morir.getText());
			
				morir.setText(finalText);
				
				data.put(keyRuleDie, Integer.parseInt(finalText));
			
			}
			
			public void focusGained(FocusEvent e) {}
		});
		
		return setBorderTitle(rules, "Reglas");
		
	}
	
	private JPanel createModePanel(int mode, int initalGroup, Map<String, Integer> data, String keyMode, String keyInitialGroup) {
		
		JPanel modePanel = new JPanel();
		
		Box elements = Box.createVerticalBox();
		
		JComboBox<String> modeSelector = new JComboBox<>(dataConfig.MODES);
		
		modeSelector.setSelectedIndex(mode);
		
		JPanel initialGroupsPanel = new JPanel();
		
		JSpinner initialGroups = new JSpinner();
		initialGroups.setModel(new SpinnerNumberModel(initalGroup, 1, 50, 1));
		
		initialGroupsPanel.add(new JLabel("Grupos iniciales: "));
		initialGroupsPanel.add(initialGroups);
		
		initialGroupsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		initialGroupsPanel.setVisible(modeSelector.getSelectedIndex()==dataConfig.MODE_AUTOMATIC);
		
		elements.add(modeSelector);
		elements.add(initialGroupsPanel);
		modePanel.add(elements);
		
		data.put(keyMode, modeSelector.getSelectedIndex());
		data.put(keyInitialGroup, (int) initialGroups.getValue());
		
		modeSelector.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				data.put(keyMode, modeSelector.getSelectedIndex());
				initialGroupsPanel.setVisible(modeSelector.getSelectedIndex()==dataConfig.MODE_AUTOMATIC);
				
			}
		});
		
		initialGroups.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				data.put(keyInitialGroup, (int) initialGroups.getValue());
			}
		});
		
		return setBorderTitle(modePanel, "Modo de creación");
		
	}
	
	private JPanel createVelPanel(int vel, Map<String, Integer> data, String keyVel) {
		
		JPanel velPanel = new JPanel();
		
		JSpinner setVel = new JSpinner();
		setVel.setModel(new SpinnerNumberModel(vel, 10, 3000, 10));
		
		velPanel.add(setVel);
				
		velPanel.add(new JLabel("ms"));
		
		data.put(keyVel, (int) setVel.getValue());
		
		setVel.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {

				data.put(keyVel, (int) setVel.getValue());
				
			}
		});
		
		return setBorderTitle(velPanel, "Velocidad de reproducción");
		
	}
	
	private JPanel createGridPanel(int sizeX, int sizeY, Map<String, Integer> data, String keySizeX, String keySizeY) {
		
		int maxNum = 50;
		int minNum = 5;
		int stepNum = 1;
		
		JPanel grid = new JPanel(new GridLayout(2, 1));
				
		JSpinner setX = new JSpinner();
		setX.setModel(new SpinnerNumberModel(sizeX, minNum, maxNum, stepNum));
		
		JSpinner setY = new JSpinner();
		setY.setModel(new SpinnerNumberModel(sizeY, minNum, maxNum, stepNum));
		
		JPanel abscisas = new JPanel();
		abscisas.add(new JLabel("Eje X:"));
		abscisas.add(setX);
		
		JPanel ordenadas = new JPanel();
		ordenadas.add(new JLabel("Eje Y:"));
		ordenadas.add(setY);

		grid.add(abscisas);
		grid.add(ordenadas);
		
		data.put(keySizeX, (int) setX.getValue());
		data.put(keySizeY, (int) setY.getValue());
		
		setX.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				data.put(keySizeX, (int) setX.getValue());
			}
		});
		
		setY.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				data.put(keySizeY, (int) setY.getValue());
			}
		});
		
		return setBorderTitle(grid, "Dimensiones");
		
	}
	
	private JPanel createButtonsActionPanel(java.awt.Window parent, dataConfig conf, String pathConf, Map<String, Integer> data, String[] keys) {
		
		JPanel buttonsAction = new JPanel();
		
		JButton save = new JButton("Guardar");
		JButton cancel = new JButton("Cancelar");
		
		save.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				conf.setRuleLive(data.get(keys[0]));
				conf.setRuleDie(data.get(keys[1]));
				conf.setMode(data.get(keys[2]));
				conf.setInitialGroup(data.get(keys[3]));
				conf.setVel(data.get(keys[4]));
				conf.setSizeX(data.get(keys[5]));
				conf.setSizeY(data.get(keys[6]));
				
				config.saveConfigurationFile(pathConf, conf);
				
				jocVida.restart(pathConf);
				
				parent.dispose();
				
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				parent.dispose();
			}
		});
		
		buttonsAction.add(save);
		buttonsAction.add(cancel);
		
		return buttonsAction;
		
	}
	
	private JPanel setBorderTitle(JPanel p, String t) {
		
		p.setBorder(new TitledBorder(t));
		
		return p;
		
	}
	
	private String filterNumber(String text) {
		
		String numbers="1234567890";
		
		String finalText = "";
		
		for(char c : text.toCharArray()) if(numbers.contains(String.valueOf(c))) finalText+=c;
		
		if(finalText.equals("")) finalText="00";

		return finalText;
		
	}
	
}
