package club.newtech.qbike.trip.domain.repository;

import club.newtech.qbike.trip.domain.core.root.Trip;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<Trip, Integer> {
}
