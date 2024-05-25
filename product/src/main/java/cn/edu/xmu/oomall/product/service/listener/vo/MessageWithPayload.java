package cn.edu.xmu.oomall.product.service.listener.vo;

import lombok.Data;

@Data
public class MessageWithPayload {

    @Data
    public class Headers {
        @Data
        public class IdName {
            private Long id;
            private String name;
        }
        private IdName user;
        private String id;
        private Long timestamp;
    }

    private String payload;

    private Headers headers;
}
