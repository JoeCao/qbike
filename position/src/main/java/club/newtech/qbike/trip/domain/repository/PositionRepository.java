package club.newtech.qbike.trip.domain.repository;

import club.newtech.qbike.trip.domain.core.root.Postion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Postion, Integer> {
//    @Query("select p from Postion p where p.driver.id = :0")
    Postion findByDriver_Id(Integer driverId);
}
