
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddText extends JFrame{
	private Clip clip; // 버튼
	private JTextField textField;
	File file = new File("단어장.txt");
	public AddText() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("resource/img/favcion_bugi.png");
		setIconImage(img);
		setTitle("단어 추가 하기");
		setSize(280, 120);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("단어 입력:");
		lblNewLabel.setFont(new Font("함초롱돋움", Font.BOLD, 16));
		lblNewLabel.setBounds(40, 10, 78, 26);
		getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("추가");
		btnNewButton.setFont(new Font("함초롱돋움", Font.BOLD, 16));
		btnNewButton.setBounds(80, 50, 78, 23);
		
		JButton quitButton = new JButton("닫기");
		quitButton.setFont(new Font("함초롱돋움", Font.BOLD, 16));
		quitButton.setBounds(165, 50, 78, 23);
		
		getContentPane().add(btnNewButton);
		getContentPane().add(quitButton);
		
		textField = new JTextField();
		textField.setBounds(130, 13, 116, 21);
		getContentPane().add(textField);
		textField.setColumns(10);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 화면 가운데 출력시키기
		setResizable(false);
		setVisible(true);
		
		btnNewButton.addActionListener(new addWordAction());
		quitButton.addActionListener(new quitAction());
	}
	
	private void addWord(String word) {
		try {
			//파일에 문자열을 쓴다
            //하지만 파일이 존재한다면 덮어쓰는게 아니라 .
            //그 뒤에 문자열을 이어서 작성한다.
			FileWriter fw = new FileWriter(file, true);
			fw.write('\n'+word);
            fw.close();
			System.out.println(word +" 저장 완료.");
		} catch (Exception e) {
			System.out.println("단어 저장 오류");
			e.printStackTrace();
		}
	}
	private class addWordAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 버튼 클릭시 효과음 재생
			buttonEffectAudio();
			//새로운 창 추가.
			//단어를 추가할 수 있는 창. 
			String word =  textField.getText();
			addWord(word);
			textField.setText("");
		}
	}
	private class quitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 버튼 클릭시 효과음 재생
			buttonEffectAudio();
			dispose();
		}
	}
	//버튼 클릭시 효과
	public void buttonEffectAudio() {
		try {
			clip = AudioSystem.getClip();
			File soundFile = new File("btnClicked.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			clip.open(audioStream);
			clip.start();
			System.out.println("맞춤");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}
}