package club.newtech.qbike.order.domain.repository;

import club.newtech.qbike.order.domain.core.entity.Candidate;
import org.springframework.data.repository.CrudRepository;

public interface CanidateRepository extends CrudRepository<Candidate, Integer> {
}
