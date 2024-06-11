package br.com.unisul.restaurante.cardapio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.com.unisul.restaurante.database.ConnectionFactory;
import br.com.unisul.restaurante.pedidos.Pedido;
import br.com.unisul.restaurante.pedidos.PedidosManager;

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
			
			PedidosManager pm = new PedidosManager();
			boolean pedidoAberto = false;
			for (Pedido p : pm.getPedidos()) {
				if (p.getProduto().getId() == produto.getId()) {
					pedidoAberto = true;
				}
			}
			if (!pedidoAberto) {
				
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

				JOptionPane.showMessageDialog(null, "Não foi possível remover o produto pois existe um pedido registrado com o mesmo."
						+ "\nSolução: cancele ou exclua o pedido e tente novamente.");
				
			}
			
		} else {
			
			JOptionPane.showMessageDialog(null, "Este produto não foi encontrado.");
			
		}
		
	} public void removerProduto(String descricao) {
		
		for (Produto pr : this.produtos) {
			
			if (pr.getDescricao().equalsIgnoreCase(descricao)) {
				
				PedidosManager pm = new PedidosManager();
				boolean pedidoAberto = false;
				for (Pedido p : pm.getPedidos()) {
					if (p.getProduto().getId() == pr.getId()) { pedidoAberto = true; }
				}
				if (!pedidoAberto) {
					
					// open: remover da database pelo objeto
					ConnectionFactory cf = new ConnectionFactory();
					try (Connection c = cf.obtemConexao()) {
						String cmd = "DELETE FROM produto WHERE id = ?";
						PreparedStatement ps = c.prepareStatement(cmd);
						ps.setInt(1, pr.getId());
						ps.execute();
						this.produtos.remove(pr);
						JOptionPane.showMessageDialog(null, "Produto removido com sucesso.");
						return;
						
					} catch (Exception ex) { ex.printStackTrace(); }
					// close.
					
				} else {

					JOptionPane.showMessageDialog(null, "Não foi possível remover o produto pois existe um pedido registrado com o mesmo."
							+ "\nSolução: cancele ou exclua o pedido e tente novamente.");
					
				}
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um produto com esta descrição.");
		
	} public void removerProduto(int id) {
		
		for (Produto pr : this.produtos) {
			
			if (pr.getId() == id) {

				PedidosManager pm = new PedidosManager();
				boolean pedidoAberto = false;
				for (Pedido p : pm.getPedidos()) {
					if (p.getProduto().getId() == pr.getId()) { pedidoAberto = true; }
				}
				if (!pedidoAberto) {
					
					// open: remover da database pelo objeto
					ConnectionFactory cf = new ConnectionFactory();
					try (Connection c = cf.obtemConexao()) {
						String cmd = "DELETE FROM produto WHERE id = ?";
						PreparedStatement ps = c.prepareStatement(cmd);
						ps.setInt(1, pr.getId());
						ps.execute();
						this.produtos.remove(pr);
						JOptionPane.showMessageDialog(null, "Produto removido com sucesso.");
						return;
						
					} catch (Exception ex) { ex.printStackTrace(); }
					// close.
					
				} else {

					JOptionPane.showMessageDialog(null, "Não foi possível remover o produto pois existe um pedido registrado com o mesmo."
							+ "\nSolução: cancele ou exclua o pedido e tente novamente.");
					
				}
				
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
