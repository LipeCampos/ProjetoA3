package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.com.felipe.luiz.cardapio.CardapioManager;
import br.com.felipe.luiz.cardapio.Produto;
import br.com.felipe.luiz.database.ConnectionFactory;
import br.com.felipe.luiz.estoque.EstoqueManager;
import br.com.felipe.luiz.estoque.Insumo;
import br.com.felipe.luiz.pagamento.FormaDePagamento;
import br.com.felipe.luiz.pagamento.FormasDePagamentoManager;
import br.com.felipe.luiz.pedidos.Pedido;
import br.com.felipe.luiz.pedidos.PedidosManager;

import java.awt.Color;
import java.awt.Button;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Image;

public class Administration extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Administration frame = new Administration();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Administration() {
		setTitle("Restaurante");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		Image img = new ImageIcon(this.getClass().getResource("/home_img.png")).getImage();
		Button button = new Button("Estoque");
		button.setFont(new Font("Dialog", Font.BOLD, 14));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				EstoqueManager em = new EstoqueManager();
				ArrayList<Insumo> insumos = em.getInsumos();
				
				String[] options = {"Registrar novo", "Consultar existentes"};
				int input = JOptionPane.showOptionDialog(null, "Selecione uma Opção:", "Gestor de Estoque", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 2);
				
