import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditPanel extends JPanel {
	private ImageIcon Icon = new ImageIcon("도라에몽.png");
    private Image img = Icon.getImage();
    private ScorePanel scorePanel = null;
	
    public EditPanel() {
		setBackground(Color.WHITE);
	}
    
    public void changeImg() {
    	revalidate();
    	repaint();
    }
    
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
