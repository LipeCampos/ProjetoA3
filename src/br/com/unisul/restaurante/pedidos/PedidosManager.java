package br.com.unisul.restaurante.pedidos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.com.unisul.restaurante.cardapio.CardapioManager;
import br.com.unisul.restaurante.database.ConnectionFactory;
import br.com.unisul.restaurante.pagamento.FormasDePagamentoManager;

public class PedidosManager {
	
	private ArrayList<Pedido> pedidos;
	
	public PedidosManager() {
		
		this.pedidos = new ArrayList<Pedido>();

		// open: alimentar variável com banco de dados
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection con = cf.obtemConexao()) {
			String cmd = "SELECT * FROM pedido";
			PreparedStatement ps = con.prepareStatement(cmd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				int id = rs.getInt("id");
				int mesa = rs.getInt("mesa");
				int idProduto = rs.getInt("id_produto");
				int idFormaDePagamento = rs.getInt("id_forma_de_pagamento");
				boolean finalizado = rs.getBoolean("finalizado");
				
				CardapioManager cm = new CardapioManager();
				FormasDePagamentoManager fpm = new FormasDePagamentoManager();
				Pedido p = new Pedido(mesa);
				p.setId(id);
				p.setMesa(mesa);
				p.setProduto(cm.getProduto(idProduto));
				p.setFormaDePagamento(fpm.getFormaDePagamento(idFormaDePagamento));
				p.setStatus(finalizado);
				
				pedidos.add(p);
				
			}
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
	}
	
	public void registrarPedido(Pedido pedido) {
		
		// open: adicionar na database
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			String cmd = "INSERT INTO pedido (mesa, id_produto, id_forma_de_pagamento, finalizado) VALUES (?, ?, ?, ?)";
			PreparedStatement ps = c.prepareStatement(cmd);
			ps.setInt(1, pedido.getMesa());
			ps.setInt(2, pedido.getProduto().getId());
			ps.setInt(3, pedido.getFormaDePagamento().getId());
			ps.setBoolean(4, pedido.isDone());
			ps.execute();
			this.pedidos.add(pedido);
			JOptionPane.showMessageDialog(null, "Pedido registrado com sucesso."
					+ "\n↪ Item: " + pedido.getProduto().getDescricao()
					+ "\n↪ Preço: R$:" + pedido.getProduto().getPreco()
					+ "\n↪ Forma de Pagamento Escolhida: " + pedido.getFormaDePagamento().getDescricao());
			return;
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
	}
	
	public void removerPedido(Pedido pedido) {
		
		if (this.pedidos.contains(pedido)) {

			// open: remover da database pelo objeto
			ConnectionFactory cf = new ConnectionFactory();
			try (Connection c = cf.obtemConexao()) {
				String cmd = "DELETE FROM pedido WHERE id = ?";
				PreparedStatement ps = c.prepareStatement(cmd);
				ps.setInt(1, pedido.getId());
				ps.execute();
				this.pedidos.remove(pedido);
				JOptionPane.showMessageDialog(null, "Pedido removido com sucesso.");
				return;
				
			} catch (Exception ex) { ex.printStackTrace(); }
			// close.
			
		} else {
			
			JOptionPane.showMessageDialog(null, "Este pedido não foi encontrado.");
			
		}
		
	} public void removerPedido(int id) {
		
		for (Pedido p : this.pedidos) {
			
			if (p.getId() == id) {

				// open: remover da database pela id
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "DELETE FROM pedido WHERE id = ?";
					PreparedStatement ps = c.prepareStatement(cmd);
					ps.setInt(1, p.getId());
					ps.execute();
					this.pedidos.remove(p);
					JOptionPane.showMessageDialog(null, "Pedido removido com sucesso.");
					return;
					
				} catch (Exception ex) { ex.printStackTrace(); }
				// close.
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um pedido com este ID.");
		
	}
	
	public Pedido getPedido(int id) {
		
		for (Pedido p : this.pedidos) {
			
			if (p.getId() == id) {
				
				return p;
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um pedido com esta ID.");
		return null;
		
	} public ArrayList<Pedido> getPedidosMesa(int mesa) {
		
		ArrayList<Pedido> lista = new ArrayList<Pedido>();
		
		for (Pedido p : this.pedidos) {
			
			if (p.getMesa() == mesa) {
				
				lista.add(p);
				
			} else { continue; }
			
		}
		
		if (!lista.isEmpty()) { return lista; }
		JOptionPane.showMessageDialog(null, "Não foram encontrados pedidos para esta mesa.");
		return null;
		
	}

	public ArrayList<Pedido> getPedidos() { return this.pedidos; }

}
