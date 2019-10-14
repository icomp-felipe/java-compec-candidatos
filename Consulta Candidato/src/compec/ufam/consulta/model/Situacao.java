package compec.ufam.consulta.model;

public class Situacao {

	private String id, resumo, descricao;

	public Situacao(String id, String resumo, String descricao) {
		this.id = id;
		this.resumo = resumo;
		this.descricao = descricao;
	}

	public String getID() {
		return id;
	}

	public String getResumo() {
		return resumo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getText() {
		return String.format("Código: %s\nResumo: %s\nDescrição: %s",id,resumo,descricao);
	}
	
}
