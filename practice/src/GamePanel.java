import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

public class GamePanel extends JPanel {
	
	// Audio 관련 클립
	private Clip GameBgmSoundClip; // 오프닝
	private Clip effectSoundClip; // 정답
	private Clip effectSoundClip2; // 오답.
	private Clip healAllClip; // 라이프 전부회복
	private Clip lifeMinusClip; // 라이프감소
	
	private Player player = new Player();
	
	private ImageIcon icon = new ImageIcon("하늘.jpg");
	private Image img = icon.getImage();
	private ImageIcon gunIcon = new ImageIcon("공기포.png");
	private Image gunImg = gunIcon.getImage();
	private ImageIcon mouseIcon = new ImageIcon("쥐.png");
	private Image mouseImg = mouseIcon.getImage();
	private ImageIcon heartIcon = new ImageIcon("하트.png");
	private Image heartImg = heartIcon.getImage();
	private ImageIcon breadIcon = new ImageIcon("팥빵.png");
	private Image breadImg = breadIcon.getImage();
	
	private JTextField input = new JTextField(20);
	private Vector<JLabel>textVector = new Vector<JLabel>(); // targetLabel을 담는 textVector
	
	// 색
	public Color skyBlue = new Color(219, 239, 255);
	public Color lightBlue = new Color(94, 177, 255);
	
	private ScorePanel scorePanel = null;
	private GameGroundPanel gameGroundPanel = new GameGroundPanel();
	private InputPanel inputPanel = new InputPanel();
	private TextSource textSource = new TextSource(); // 단어 벡터 생성
	
	// 단어를 생성하는 스레드
	private MakeThread makeThread = new MakeThread(textVector, player);
	// 단어를 떨어뜨리는 스레드
	private MoveThread moveThread = new MoveThread(textVector,player);
	// 땅에 닿은 단어 감지하는 스레드
	private RemoveThread removeThread = new RemoveThread(textVector);
	
	// 레벨에 따른 난이도 조절
	private int [] makeSpeed = {4000,3000,2000,1000,800};
	private int [] moveSpeed = {400,300,200,80,40};
	
	private int currentAbility;
	private boolean bgmFlag = true;
	
	public GamePanel() {
	}
	
