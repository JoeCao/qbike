package club.newtech.qbike.order.domain.service;

import club.newtech.qbike.order.domain.core.root.Order;
import club.newtech.qbike.order.domain.core.vo.CustomerVo;
import club.newtech.qbike.order.domain.core.vo.DriverVo;
import club.newtech.qbike.order.domain.core.vo.IntentionVo;
import club.newtech.qbike.order.domain.core.vo.Status;
import club.newtech.qbike.order.domain.repository.OrderRepository;
import club.newtech.qbike.order.infrastructure.UserRibbonHystrixApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    UserRibbonHystrixApi userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SequenceFactory sequenceFactory;

    private String generateOrderId() {
        return "T" + String.format("%010d", sequenceFactory.generate("order"));
    }



    @Transactional
    protected Order createOrder(IntentionVo intention) {
        //在调用远程user服务获取用户信息的时候，必须有熔断，否则在事务中很危险
        Order order = new Order();
        order.setOid(generateOrderId());
        CustomerVo customerVo = userService.findCustomerById(intention.getCustomerId());
        DriverVo driverVo = userService.findDriverById(intention.getDriverId());
        order.setCustomer(customerVo);
        order.setDriver(driverVo);
        order.setOrderStatus(Status.OPENED);
        order.setOpened(new Date());
        order.setStartLong(intention.getStartLong());
        order.setStartLat(intention.getStartLat());
        order.setDestLong(intention.getDestLong());
        order.setDestLat(intention.getDestLat());
        order.setIntentionId(String.valueOf(intention.getIntentionId()));
        orderRepository.save(order);
        return order;
    }




}
