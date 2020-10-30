package keray.discord.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;

public class UpdateChecker {

	public void update() {
		final ProgressWindow window = new ProgressWindow();
		
		window.progressBar.setIndeterminate(true);

		try {
			String releasesLocal = getLocalReleases();
			
			window.label.setText("Local version: " + releasesLocal);
			
			String releasesRemote = getRemoteReleases();
			
			window.label.setText("Remote version: " + releasesRemote);
			
			var listener = new ProgressListener() {
				public void setMax(int max) {
					window.progressBar.setMaximum(max);
					window.progressBar.setIndeterminate(false);
				}
				public void setCurrent(int current) {
					window.progressBar.setValue(current);
				}
			};
			
			if(releasesRemote == null) {
				window.label.setText("No update found");
				System.out.println("No update found");
				return;
			}
			
			if (releasesLocal == null || !releasesLocal.contentEquals(releasesRemote)) {
				String[] split = releasesRemote.split(" ");
				String urlString = split[1];

				window.label.setText("Downloading update");
				System.out.println("download url: " + urlString);
				File packageZip = new File("package.zip");
				packageZip.delete();
				
				Utils.download(new URL(urlString), packageZip, listener);

				window.label.setText("Installing update");
				window.progressBar.setIndeterminate(true);
				
				install();
				
				Files.writeString(Path.of("releases.txt"), releasesRemote);
			} else {
				System.out.println("No update needed");
			}
			
			Runtime.getRuntime().exec("package\\Discord.exe");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();

			window.label.setText("ERROR");
			JOptionPane.showMessageDialog(window.frame, e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		System.exit(1);
	}
	
	private void install() {
		File packageZip = new File("package.zip");
		File packageDir = new File("package");

		Utils.deleteDirectoryContent(packageDir);
		packageDir.mkdir();

		try {
			ZipFile zip = new ZipFile(packageZip);
			Iterator<? extends ZipEntry> iterator = zip.entries().asIterator();
			while(iterator.hasNext()) {
				ZipEntry entry = iterator.next();
				String path = entry.getName();
				
				if(path.startsWith("lib/net45")) {
					
					if(path.toLowerCase().contains("squirrel.exe")) {
						continue;
					}
					
					System.out.println("unpacking: " + entry.getName());
					
					String subpath = path.substring("lib/net45".length());
					File f = new File(packageDir, subpath);
					
					f.getParentFile().mkdirs();
					
					try ( InputStream input = zip.getInputStream(entry); OutputStream output = new FileOutputStream(f); ) {
						Utils.pipe(input, output);
					}
				}
				
			}
			
			zip.close();
			packageZip.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getRemoteReleases() {
		try {
			String arch = System.getProperty("os.arch");
			URL url = new URL("https", "discord.com", "/api/updates/stable/RELEASES?id=Discord&arch=" + arch);
			return Utils.download(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getLocalReleases() {
		try {
			Path releasesPath = Path.of("releases.txt");
			if(Files.exists(releasesPath)) {
				return Files.readString(releasesPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
