package FAB.demo.Controllers;


import FAB.demo.Domain.Tarefa.Tarefa;
import FAB.demo.Domain.Tarefa.tarefaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefa")
public class tarefaController {

    @Autowired // injeta instância da classe diretamente
    private tarefaRepository tarefaRepository;

    //POST - Criar tarefa
    //@Valid ativa validações como NOTBLANK
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa criarTarefa (@Valid @RequestBody Tarefa tarefa) {
        tarefa.setStatus_mantenedor("PENDENTE"); // Define um status padrão
        tarefa.setStatus_inspetor("PENDENTE"); // Define um status padrão
        return tarefaRepository.save(tarefa);
    };



    //GET - Listar todas as tarefas
    @GetMapping
    public List<Tarefa> listaTarefa () {
        return tarefaRepository.findAll();
    }


    //GET - Buscar tarefa
    @GetMapping("{/id}")
    public ResponseEntity<Tarefa> buscaTarefa (@PathVariable Long id) {
      
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        
        if (tarefa.isPresent()) {
            return ResponseEntity.ok(tarefa.get()); //retorna http 200
        }

        else {
            return ResponseEntity.notFound().build(); // retorna http 400 NOT FOUND
        }
    };



    //PUT - Edita tarefa
    @PutMapping("{/id}")
    public ResponseEntity<Tarefa> atualizaTarefa (@Valid @PathVariable Long id, @RequestBody Tarefa detalhesTarefa){
        Optional<Tarefa> optionalTarefa = tarefaRepository.findById(id);

        if (!optionalTarefa.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Tarefa tarefaExistente = optionalTarefa.get();
        tarefaExistente.setDescricao(detalhesTarefa.getDescricao());
        tarefaExistente.setStatus_inspetor(detalhesTarefa.getStatus_inspetor());
        tarefaExistente.setStatus_mantenedor(detalhesTarefa.getStatus_mantenedor());

        Tarefa atualizaTarefa = tarefaRepository.save(tarefaExistente);

        return ResponseEntity.ok(atualizaTarefa);
    };



    //DELETE - Apagar tarefa (Realmente apaga)
    @DeleteMapping("{/id}")
    public ResponseEntity<Void> deletarTarefa (@PathVariable Long id) {
        if (!tarefaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tarefaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); //Retorna http 204 (No Content)
    }



}
