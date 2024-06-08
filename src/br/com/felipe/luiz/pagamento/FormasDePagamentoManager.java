package br.com.felipe.luiz.pagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.com.felipe.luiz.database.ConnectionFactory;

public class FormasDePagamentoManager {
	
	private ArrayList<FormaDePagamento> formasDePagamento;
	
	public FormasDePagamentoManager() {
		
		this.formasDePagamento = new ArrayList<FormaDePagamento>();

		// open: alimentar variável com banco de dados
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			String cmd = "SELECT * FROM forma_de_pagamento";
			PreparedStatement ps = c.prepareStatement(cmd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				int id = rs.getInt("id");
				String descricao = rs.getString("descricao");
				
				FormaDePagamento fp = new FormaDePagamento(descricao);
				fp.setId(id);
				
				this.formasDePagamento.add(fp);
				
			}
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
	}
	
	public void registrarFormaDePagamento(FormaDePagamento formaDePagamento) {
		
		// open: adicionar na database
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			String cmd = "INSERT INTO forma_de_pagamento (descricao) VALUES (?)";
			PreparedStatement ps = c.prepareStatement(cmd);
			ps.setString(1, formaDePagamento.getDescricao());
			ps.execute();
			this.formasDePagamento.add(formaDePagamento);
			JOptionPane.showMessageDialog(null, "Forma fe pagamento registrada com sucesso.");
			return;
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
		this.formasDePagamento.add(formaDePagamento);
		
	}
	
	public void removerFormaDePagamento(FormaDePagamento formaDePagamento) {
		
		if (this.formasDePagamento.contains(formaDePagamento)) {

			// open: remover da database pelo objeto
			ConnectionFactory cf = new ConnectionFactory();
			try (Connection c = cf.obtemConexao()) {
				String cmd = "DELETE FROM forma_de_pagamento WHERE id = ?";
				PreparedStatement ps = c.prepareStatement(cmd);
				ps.setInt(1, formaDePagamento.getId());
				ps.execute();
				this.formasDePagamento.remove(formaDePagamento);
				JOptionPane.showMessageDialog(null, "Forma de pagamento removida.");
				return;
				
			} catch (Exception ex) { ex.printStackTrace(); }
			// close.
			
		} else {
			
			JOptionPane.showMessageDialog(null, "Esta forma de pagamento não foi encontrada.");
			
		}
		
	} public void removerFormaDePagamento(String descricao) {
		
		for (FormaDePagamento fp : this.formasDePagamento) {
			
			if (fp.getDescricao().equalsIgnoreCase(descricao)) {

				// open: remover da database pela descricao
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "DELETE FROM forma_de_pagamento WHERE id = ?";
					PreparedStatement ps = c.prepareStatement(cmd);
					ps.setInt(1, fp.getId());
					ps.execute();
					this.formasDePagamento.remove(fp);
					JOptionPane.showMessageDialog(null, "Forma de pagamento removida.");
					return;
					
				} catch (Exception ex) { ex.printStackTrace(); }
				// close.
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrada uma forma de pagamento com esta descrição.");
		
	} public void removerFormaDePagamento(int id) {
		
		for (FormaDePagamento fp : this.formasDePagamento) {
			
			if (fp.getId() == id) {

				// open: remover da database pela id
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "DELETE FROM forma_de_pagamento WHERE id = ?";
					PreparedStatement ps = c.prepareStatement(cmd);
					ps.setInt(1, fp.getId());
					ps.execute();
					this.formasDePagamento.remove(fp);
					JOptionPane.showMessageDialog(null, "Forma de pagamento removida.");
					return;
					
				} catch (Exception ex) { ex.printStackTrace(); }
				// close.
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrada uma forma de pagamento com este ID.");
		
	}
	
	public FormaDePagamento getFormaDePagamento(String descricao) {
		
		for (FormaDePagamento fp : this.formasDePagamento) {
			
			if (fp.getDescricao().equalsIgnoreCase(descricao)) {
				
				return fp;
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrada uma forma de pagamento com esta descrição.");
		return null;
		
	} public FormaDePagamento getFormaDePagamento(int id) {
		
		for (FormaDePagamento fp : this.formasDePagamento) {
			
			if (fp.getId() == id) {
				
				return fp;
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrada uma forma de pagamento com esta ID.");
		return null;
		
	}
	
	public ArrayList<FormaDePagamento> getFormasDePagamento() { return this.formasDePagamento; }

}
