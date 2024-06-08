package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.com.felipe.luiz.cardapio.CardapioManager;
import br.com.felipe.luiz.cardapio.Produto;
import br.com.felipe.luiz.database.ConnectionFactory;
import br.com.felipe.luiz.pagamento.FormaDePagamento;
import br.com.felipe.luiz.pagamento.FormasDePagamentoManager;
import br.com.felipe.luiz.pedidos.Pedido;
import br.com.felipe.luiz.pedidos.PedidosManager;

import java.awt.Color;
import java.awt.Button;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.SystemColor;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
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

@SuppressWarnings("deprecation")
public class Home extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
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
	public Home() {
		setTitle("Restaurante");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.GRAY);
		panel_1.setBackground(UIManager.getColor("Button.highlight"));
		panel_1.setBounds(331, 0, 453, 461);
		contentPane.add(panel_1);
		
		JLabel lblNewLabel_1 = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/home_img.png")).getImage();
		lblNewLabel_1.setIcon(new ImageIcon(img));
		
		JLabel lblNewLabel_2 = new JLabel("Restaurante");
		lblNewLabel_2.setForeground(new Color(11, 3, 91));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 29));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(80, Short.MAX_VALUE)
					.addComponent(lblNewLabel_1)
					.addGap(73))
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addGap(149)
					.addComponent(lblNewLabel_2)
					.addContainerGap(258, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(141, Short.MAX_VALUE)
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1)
					.addGap(80))
		);
		panel_1.setLayout(gl_panel_1);
		
		Button button = new Button("Login");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String auth = "";
				ConnectionFactory cf = new ConnectionFactory();
				try (Connection c = cf.obtemConexao()) {
					
					String cmd = "SELECT * FROM login_auth";
					PreparedStatement ps = c.prepareStatement(cmd);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						
						if (rs.getInt("id") == 1) { auth = rs.getString("senha"); }
						
					}
					
				} catch (Exception ex) { ex.printStackTrace(); }
				
				if (passwordField.getText().equals(auth)) {
					
					Administration admView = new Administration();
					admView.setVisible(true);
					
				} else {
					
					JOptionPane.showMessageDialog(null, "Senha incorreta.");
					
				}
				
			}
		});
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(11, 3, 91));
		button.setBounds(85, 146, 157, 34);
		contentPane.add(button);
		
		Button button_1 = new Button("Cardápio");
		button_1.setForeground(UIManager.getColor("Button.highlight"));
		button_1.setBackground(new Color(11, 3, 91));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CardapioManager cm = new CardapioManager();
				FormasDePagamentoManager fpm = new FormasDePagamentoManager();
				ArrayList<FormaDePagamento> fps = fpm.getFormasDePagamento();
				String[] options = {"Comprar", "Não comprar"};
				String[] pagamentoOptions = new String[fps.size()];
				for (int c = 0; c < fps.size(); c++) { pagamentoOptions[c] = fps.get(c).getDescricao(); }
				for (Produto p : cm.getProdutos()) {
					
					if (p.isVisible()) {
					
						int input = JOptionPane.showOptionDialog(null, p.getDescricao() + "\n↪ R$: " + p.getPreco(), "Oferecimento", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 1);
						if (input == 0) {
							
							String formaDePagamentoSelecionada = (String) JOptionPane.showInputDialog(null, "Escolha a forma de pagamento:", "Janela de Pagamento", JOptionPane.DEFAULT_OPTION, null, pagamentoOptions, 0);
							if (formaDePagamentoSelecionada != "") {
								
								PedidosManager pm = new PedidosManager();
								Pedido pe = new Pedido(1);
								pe.setProduto(p);
								pm.registrarPedido(pe);
								JOptionPane.showMessageDialog(null, "Compra realizada!\nAgradecemos a preferência.");
								break;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Compra interrompida!\nForma de pagamento não selecionada");
								continue;
								
							}
							
						} else { continue; }
						
					}
					
				}
				
				
			}
		});
		button_1.setBounds(85, 298, 157, 98);
		contentPane.add(button_1);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.controlHighlight);
		panel.setForeground(Color.GRAY);
		panel.setBounds(0, 0, 333, 461);
		contentPane.add(panel);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("asd");
		
		JLabel lblNewLabel = new JLabel("Senha");
		
		JSeparator separator = new JSeparator();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(101)
							.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(149)
							.addComponent(lblNewLabel)))
					.addGap(103))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addGap(81)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(118)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(220, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}
}
