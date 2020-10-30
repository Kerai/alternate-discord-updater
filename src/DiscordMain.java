import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import keray.discord.downloader.ProgressListener;
import keray.discord.downloader.UpdateChecker;

public class DiscordMain {
	public static void main(String[] args) {
		initLAF();
		new UpdateChecker().update();
	}

	private static void initLAF() {
		System.setProperty("awt.useSystemAAFontSettings", "true");
		System.setProperty("swing.aatext", "true");
		System.setProperty("sun.java2d.xrender", "false");
		System.setProperty("sun.java2d.d3d", "false");
		System.setProperty("sun.java2d.opengl", "false");
		System.setProperty("sun.java2d.ddscale", "false");
		System.setProperty("sun.java2d.noddraw", "true");
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			return;
		} catch (Exception e) { }
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			return;
		} catch (Exception e) { }
	}
}
