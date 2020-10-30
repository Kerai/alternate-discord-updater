package keray.discord.downloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

	public static int download(URL url, File file, ProgressListener listener) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			int responseCode = connection.getResponseCode();
			int contentLength = connection.getContentLength();
			
			if(listener != null) {
				listener.setMax(contentLength);
			}
			
			try ( InputStream input = connection.getInputStream(); OutputStream output = new FileOutputStream(file); )
			{
				byte[] bytes = new byte[1024 * 16];
				
				int byteCounter = 0;
				
				while(true) {
					int read = input.read(bytes);
					if(read < 0)
						break;
					byteCounter += read;
					output.write(bytes, 0, read);
					
					if(listener != null) {
						listener.setCurrent(byteCounter);
					}
				}
				output.flush();
				
				return byteCounter;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static long pipe(InputStream input, OutputStream output) throws IOException {
		byte[] bytes = new byte[1024 * 16];
		long pipedBytes = 0;
		
		while(true) {
			int read = input.read(bytes);
			if(read < 0)
				break;
			pipedBytes += read;
			output.write(bytes, 0, read);
		}
		output.flush();
		return pipedBytes;
	}
	
	public static void deleteDirectoryContent(File directory) {
		File[] files = directory.listFiles();
		if(files != null) {
			for(File f : files) {
				deleteRecursively(f);
			}
		}
	}
	
	public static void deleteRecursively(File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			if(files != null) {
				for(File f : files) {
					deleteRecursively(f);
				}
			}
		}
		file.delete();
	}
	
	public static String download(URL url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			int responseCode = connection.getResponseCode();
			
			InputStream input = connection.getInputStream();
			var reader = new BufferedReader(new InputStreamReader(input));
			var builder = new StringBuilder();
			
			char[] chars = new char[1024];
			
			while(true) {
				int read = reader.read(chars);
				if(read < 0)
					break;
				builder.append(chars, 0 , read);
			}
			
			return builder.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
