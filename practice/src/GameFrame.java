import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar; 
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class GameFrame extends JFrame {
	private Clip clip; // 버튼
	private Player player = new Player();
	private int score = 0;
	private Clip loginClip; // 오프닝
	private LoginPanel loginPanel = new LoginPanel();
	private ScorePanel scorePanel = new ScorePanel();
	private GamePanel gamePanel = new GamePanel();
	
	private ImageIcon titleIcon = new ImageIcon("title.png");
	private Image titleImg = titleIcon.getImage();
	private ImageIcon mouseIcon = new ImageIcon("생쥐.png");
	private ImageIcon surpriseIcon = new ImageIcon("놀람.png");
	private boolean bgmFlag = true;
	
	public GameFrame() {
		setTitle("도라에몽, 생쥐와의 전쟁");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		makeMenu();
		
		getContentPane().add(loginPanel, BorderLayout.CENTER);
		setResizable(false);
		setVisible(true);
		loadGameBgmAudio();
	}
	private void makeMenu() {
		JMenuBar mb = new JMenuBar();
		this.setJMenuBar(mb);
		
		JMenu fileMenu = new JMenu("Settings");
		JMenuItem quitItem = new JMenuItem("QuitGame");
		JMenuItem mainItem = new JMenuItem("Go Main");
		quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// 버튼 클릭시 효과음 재생
				buttonEffectAudio();
				gamePanel.offBGM();
                gamePanel.endGame();
            }
        });

		mainItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// 버튼 클릭시 효과음 재생
				buttonEffectAudio();
				gamePanel.offBGM();
            	gamePanel.gameOver();
            	dispose();
				GameFrame f = new GameFrame();
            }
        });
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 버튼 클릭시 효과음 재생
				buttonEffectAudio();
				System.exit(0);
			}
		});
		fileMenu.add(quitItem);
		fileMenu.add(mainItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		mb.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		JMenuItem addItem = new JMenuItem("Add");
		addItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 새로운 창 추가.
				// 단어를 추가할 수 있는 창.
				// 버튼 클릭시 효과음 재생
				buttonEffectAudio();
				AddText addText = new AddText();
			}
		});
		editMenu.add(addItem);
		mb.add(editMenu);
		
		JMenu musicMenu = new JMenu("Music");
		JMenuItem onItem = new JMenuItem("LoginBgm On/Off");
		onItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 버튼 클릭시 효과음 재생
				buttonEffectAudio();
				setBGM();
			}
		});
		JMenuItem offItem = new JMenuItem("GameBgm On/Off");
		offItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 버튼 클릭시 효과음 재생
				buttonEffectAudio();
				gamePanel.setBGM();
			}
		});
		musicMenu.add(onItem);
		musicMenu.add(offItem);
		mb.add(musicMenu);
	}

	
	public class LoginPanel extends JPanel{
		
		// LoginPage
		// 그룹을 만들어 한번에 부착하도록 하기 위함
		private Box levelBox = Box.createHorizontalBox();
		private Box nameBox = Box.createHorizontalBox();
		
		private JLabel mainTitle = new JLabel("Typing Game");
		private JLabel lvLabel = new JLabel("레벨   ");
		private String [] level = {"Lv.1", "Lv.2", "Lv.3", "Lv.4", "Lv.5"};
		private JComboBox<String> lvCombo = new JComboBox<String>(level); // 레벨 고르는 콤보박스
		private ButtonGroup g = new ButtonGroup();
		private JLabel name = new JLabel("이름   "); 
		private JTextField inputName = new JTextField(30); // 플레이어 이름 입력칸
		private JButton gameStartBtn = new JButton("게임시작");
		private JButton rankViewBtn = new JButton("랭킹보기");
		
		// Ranking Page
		private JLabel rankTitle = new JLabel("Top 10");
		private JButton goMainBtn = new JButton("닫기");
		private JLabel modeTitle = new JLabel();
		
		private String line;
		private String []splitLine = new String[2];
		private String []rankText = new String[10];
		private JLabel []rankLabel = new JLabel[10];
		private JLabel []scoreText = new JLabel[10];
		private boolean showBackgroundImage = true;
		
		@Override
		public void paintComponent(Graphics g) { // 배경이미지 설정
			super.paintComponent(g);
			if (showBackgroundImage) {
				
	            ImageIcon icon = new ImageIcon("초기.png");
	            g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	            
	            ImageIcon subtitleIcon = new ImageIcon("부제.png");
	            g.drawImage(subtitleIcon.getImage(), 260, 180, 230, 80, this);
	            g.drawImage(titleImg, 130, 110, 450, 100, this);
	            
	            g.drawImage(mouseIcon.getImage(), 370, 420, 100, 100, this);
	            for(int i=0;i<30;i+=10) {
	            g.drawImage(surpriseIcon.getImage(), 470+i, 200-i/2, 300, 280, this);
	            }
	            setOpaque(false);
	        }
			
		}
		
		public LoginPanel() {
			
			this.setLayout(null); // 원하는 좌표에 컴포넌트 부착 위함
			
			name.setFont(new Font("함초롬돋움",1,15));
			inputName.setFont(new Font("함초롬돋움",1,15));
			nameBox.add(name);
			nameBox.add(inputName);
			nameBox.setBounds(100, 310, 200, 30);

			lvLabel.setFont(new Font("함초롬돋움",1,15));
			lvCombo.setFont(new Font("함초롬돋움",1,15));
			levelBox.add(lvLabel);
			levelBox.add(lvCombo);
			levelBox.setBounds(100, 260, 200, 50);
			
			gameStartBtn.setFont(new Font("함초롬돋움",1,15));
			gameStartBtn.setBounds(100, 350, 200, 40);
			gameStartBtn.setBorderPainted(false);	
			
			rankViewBtn.setFont(new Font("함초롬돋움",1,15));
			rankViewBtn.setBounds(100, 400, 200, 40);
			rankViewBtn.setBorderPainted(false);
			
			add(levelBox);
			add(nameBox);
			add(gameStartBtn);
			add(rankViewBtn);
			
			// 게임 시작 버튼
			gameStartBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 버튼 클릭시 효과음 재생 false면 한번만 재생한다.
					buttonEffectAudio();

					// Player객체 설정
					player = new Player(inputName.getText(),
							lvCombo.getSelectedIndex()+1, score);
					player.setName(inputName.getText());
					player.setLevel(lvCombo.getSelectedIndex()+1);
					
					loginClip.stop();
					
					// gamePanel생성
					gamePanel = new GamePanel(scorePanel, player);
					
					// LoginPanel의 모든 요소를 안보이도록 설정
					setLoginPageHidden();
					
					// 부착할 패널의 레이아웃 설정
					getContentPane().setLayout(new BorderLayout());
					splitPane(); // JsplitPane을 생성하여 ContentPane의 CENTER에 부착
					makeInfoPanel(player);
					setResizable(false); // 사이즈 조정 못하도록
					showBackgroundImage = false;
					repaint();
					
					gamePanel.gameStart(player);
				}
			}); // end of ActionListener
			
			rankViewBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 버튼 클릭시 효과음 재생
					buttonEffectAudio();
					RankView rankView = new RankView();
				}
			}); // end of ActionListener
		
		
	} // end of LoginPanel()
		public class RankView extends JFrame{
			@Override
		    public void paint(Graphics g) {
				super.paint(g);

		        // Draw Doraemon image at the specified location
		        ImageIcon doraemonIcon = new ImageIcon("도라에몽.png");
		        Image doraemonImg = doraemonIcon.getImage();
		        g.drawImage(doraemonImg, 80, 330, 200, 250, this);
			}
			public RankView() {
				setSize(800, 600);
				getContentPane().setLayout(null);
				setResizable(false);
				setVisible(true);
				
				

				getContentPane().setBackground(new Color(189, 215, 238));

				repaint();
				
				// 필요한 Player정보 저장
				player = new Player(inputName.getText(),
						lvCombo.getSelectedIndex()+1, score);					
				player.setLevel(lvCombo.getSelectedIndex()+1);
				
				rankTitle.setFont(new Font("Goudy Stout",1,45));
				rankTitle.setBounds(220, 60, 800, 40);
				
				setModeTitle(player);
				modeTitle.setFont(new Font("Goudy Stout",1,20));
				modeTitle.setBounds(340, 130, 400, 20);
				
				goMainBtn.setBounds(450, 450, 100, 30);
				
				// 내림차순으로 Sorting한 기록파일 불러옴
				String fileName = "sorted" + player.getLevel()+".txt";
				
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(
							new FileInputStream(fileName), "UTF-8"));
					
					int i=0;
					while (i<10) {
						line = in.readLine();
						if(line == null) break; // 랭킹이 10위까지 있지 않을 때
						splitLine = line.trim().split(",");
						// data[0]은 name, data[1]은 score
						rankText[i] = Integer.toString(i+1) + " 위     " + splitLine[0];
						rankLabel[i] = new JLabel(rankText[i]);
						rankLabel[i].setFont(new Font("함초롬돋움",1,15));
						rankLabel[i].setBounds(340, 200+i*22, 700, 20);
						
						scoreText[i] = new JLabel(splitLine[1]);
						scoreText[i].setFont(new Font("함초롬돋움",1,15));
						scoreText[i].setBounds(540, 200+i*22, 700, 20);
						add(rankLabel[i]);
						add(scoreText[i]);
						
						i++;
					}
						
				} catch (IOException e1) {
					System.out.println("해당 랭킹파일 없음");
				} finally {
					add(rankTitle);
					add(modeTitle);
					add(goMainBtn);
					
					// 다시 LoginPanel
					goMainBtn.addActionListener(new ActionListener() { 
						@Override
						public void actionPerformed(ActionEvent e) {
							// 버튼 클릭시 효과음 재생
							buttonEffectAudio();
							dispose();
						}
					});
				}
			}
		}
		public void setLoginPageHidden() {
			mainTitle.setVisible(false);
			levelBox.setVisible(false);
			nameBox.setVisible(false);
			gameStartBtn.setVisible(false);
			rankViewBtn.setVisible(false);
		}
			
		public void setModeTitle(Player player) {
			modeTitle = new JLabel(" Mode Lv." + player.getLevel());
		}
	
	protected void editWord(String fileName) {
		JOptionPane edit = new JOptionPane();
		String str = edit.showInputDialog("추가할 단어를 입력하세요!");
		String word = str.trim(); // 혹시모를 공백 제거
		try {
			FileWriter out = new FileWriter(fileName,true);
			out.write("\n" + word);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}

	private void splitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER); // CENTER에 부착
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // 수평으로 쪼갬
		hPane.setDividerLocation(600);
		hPane.setEnabled(false); // 활성화 막아버림(못움직이게)
		
		JSplitPane pPane = new JSplitPane();
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(300);
		
		pPane.setTopComponent(scorePanel);
		pPane.setBottomComponent(new EditPanel());
		
		hPane.setRightComponent(pPane);
		hPane.setLeftComponent(gamePanel);
	}
	
	public void makeInfoPanel(Player player) {
		
		getContentPane().add(new UserInfoPanel(player), BorderLayout.NORTH);
	}
	
	public class UserInfoPanel extends JPanel{
		// 게임 플레이 중 상단에 플레이어 정보를 표시
		public UserInfoPanel(Player player) {
			int level;
			String userName;
			level = player.getLevel();
			userName = player.getName();
			
			this.setLayout(new FlowLayout());
			
			JLabel name = new JLabel("플레이어:");
			JLabel userNameInfo = new JLabel("");
			userNameInfo.setText(userName + "  / ");
			JLabel levelInfo = new JLabel("");
			levelInfo.setText("Lv." + Integer.toString(level));
		
			name.setFont(new Font("함초롬돋움",Font.BOLD,12));
			userNameInfo.setFont(new Font("함초롬돋움",Font.BOLD,12));
			levelInfo.setFont(new Font("함초롬돋움",Font.BOLD,12));
			
			add(name); 
			add(userNameInfo);
			add(levelInfo);
		}
	}
	} // end of Class LoginPanel
	
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
	
	// 게임 BGM
	private void loadGameBgmAudio() {
		try {
			loginClip = AudioSystem.getClip();
			File soundFile = new File("basicBGM.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			loginClip.open(audioStream);
			loginClip.start();
			System.out.println("맞춤");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}
	
	public void setBGM() {
		if(!bgmFlag)
			loginClip.start();
		else
			loginClip.stop();
		bgmFlag = (bgmFlag == true)?false:true;
	}
}