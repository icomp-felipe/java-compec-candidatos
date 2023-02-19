package compec.ufam.consulta.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.phill.libs.ResourceManager;

import compec.ufam.consulta.model.Candidato;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FichaCandidato {
	
	public static void main(String[] args) throws Exception {
		Candidato c = new Candidato();
		c.setNome("FELIPE");
		c.setSexo("M");
		c.setDataNascimento("25061993");
		c.setDataInscricao("2023-01-01 11:54:32.223");
		show(c);
	}

	public static void show(final Candidato candidato) throws Exception {
		
		/** Leitura dos arquivos */
		File     reportPath = ResourceManager.getResourceAsFile("relatorios/FichaCandidato.jasper");
		BufferedImage  logo = ImageIO.read(ResourceManager.getResourceAsFile("img/logo.jpg"));
		JasperReport report = (JasperReport) JRLoader.loadObject(reportPath);
		
		/** Preparação dos parâmetros */
		Map<String, Object> parameters = new HashMap<String,Object>(1);
		parameters.put("PAR_LOGO", logo);
		
		// Preparando o datasource
		List<Candidato> beans = new ArrayList<Candidato>(1); beans.add(candidato);
		
		/** Preenchendo o relatório */
		JasperPrint  prints = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(beans, false));
		prints.setProperty("net.sf.jasperreports.export.xls.ignore.graphics", "true");
		
		/** Preparando e exibindo o relatório */
		JasperViewer jrv = new JasperViewer(prints,false);
		jrv.setTitle("Ficha de Candidato - " + candidato.getNome());
		jrv.setVisible(true);
		
	}
	
}