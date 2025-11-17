package FAB.demo.Domain.Tarefa;

import jakarta.validation.constraints.NotBlank;

public class DadosCadastroTarefa (@NotBlank String Descricao,
                                  String status_inspetor,
                                  String status_mantenedor,
                                  String secao) {
}
