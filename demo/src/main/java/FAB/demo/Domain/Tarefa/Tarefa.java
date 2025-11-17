package FAB.demo.Domain.Tarefa;


import FAB.demo.Domain.Caderno.Caderno;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "Tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //BD cuida da geração da Primary Key
    private Long id;


    @NotNull
    private String descricao;

    @ManyToOne (fetch = FetchType.LAZY) //Lazy = só carregue do BD se for solicitado
    @JoinColumn(name = "id_caderno") // Nome da coluna da chave estrangeira
    @JsonBackReference //Mostra que essa é a classe Filha (Evita StackOverFlow de serealizacao JSON)
    private Caderno caderno;

    private String status_mantenedor; //Pendente , Executado
    private String status_inspetor; // Pendente , Inspetorado
    private String secao; // CEL, HID, ROT, AVI, MOT, EQV

    public Tarefa (String descricao, String status_inspetor, String status_mantenedor, String secao){
        this.descricao = descricao;
        this.status_inspetor = "Pendente";
        this.status_mantenedor = "Pendente";
        this.secao = secao;

    }


}
