//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.prodorder.config;

import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

@Configuration
public class ProdOrderConfig {

    private final static Logger logger = LoggerFactory.getLogger(ProdOrderConfig.class);

    @Value("${oomall.data-center}")
    private Long dataCenterId;

    @Value("${rocketmq.name-server}")
    private String namesrv;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Bean
    public RocketMQTemplate rocketMQTemplate(){
        DefaultMQProducer producer = new DefaultMQProducer();
        int random = new Random().nextInt(100);
        producer.setProducerGroup(String.format("%s-%d",this.producerGroup,random));
        producer.setNamesrvAddr(this.namesrv);
        RocketMQTemplate template = new RocketMQTemplate();
        template.setProducer(producer);
        return template;
    }

    @Bean
    public SnowFlakeIdWorker snowFlakeIdWorker() {
        if (this.dataCenterId > SnowFlakeIdWorker.maxDatacenterId) {
            throw new IllegalArgumentException("oomall.datacenter大于最大值" + SnowFlakeIdWorker.maxDatacenterId);
        }

        InetAddress ip = null;
        try {
            ip = Inet4Address.getLocalHost();
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String ipAddress = ip.getHostAddress();
        logger.debug("snowFlakeIdWorker: ip = {}", ipAddress);
        Long ipLong = Common.ipToLong(ipAddress);
        Long workerId = ipLong % SnowFlakeIdWorker.maxWorkerId;
        return new SnowFlakeIdWorker(workerId, this.dataCenterId);
    }
}
