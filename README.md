# Java-Project

![image](https://github.com/seungjae708/Java-Project/assets/64647590/be7b72e9-d438-4268-a4ed-d1674b6ac7ac)

게임을 실행하면 다음과 같은 로비 화면이 나타난다. 
게임 시작을 위해서는 레벨 5개 중에서 자신이 플레이하고 싶은 레벨을 선택하고 이름을 작성한 후 게임시작 버튼을 눌러 실행할 수 있다.
랭킹보기 버튼을 누르면 지금까지 플레이했던 플레이어들의 점수와 순위를 확인할 수 있다. 총 5개의 레벨이 존재하기 때문에 각각의 레벨별로 5개의 랭킹이 존재하고 이를 보기 위해서는 보고싶은 레벨을 선택하고 랭킹보기 버튼을 누르면 된다.
상단의 Music메뉴의 loginBgm On/Off를 누르면 loginBgm 끄고 켤 수 있다. 

![image](https://github.com/seungjae708/Java-Project/assets/64647590/a1c9c359-cd69-4852-a302-bd92f4addd7b)
 
또한 상단의 Edit메뉴의 add를 누르면 다음과 같은 단어 추가할 수 있는 창이 뜬다. 빈칸에 본인이 원하는 단어를 입력한 후 추가버튼을 눌러 단어를 단어장.txt파일에 추가하고, 닫기 버튼으로 창을 닫는다.

![image](https://github.com/seungjae708/Java-Project/assets/64647590/73777670-a647-43a1-a161-a16e2be0bb12)![image](https://github.com/seungjae708/Java-Project/assets/64647590/f565570e-17eb-4c16-b26d-f87bf81e3e0e)

Settings메뉴에서 QuitGame을 선택하면 곧바로 게임오버되어 게임오버창이 뜬다. 이때의 점수는 랭킹에 저장되지 않는다. GoMain을 선택하면 게임오버창이 뜨지 않고 곧바로 첫 로비화면으로 돌아간다. Exit을 선택하면 곧바로 프로그램이 종료된다.
Music메뉴에서 GameBgm On/Off를 선택하면 Bgm을 끄고 킬 수 있다.

![image](https://github.com/seungjae708/Java-Project/assets/64647590/f33fada1-f80b-4f21-b3b3-3b3b515591df)

게임시작 버튼을 누르면 다음처럼 화면이 전환되고 단어들이 생성되어 왼쪽에서 오른쪽으로 이동한다. 70% 확률로 생쥐, 20% 확률로 하트, 10% 확률로 팥빵이 등장한다. 생쥐를 맞추면 +10점, 하트는 +생명1개, 팥빵은 생명을 모두 회복하고 100점의 점수를 얻는다. 단어를 잘못 입력하여 Label과 맞지 않는 단어를 입력하면 점수가 -10점이 되고 도라에몽이 있는 곳까지 단어를 맞추지 못하면 생명을 하나 잃는다. 

![image](https://github.com/seungjae708/Java-Project/assets/64647590/86ba2f2e-39c0-4074-b9a6-8337f492706b)

그렇게 모든 생명을 잃게 되면 게임 오버되고 다음과 같이 게임 오버 창이 뜨고 점수를 알려주며 이 점수가 랭킹에 저장된다. 여기서 ‘예’를 선택하면 게임은 곧바로 종료되고 ‘메뉴로 go’를 선택하면 첫 로비화면으로 돌아간다.

![image](https://github.com/seungjae708/Java-Project/assets/64647590/9df810c9-e3c0-44af-a20f-98284505e3ae)![image](https://github.com/seungjae708/Java-Project/assets/64647590/23bce26d-1f4e-4f6e-87d8-fb2fdb3055c6)


보고싶은 레벨을 선택하여 RankView버튼을 클릭하면 다음과 같이 각각의 레벨에 해당하는 랭크를 확인할 수 있다. 총 10위까지의 플레이어의 이름과 그의 점수를 확인 가능하다.
