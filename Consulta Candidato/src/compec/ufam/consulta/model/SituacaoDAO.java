package compec.ufam.consulta.model;

import java.io.*;
import java.util.*;
import com.phill.libs.*;
import com.phill.libs.files.PhillFileUtils;

public class SituacaoDAO {

	
	private static SituacaoDAO instance;
	private final HashMap<String,Situacao> listaSituacoes;
	
	public SituacaoDAO() {
		this.listaSituacoes = loadSituacoesToMap();
	}
	
	private HashMap<String,Situacao> loadSituacoesToMap() {
		
		HashMap<String,Situacao> listaSituacoes = new HashMap<String,Situacao>();
		
		for (String line: loadSituacoesToString()) {
			
			String[] aux = line.split(";");
			
			Situacao situacao = new Situacao(aux[0],aux[1],aux[2]);
			listaSituacoes.put(situacao.getID(),situacao);
			
		}
		
		return listaSituacoes;
		
	}
	
	private String[] loadSituacoesToString() {
		
		File    resource = ResourceManager.getResourceAsFile("config/situacoes.csv");
		String situacoes = PhillFileUtils .readFileToString (resource);
		
		return situacoes.split("\n");
		
	}
	
	private static SituacaoDAO getInstance() {
		
		if (instance == null)
			instance = new SituacaoDAO();
		
		return instance;
		
	}
	
	public static Situacao getSituacao(String id) {
		
		Situacao situacao = getInstance().listaSituacoes.get(id);
		
		return (situacao == null) ? getInstance().listaSituacoes.get("-1") : situacao;
	}
	
}
