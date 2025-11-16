package FAB.demo.Domain.Caderno;


import FAB.demo.Domain.Tarefa.Tarefa;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cadernos")
public class Caderno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //BD cuida da geração da Primary Key
    private Long caderno_id;


    @NotNull
    private String Tipo_de_caderno; // 40H, 120H, 360H, 480H e 960H

    // mappedBy = mostra que Caderno é a classe PAI
    // cascade = diz que se um caderno for apagado, todas as tarefas devem ser apagadas
    // orphanRemoval = se uma tarefa for apagada do caderno, ela será apagada permanentemente do BD
    @OneToMany (mappedBy = "caderno", cascade = CascadeType.ALL, orphanRemoval = true )
    @JsonManagedReference // Evita StackOverFlow ao serealizar a classe PAI (Caderno)
    private List<Tarefa> tarefas = new ArrayList<>();


    public Caderno(@Valid DadosCadastroCaderno dadosCaderno) {
        this.Tipo_de_caderno = dadosCaderno.Tipo_de_caderno();
    }
}
