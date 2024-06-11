package br.com.unisul.restaurante.cardapio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.com.unisul.restaurante.database.ConnectionFactory;

public class Produto {
	
	private int id;
	private String descricao;
	private float preco;
	private boolean visivel;
	
	public Produto(String descricao, float preco) {

		// open: auto_increment do id
		ArrayList<Integer> iDsExistentes = new ArrayList<Integer>();
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			
			String cmd = "SELECT id FROM produto";
			PreparedStatement ps = c.prepareStatement(cmd);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) { iDsExistentes.add(rs.getInt("id")); }
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
		iDsExistentes.sort(null);
		int c = 1;
		for (Integer i : iDsExistentes) {
			
			if (i == c + 1) {
				
				this.id = c;
				break;
				
			} else { c++; continue; }
			
		}
		
		this.descricao = descricao;
		this.preco = preco;
		this.visivel = true;
		
	}

	public boolean isVisible() { return visivel; }
	public void setStatus(boolean visivel) { this.visivel = visivel; }

	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }

	public String getDescricao() { return this.descricao; }
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public float getPreco() { return this.preco; }
	public void setPreco(float preco) {
		this.preco = preco;
	}

}
