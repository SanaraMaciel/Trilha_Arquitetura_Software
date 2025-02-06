package br.com.sanara.rh.service.reajuste;

import java.math.BigDecimal;

import br.com.sanara.rh.model.Funcionario;

public interface ValidacaoReajuste {
	
	void validar(Funcionario funcionario, BigDecimal aumento);

}
