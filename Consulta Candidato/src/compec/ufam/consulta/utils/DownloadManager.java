package compec.ufam.consulta.utils;

import java.io.*;
import java.net.*;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import jcifs.netbios.*;

import org.apache.commons.io.*;

import com.phill.libs.*;

public class DownloadManager {

	private final JLabel labelInfos;
	private long bytesDownloaded;
	
	
	private final int BUFFER = 65536;
	private final int UPDATE_INTERVAL = 250;
	private final URL SHEETS_SERVER  = findServer();
	private final File SHEETS_FOLDER  = ResourceManager.getResourceAsFile("sheets");
	
	
	/** Baixa as planilhas de dados do servidor descrito no arquivo de propriedades do sistema.
	 *  @param labelInfos - label para mostrar as atualizações */
	public static void run(final JLabel labelInfos) throws IOException {
		new DownloadManager(labelInfos).downloadSheets();
	}
	
	/** Construtor privado inicializando o label.
	 *  @param labelInfos - label para mostrar as atualizações */
	private DownloadManager(final JLabel labelInfos) {
		this.labelInfos = labelInfos;
	}
	
	/** Baixa as planilhas do servidor descrito no arquivo de propriedades.
	 *  @throws IOException quando os arquivos não podem ser baixados ou escritos no disco. */
	private void downloadSheets() throws IOException {
		
		// Recuperando lista de download
		String[] downloadList = getDownloadList();
		
		// Apaga todos os arquivos contidos em 'res/sheets'
		for (File arquivo: SHEETS_FOLDER.listFiles())
			arquivo.delete();
		
		// Baixa as planilhas do servidor
		for (String remoteFile: downloadList) {
			
			URL  inputFile  = new URL (SHEETS_SERVER, remoteFile);
			File outputFile = new File(SHEETS_FOLDER, remoteFile);
			
			downloadFile(inputFile, outputFile);
			
		}
		
	}
	
	/** Recupera a lista de downloads [index.txt] de planilhas do servidor.
	 *  @return Array com os nomes dos arquivos a serem baixados.
	 *  @throws IOException quando o arquivo 'index.txt' não pôde ser baixado do servidor. */
	private String[] getDownloadList() throws IOException {
		
		URL index = new URL(SHEETS_SERVER, "index.txt");
		String fileList = IOUtils.toString(index, "UTF-8");
		
		return fileList.split("\n");
	}
	
	/** Baixa um <code>arqRemoto</code> para um <code>arqDestino</code>.
	 *  @param arqRemoto - link de um arquivo remoto
	 *  @param arqDestino - arquivo de destino
	 *  @throws IOException quando os arquivos não podem ser baixados ou escritos no disco. */
	private void downloadFile(final URL arqRemoto, final File arqDestino) throws IOException {
		
		// Preparando variáveis de download
		int bytesRead = 0;
		final byte buffer[] = new byte[BUFFER];
		
		// Preparando streams
		final BufferedInputStream inputStream  = new BufferedInputStream(arqRemoto.openStream());
		final FileOutputStream    outputStream = new FileOutputStream   (arqDestino);
		
		// Inicializando o contador de bytes
		this.bytesDownloaded = 0;
		
		// Inicializando o monitor de eventos
		Thread monitor = createAndStartMonitor(arqDestino.getName());
		
		// Realizando o download propriamente dito
		while ((bytesRead = inputStream.read(buffer,0,BUFFER)) != -1) {
			
        	bytesDownloaded += bytesRead;
        	outputStream.write(buffer, 0, bytesRead);
        }
		
		// Limpando recursos
		monitor.interrupt();
		
		inputStream .close();
		outputStream.close();
		
	}
	
	/** Imprime uma mensagem no label.
	 *  @param message - mensagem a ser impressa */
	private void showMessageLabel(final String message) {
		SwingUtilities.invokeLater(() -> this.labelInfos.setText(message));
	}
	
	private URL findServer() {
		
		showMessageLabel("Buscando servidor...");
		
		String hostname = PropertiesManager.getString("net.home", null);
		
		try {
			
			String serverIP = NbtAddress.getByName(hostname).getHostAddress();
			
			return new URL("http://" + serverIP);
		}
		catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
		
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
			showMessageLabel(message + getBytesDownloaded());
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
