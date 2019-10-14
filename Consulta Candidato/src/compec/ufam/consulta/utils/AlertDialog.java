package compec.ufam.consulta.utils;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.datatransfer.*;

/** Classe que contém métodos úteis de manipulação de telas de diálogo
 *  @author Felipe André
 *  @version 2.5, 19/01/2015 */
public class AlertDialog {
	
	/** Mostra uma mensagem de informação padrão */
	public static void informativo(String mensagem) {
		informativo("Informação", mensagem);
	}
	
	/** Mostra uma mensagem de informação personalizada */
	public static void informativo(String titulo, String mensagem) {
		JOptionPane.showMessageDialog(null,mensagem,titulo,JOptionPane.INFORMATION_MESSAGE);
	}
	
	/** Mostra uma mensagem de erro padrão */
	public static void erro(String mensagem) {
		erro("Tela de Erro",mensagem);
	}
	
	/** Mostra uma mensagem de erro personalizada */
	public static void erro(String titulo, String mensagem) {
		JOptionPane.showMessageDialog(null,mensagem,titulo,JOptionPane.ERROR_MESSAGE);
	}
	
	/** Mostra uma janela de diálogo */
	public static int dialog(String mensagem) {
		return JOptionPane.showConfirmDialog(null,mensagem);
	}
	
	/** Cola um texto na área de transferência */
	public static void pasteToClibpoard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
    }
	
	public static void showMessageForAWhile(JTextComponent textField, String message, int secs) {
		new TemporaryMessage(textField,message,secs).start();
	}

	private static class TemporaryMessage extends Thread {
		
		private JTextComponent textField;
		private String message;
		private long sleep;
		
		public TemporaryMessage(JTextComponent textField, String message, int secs) {
			this.textField = textField;
			this.message = message;
			this.sleep = secs * 1000L;
		}

		@Override
		public void run() {
			try {
				updateField(textField, message);
				sleep(sleep);
				updateField(textField, null);
			}
			catch (InterruptedException exception) {
				return;
			}
		}
		
	}
	
	private static class TextFieldUpdater implements Runnable {
		
		private JTextComponent textField;
		private String message;
		
		public TextFieldUpdater(JTextComponent textField, String message) {
			this.textField = textField;
			this.message = message;
		}

		@Override
		public void run() {
			textField.setText(message);
		}
	}
	
	private static void updateField(JTextComponent textField, String message) {
		Runnable job = new TextFieldUpdater(textField,message);
		SwingUtilities.invokeLater(job);
	}
	
	/** Copia um texto da área de transferência */
	public static String copyFromClipboard() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
		   	try { result = (String)contents.getTransferData(DataFlavor.stringFlavor); }
		   	catch (UnsupportedFlavorException | IOException ex) { ex.printStackTrace(); }
		}
	    return result;
	}

	public static void showMessageForAWhile(String string) {
		try {
			System.out.println(string);
			Thread.sleep(5000L);
			System.out.println();
		}
		catch (InterruptedException e) {
			
		}
	}
	
}
