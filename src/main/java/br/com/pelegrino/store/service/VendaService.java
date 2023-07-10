package br.com.pelegrino.store.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.com.pelegrino.store.model.VendaCompraLojaVirtual;
import br.com.pelegrino.store.repository.VendaCompraLojaVirtualRepository;

@Service
public class VendaService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	
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
	
	
	public List<VendaCompraLojaVirtual> consultaVendaFaixaData(String data1, String data2) throws ParseException{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dddd");
		
		Date date1 = dateFormat.parse(data1);
		Date date2 = dateFormat.parse(data2);
		
		return vendaCompraLojaVirtualRepository.consultaVendaFaixaData(date1, date2);
		
	}
		
}
