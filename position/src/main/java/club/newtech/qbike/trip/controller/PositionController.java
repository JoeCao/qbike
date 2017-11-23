package club.newtech.qbike.trip.controller;

import club.newtech.qbike.trip.domain.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionController {
    @Autowired
    PositionService positionService;

    @PostMapping("/trips/updatePosition")
    public void positionUpdate(Integer driverId, String position) {
        positionService.updatePosition(driverId, position);
    }
}
