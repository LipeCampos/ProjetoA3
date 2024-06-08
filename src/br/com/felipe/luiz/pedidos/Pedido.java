package br.com.felipe.luiz.pedidos;

import br.com.felipe.luiz.cardapio.Produto;
import br.com.felipe.luiz.pagamento.FormaDePagamento;

public class Pedido {
	
	private int id;
	private int mesa;
	private Produto produto;
	private FormaDePagamento formaDePagamento;
	private boolean finalizado;
	
	public Pedido(int mesa) {
		
		this.mesa = mesa;
		this.finalizado = false;
		
	}

	public int getId() { return this.id; }
	public void setId(int id) { this.id = id; }
	
	public Produto getProduto() { return produto; }
	public void setProduto(Produto produto) { this.produto = produto; }

	public int getMesa() { return this.mesa; }
	public void setMesa(int mesa) { this.mesa = mesa; }

	public boolean isDone() { return this.finalizado; }
	public void setStatus(boolean finalizado) { this.finalizado = finalizado; }

	public FormaDePagamento getFormaDePagamento() { return formaDePagamento; }
	public void setFormaDePagamento(FormaDePagamento formaDePagamento) { this.formaDePagamento = formaDePagamento; }

}
