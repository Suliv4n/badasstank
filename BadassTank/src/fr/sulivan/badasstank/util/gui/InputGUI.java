package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class InputGUI {
	
	private TextField textField;
	private int border = 0;
	private Color borderColor;
	
	private GUIContext container;
	private boolean binded;
	private RangeGUI bind;
	
	public InputGUI(GUIContext container, int width, int height){
		this.container = container;
		textField = new TextField(container, container.getDefaultFont(), 0, 0, width, height);
		textField.setBackgroundColor(Color.black);
		textField.setTextColor(Color.white);
	}
	
	public void setBorder(int border, Color color){
		this.border = border;
		borderColor = color;
		textField.setBorderColor(color);
	}
	
	public void render(Graphics g){
		
		textField.render(container, g);
		if(border > 0){
			g.setColor(borderColor);
			for(int i=0; i<border;i++){
				g.drawRect(textField.getX()-i, textField.getY()-i, textField.getWidth()+i*2+1, textField.getHeight()+i*2+1);
			}
		}
		if(bind != null){
			if(!textField.hasFocus() && !bind.isDragging()){
				if(textField.getText().isEmpty()){
					textField.setText(String.valueOf(bind.getMin()));	
				}
				try{
					String input = textField.getText();
					int value = Integer.parseInt(input);
					bind.setValue(value);
				}catch(Exception e){
					textField.setText(String.valueOf(bind.getIntValue()));
				}
			}
			if(!textField.hasFocus() && !textField.getText().equals(String.valueOf(bind.getIntValue()))){
				textField.setText(String.valueOf(bind.getIntValue()));
			}
		}
	}
	
	public void setLocation(int x, int y){
		textField.setLocation(x, y);
		
	}
	
	public void bind(RangeGUI range){
		bind = range;
		
	}

	public int getHeight() {
		return textField.getHeight() + border*2;
	}
}
