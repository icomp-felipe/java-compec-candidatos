package compec.ufam.consulta.utils;

import java.io.*;
import java.net.*;
import org.apache.commons.io.*;
import com.phill.libs.*;

import jcifs.netbios.NbtAddress;

public class DownloadManager {

	private long bytesDownloaded;
	private static final int BUFFER = 65536;
	private static final int UPDATE_INTERVAL = 250;
	private static final String LINE_SEPARATOR = System.getProperty("file.separator");
	private static final String SHEETS_SERVER  = findServer();
	private static final String SHEETS_FOLDER  = ResourceManager.getResource("sheets") + LINE_SEPARATOR;
	
	public static void updateSheets() {
		try { new DownloadManager().downloadSheets(); }
		catch (IOException exception) {
			AlertDialog.showMessageForAWhile("Falha ao baixar planilhas!");
		}
	}
	
	private static String findServer() {
		System.out.print("Buscando servidor...");
		System.out.flush();
		
		String hostname = PropertiesManager.getProperty("net.home");
		
		try {
			String serverIP = NbtAddress.getByName(hostname).getHostAddress();
			System.out.println(serverIP);
			return String.format("http://%s/", serverIP);
		}
		catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
		
	}
	
	private void downloadSheets() throws IOException {
		String[] downloadList = getDownloadList();
		
		cleanSheetsDirectory();
		
		for (String remoteFile: downloadList) {
			URL  inputFile  = getSourceFile(remoteFile);
			File outputFile = getTargetFile(remoteFile);
			downloadFile(inputFile, outputFile);
		}
		
		AlertDialog.showMessageForAWhile("Planilhas baixadas com sucesso!");
	}
	
	private URL getSourceFile(String remoteSite) throws MalformedURLException {
		return new URL(SHEETS_SERVER + remoteSite);
	}
	
	private File getTargetFile(String localFile) {
		return new File(SHEETS_FOLDER + localFile);
	}
	
	private String[] getDownloadList() throws IOException {
		URL index = new URL(SHEETS_SERVER + "index.txt");
		String fileList = IOUtils.toString(index,"UTF-8");
		return fileList.split("\n");
	}
	
	private void cleanSheetsDirectory() throws IOException {
		File sheetsDirectory = new File(SHEETS_FOLDER);
		
		for (File arquivo: sheetsDirectory.listFiles())
			arquivo.delete();
	} 
	
	private void downloadFile(URL remoteFile, File destination) throws IOException {
		int bytesRead = 0;
		final byte buffer[] = new byte[BUFFER];
		
		final BufferedInputStream  inputStream  = new BufferedInputStream(remoteFile.openStream());
		final FileOutputStream     outputStream = new FileOutputStream(destination);
		
		bytesDownloaded = 0;
		
		Thread monitor = createAndStartMonitor(destination.getName());
		
		while ((bytesRead = inputStream.read(buffer,0,BUFFER)) != -1) {
        	bytesDownloaded += bytesRead;
        	outputStream.write(buffer,0,bytesRead);
        }
		
		monitor.interrupt();
		
		inputStream .close();
		outputStream.close();
	}
	
	private Thread createAndStartMonitor(String destination) {
		Thread thread = new DownloadMonitor(destination);
		
		thread.setName(destination + " monitor");
		thread.start();
		
		return thread;
	}
	
	private class DownloadMonitor extends Thread {

		private final String message;
		
		public DownloadMonitor(String destination) {
			message = String.format("Baixando planilha \"%s\"...",destination);
		}
		
		private String getBytesDownloaded() {
			
			final double size = (double) bytesDownloaded;
			
			if (bytesDownloaded >= 1048576L)
				return String.format("%.2f MB", size/1048576F);
			
			if (bytesDownloaded >= 1024L)
				return String.format("%.2f KB", size/1024F);
			
			return String.format("%d bytes", bytesDownloaded);
			
		}
		
		private void showProgressMessage() {
			System.out.print(message + getBytesDownloaded());
			System.out.flush();
		}
		
		@Override
		public void run() {
			do {
				try {
					showProgressMessage();
					sleep(UPDATE_INTERVAL);
				}
				catch (InterruptedException exception) {
					return;
				}
			} while (true);
		}
		
	}
	
}
