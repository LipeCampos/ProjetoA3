package br.com.unisul.restaurante.pagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.com.unisul.restaurante.database.ConnectionFactory;

public class FormaDePagamento {
	
	private int id;
	private String descricao;
	
	public FormaDePagamento(String descricao) {
		
		// open: auto_increment do id
		ArrayList<Integer> iDsExistentes = new ArrayList<Integer>();
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			
			String cmd = "SELECT id FROM forma_de_pagamento";
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
		
		this.setDescricao(descricao);
		
	}

	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }

	public String getDescricao() { return this.descricao; }
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
