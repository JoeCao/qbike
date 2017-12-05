package club.newtech.qbike.trip.controller;

import club.newtech.qbike.trip.domain.core.root.DriverStatus;
import club.newtech.qbike.trip.domain.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PositionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionController.class);
    @Autowired
    PositionService positionService;

    @GetMapping("/trips/updatePosition")
    public void positionUpdate(Integer driverId, Double longitude, Double latitude) {
        LOGGER.info(String.format("update position %s %s %s", driverId, longitude, latitude));
        positionService.updatePosition(driverId, longitude, latitude);
    }

    @GetMapping("/trips/match")
    public Collection<DriverStatus> match(Double longitude, Double latitude) {
        return positionService.matchDriver(longitude, latitude);
    }
}
