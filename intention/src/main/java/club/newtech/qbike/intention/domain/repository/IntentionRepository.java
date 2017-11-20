package club.newtech.qbike.intention.domain.repository;

import club.newtech.qbike.intention.domain.core.root.Intention;
import org.springframework.data.repository.CrudRepository;

public interface IntentionRepository extends CrudRepository<Intention, Integer> {
}
