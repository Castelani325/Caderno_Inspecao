package FAB.demo.Controllers;


import FAB.demo.Domain.Caderno.Caderno;
import FAB.demo.Domain.Caderno.CadernoDeInspRepository;
import FAB.demo.Domain.Caderno.DadosCadastroCaderno;
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
@RequestMapping("/caderno")
public class cadernoController {

    @Autowired
    private CadernoDeInspRepository cadernoDeInspRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    //POST Criar caderno
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Caderno criarCaderno (@Valid @RequestBody DadosCadastroCaderno dadosCaderno) {
        Caderno novoCaderno = new Caderno(dadosCaderno);
        return cadernoDeInspRepository.save(novoCaderno);
    };

    @GetMapping
    public ResponseEntity<List<Caderno>> listarTodosOsCadernos() {
        List<Caderno> cadernos = cadernoDeInspRepository.findAll();
        return ResponseEntity.ok(cadernos);
    }

    //GET Listar tarefas de um caderno
    @GetMapping("/{cadernoId}/tarefas")
    public ResponseEntity<List<Tarefa>> listarTarefasDoCaderno(@PathVariable Long cadernoId) {

        if (!cadernoDeInspRepository.existsById(cadernoId)){
            return ResponseEntity.notFound().build();
        }

        List<Tarefa> tarefas = tarefaRepository.findByCadernoId(cadernoId);
        return ResponseEntity.ok(tarefas);
    }

    //GET Buscar cadernos por ID
    @GetMapping("/{idCaderno}")
    public ResponseEntity <Caderno> buscaCaderno (@PathVariable Long cadernoId) {

        return cadernoDeInspRepository.findById(cadernoId).
                map(ResponseEntity::ok). // Se achar, retorna 200 OK
                orElse(ResponseEntity.notFound(). // Se não, 404
                build());
    };


    //DELETE deletar cadernos
    @DeleteMapping("/{idCaderno}")
    public ResponseEntity<Void> deleteCaderno (@PathVariable Long cadernoId) {
        if (!cadernoDeInspRepository.existsById(cadernoId)) {
            return ResponseEntity.notFound().build();
        }

        cadernoDeInspRepository.deleteById(cadernoId);
        return ResponseEntity.noContent().build();
    }




}
