
package br.com.correios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.correios.dto.FaixaCepDTO;
import br.com.correios.dto.FaixaCepCidadeDTO;
import br.com.correios.repository.EstadoRepository;

import br.com.correios.model.Estado;

@Controller                             // O Spring vai reconhecela como Classe CONTROLADORA
@RequestMapping("/buscaFaixaCep")       // Aqui indentifica que ira usar essa classe se reuisitado "/buscaFaixaCep" no broswer
public class BuscaFaixaCepController {
		
	@Autowired                            // Referenciando o repositorio - injeção de dependências
	private EstadoRepository estadoRepository;
	
	
	@ModelAttribute (name = "estados")
		public List<Estado> estados() {
		return estadoRepository.findAll();
	}
	
	
	@GetMapping                        //
	public String buscaFaixaCep() {
		return "BuscaFaixaCep";
	}

	@GetMapping(value="/resultadoBusca")     // "value" ou "path"
	
	// O ModeAndView esta especificando a View "resultadiBuscaFaixaCep" que sera renderizada para o usuário final
	// e informarmos quais o dados ela utilizará para isso
	public ModelAndView resultadoBuscaFaixaCep(@RequestParam("uF") Long uF,@RequestParam("localidade") String localidade,RedirectAttributes attributes) {  
	      List<FaixaCepDTO> faixasCep = estadoRepository.buscaPorUF(uF);                                                                                   // uma lista de objetos que serão exibidas por ela
	      List<FaixaCepCidadeDTO> faixasCepCidade = estadoRepository.buscaPorUFeLocalidade(uF,localidade); 
          
      if(uF.equals(null)) {
    		ModelAndView mv = new ModelAndView("redirect:/buscaFaixaCep");
    		attributes.addFlashAttribute("mensagem", " O CAMPO DE BUSCA É OBRIGATORIO");
    		return mv;
    	}

      ModelAndView mv = new ModelAndView("ResultadoBuscaFaixaCep");
	  mv.addObject("faixasCep", faixasCep);                             // o método "addObjeto" é utilizado para dicionarmos uma
	  mv.addObject("faixasCepCidade", faixasCepCidade);                 // uma lista de objetos que serão exibidas por ela
	  
	  if(faixasCep.isEmpty()) {
		  mv.addObject("mensagem", "A BUSCA NÂO RETORNOU DADOS!");
	  }else {
	      mv.addObject("mensagem", "DADOS ENCONTRADOS COM SUCESSO");
	      }
	  
	  return mv;
	}
}
