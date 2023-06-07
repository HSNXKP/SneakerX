package top.naccl.config.properties;

public interface PaymentConstants {

    // 支付宝沙箱接口
    String serverUrl = "https://openapi.alipaydev.com/gateway.do";

    // 沙箱应用ID
    String APP_ID = "2021000122676248";

    // 应用私钥
    String PRIVATE_KEY =
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCzSu2ryb3QiUbCUmIZqVKF1AQp6GQMOQCH/3hmOPXrqafSfPdnMgjQM0fV0udlKaak42fM7kQKw8hp6qEkZcZlaTYFTFPv5bUs97SeZBOFqIsRwqOSpd/bRcCBid/jjJmjdMOzNdaugxalORIwuupR4GKw88+v+irb2xrDrTGUWnmvENT8bKjRfCKed400mHJY3rOrYLbPEv4nW9T0Bi6IcYCMku4/XllRTVOEJ8n5GXmjoSdQFYhV0jt2XUumISeYGOx5+6qZ6l5cBE4xXdrUXMeZppOJcJlK8boOL6FQgiqKD7VtHAXU0r1Q9WKgz/ZwMQnfpcVL3xzkBLxnbSUnAgMBAAECggEAXQ0NLrQkVD96vs185m6PSq+PfaSDkChhUzPOa9cyIp3JYJ/E0wUPt+Z1aM7tkv4746W2MJCUNbYIpJfGDBraDbW9+J/1jDL+hZkFX5L24s5nZs9Kj4iRFr8rus8wqGtHLaMjEjZl9epI7XUiMLJBfD7lWYsgBefjKHmdeWLAsYom7Qw6kFAG7hXx8LxJVUkQVrf2oEzI+DXs6Msy6JzWi8UtJP/xiU/bmNTBG7oD+j/iTIQJtYvL4osl2Vp93lIRZylf6Z+pG6tAbZy6bnchLvqGKx0LH0zvxLD3mIE1DWpj9/gLkP4aCZZp5yxO45alxPnBfP+ZWhFyK4OyX4+I6QKBgQD0/T++JrS7ccc6rIBw/ECSfk5THnvw46bC4K3UJAemHC47ye3FU0rqytmGkwYKDa2koiLcBuv9an20sOpaaCs7dL0Jm8bDKgRAQITZxy09lUjk/rE+uZTtUNaW0Vu9cPoD1DXdMNtHP6NAFsLYbpIA09II0nSvLeUy0AjyK2EX9QKBgQC7WczglFqFaLOp+V6jHxLd0NKGd5FgiYiRa1z7dmhoT2SgeEZaAiAswry7rhHJwyfCCTSbjUf4HJVlCoZjpZJF73ruvrBRkuUCrocMduIIR8m/aflbMR2S11XdcrJTOygSdhYHw+BC+CQXHjJ2zwXxut9IaZY5rraHC1JO7tVDKwKBgQDMS59rNyXBpfjIke8tyP0j7FXudF5Qm9aFbg0m+cCzIY7gZtxzj6jKsMvCsDn6CjwCLP660r7afYJEOr/DEljy5L97Er8mIpSW6/adEL5GO4YlB8rQjruFRsMb04wYJu0mYkFkqYhCGuxM1m43Q8VXEUx19OI+7xvRSrAbGleGPQKBgQCuib97rdoTFXLjtET9VNKfwY6P5U6TkC2hxFLwleQ44VYOxV5NNlPBP37Snh1GSUB93VOcYuecdwybAE4pATQfYTa2uwHKTT563BtGem6jZjXG1LSQx1YU2fXNg7AsKB1x7X8iptIDseb1fpVm6AFSrVScwnlIrcKwVtTMYmZd1wKBgQDaY+CgdQWcDaAFlRH5k9/N/wQRh41djKLchpxVGlQ6OEt8LXEhvJ97YHU8YshUNb8Ek+hvjc+J+GbcneMONXLNA8itOeoBbnmKNxFtJVcSU/CdOlJt/meP7mS8Lk+HI3pkg/YP7Mvwx1Ui2V4OJEX5LYo9KZing78sE1n1Plv7Mw==";

    // 异步回调地址
//    String notifyUrl = "http://sneakerx.nat300.top/alipay/notify";
    String notifyUrl = "http://43.138.9.213:8090/alipay/notify";


    // 前端回调地址
//    String returnUrl = "http://localhost:8080/order";
    String returnUrl = "http://43.138.9.213:8080/order";


    // 支付宝公钥
    String PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApI+B6YwRw3PHrOpWsfkgY76yeUF4amiF1OKVgcpDL7H1vl2TD9KgVQWPZZ/mgBEsOX2KITWhCzYWd7gDcbYRd/rIxbMFNhnBY+n+Z4rFY8DVNyYLkehfhd64nkx9/RVZYAG3GLnRMHXm7xolBzpbDNb6uRTWe82l01glrxPvHZNwrNUiqUE0lpOUPh2Y7tLLXgFV7jzuS5vBDdI+iLRjBDnbhhFMRRbTuehDlws9CjE/7EspAC0eapi5k5v/+wEurdywZOeLXYEMKuygCR67aqioZRMa28O//FllfLtZNFjbrZf/dsK1UGEpSdpQvNQ/LiAkpNpSlNGAmlJxmjWf4QIDAQAB";
}
