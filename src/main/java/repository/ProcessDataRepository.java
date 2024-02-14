package repository;

import model.ProcessData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessDataRepository extends JpaRepository<ProcessData, Long> {
}
