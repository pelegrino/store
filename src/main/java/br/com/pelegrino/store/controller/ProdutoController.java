package br.com.pelegrino.store.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.ExceptionStore;
import br.com.pelegrino.store.model.Produto;
import br.com.pelegrino.store.repository.ProdutoRepository;
import br.com.pelegrino.store.service.ServiceSendEmail;

@Controller
@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@ResponseBody
	@PostMapping(value = "/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionStore, MessagingException, IOException {
		
		if (produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {
			throw new ExceptionStore("Tipo da unidade deve ser informada.");
		}
		
		if (produto.getNome().length() < 10) {
			throw new ExceptionStore("Nome do produto deve ter mais de 10 letras.");
		}
		
		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionStore("Empresa responsável deve ser informada.");
		}
		
		if (produto.getId() == null) {
			
			List<Produto> produtos = produtoRepository.buscaProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());
			
			if (!produtos.isEmpty()) {
				throw new ExceptionStore("Já existe produto com a descrição: " + produto.getNome());
				
			}
		}
		
		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
			throw new ExceptionStore("Categoria deve ser informada.");
		}
		
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
			throw new ExceptionStore("Marca deve ser informada.");
		}
		
		if (produto.getQtdEstoque() < 1) {
			throw new ExceptionStore("O produto deve ter no mínimo 1 no estoque.");
		}
		
		if (produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0) {
			throw new ExceptionStore("Deve ser informado imagens para o produto.");
		}
		
		if (produto.getImagens().size() < 3) {
			throw new ExceptionStore("Deve ser informado pelo menos 3 imagens do produto.");
		}
		
		if (produto.getImagens().size() > 6) {
			throw new ExceptionStore("Deve ser informado no máximo 6 imagens do produto.");
		}
		
		if (produto.getId() == null) {
			
			for (int x = 0; x < produto.getImagens().size(); x++) {
				produto.getImagens().get(x).setProduto(produto);
				produto.getImagens().get(x).setEmpresa(produto.getEmpresa());
				
				String base64Image = "";
			
				if (produto.getImagens().get(x).getImagemOriginal().contains("data:image")) {
					base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];
					
				} else {
					base64Image = produto.getImagens().get(x).getImagemOriginal();
				}
				
				byte[] imagemBytes = DatatypeConverter.parseBase64Binary(base64Image);
				
				BufferedImage  bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemBytes));
				
				if (bufferedImage != null) {
					
					int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
					int largura = Integer.parseInt("800");
					int altura = Integer.parseInt("600");
					
					BufferedImage resizedImage = new BufferedImage(largura, altura, type);
					Graphics2D g = resizedImage.createGraphics();
					g.drawImage(bufferedImage, 0, 0, largura, altura, null);
					g.dispose();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(resizedImage, "png", baos);
					
					String miniImgBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
					
					produto.getImagens().get(x).setImagemMiniatura(miniImgBase64);
					
					bufferedImage.flush();
					resizedImage.flush();
					baos.flush();
					baos.close();
				}
			}
		}
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		if (produto.getAlertaQtdEstoque() && produto.getQtdEstoque() <= 1) {
			
			StringBuilder html = new StringBuilder();
			html.append("<h2>")
			.append("Produto: " + produto.getNome())
			.append(" com estoque baixo: " + produto.getQtdEstoque());
			html.append("<p> Id Prod.: ").append(produto.getId()).append("</h2>");
			
			if (produto.getEmpresa().getEmail() != null) {
				serviceSendEmail.enviarEmailHtml("Produto sem estoque", html.toString(), produto.getEmpresa().getEmail());
			}
		}
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/deleteProduto")
	public ResponseEntity<String> deleteProduto(@RequestBody Produto produto) {
		
		produtoRepository.deleteById(produto.getId());
		
		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteProdutoPorId/{id}")
	public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) {
		
		produtoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionStore {
		
		Produto produto = produtoRepository.findById(id).orElse(null);
		
		if (produto == null) {
			throw new ExceptionStore("Não encontrou produto com o código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarProdNome/{desc}")
	public ResponseEntity<List<Produto>> buscaProdNome(@PathVariable("desc") String desc) {
		
		List<Produto> produto = produtoRepository.buscaProdutoNome(desc.toUpperCase());
		
		return new ResponseEntity<List<Produto>>(produto, HttpStatus.OK);
	
	}
	
}
