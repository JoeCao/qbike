package club.newtech.qbike.order.domain.repository;

import club.newtech.qbike.order.domain.core.root.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {
}
