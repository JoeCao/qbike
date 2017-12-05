package club.newtech.qbike.intention.controller;

import club.newtech.qbike.intention.controller.bean.MyIntention;
import club.newtech.qbike.intention.domain.core.vo.Customer;
import club.newtech.qbike.intention.domain.core.vo.DriverStatusVo;
import club.newtech.qbike.intention.domain.service.IntentionService;
import club.newtech.qbike.intention.infrastructure.PositionApi;
import club.newtech.qbike.intention.infrastructure.UserRibbonHystrixApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class RibbonHystrixController {
    @Autowired
    private UserRibbonHystrixApi userRibbonHystrixApi;
    @Autowired
    private IntentionService intentionService;
    @Autowired
    private PositionApi positionApi;

    @GetMapping("/ribbon/{id}")
    public Customer findById(@PathVariable Integer id) {
        return this.userRibbonHystrixApi.findById(id);
    }

    @GetMapping("/ribbon/match")
    public Collection<DriverStatusVo> match(double longitude, double latitude) {
        return this.positionApi.match(longitude, latitude);
    }

    @PostMapping("/intentions/place")
    public void place(@RequestBody
                              MyIntention myIntention) {
        intentionService.placeIntention(myIntention.getUserId(),
                myIntention.getStartLongitude(), myIntention.getStartLatitude(),
                myIntention.getDestLongitude(), myIntention.getDestLatitude());
    }

    @PostMapping("/intention/confirm")
    public boolean confirm(int driverId, int intentionId) {
        return intentionService.confirmIntention(driverId, intentionId);
    }
}
