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

    //GET Listar cadernos
    @GetMapping("/cadernos/{caderno_id}/tarefa")
    public ResponseEntity <List<Tarefa>> listarCadernos (@PathVariable Long caderno_id){

        if (!cadernoDeInspRepository.existsById(caderno_id)){
            return ResponseEntity.notFound().build();
        }

        List<Tarefa> tarefa = tarefaRepository.findBycaderno_id(caderno_id);
        return ResponseEntity.ok(tarefa);
    }

    //GET Buscar cadernos por ID
    @GetMapping("/{caderno_id}")
    public ResponseEntity <Caderno> buscaCaderno (@PathVariable Long caderno_id) {

        return cadernoDeInspRepository.findById(caderno_id).
                map(ResponseEntity::ok). // Se achar, retorna 200 OK
                orElse(ResponseEntity.notFound(). // Se não, 404
                build());
    };


    //DELETE deletar cadernos
    @DeleteMapping("/{caderno_id}")
    public ResponseEntity<Void> deleteCaderno (@PathVariable Long caderno_id) {
        if (!cadernoDeInspRepository.existsById(caderno_id)) {
            return ResponseEntity.notFound().build();
        }

        cadernoDeInspRepository.deleteById(caderno_id);
        return ResponseEntity.noContent().build();
    }




}
