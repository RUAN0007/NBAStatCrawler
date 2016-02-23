import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	private static final String NBA_SITE = "http://stats.nba.com/player";
	public static void main(String[] args) throws Exception {
		for(File f: Paths.get("page").toFile().listFiles()){
			String content = new String(Files.readAllBytes(f.toPath()));
			Document doc = Jsoup.parse(content);
			Elements times = doc.select("div div div p i");
			Elements headlines = doc.select("div div div strong a");
			String team = doc.select("div div").first().text();
			Elements players = doc.select("div div h3 a");
			System.out.println();
			int newsCount = times.size();
			if (newsCount != headlines.size() || newsCount != players.size()) {
				System.err.println("Inconsistent count...");
			}

			Path path = Paths.get("news").resolve(team + ".csv");

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()))) {
				
				bw.write("time;");
				bw.write("player;");
				bw.write("headline;");
				bw.write("playerlink;");
				bw.write("\n");
				for(int i = 0;i < newsCount;i++){
					String time = times.get(i).text();
					bw.write(time + ";");
					String player = players.get(i).text();			
					bw.write(player + ";");

					String headline = headlines.get(i).text();
					bw.write(headline + ";");

					String playerLink = NBA_SITE + players.get(i).attr("href");
					bw.write(playerLink + ";");
					bw.write("\n");

				}
			}catch (FileNotFoundException ex) {
				System.out.println(ex.toString());
			}
		}
		
		

	}

}
