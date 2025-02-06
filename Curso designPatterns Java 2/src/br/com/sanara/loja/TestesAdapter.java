package br.com.sanara.loja;

import java.math.BigDecimal;

import br.com.sanara.loja.http.ApacheHttpAdapter;
import br.com.sanara.loja.orcamento.ItemOrcamento;
import br.com.sanara.loja.orcamento.Orcamento;
import br.com.sanara.loja.orcamento.RegistroOrcamento;

public class TestesAdapter {

	public static void main(String[] args) {
		Orcamento orcamento = new Orcamento();
		orcamento.adicionarItem(new ItemOrcamento(BigDecimal.TEN));
		orcamento.aprovar();
		orcamento.finalizar();

		RegistroOrcamento registro = new RegistroOrcamento(new ApacheHttpAdapter());
		registro.registrar(orcamento);
	}

}
