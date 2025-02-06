package br.com.sanara.loja.imposto;

import java.math.BigDecimal;

import br.com.sanara.loja.orcamento.Orcamento;

public class ICMS extends Imposto {

	public ICMS(Imposto outro) {
		super(outro);
	}

	public BigDecimal realizarCalculoEspecifico(Orcamento orcamento) {
		return orcamento.getValor().multiply(new BigDecimal("0.1"));
	}

}
