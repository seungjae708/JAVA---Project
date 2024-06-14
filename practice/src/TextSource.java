import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.Scanner;

public class TextSource {
	private Vector<String> v = new Vector<String>();
	
	public TextSource() {
		readFile();
	}
	

	
	public void readFile() {
		String fileName;
		String word;
		fileName = "단어장.txt";
		try {
			String line;
			// 한글 깨지는 것 때문에 UTF-8로 인코딩
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			while((line = br.readLine()) != null) {
				word = line.trim(); // 양옆 White space 제거
				v.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	        
	}

		
			
	
	public String get() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
}