				if (input == 0) {
					
					String descricao = JOptionPane.showInputDialog("Insira a descrição do novo insumo:");
					em.registrarInsumo(new Insumo(descricao));
					
				} else {
					
					if (insumos == null || insumos.isEmpty() || insumos.size() == 0) {
						
						JOptionPane.showMessageDialog(null, "Estoque vazio.\nNenhum insumo registrado.");
						
					} else {
						
						for (Insumo i : insumos) {
							
							String[] editOptions = {"Editar", "Excluir", "Próximo ->"};
							input = JOptionPane.showOptionDialog(null, i.getDescricao()
									+ "\n↪ Código: " + i.getId(), "Gestor de Insumos", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, editOptions, 0);
							
							if (input == 0) {
								
								String descricaoNova = JOptionPane.showInputDialog("Insira uma descrição nova:");
								ConnectionFactory cf = new ConnectionFactory();
								try (Connection c = cf.obtemConexao()) {
									String cmd = "UPDATE insumo SET descricao = ? WHERE id = ?";
									PreparedStatement ps = c.prepareStatement(cmd);
									ps.setString(1, descricaoNova);
									ps.setInt(2, i.getId());
									ps.execute();
									JOptionPane.showMessageDialog(null, "Descrição atualizada com sucesso.");
									
								} catch (Exception ex) { ex.printStackTrace(); }
								i.setDescricao(descricaoNova);
								
							} else if (input == 1) { em.removerInsumo(i); break; } else { continue; }
							
						}
						
					}
					
				}
				
			}
			
		});
		
		Button button_2 = new Button("Trocar Senha");
		button_2.setActionCommand("Trocar Senha");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String novaSenha = null;
				String input = JOptionPane.showInputDialog("Insira a senha atual:");
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					String cmd = "SELECT * FROM login_auth";
					PreparedStatement ps = c.prepareStatement(cmd);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						if (rs.getInt("id") == 1) {
							String senha = rs.getString("senha");
							if (input != null && input != "") {
								
								if (input.equals(senha)) {
									
									while (true) {
										
										input = JOptionPane.showInputDialog("Insira a nova senha:");
										String pswConfirm = JOptionPane.showInputDialog("Confirme a nova senha:");
										if (pswConfirm != null && input.equals(pswConfirm)) { novaSenha = input; break; }
										else {
											JOptionPane.showMessageDialog(null, "A confirmação da nova senha falhou.\nTente novamente.");
											break;
										}
										
									}
									
								} else {
									
									JOptionPane.showMessageDialog(null, "Senha incorreta.");
									
								}
								
							}
							
						}
						
					}
					if (novaSenha != null && novaSenha != "") {
						cmd = "UPDATE login_auth SET senha = ? WHERE id = " + 1;
						ps = c.prepareStatement(cmd);
						ps.setString(1, novaSenha);
						ps.execute();
						JOptionPane.showMessageDialog(null, "Senha do painel administrativo alterada com sucesso.");
					}
					
				} catch (Exception ex) { ex.printStackTrace(); }
				
			}
		});
		button_2.setForeground(Color.WHITE);
		button_2.setFont(new Font("Dialog", Font.BOLD, 14));
		button_2.setBackground(new Color(11, 3, 91));
		button_2.setBounds(584, 9, 190, 24);
		contentPane.add(button_2);
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(11, 3, 91));
		button.setBounds(11, 9, 190, 101);
		contentPane.add(button);
		
		Button button_1 = new Button("Gestão de Cardápio");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CardapioManager cm = new CardapioManager();
				ArrayList<Produto> produtos = cm.getProdutos();
				
				String[] options = {"Registrar novo", "Consultar existentes"};
				int input = JOptionPane.showOptionDialog(null, "Selecione uma Opção:", "Gestor de Cardápio", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 2);
				
				if (input == 0) {
					

					String descricao = JOptionPane.showInputDialog("Insira a descrição do novo produto:");
					
					float preco = 0;
					while (true) {
					
						String precoInput = JOptionPane.showInputDialog("Insira o preço do novo produto:\n↪ Formato: 0.0");
						try {
							
							preco = Float.parseFloat(precoInput);
							break;
							
						} catch (Exception ex) {
							
							JOptionPane.showMessageDialog(null, "Insira um número real.\n↪ Formato: 0.0");
							
						}
					
					}
					
					cm.registarProduto(new Produto(descricao, preco));
					
				} else {
					
					if (produtos == null || produtos.isEmpty() || produtos.size() == 0) {
						
						JOptionPane.showMessageDialog(null, "Cardápio vazio.\nNenhum produto registrado.");
						
					} else {
						
						for (Produto p : produtos) {
							
							String alteracao = "Tornar visível";
							String status = "Desabilitado";
							if (p.isVisible()) { alteracao = "Desabilitar"; status = "Habilitado"; }
							String[] editOptions = {alteracao, "Excluir", "Próximo ->"};
							input = JOptionPane.showOptionDialog(null, p.getDescricao()
									+ "\n↪ Preço: R$:" + p.getPreco()
									+ "\n↪ Status: " + status
									+ "\n↪ Código: " + p.getId(), "Gestor de Produtos", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, editOptions, 0);
							
							if (input == 0) {
								
								ConnectionFactory cf = new ConnectionFactory();
								try (Connection c = cf.obtemConexao()) {
									String cmd = "UPDATE produto SET visivel = ? WHERE id = ?";
									PreparedStatement ps = c.prepareStatement(cmd);
									ps.setBoolean(1, !p.isVisible());
									ps.setInt(2, p.getId());
									ps.execute();
									JOptionPane.showMessageDialog(null, "Visibilidade atualizada com sucesso.");
									
								} catch (Exception ex) { ex.printStackTrace(); }
								p.setStatus(!p.isVisible());
								
							} else if (input == 1) { cm.removerProduto(p); break; } else { continue; }
							
						}
						
					}
					
				}
				
			}
		});
		button_1.setFont(new Font("Dialog", Font.BOLD, 14));
		button_1.setForeground(Color.WHITE);
		button_1.setBackground(new Color(11, 3, 91));
		button_1.setBounds(10, 122, 190, 101);
		contentPane.add(button_1);
		
		Button button_1_1 = new Button("Gerir formas de Pagamento");
		button_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				FormasDePagamentoManager fpm = new FormasDePagamentoManager();
				ArrayList<FormaDePagamento> formasDePagamento = fpm.getFormasDePagamento();
				
				String[] options = {"Registrar nova", "Consultar existentes"};
				int input = JOptionPane.showOptionDialog(null, "Selecione uma Opção:", "Gestor de Formas de Pagamento", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 2);
				
				if (input == 0) {

					String descricao = JOptionPane.showInputDialog("Insira a descrição da nova forma de pagamento:");
					fpm.registrarFormaDePagamento(new FormaDePagamento(descricao));
					
				} else {
					
					if (formasDePagamento == null || formasDePagamento.isEmpty() || formasDePagamento.size() == 0) {
						
						JOptionPane.showMessageDialog(null, "Registros vazios.\nNenhuma forma de pagamento registrada.");
						
					} else {
						
						for (FormaDePagamento fp : formasDePagamento) {
							
							String[] editOptions = {"Excluir", "Próximo ->"};
							input = JOptionPane.showOptionDialog(null, fp.getDescricao()
									+ "\n↪ Código: " + fp.getId(), "Gestor de Formas de Pagamento", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, editOptions, 0);
							
							if (input == 0) { fpm.removerFormaDePagamento(fp); break; } else { continue; }
							
						}
						
					}
					
				}
				
			}
		});
		button_1_1.setFont(new Font("Dialog", Font.BOLD, 13));
		button_1_1.setForeground(Color.WHITE);
		button_1_1.setBackground(new Color(11, 3, 91));
		button_1_1.setBounds(11, 236, 190, 101);
		contentPane.add(button_1_1);
		
		Button button_1_1_1 = new Button("Visualizar os Pedidos Abertos");
		button_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PedidosManager pm = new PedidosManager();
				CardapioManager cm = new CardapioManager();
				ArrayList<Pedido> pedidos = pm.getPedidos();
				
				String[] options = {"Abrir pedido", "Consultar existentes"};
				int input = JOptionPane.showOptionDialog(null, "Selecione uma Opção:", "Gestor de Pedidos", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 2);
				
				if (input == 0) {
					
					if (cm.getProdutos() == null || cm.getProdutos().isEmpty() || cm.getProdutos().size() == 0) {

						JOptionPane.showMessageDialog(null, "Primeiro registre algum produto no gestor de cardápio.\nNenhum produto registrado para venda.");
						
					} else {
						
						for (Produto p : cm.getProdutos()) {
							
							if (p.isVisible()) {
	
								FormasDePagamentoManager fpm = new FormasDePagamentoManager();
								ArrayList<FormaDePagamento> fps = fpm.getFormasDePagamento();
								String[] optionsProduto = {"Selecionar", "Não selecionar (->)"};
								String[] pagamentoOptions = new String[fps.size()];
								for (int c = 0; c < fps.size(); c++) { pagamentoOptions[c] = fps.get(c).getDescricao(); }
								input = JOptionPane.showOptionDialog(null, p.getDescricao()	+ "\n↪ R$: " + p.getPreco(), "Oferecimento", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsProduto, 1);
								if (input == 0) {
									
									String formaDePagamentoSelecionada = (String) JOptionPane.showInputDialog(null, "Escolha a forma de pagamento:", "Janela de Pagamento", JOptionPane.DEFAULT_OPTION, null, pagamentoOptions, 0);
									if (formaDePagamentoSelecionada != "") {
										
										Pedido pe = new Pedido(1);
										pe.setProduto(p);
										pe.setFormaDePagamento(fpm.getFormaDePagamento(formaDePagamentoSelecionada));
										pm.registrarPedido(pe);
										JOptionPane.showMessageDialog(null, "Pedido realizado com sucesso.");
										break;
										
									} else {
										
										JOptionPane.showMessageDialog(null, "Compra interrompida!\nForma de pagamento não selecionada");
										continue;
										
									}
									
								} else { continue; }
								
							}
							
						}
						
					}
					
				} else {
					
					if (pedidos == null || pedidos.isEmpty() || pedidos.size() == 0) {
						
						JOptionPane.showMessageDialog(null, "Fila vazia.\nNão há pedidos abertos.");
						
					} else {
						
						for (Pedido p : pedidos) {
							String alteracao = "Finalizar";
							String deny = "Cancelar";
							if (p.isDone()) { alteracao = "Reabrir"; deny = "Excluir"; }
							
							String[] editOptions = {alteracao, deny, "Próximo ->"};
							String status = "Aberto";
							if (p.isDone()) status = "Finalizado";
							input = JOptionPane.showOptionDialog(null, "Pedido Nº" + p.getId()
									+ "\n↪ Produto: " + p.getProduto().getDescricao()
									+ "\n↪ Valor: R$:" + p.getProduto().getPreco()
									+ "\n↪ Forma de Pagamento: " + p.getFormaDePagamento().getDescricao()
									+ "\n↪ Status: " + status, "Gestor de Pedidos", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, editOptions, 0);
							
							if (input == 0) {
								
								ConnectionFactory cf = new ConnectionFactory();
								try (Connection c = cf.obtemConexao()) {
									String cmd = "UPDATE pedido SET finalizado = ? WHERE id = ?";
									PreparedStatement ps = c.prepareStatement(cmd);
									ps.setBoolean(1, !p.isDone());
									ps.setInt(2, p.getId());
									ps.execute();
									JOptionPane.showMessageDialog(null, "Pedido marcado como finalizado com sucesso.");
									
								} catch (Exception ex) { ex.printStackTrace(); }
								p.setStatus(!p.isDone());
								
							} else if (input == 1) { pm.removerPedido(p); break; } else { continue; }
							
						}
						
					}
					
				}
				
			}
		});
		button_1_1_1.setFont(new Font("Dialog", Font.BOLD, 12));
		button_1_1_1.setForeground(Color.WHITE);
		button_1_1_1.setBackground(new Color(11, 3, 91));
		button_1_1_1.setBounds(11, 350, 190, 101);
		contentPane.add(button_1_1_1);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.controlHighlight);
		panel.setForeground(Color.GRAY);
		panel.setBounds(0, 0, 215, 461);
		contentPane.add(panel);
		
		JSeparator separator = new JSeparator();
		
		JSeparator separator_1 = new JSeparator();
		
		JSeparator separator_1_1 = new JSeparator();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(separator, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(separator_1_1, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(116)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(111)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(112)
					.addComponent(separator_1_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(116, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.GRAY);
		panel_1.setBackground(UIManager.getColor("Button.highlight"));
		panel_1.setBounds(216, 0, 568, 461);
		contentPane.add(panel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Restaurante");
		lblNewLabel_2.setForeground(new Color(11, 3, 91));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 29));
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(img));
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(135, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(69)
							.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
					.addGap(133))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(139, Short.MAX_VALUE)
					.addGap(4)
					.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
					.addGap(82))
		);
		panel_1.setLayout(gl_panel_1);
	}
}
