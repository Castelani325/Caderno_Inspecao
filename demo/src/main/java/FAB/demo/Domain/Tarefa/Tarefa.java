package FAB.demo.Domain.Tarefa;


import FAB.demo.Domain.Caderno.Caderno;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Entity
@Table (name = "Tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //BD cuida da geração da Primary Key
    private Long id;


    @NotNull
    private String descricao;

    @ManyToOne (fetch = FetchType.LAZY) //Lazy = só carregue do BD se for solicitado
    @JoinColumn(name = "ccaderno_id") // Nome da coluna da chave estrangeira
    @JsonBackReference //Mostra que essa é a classe Filha (Evita StackOverFlow de serealizacao JSON)
    private Caderno caderno;

    private String status_mantenedor; //Pendente , Executado
    private String status_inspetor; // Pendente , Inspetorado
    private String secao; // CEL, HID, ROT, AVI, MOT, EQV


}
