package FAB.demo.Controllers;


import FAB.demo.Domain.Caderno.Caderno;
import FAB.demo.Domain.Caderno.CadernoDeInspRepository;
import FAB.demo.Domain.Caderno.DadosCadastroCaderno;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caderno")
public class cadernoController {

    @Autowired
    private CadernoDeInspRepository cadernoDeInspRepository;

    //POST Criar caderno
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Caderno criarCaderno (@Valid @RequestBody DadosCadastroCaderno dadosCaderno) {
        Caderno novoCaderno = new Caderno(dadosCaderno);
        return cadernoDeInspRepository.save(novoCaderno);
    };
    //GET Listar cadernos
    //GET Buscar cadernos por ID
    //PUT Inativar cadernos (DELETE)



}
