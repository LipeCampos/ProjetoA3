package br.com.felipe.luiz.estoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.com.felipe.luiz.database.ConnectionFactory;

public class EstoqueManager {
	
	private ArrayList<Insumo> insumos;

	public EstoqueManager() {
		
		this.insumos = new ArrayList<Insumo>();
		
		// open: alimentar variável com banco de dados
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			String cmd = "SELECT * FROM insumo";
			PreparedStatement ps = c.prepareStatement(cmd);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				int id = rs.getInt("id");
				String descricao = rs.getString("descricao");
				
				Insumo i = new Insumo(descricao);
				i.setId(id);
				
				this.insumos.add(i);
				
			}
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
	}

	public void registrarInsumo(Insumo insumo) {

		// open: adicionar na database
		ConnectionFactory cf = new ConnectionFactory();
		try (Connection c = cf.obtemConexao()) {
			String cmd = "INSERT INTO insumo (descricao) VALUES (?)";
			PreparedStatement ps = c.prepareStatement(cmd);
			ps.setString(1, insumo.getDescricao());
			ps.execute();
			this.insumos.add(insumo);
			JOptionPane.showMessageDialog(null, "Insumo registrado com sucesso.");
			
		} catch (Exception ex) { ex.printStackTrace(); }
		// close.
		
	}

	public void removerInsumo(Insumo insumo) {
		
		if (this.insumos.contains(insumo)) {

			// open: remover da database
			ConnectionFactory cf = new ConnectionFactory();
			try (Connection c = cf.obtemConexao()) {
				String cmd = "DELETE FROM insumo WHERE id = ?";
				PreparedStatement ps = c.prepareStatement(cmd);
				ps.setInt(1, insumo.getId());
				ps.execute();
				this.insumos.remove(insumo);
				JOptionPane.showMessageDialog(null, "Insumo removido com sucesso.");
				
			} catch (Exception ex) { ex.printStackTrace(); }
			// close.
			
		} else {
			
			JOptionPane.showMessageDialog(null, "Este insumo não foi encontrado.");
			
		}
		
	} public void removerInsumo(String descricao) {
		
		for (Insumo i : this.insumos) {
			
			if (i.getDescricao().equalsIgnoreCase(descricao)) {

				// open: remover da database
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "DELETE FROM insumo WHERE id = ?";
					PreparedStatement ps = c.prepareStatement(cmd);
					ps.setInt(1, i.getId());
					ps.execute();
					this.insumos.remove(i);
					JOptionPane.showMessageDialog(null, "Insumo removido com sucesso.");
					return;
					
				} catch (Exception ex) { ex.printStackTrace(); }
				// close.
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um insumo com esta descrição.");
		
	} public void removerInsumo(int id) {
		
		for (Insumo i : this.insumos) {
			
			if (i.getId() == id) {

				// open: remover da database
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "DELETE FROM insumo WHERE id = ?";
					PreparedStatement ps = c.prepareStatement(cmd);
					ps.setInt(1, i.getId());
					ps.execute();
					this.insumos.remove(i);
					JOptionPane.showMessageDialog(null, "Insumo removido com sucesso.");
					return;
					
				} catch (Exception ex) { ex.printStackTrace(); }
				// close.
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um insumo com este ID.");
		
	}

	public Insumo getInsumo(String descricao) {
		
		for (Insumo i : this.insumos) {
			
			if (i.getDescricao().equalsIgnoreCase(descricao)) {
				
				return i;
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um insumo com esta descrição.");
		return null;
		
	} public Insumo getInsumo(int id) {
		
		for (Insumo i : this.insumos) {
			
			if (i.getId() == id) {
				
				return i;
				
			} else { continue; }
			
		}
		
		JOptionPane.showMessageDialog(null, "Não foi encontrado um insumo com esta ID.");
		return null;
		
	}

	public ArrayList<Insumo> getInsumos() { return this.insumos; }

}