	public GamePanel(ScorePanel scorePanel, Player player) {
		// 기억해둔다.
		this.scorePanel = scorePanel;
		this.player = player;
		
		// 스레드 생성자 부르기
		makeThread = new MakeThread(textVector, player);
		moveThread = new MoveThread(textVector,player);
		textSource = new TextSource(); // 단어 벡터 생성

		//레이아웃 설정
		setLayout(new BorderLayout());
		add(gameGroundPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
		
		// 입력란
		input.setHorizontalAlignment(JTextField.CENTER); // input JTextField 가운데정렬
		input.setFont(new Font("Aharoni", Font.PLAIN, 20));
		
		// textfield에서 enter 누르면 실행됨
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(textVector) {
					JTextField t = (JTextField)(e.getSource());
					String inWord = t.getText(); // 사용자가 입력한 단어
					for (int i=0; i < textVector.size(); i++) {
						String text = textVector.get(i).getText();
						if(text.equals(inWord)) { // 단어맞추기 성공
							loadAnswerEffectAudio(); // 정답 맞추면 싸운드
							System.out.println(inWord + " 맞춤"); // 콘솔에서 확인 위함
							int ability = (int) textVector.get(i).getClientProperty("ability");
	                    	switch(ability) {
		                    	case 0:
		                    		// 점수 증가
									scorePanel.increase(player);
		                        	break;
		                        case 1:
		                        	scorePanel.heal();
		                        	break;
		                        case 2:
		                        	healAllEffectAudio();
		                        	scorePanel.healAll();
		                        	for(int j=0;j<10;j++) {
		                        		scorePanel.increase(player);
		                        	}
		                        	
		                        	break;
		                    }

							scorePanel.repaintScore();
							gameGroundPanel.remove(textVector.get(i)); // 패널에서 라벨 떼기
							textVector.remove(i); // textVector에서 삭제
							t.setText(null); // input 비우기
							break;
						}
						// 벡터 마지막원소에서도 일치하는 단어 못찾음
						if((i == (textVector.size() - 1)) && !textVector.get(i).getText().equals(inWord)) {
							System.out.println(inWord + "틀림");
							loadWrongAnswerEffectAudio();
							// 점수 감소
							scorePanel.decrease(player);
							scorePanel.repaintScore();
							t.setText(null);
						}
						t.requestFocus(); // 엔터 친 후에도 textField에 focus유지
					} // end of for
				}
			} // end of actionPerformed()
		});
	}

	class GameGroundPanel extends JPanel{ // 단어내려오는곳 배경이미지
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("gamePanelBack.jpg");
			g.drawImage(icon.getImage(), 0, 0, gameGroundPanel.getWidth(),
					gameGroundPanel.getHeight(), gameGroundPanel);
			setOpaque(false);
		}
		public GameGroundPanel() {
			this.setBackground(skyBlue);
			// 단어가 마구잡이로 내려와야함.
			setLayout(null);
		}
	}
	
	class InputPanel extends JPanel{ // 단어 입력하는곳
		public InputPanel() {
			setLayout(new FlowLayout());
			this.setBackground(lightBlue);
			add(input);
		}
	}
	
	public void gameStart(Player player) {
		this.player = player;
		loadGameBgmAudio();// BGM재생
		
		// 단어생성 시작
		makeThread.start();
		// 단어 떨어뜨리기 시작
		moveThread.start();
		// 땅에 닿은 단어 감지 시작
		removeThread.start();
	}
	
	public void gameOver() { // 게임종료	
		// 단어생성 중단
		makeThread.interrupt();
		// 단어 떨어뜨리기 중단
		moveThread.interrupt();
		// 땅에 닿은 단어 감지 중단
		removeThread.interrupt();
	}
	
	public void endGame() {
		makeThread.interrupt();
		// 단어 떨어뜨리기 중단
		moveThread.interrupt();
		// 땅에 닿은 단어 감지 중단
		removeThread.interrupt();
		scorePanel.setLifeZero();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		g.drawImage(gunImg, 365, 230, 370, 300, this);
    }
	
	private int getAbility() {
    	int ability = Math.random() < 0.7 ? 0 : -1;
	    if (ability == 0) {
	    	ability = 0; // color == Black;
	    }
	    // 20프로 확률로 능력 랜덤으로 출력.
	    else if (ability == -1) {
	    	double percent = Math.random();
	        // 3가지 능력
	        if (percent < 0.67) {
	        	ability = 1;
	        } else {
	            ability = 2;
	        }
	    }
	    return ability;
    }
	
	// 단어 생성하는 스레드
	public class MakeThread extends Thread{
		
		private Vector<JLabel>textVector = null;
		private Player player = null;
		
		// 단어 가져와 Label설정, 부착하는 메소드
		synchronized void make(Player player) {
			JLabel textLabel = new JLabel("");
			// 단어 한 개 선택
			currentAbility = getAbility();
			String newWord = textSource.get();
			textLabel.setText(newWord);
			textLabel.putClientProperty("ability", currentAbility);
            switch (currentAbility) {
            case 0:
            	textLabel.setIcon(mouseIcon);
            	textLabel.setForeground(Color.BLACK);
            	break;
            case 1:
            	textLabel.setIcon(heartIcon);
            	textLabel.setForeground(Color.RED);
            	break;
            case 2:
            	textLabel.setIcon(breadIcon);
            	textLabel.setForeground(Color.ORANGE);
            	break;
            }
			// targetLabel 모양
            textLabel.setHorizontalAlignment(JLabel.CENTER); // JLabel 가운데정렬
            textLabel.setSize(200, 40);
            textLabel.setFont(new Font("함초롬돋움",1,21));
			
			// y좌표 랜덤 설정
			int startY = (int) (Math.random()*gameGroundPanel.getHeight());
			while(true) {
				if ((startY + textLabel.getWidth()) > gameGroundPanel.getHeight()) 
					startY = (int) (Math.random()*gameGroundPanel.getHeight());
				else
					break;
			}
			
			textLabel.setLocation(-50,startY);
			
			textLabel.setOpaque(false); // 배경 투명하게
			textVector.addElement(textLabel); // textVector에 생성한 newWord 추가
			gameGroundPanel.add(textLabel);
		}
		
		public MakeThread(Vector<JLabel>textVector, Player player) {
			this.textVector = textVector;
			this.player = player;
		}
		
		@Override
		public void run() {
			while(true) {
				int generationTime = makeSpeed[player.getLevel()-1];
				make(player);
				gameGroundPanel.repaint();
				try {
					sleep(generationTime);
				} catch (InterruptedException e) {
					return;
				}
			} // end of while
		} // end of run()
	} // end of MakeThread
	
	// 단어 아래로 내리는 스레드
	public class MoveThread extends Thread{
		
		private Vector<JLabel>textVector = null;
		private Player player = null;
		
		public MoveThread(Vector<JLabel>textVector, Player player) {
			this.textVector = textVector;
			this.player = player;
		}
		
		// y좌표 증가해 단어 밑으로 내림
		synchronized void move(Player player) {
			for (int i=0; i<textVector.size(); i++) {
				int x = textVector.get(i).getX();
				int y = textVector.get(i).getY();
				textVector.get(i).setLocation(x+5, y);
				gameGroundPanel.repaint();
			} // end of for
		}
		
		// textVector에 들어있는 모든 JLabel들의 y좌표 증가
		@Override
		public void run() {
			 while (true){
				 int dropTime = moveSpeed[player.getLevel()-1];
				 move(player);
				 gameGroundPanel.repaint();
				 try {
					 sleep(dropTime);
					} catch (InterruptedException e) {
						return;
					}
			} // end of while
		} // end of run()
	} // end of moveThread
	
	public class RemoveThread extends Thread {
		
		private Vector<JLabel>textVector = null;
		
		public RemoveThread(Vector<JLabel>textVector) {
			this.textVector = textVector;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					sleep(1);
					for(int i=0; i<textVector.size(); i++) {
						// 바닥에 닿은 단어 구하기 위함
						int x = ((JLabel)textVector.get(i)).getX();
						if (x > gameGroundPanel.getWidth()-200) {
							System.out.println(textVector.get(i).getText() + " 떨어짐");
							loadLifeEffectAudio();
							// true값이 반환되면 게임을 종료한다.
							boolean isGameOver =scorePanel.decreaseLife(player);
							if(isGameOver == true) { // 모든스레드 종료
								GameBgmSoundClip.stop();
								gameOver();
							}
							
							// 게임이 종료되지 않을 경우 패널에서 라벨 제거 게임 계속됨
							gameGroundPanel.remove(textVector.get(i)); // 패널에서 라벨 떼기
							textVector.remove(i); // textVector에서 삭제
						}
					}
				} catch (InterruptedException e) {
					return;
				}
			} // end of while
		} // end of run()
	}// end of Thread
	
	// 게임 BGM
	private void loadGameBgmAudio() {
		try {
			GameBgmSoundClip = AudioSystem.getClip();
			File soundFile = new File("gameBGM.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			GameBgmSoundClip.open(audioStream);
			GameBgmSoundClip.start();
			System.out.println("맞춤");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}
	// 맞출때 효과
	private void loadAnswerEffectAudio() {
		try {
			effectSoundClip = AudioSystem.getClip();
			File soundFile = new File("successBGM.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			effectSoundClip.open(audioStream);
			effectSoundClip.start();
			System.out.println("맞춤");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}

	// 틀릴때 효과
	private void loadWrongAnswerEffectAudio() {
		try {
			effectSoundClip2 = AudioSystem.getClip();
			File soundFile = new File("failBGM.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			effectSoundClip2.open(audioStream);
			effectSoundClip2.start();
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}
	
	// 라이프 회복 효과
		private void healAllEffectAudio() {
			try {
				healAllClip = AudioSystem.getClip();
				File soundFile = new File("회복.wav");
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
				healAllClip.open(audioStream);
				healAllClip.start();
				// System.out.println("라이프 감소 사운드");
			} catch (Exception e) {
				System.out.println("오디오 에러");
			}
		}

	// 라이프 감소 효과
	private void loadLifeEffectAudio() {
		try {
			lifeMinusClip = AudioSystem.getClip();
			File soundFile = new File("crash.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			lifeMinusClip.open(audioStream);
			lifeMinusClip.start();
			// System.out.println("라이프 감소 사운드");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}
	public void setBGM() {
		if(!bgmFlag)
			GameBgmSoundClip.start();
		else
			GameBgmSoundClip.stop();
		bgmFlag = (bgmFlag == true)?false:true;
	}
	public void offBGM() {
		if(bgmFlag)
			GameBgmSoundClip.stop();
		bgmFlag = (bgmFlag == true)?false:true;
	}
}