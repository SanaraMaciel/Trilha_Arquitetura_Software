package br.com.sanara.loja.imposto;

import java.math.BigDecimal;

import br.com.sanara.loja.orcamento.Orcamento;

public class CalculadoraDeImpostos {

	public BigDecimal calcular(Orcamento orcamento, Imposto imposto) {
		return imposto.calcular(orcamento);
	}

}
