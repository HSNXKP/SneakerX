package top.naccl.config.properties;

public interface PaymentConstants {

    // 支付宝沙箱接口
    String serverUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 沙箱应用ID
    String APP_ID = "2021000122668674";

    // 应用私钥
    String PRIVATE_KEY =
            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCT4UWD1fJmFokA2gWc8b1UU5nmGyWKpTxcxuzfj1yxAUHuvKw8kXRx8k0DHBN9YjSmcoUHKTNrG+UBICMbK2HEw0uBhPrhz6FzpcOWTjDncOGl2KgCNSQiF2LBAzjqJZbJB599x7LGZw3hkYOiu1l52QDqauVctau3UJ+v9kUEgXAzGLrC6x2cNf5p0uzm3fnfooOedoUtOAPnLYQJaJeq70m6DJFKNaL58S4NI5/Rw1HIJgDUwGQubhChTwf/fD7GBeT+KIMqS1C/ZZ6jNxsbgr1F9HGD0cAc+iw37lcBWSCgQLji68Dsa8S96YwA/cwaemLzlelDPklK2Aln+Hc1AgMBAAECgf9RcsHW2v+nOsI6tdphlsOwrlsVYd5LfrbAYtAUUeG7m/W0iDZVJcf8+ndwPwZ9CJn3Exwx1X98fuCivhCX5kzzEp2DuLp1pOZHzATtxCMrVgDyV2Ix1RMRs8+wfxXZ4Nor3wb5QAQ3QotiscLO3mFpuxvGARpS9byeOsm5IOrij4wPS7/e6J913WZzLwCv5hiuZC0giu33HAg5XJFFLi3ptR32Jhi1tVdkzFE5SOjWg6L01fs8WYPO7y+k4CxOBtQueH1JZHTn/Mx7nX36R9RnNYAoc+8n3LKPCqQtAQVFFolpMZhhfGZbCNFoVAotqbl83bwstiQgrbU3wxyvPEECgYEA9aHKNtnknuleDQWvutTVa7nUt0v9uk5ozIbAYBu22a4c7L0dDcCewLGMjZG1e0kjuXZE4Mz3imxxPMNNYZ218Mew/H+XjgDfXzNpPACY4LHwAKaHpTvwYkA/7jv5bqYwEoIPhAI4aHaOX+XzqmWIuz3X0UHcg45oroFf+MdMdSUCgYEAmh81cXOhvZMXvfuWkog5+DcTDL4aRzmEyATubTiXgec9be3tl8QK/VTZWaJHxeZD9A+o9FNwVr9bP8DYOR48E4Qke/+wSfP2dwwkX75DzDwc5bccvnd0sm5zEgPc8sCjDTZIzFhiCq1cK8HoXCX7mMIk41KAik988E74X7QDRNECgYAiPduiqd1lCcNCrXhB40mdKtqscrt8LVIOsPpAa5yh29PMbNCQCjoXQqkFUzqpZRjF0eboL6qScWuXvOIcqY+jcTMK//5wnU6GRvR0mOC28hczAAQI1MI98Bb5bwLmmeQ5sEfZbzXN2jkexT+ikWTuNUDQuw2yiTeum/p1A9N0wQKBgQCNYp3rYvF4cKzyGw/JgvDzFMsEAI0o08pMl8GGLslGYTTIWOGCPVT7i1AM20NOd+vUxFzxLm47FjaGaMmytFrWaH3zxt8ZKJXsJhZkQq39eRPlZDMZQ4qpeYtUdnjFtRSfNTJZS6c9NkX82ljn1+xT+Z5Pb8r9luf9p+RQH1p0UQKBgQDhGPRtsQV0oWBjTwJhdv2ty8NoEHoaZ5chofVS7PGJlz0/YOB5l1vTDUAYKkpgXFE0B0Gzu1fnTtltHUMU9lyppY7hx2t136AwRpSiRpPGOqw/0Tmu/miyBXnjBch9yM77JGsn0HgCkZZDPhS/00gygXC/lTUz6jrEQm2ry3M8Gg==";
    // 异步回调地址
//    String notifyUrl = "http://sneakerx.nat300.top/alipay/notify";
    String notifyUrl = "http://43.138.9.213:8090/alipay/notify";


    // 前端回调地址
//    String returnUrl = "http://localhost:8080/order";
    String returnUrl = "http://43.138.9.213:8080/order";


    // 支付宝公钥
    String PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2CsvdezWgCx97ENywBRGEAILrbG3HXEZZBA4AHXX9z5nb8tInzuzmx4mchUZvGk+utdyVYjQv/LL46dnzR2Vu1pVr/VlPl38CUH/DH+mSDkfpKpOBY3i7Rx0xu3wp51f79Gwppoq78FhxzqZpoXWntbKMcCgOC+eYdwwbUN/sF8vmdgunIx5qhtFANgwKvSkJZQDMq974NpgMYT7Jlaj2VOVmAGARfZOTuarnDohvCFj4sHr3ZsIb2YSAniVosHrsFeft3ZnsN6BxNUvkOmTozn9imqW4g30uuoC3M6AevoLUHIrNZwF32SQXMWpmGwgs2EEvW3hobeOb1rxsZ5G+wIDAQAB";
}
