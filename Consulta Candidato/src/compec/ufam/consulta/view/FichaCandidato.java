package compec.ufam.consulta.view;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import com.phill.libs.ResourceManager;

import compec.ufam.consulta.model.Candidato;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/** Classe responsável pela geração do relatório 'Ficha de Candidato'.
 *  @author Felipe André - felipeandresouza@hotmail.com
 *  @version 2.0, 20/FEV/2023 */
public class FichaCandidato {
	
	/** Exibe o relatório de 'Ficha de Colaborador' com os dados de um <code>colaborador</code>.
	 *  @param colaborador - colaborador 
	 *  @throws IOException quando o arquivo de logo do relatório (res/img/logo.jpg) está inacessível
	 *  @throws JRException quando há alguma falha no carregamento do relatório Jasper
	 *  @throws SQLException quando o banco de dados está inacessível */
	public static void show(final Candidato candidato) throws IOException, JRException, SQLException {
		
		// Preenchendo o relatório
		JasperPrint  prints = getJasperPrint(candidato);
		
		// Exibindo o relatório
		JasperViewer viewer = new JasperViewer(prints,false);
		viewer.setTitle("Ficha de Candidato - " + candidato.getNome());
		viewer.setVisible(true);
		
	}
	
	/** Imprime o relatório de 'Ficha de Colaborador' com os dados de um <code>colaborador</code> diretamente na impressora padrão do sistema.
	 *  @param colaborador - colaborador 
	 *  @throws IOException quando o arquivo de logo do relatório (res/img/logo.jpg) está inacessível
	 *  @throws JRException quando há alguma falha no carregamento do relatório Jasper
	 *  @throws SQLException quando o banco de dados está inacessível */
	public static void print(final Candidato candidato) throws IOException, JRException, SQLException {
		
		// Preenchendo o relatório
		JasperPrint  prints = getJasperPrint(candidato);
		
		// Imprimindo o relatório
		JasperPrintManager.printReport(prints, false);
		
	}
	
	/** Prepara o relatório de 'Ficha de Colaborador' com os dados de um <code>colaborador</code>.
	 *  @param colaborador - colaborador 
	 *  @return Objeto contendo o relatório preenchido e pronto para exibição ou impressão
	 *  @throws IOException quando o arquivo de logo do relatório (res/img/logo.jpg) está inacessível
	 *  @throws JRException quando há alguma falha no carregamento do relatório Jasper
	 *  @throws SQLException quando o banco de dados está inacessível */
	private static JasperPrint getJasperPrint(final Candidato candidato) throws IOException, JRException, SQLException {
		
		// Leitura dos arquivos
		File     reportPath = ResourceManager.getResourceAsFile("relatorios/FichaCandidato.jasper");
		BufferedImage  logo = ImageIO.read(ResourceManager.getResourceAsFile("img/logo.jpg"));
		JasperReport report = (JasperReport) JRLoader.loadObject(reportPath);
		
		// Preparação dos parâmetros
		Map<String, Object> parameters = new HashMap<String,Object>(1);
		parameters.put("PAR_LOGO", logo);
		
		// Preparando o datasource
		List<Candidato> beans = new ArrayList<Candidato>(1); beans.add(candidato);
		
		// Preenchendo o relatório
		JasperPrint  prints = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(beans, false));
		prints.setProperty("net.sf.jasperreports.export.xls.ignore.graphics", "true");
		
		return prints;
	}
	
}