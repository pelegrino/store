package br.com.pelegrino.store.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.pelegrino.store.model.VendaCompraLojaVirtual;

@Service
public class VendaService {
	
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void exclusaoTotalVendaBanco2(Long idVenda) {
		String sql = "begin; update venda_compra_loja_virtual set excluido = true where id = " + idVenda + "; commit;";
		jdbcTemplate.execute(sql);
	}

	public void exclusaoTotalVendaBanco(Long idVenda) {

		String value = "begin;"
				+ " UPDATE nota_fiscal_venda SET venda_compra_loja_virtual_id = null WHERE venda_compra_loja_virtual_id = " + idVenda + "; "
				+ " DELETE FROM nota_fiscal_venda WHERE venda_compra_loja_virtual_id = " + idVenda + "; "
				+ " DELETE FROM item_venda_loja WHERE venda_compra_loja_virtual_id = " + idVenda + "; "
				+ " DELETE FROM status_rastreio WHERE venda_compra_loja_virtual_id = " + idVenda + "; "
				+ " DELETE FROM venda_compra_loja_virtual WHERE id = " + idVenda + "; "
				+ " commit;";
		
		jdbcTemplate.execute(value);
		
	}

	public void ativaRegistroBanco(Long idVenda) {
		String sql = "begin; update venda_compra_loja_virtual set excluido = false where id = " + idVenda + "; commit;";
		jdbcTemplate.execute(sql);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<VendaCompraLojaVirtual> consultaVendaFaixaData(String data1, String data2){
		
		String sql = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
				+ " where i.vendaCompraLojaVirtual.excluido = false "
				+ " and i.vendaCompraLojaVirtual.dataVenda >= '" + data1 + "'"
				+ " and i.vendaCompraLojaVirtual.dataVenda <= '" + data2 + "'";
		
		return entityManager.createQuery(sql).getResultList();
		
	}
		
}
