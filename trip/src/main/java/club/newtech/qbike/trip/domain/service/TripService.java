package club.newtech.qbike.trip.domain.service;

import club.newtech.qbike.trip.domain.core.Status;
import club.newtech.qbike.trip.domain.core.root.Trip;
import club.newtech.qbike.trip.domain.core.vo.Driver;
import club.newtech.qbike.trip.domain.repository.TripRepository;
import club.newtech.qbike.trip.infrastructure.UserRibbonHystrixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TripService {
    @Autowired
    TripRepository tripRepository;
    @Autowired
    UserRibbonHystrixApi userService;

    public void updatePosition(Integer driverId, String postion) {
        Trip trip = tripRepository.findOne(driverId);
        if (trip != null) {
            tripRepository.save(trip);
        } else {
            Driver driver = userService.findById(driverId);
            trip = new Trip().setDriver(driver).setMyPostion(postion).setStatus(Status.ONLINE);
            tripRepository.save(trip);
        }

    }
}
