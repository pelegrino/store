package br.com.pelegrino.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.pelegrino.store.model.ImagemProduto;
import br.com.pelegrino.store.model.dto.ImagemProdutoDTO;
import br.com.pelegrino.store.repository.ImagemProdutoRepository;

@RestController
public class ImagemProdutoController {
	
	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto) {
		
		imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);
		
		ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
		imagemProdutoDTO.setId(imagemProduto.getId());
		imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
		imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
		imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
		imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
		
		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteTodoImagemProduto/{id}")
	public ResponseEntity<?> deleteTodoImagemProduto(@PathVariable("id") Long idProduto) {
		
		imagemProdutoRepository.deleteImagem(idProduto);
		return new ResponseEntity<String>("Imagens do produto removidas.", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteImagemObjeto")
	public ResponseEntity<?> deleteImagemObjeto(@RequestBody ImagemProduto imagemProduto) {

		if (!imagemProdutoRepository.existsById(imagemProduto.getId())) {
			return new ResponseEntity<String>("Imagem já foi removida ou não existe esse id: " + imagemProduto.getId(), HttpStatus.OK);
		}
		
		
		imagemProdutoRepository.deleteById(imagemProduto.getId());
		return new ResponseEntity<String>("Imagem removida.", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteImagemProdutoPorId/{id}")
	public ResponseEntity<?> deleteImagemProdutoPorId(@PathVariable("id") Long id) {
		
		if (!imagemProdutoRepository.existsById(id)) {
			return new ResponseEntity<String>("Imagem já foi removida ou não existe esse id: " + id, HttpStatus.OK);
		}
		
		imagemProdutoRepository.deleteById(id);
		return new ResponseEntity<String>("Imagem removida.", HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "/obterImagemPorProduto/{idProduto}")
	public ResponseEntity<List<ImagemProdutoDTO>> obterImagemPorProduto(@PathVariable("idProduto") Long idProduto) {
		
		List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();
		
		List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(idProduto);
		
		for (ImagemProduto  imagemProduto : imagemProdutos) {
			
			ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
			imagemProdutoDTO.setId(imagemProduto.getId());
			imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
			imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
			imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
			imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
			dtos.add(imagemProdutoDTO);
			
		}
		
		return new ResponseEntity<List<ImagemProdutoDTO>>(dtos, HttpStatus.OK);
		
	}

}
