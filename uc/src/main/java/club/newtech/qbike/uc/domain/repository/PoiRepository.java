package club.newtech.qbike.uc.domain.repository;

import club.newtech.qbike.uc.domain.root.Poi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoiRepository extends JpaRepository<Poi, Integer> {
}
