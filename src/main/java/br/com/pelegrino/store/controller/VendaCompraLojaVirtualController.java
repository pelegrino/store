package br.com.pelegrino.store.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.ExceptionStore;
import br.com.pelegrino.store.model.Endereco;
import br.com.pelegrino.store.model.ItemVendaLoja;
import br.com.pelegrino.store.model.PessoaFisica;
import br.com.pelegrino.store.model.StatusRastreio;
import br.com.pelegrino.store.model.VendaCompraLojaVirtual;
import br.com.pelegrino.store.model.dto.ItemVendaDTO;
import br.com.pelegrino.store.model.dto.VendaCompraLojaVirtualDTO;
import br.com.pelegrino.store.repository.EnderecoRepository;
import br.com.pelegrino.store.repository.NotaFiscalVendaRepository;
import br.com.pelegrino.store.repository.StatusRastreioRepository;
import br.com.pelegrino.store.repository.VendaCompraLojaVirtualRepository;
import br.com.pelegrino.store.service.VendaService;

@RestController
public class VendaCompraLojaVirtualController {
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaService vendaService;
	
	@ResponseBody
	@PostMapping(value = "/salvarVendaCompraLojaVirtual")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaCompraLojaVirtual(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionStore {
		
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);
		
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		for(int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
			
		}
		
		//Salva a venda
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);
		
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja Local");
		statusRastreio.setCidade("Tupã");
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		statusRastreio.setEstado("SP");
		statusRastreio.setStatus("Inicio Compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		statusRastreioRepository.save(statusRastreio);
		
		//Associa a venda no BD com a Nota Fiscal
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		//Persiste a nota fiscal novamente no BD amarrada com a venda
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
		
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		
		for(ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProduto());
			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable(value = "id") Long idVenda) {
		
		VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(idVenda);
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new VendaCompraLojaVirtual();
		}
		
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());
		compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesconto(compraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(compraLojaVirtual.getId());
		
		for(ItemVendaLoja item : compraLojaVirtual.getItemVendaLojas()) {
			
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProduto());
			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteVendaTotalBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable(value = "idVenda") Long idVenda) {
		
		vendaService.exclusaoTotalVendaBanco(idVenda);
		
		return new ResponseEntity<String>("Venda excluída com sucesso.", HttpStatus.OK);
		
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteVendaTotalBanco2/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco2(@PathVariable(value = "idVenda") Long idVenda) {
		
		vendaService.exclusaoTotalVendaBanco2(idVenda);
		
		return new ResponseEntity<String>("Venda excluída logicamente com sucesso.", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PutMapping(value = "/ativaRegistroBanco/{idVenda}")
	public ResponseEntity<String> ativaRegistroBanco(@PathVariable(value = "idVenda") Long idVenda) {
		
		vendaService.ativaRegistroBanco(idVenda);
		
		return new ResponseEntity<String>("Venda ativada com sucesso.", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaDinamicaFaixaData/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamicaFaixaData(
			@PathVariable("data1") String data1,
			@PathVariable("data2") String data2) throws ParseException {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = null;
		
		compraLojaVirtual = vendaService.consultaVendaFaixaData(data1, data2);
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
		
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId());
		
			for(ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamica(
			@PathVariable(value = "valor") String valor,
			@PathVariable(value = "tipoconsulta") String tipoconsulta) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = null;
		
		if (tipoconsulta.equalsIgnoreCase("POR_ID_PROD")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(Long.parseLong(valor));
			
		} else if (tipoconsulta.equalsIgnoreCase("POR_NOME_PROD")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeProduto(valor.toUpperCase().trim());
			
		} else if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorNomeCliente(valor.toUpperCase().trim());
			
		} else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEnderecoCobranca(valor.toUpperCase().trim());
			
		} else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_ENTREGA")) {
			compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorEnderecoEntrega(valor.toUpperCase().trim());
			
		}
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
		
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId());
		
			for(ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaPorCliente/{idCliente}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorCliente(@PathVariable("idCliente") Long idCliente) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorCliente(idCliente);
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
		
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId());
		
			for(ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProdutoId(@PathVariable(value = "id") Long idProd) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = vendaCompraLojaVirtualRepository.vendaPorProduto(idProd);
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
		
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesconto(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId());
		
			for(ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
		
	}
	
}
