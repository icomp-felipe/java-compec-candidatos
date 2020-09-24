package compec.ufam.consulta.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.phill.libs.ResourceManager;
import com.phill.libs.StringUtils;

import compec.ufam.consulta.model.Candidato;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FichaCandidato {

	public static void show(Candidato candidato) throws Exception {
		
		/** Leitura dos arquivos */
		File     reportPath = ResourceManager.getResourceAsFile("relatorios/FichaCandidato.jasper");
		BufferedImage  logo = ImageIO.read(ResourceManager.getResourceAsFile("img/logo.jpg"));
		JasperReport report = (JasperReport) JRLoader.loadObject(reportPath);
		
		/** Preparação dos parâmetros */
		Map<String,Object> parameters = getParameters(candidato);
		parameters.put("PAR_LOGO",logo);
		
		/** Preenchendo o relatório */
		JasperPrint  prints = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
		prints.setProperty("net.sf.jasperreports.export.xls.ignore.graphics", "true");
		
		/** Preparando e exibindo o relatório */
		JasperViewer jrv = new JasperViewer(prints,false);
		jrv.setTitle(getScreenTitle(candidato));
		jrv.setVisible(true);
		
	}
	
	private static Map<String,Object> getParameters(Candidato candidato) {
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		
		if (candidato != null) {
			
			parameters.put("PAR_NOME",candidato.getNome(true));
			parameters.put("PAR_NASCIMENTO",candidato.getDataNascimento());
			parameters.put("PAR_CPF",candidato.getCPF(true));
			parameters.put("PAR_SEXO",candidato.getSexo());
			
			parameters.put("PAR_RG_NUM", candidato.getRG());
			parameters.put("PAR_RG_ORGAO", candidato.getOrgaoEmissor());
			parameters.put("PAR_RG_UF",candidato.getUF());
			
			parameters.put("PAR_LOGRADOURO", StringUtils.BR.normaliza(candidato.getLogradouro()));
			parameters.put("PAR_NUMERO", candidato.getNumeroCasa());
			parameters.put("PAR_BAIRRO", StringUtils.BR.normaliza(candidato.getBairro()));
			parameters.put("PAR_CEP",candidato.getCEP(true));
			parameters.put("PAR_MUNICIPIO", candidato.getCidadeUF());
			
			parameters.put("PAR_TELEFONE", candidato.getFone());
			parameters.put("PAR_EMAIL", candidato.getEmail());
			
			parameters.put("PAR_CONCURSO", candidato.getConcurso());
			parameters.put("PAR_CODIGO", candidato.getCodigoAsString());
			parameters.put("PAR_LAST_INSC", candidato.getDataInscricao());
			parameters.put("PAR_SITUACAO", candidato.getSituacao().getText());
			
		}
		
		return parameters;
		
	}
	
	private static String getScreenTitle(Candidato candidato) {
		
		String sufixo = (candidato == null) ? "Modelo" : candidato.getNome(true);
		
		return String.format("Ficha de Candidato - %s", sufixo);
		
	}
	
}
