package club.newtech.qbike.intention.domain.repository;

import club.newtech.qbike.intention.domain.core.vo.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
}
