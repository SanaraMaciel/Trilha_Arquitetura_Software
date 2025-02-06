package br.com.sanara.loja.imposto;

import java.math.BigDecimal;

import br.com.sanara.loja.orcamento.Orcamento;

public interface Imposto {

	BigDecimal calcular(Orcamento orcamento);

}
