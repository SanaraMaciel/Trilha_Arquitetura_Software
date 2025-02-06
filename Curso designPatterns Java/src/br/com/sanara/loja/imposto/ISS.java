package br.com.sanara.loja.imposto;

import java.math.BigDecimal;

import br.com.sanara.loja.orcamento.Orcamento;

public class ISS {
	
	public BigDecimal calcular(Orcamento orcamento) {
		return orcamento.getValor().multiply(new BigDecimal("0.06"));
	}

}
