// Matheus Dias Alcasa e Vitor Cordeiro de Souza 3AI
package br.com.etechoracio.boa_viagem.controller;

import br.com.etechoracio.boa_viagem.entity.Gasto;
import br.com.etechoracio.boa_viagem.exceptions.ViagemInvalidaException;
import br.com.etechoracio.boa_viagem.repository.GastoRepository;
import br.com.etechoracio.boa_viagem.entity.Viagem;
import br.com.etechoracio.boa_viagem.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/viagens")
public class
ViagemController {
    @Autowired
    private ViagemRepository repository;
    @Autowired
    private GastoRepository gastoRepository;

    @GetMapping
    public ResponseEntity<?> buscarTodos(){
        List<Viagem> viagens = repository.findAll();
        if(viagens.isEmpty()){
            return ResponseEntity.notFound().build();
        } else
            return ResponseEntity.ok(viagens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id){
        Optional<Viagem> viagem = repository.findById(id);
        if(viagem.isPresent()){
            return ResponseEntity.ok(viagem);
        } else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody Viagem body){
        Viagem inserida = repository.save(body);
        return ResponseEntity.ok(inserida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Viagem body){
        try{
            Optional<Viagem> alterar = repository.findById(id);

            if(alterar.get().getSaida() != null) {
                throw new ViagemInvalidaException("Viagem finalizada não permite atualização");
            }

            if(alterar.isPresent()) {
                repository.save(body);
                return ResponseEntity.ok().build();
            } else
                return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        Optional<Viagem> existe = repository.findById(id);
        List<Gasto> gastosViagem = gastoRepository.findByViagem(id);

        if (!gastosViagem.isEmpty())
            throw new IllegalArgumentException("Verifique os gastos com a viagem Id: " + id);

        if (existe.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}
