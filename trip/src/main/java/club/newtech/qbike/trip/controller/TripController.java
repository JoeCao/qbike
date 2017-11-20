package club.newtech.qbike.trip.controller;

import club.newtech.qbike.trip.domain.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripController {
    @Autowired
    TripService tripService;

    @PostMapping("/trips/updatePostion")
    public void positionUpdate(Integer driverId, String position) {
        tripService.updatePosition(driverId, position);
    }
}
