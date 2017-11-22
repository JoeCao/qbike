package club.newtech.qbike.order.domain.repository;


import club.newtech.qbike.order.domain.core.entity.DriverPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverPositionRepository extends CrudRepository<DriverPosition, Integer> {
}
