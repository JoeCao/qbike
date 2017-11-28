package club.newtech.qbike.trip.domain.repository;

import club.newtech.qbike.trip.domain.core.root.DriverStatus;
import org.springframework.data.repository.CrudRepository;

public interface DriverStatusRepo extends CrudRepository<DriverStatus, Integer> {
    DriverStatus findByDriver_Id(Integer driverId);
}
