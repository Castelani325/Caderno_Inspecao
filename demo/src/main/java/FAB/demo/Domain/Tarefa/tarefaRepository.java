package FAB.demo.Domain.Tarefa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface tarefaRepository extends JpaRepository <Tarefa, Long> {

    //String DATA JPA cria querys automaticamente
    // Select * FROM Tarefas WHERE caderno_id = XXXX
    List<Tarefa> findCadernoById (Long caderno_Id);
}
