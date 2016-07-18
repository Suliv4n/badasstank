package fr.sulivan.badasstank.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

public class ColorPickerGUI extends PopupGUI{

	private Color color = Color.black;
	
	private RangeGUI redRange;
	private RangeGUI greenRange;
	private RangeGUI blueRange;
	
	private InputGUI redInput;
	private InputGUI greenInput;
	private InputGUI blueInput;
	
	private TexturedButtonGUI buttonValid;
	private TexturedButtonGUI buttonCancel;
	
	public ColorPickerGUI(GUIContext container, String title, Image closeImage, Image buttonTexture, Image buttonOverTexture){
		super(title, 350, 150, closeImage);
		
		redRange = new RangeGUI(Color.red, 255, 0, 255);
		greenRange = new RangeGUI(Color.green, 255, 0, 255);
		blueRange = new RangeGUI(Color.blue, 255, 0, 255);
		
		redInput = new InputGUI(container, 60, 20);
		redInput.setBorder(1, Color.red);
		blueInput = new InputGUI(container, 60, 20);
		blueInput.setBorder(1, Color.blue);
		greenInput = new InputGUI(container, 60, 20);
		greenInput.setBorder(1, Color.green);
		
		redInput.bind(redRange);
		greenInput.bind(greenRange);
		blueInput.bind(blueRange);
		
		buttonCancel = new TexturedButtonGUI(buttonTexture, 100, 30, "Cancel");
		buttonValid = new TexturedButtonGUI(buttonTexture, 100, 30, "Valid");
		buttonCancel.setLabelColor(Color.white);
		buttonValid.setLabelColor(Color.white);
		buttonValid.setBorder(2, Color.white);
		buttonCancel.setBorder(2, Color.white);
		buttonValid.setTextureMouseOver(buttonOverTexture);
		buttonCancel.setTextureMouseOver(buttonOverTexture);
		
		buttonValid.setOnClick(() -> close());
		buttonCancel.setOnClick(() -> close());
		
		setContentRenderer((g,r) -> {
			int x = (int) (r.getX() + 10);
			int y = (int) (r.getY() + 10);
			
			redRange.setLocation(x, y + redInput.getHeight()/2 - redRange.getHeight()/2);
			redRange.render(g);
			redInput.setLocation(x + redRange.getWidth() + 10, y);
			redInput.render(g);
			
			y += Math.max(redRange.getHeight(), redInput.getHeight()) + 5;
			blueRange.setLocation(x,y + blueInput.getHeight()/2 - blueRange.getHeight()/2);
			blueRange.render(g);
			blueInput.setLocation(x + blueRange.getWidth() + 10, y);
			blueInput.render(g);
			
			y += Math.max(greenRange.getHeight(), greenInput.getHeight()) + 5;
			greenRange.setLocation(x, y + greenInput.getHeight()/2 - greenRange.getHeight()/2);
			greenRange.render(g);
			greenInput.setLocation(x + greenRange.getWidth() + 10, y);
			greenInput.render(g);
			
			y += Math.max(greenRange.getHeight(), greenInput.getHeight()) + 10;
			buttonCancel.setLocation(x, y);
			buttonValid.setLocation(x + buttonCancel.getWidth() + 10, y);
			buttonCancel.render(g);
			buttonValid.render(g);
			
			g.setColor(getColor());
			int pastilleRadius = 15;
			g.fillOval(x + buttonCancel.getWidth() + buttonValid.getWidth() + 40, y + buttonValid.getHeight()/2 - pastilleRadius/2, pastilleRadius, pastilleRadius);
			g.setColor(Color.white);
			g.drawOval(x + buttonCancel.getWidth() + buttonValid.getWidth() + 39, y + buttonValid.getHeight()/2 - pastilleRadius/2-1, pastilleRadius+2, pastilleRadius+2);
		});
		
	}
	
	public void update(GameContainer container){
		if(isOpen()){
			super.update(container);
			
			redRange.update(container);
			greenRange.update(container);
			blueRange.update(container);
			
			buttonCancel.update(container);
			buttonValid.update(container);
		}
	}

	public Color getColor(){
		return new Color(redRange.getIntValue(), greenRange.getIntValue(), blueRange.getIntValue());
	}
	
	public void setColor(Color color){
		redRange.setValue(color.getRed());
		greenRange.setValue(color.getGreen());
		blueRange.setValue(color.getBlue());
	}
	
	public void setOnValidate(Runnable action){
		buttonValid.setOnClick(action);
	}
	
}
