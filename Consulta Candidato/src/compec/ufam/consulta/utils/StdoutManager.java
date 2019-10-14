package compec.ufam.consulta.utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JLabel;

public class StdoutManager extends OutputStream {
	
	private final JLabel label;
	
	public StdoutManager(JLabel label) {
		this.label = label;
	}
	
	@Override
	public void write(int args) throws IOException {
		write(new byte[] {(byte) args}, 0, 1);
	}
	
	@Override
	public void write(byte[] charSequence, int offset, int length) throws IOException {
		String nova = new String(charSequence,offset,length);
		label.setText(nova);
	}

}
