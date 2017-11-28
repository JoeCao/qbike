package club.newtech.qbike.trip.domain.repository;

import club.newtech.qbike.trip.domain.core.vo.Position;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, Integer> {
}
