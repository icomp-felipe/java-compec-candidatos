package compec.ufam.consulta.utils;

import java.awt.*;
import java.io.*;
import javax.swing.border.*;
import javax.swing.text.*;

import com.phill.libs.ResourceManager;

/** Classe que contém métodos úteis de manipulação de GUI
 *  @author Felipe André
 *  @version 2.5, 19/01/2015 */
public class GraphicsHelper {
	
	/** Atributos de criação de fonte */
	private static final File ttfFont  = new File(ResourceManager.getResource("fonts/swiss.ttf"));
	private static final Font baseFont = createFont(ttfFont);
	private static final Font normFont = deriveFont(baseFont, Font.PLAIN, 15);
	
	/** Recursos gráficos */
	private static final Color color = new Color(31,96,203);
	
	/** Retorna a fonte atual */
	public static Font getFont() {
		return normFont;
	}
	
	/** Retorna uma nova fonte com o tamanho especificado */
	public static Font getFont(int size) {
		return deriveFont(baseFont, Font.PLAIN, size);
	}
	
	/** Retorna a cor azul */
	public static Color getColor() {
		return color;
	}
	
	/** Cria uma Borda Personalizada para os painéis do Java Swing e AWT */
	public static TitledBorder getTitledBorder(String title) {
		return new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, getFont(), Color.BLACK);
	}
	
	/** Cria uma Máscara Personalizada para os text fields do Java Swing */
	public static MaskFormatter getMascara(String mascara) {
		return getMascara(mascara,' ');
	}
	
	/** Cria uma Máscara Personalizada com preenchimento personalizado para os text fields do Java Swing */
	public static MaskFormatter getMascara(String mascara, char preenchimento) {
		MaskFormatter formato = new MaskFormatter();
		formato.setValueContainsLiteralCharacters(false);
		try {
			formato.setMask(mascara);
			formato.setPlaceholderCharacter(preenchimento);
		}
		catch (Exception exception) {  
			exception.printStackTrace();
	    }
		return formato;  
	}
	
	/** Cria uma nova fonte TTF a partir de um arquivo especificado */
	public static Font createFont(File arquivoFonte) {
		try {  return Font.createFont(Font.TRUETYPE_FONT, arquivoFonte); }
		catch (Exception exception) { return null; }
	}
	
	/** Cria uma fonte personalizada a partir de uma já existente */
	public static Font deriveFont(Font arquivoFonte, int tipoFonte, int tamanho) {
		try { return arquivoFonte.deriveFont(tipoFonte, (float) tamanho); }
		catch (Exception exception) { return null; }
	}
	
}
