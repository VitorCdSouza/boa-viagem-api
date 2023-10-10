package br.com.etechoracio.boa_viagem.controller;

import br.com.etechoracio.boa_viagem.entity.Gasto;
import br.com.etechoracio.boa_viagem.exceptions.ViagemInvalidaException;
import br.com.etechoracio.boa_viagem.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gastos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GastoController {

	@Autowired
	private GastoRepository repository;

	@GetMapping
	public List<Gasto> listarTodos() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Gasto> buscarPorId(@PathVariable Long id) {
		Optional<Gasto> existe = repository.findById(id);
		return existe.isPresent() ? ResponseEntity.ok(existe.get())
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Gasto> inserir(@RequestBody Gasto obj) {
		Gasto salva = repository.save(obj);
		if(obj.getViagem().getSaida() != null) {
			throw new ViagemInvalidaException("Viagem finalizada não permite inclusão de gastos");
		}
		return ResponseEntity.ok(salva);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Gasto> atualizar(@PathVariable Long id,
											@RequestBody Gasto gasto) {
		Optional<Gasto> existe = repository.findById(id);
		if (existe.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Gasto salva = repository.save(gasto);
		return ResponseEntity.ok(salva);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		boolean existe = repository.existsById(id);
		if (existe) {
			repository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
}
