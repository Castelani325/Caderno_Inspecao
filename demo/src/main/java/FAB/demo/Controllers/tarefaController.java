package FAB.demo.Controllers;


import FAB.demo.Domain.Caderno.Caderno;
import FAB.demo.Domain.Caderno.CadernoDeInspRepository;
import FAB.demo.Domain.Tarefa.Tarefa;
import FAB.demo.Domain.Tarefa.TarefaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
//@RequestMapping("/tarefa")
public class tarefaController {

    @Autowired // injeta instância da classe diretamente
    private TarefaRepository tarefaRepository;

    @Autowired
    private CadernoDeInspRepository cadernoDeInspRepository;

    //POST - Criar tarefa
    //@Valid ativa validações como NOTBLANK
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Tarefa> criarTarefa (@PathVariable Long caderno_id, @Valid @RequestBody Tarefa tarefa) {

        Optional<Caderno> optionalCaderno = cadernoDeInspRepository.findById(caderno_id);

        if (!optionalCaderno.isPresent()){
            return ResponseEntity.notFound().build(); //Caderno não exisate
        }


        tarefa.setCaderno(optionalCaderno.get());
        tarefa.setStatus_mantenedor("PENDENTE"); // Define um status padrão
        tarefa.setStatus_inspetor("PENDENTE"); // Define um status padrão

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        return new ResponseEntity<>(tarefaSalva, HttpStatus.CREATED);

    };



    //GET - Listar todas as tarefas
    @GetMapping ("/tarefa/{id}")
    public ResponseEntity<Tarefa> listaTarefas (@PathVariable Long id) {;

        return tarefaRepository.findById(id).
                map(ResponseEntity::ok).
                orElse(ResponseEntity.notFound().
                        build());
    };


    //GET - Buscar tarefa
    @GetMapping("/{id}")
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
    @PutMapping("/tarefa/{id}")
    public ResponseEntity<Tarefa> atualizaTarefa (@Valid @PathVariable Long id,@Valid @RequestBody Tarefa detalhesTarefa){

        return tarefaRepository.findById(id).map(tarefaExistente -> {
            tarefaExistente.setDescricao(detalhesTarefa.getDescricao());
            tarefaExistente.setStatus_mantenedor(detalhesTarefa.getStatus_mantenedor());
            tarefaExistente.setStatus_inspetor(detalhesTarefa.getStatus_inspetor());
            Tarefa tarefaAtualizada = tarefaRepository.save(tarefaExistente);

            return ResponseEntity.ok(tarefaAtualizada);}).orElse(ResponseEntity.notFound().build());
    };



    //DELETE - Apagar tarefa (Realmente apaga)
    @DeleteMapping("/tarefa/{id}")
    public ResponseEntity<Void> deletarTarefa (@PathVariable Long id) {
        if (!tarefaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tarefaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); //Retorna http 204 (No Content)
    }

}
