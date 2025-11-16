package FAB.demo.Domain.tarefa;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //BD cuida da geração da Primary Key
    private Long id;


    @NotNull
    private String descricao;

    private String status_mantenedor; //Pendente , Executado
    private String status_inspetor; // Pendente , Inspetorado
    private String secao; // CEL, HID, ROT, AVI, MOT, EQV


}
