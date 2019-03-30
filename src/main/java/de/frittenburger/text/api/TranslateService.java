package de.frittenburger.text.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class TranslateService {

	
	private final Logger logger = LogManager.getLogger(TranslateService.class);

	private final String url;
	private final TranslationConverter converter = new TranslationConverter();

	public TranslateService(String url) {
		this.url = url;
	}

	public Translation translate(String sourcelanguage, String targetLanguage, String text) throws IOException {
	
		
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "LearnSubtitle");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Authorization","Bearer XXXXX");

		String urlParameters = "sourcelanguage="+sourcelanguage+"&targetlanguage="+targetLanguage+"&text="+URLEncoder.encode(text, "UTF-8");;
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		logger.info("Sending 'POST' request to URL : " + url);
		logger.info("Post parameters : " + urlParameters);
		logger.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		
		return converter.convert(response.toString());
		
	}
	
	public static TranslateService getInstance()
	{
		return new TranslateService("https://text.frittenburger.de/translate");
		//return new TranslateService("http://localhost:4567/translate");
	}
}
