package club.newtech.qbike.controller;

import club.newtech.qbike.domain.core.service.UserRibbonHystrixService;
import club.newtech.qbike.domain.core.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RibbonHystrixController {
    @Autowired
    private UserRibbonHystrixService ribbonHystrixService;

    @GetMapping("/ribbon/{id}")
    public User findById(@PathVariable Integer id) {
        return this.ribbonHystrixService.findById(id);
    }
}
