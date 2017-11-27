package club.newtech.qbike.trip.domain.repository;

import club.newtech.qbike.trip.domain.core.root.Position;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, Integer> {
//    @Query("select p from Postion p where p.driver.id = :0")
    Position findByDriver_Id(Integer driverId);
}
