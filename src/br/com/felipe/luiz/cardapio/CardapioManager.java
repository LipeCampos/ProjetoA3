package br.com.felipe.luiz.cardapio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.com.felipe.luiz.database.ConnectionFactory;

public class CardapioManager {
	
	private ArrayList<Produto> produtos;
	
	public CardapioManager() {
		
		this.produtos = new ArrayList<Produto>();

		// open: alimentar variável com banco de dados
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			String cmd = "SELECT * FROM produto";
			PreparedStatement ps = c.prepareStatement(cmd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				int id = rs.getInt("id");
				String descricao = rs.getString("descricao");
				float preco = rs.getFloat("preco");
				boolean visivel = rs.getBoolean("visivel");
				
				Produto p = new Produto(descricao, preco);
				p.setId(id);
				p.setStatus(visivel);
				
				this.produtos.add(p);
				
			}
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
	}

	public void registarProduto(Produto produto) {

		// open: adicionar na database
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			String cmd = "INSERT INTO produto (descricao, preco, visivel) VALUES (?, ?, ?)";
			PreparedStatement ps = c.prepareStatement(cmd);
			ps.setString(1, produto.getDescricao());
			ps.setFloat(2, produto.getPreco());
			ps.setBoolean(3, produto.isVisible());
			ps.execute();
			this.produtos.add(produto);
			JOptionPane.showMessageDialog(null, "Produto registrado com sucesso.");
			return;
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
	}

	public void removerProduto(Produto produto) {
		
		if (this.produtos.contains(produto)) {

			// open: remover da database pelo objeto
			ConnectionFactory cf = new ConnectionFactory();
			try (Connection c = cf.obtemConexao()) {
				String cmd = "DELETE FROM produto WHERE id = ?";
				PreparedStatement ps = c.prepareStatement(cmd);
				ps.setInt(1, produto.getId());
				ps.execute();
				this.produtos.remove(produto);
				JOptionPane.showMessageDialog(null, "Produto removido com sucesso.");
				return;
				
			} catch (Exception ex) { ex.printStackTrace(); }
			// close.
			
		} else {
			
			JOptionPane.showMessageDialog(null, "Este produto não foi encontrado.");
			
		}
		
	} public void removerProduto(String descricao) {
		
		for (Produto p : this.produtos) {
			
			if (p.getDescricao().equalsIgnoreCase(descricao)) {

				// open: remover da database pela descricao
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "DELETE FROM produto WHERE id = ?";
					PreparedStatement ps = c.prepareStatement(cmd);
					ps.setInt(1, p.getId());
					ps.execute();
					this.produtos.remove(p);
					JOptionPane.showMessageDialog(null, "Produto removido com sucesso.");
					return;
					
				} catch (Exception ex) { ex.printStackTrace(); }
				// close.
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um produto com esta descrição.");
		
	} public void removerProduto(int id) {
		
		for (Produto p : this.produtos) {
			
			if (p.getId() == id) {

				// open: remover da database pela id
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "DELETE FROM produto WHERE id = ?";
					PreparedStatement ps = c.prepareStatement(cmd);
					ps.setInt(1, p.getId());
					ps.execute();
					this.produtos.remove(p);
					JOptionPane.showMessageDialog(null, "Produto removido com sucesso.");
					return;
					
				} catch (Exception ex) { ex.printStackTrace(); }
				// close.
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um produto com este ID.");
		
	}

	public Produto getProduto(String descricao) {
		
		for (Produto p : this.produtos) {
			
			if (p.getDescricao().equalsIgnoreCase(descricao)) {
				
				return p;
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um produto com esta descrição.");
		return null;
		
	} public Produto getProduto(int id) {
		
		for (Produto p : this.produtos) {
			
			if (p.getId() == id) {
				
				return p;
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um produto com esta ID.");
		return null;
		
	}

	public ArrayList<Produto> getProdutos() { return this.produtos; }
	
	

}
