package com.ngyb.takeout.constant;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/17 16:46
 */
public class Constant {
        public static final String BASEURL = "http://it.nangongyibin.com:8080/TakeOut/";
//    public static final String BASEURL = "http://192.168.0.100:8080/TakeOut/";

    public static final String KEY = "seller";

    public static final String PSF = "配送费：";

    public static final String QS = "起送：";

    public static final int ADD = 0; //0代表有一个添加行为
    public static final int DELETE = 1;//1代表有一个减少行为
    public static final String SHOPCARTLIST = "shopcartList";
    public static final String DELIVERFEE = "deliverFee";

    public static final String RECEIPTADDRESS = "receiptAddress";

    public static final String APPID = "2021001167647406";

    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCOKVDVhW4wvq5Si+A6RB87WcrwZIHQH9AXYk55mF6gcyDJ+OlfVjonSOsMzPDvJ8YaqDAy/fK39EMu4tAQG5ox2HrDph33uvNhdJaofNXW2HB8sx2icLUYM+tcyP45h9ekFEjzZFjIIhKiXZ8irTESoaVV9En5QgNtOouBDqbjpCukuW3CfUfH2jVQ8lapiycjlKCMI3p5J7aEmRqIKzj5hvHCrK9K+Vob0vGNmPcspNIGb+CD8cnGCGoHGxxuyeEEMlcOI+7e5NtM2YI7dlqr/Y5vpgLMNbRZQZuRumAgtnUnV1+M5GKPx/fr7aGHZO/Njgo/PsW0LPL7iwSIIf17AgMBAAECggEAJVnfniovmGkRHikYIdbyKcLe2iheHxOstegjXDlXa2q0S7y1fFIId3fmIffauctGdoyIFW25jvGDkVcQeZr2+W+6WZtUtlkNRya9ysj52jgF3g2llJ9uRUym4zr/6JezZa3auPA9j1+8a6KThCXjBdUovpuvr+jTQ6ilLKRv3tFFcl1wa0/DQ+tR1wW7pX1CCECl7BaSekbMrUJNdd06ZBdUBYoFEXGC07KLgwWIfNFIPrIWs85pREWYdfjJvT5/052abfeEAVdm9bel0Ng8JOQ1SG2ICMRIrNq4cC3W+UQAJ8mTkf9ubk7dYmMFhl5gdtaL0uce1Bl0JXe3gouA2QKBgQDySezakYXLubNPpBBO1A67sXupSAfNDEJp2NivfjZvgc5UnNiOUIOGBIfSGoh31dHldpxDQDUnH6iNYbgR5OykoRUkyiCr79j/GmV4zC5krrXjtQns4kCQ7x5EKx0DUQ2O0FwD6jZl07rI/vHQU5JBMEBAtHHAXFQ2TQ6sEjSnTQKBgQCWNNRpyiB+sQeEEQFzO2NNFD656aIdL2K0sk70f9vGwc8fC5ojKC73iBI8ygH6t7evrjmsIiow/n63LAlKpVXeOxmJz4sLfuk+neeyThToIZSlrfIOapWvCAXa7u3HCVSUwE5ITKhNM0SZKedgP/D4IUuqkSKZ3TtZIZ6/sf2j5wKBgDUyH+u5KawaIJFqzx3Pl1CEXkdDhlrxxx8gOisFxgPjdx7CtRVB/i6NjhiGNNJCwuvjh9mIHmNxovsvqk9beM2IhHDoZBX7+iPAcuj7w2/doBfXps84ub5Ykcebw6n3wMdLXfiKEtl2jDsXJpZ70s+3PVQ3igYW/3JQptkGwkK9AoGBAIb2dR2Q9l2fp0ctGIZ3iCQL2silenqKxz/hLzxVXh5aqkYU4KxSQNE9PFXOlLS4Op3qB/v4vrxDD4x6Tub5ZAxIQgwnpaHwqJcQvszhh7uD4NJ1VxdJAbHBnbE4GgJLCeqWWEByUI79T9fnZPSAv33UPO7HdbWWqOrPYJqRzXtlAoGBAN1LE4jK4ksGtkjPqOf3ZRji2jo6ZgA9eOjiG2vXsR5nEivLzyK7sxdpGmo2shIUm7mLURzKVZeOrRRQDpyxjdXfpsU34Y7kzpTP7IsU3maFCeMhoP8UxVxnGyGt0nfu0o7XMtnAap0nCYf9lERdrnUJiV4mgJM1gA0Ao1+fCgN3";
    public static final String RSA2_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjilQ1YVuML6uUovgOkQfO1nK8GSB0B/QF2JOeZheoHMgyfjpX1Y6J0jrDMzw7yfGGqgwMv3yt/RDLuLQEBuaMdh6w6Yd97rzYXSWqHzV1thwfLMdonC1GDPrXMj+OYfXpBRI82RYyCISol2fIq0xEqGlVfRJ+UIDbTqLgQ6m46QrpLltwn1Hx9o1UPJWqYsnI5SgjCN6eSe2hJkaiCs4+YbxwqyvSvlaG9LxjZj3LKTSBm/gg/HJxghqBxscbsnhBDJXDiPu3uTbTNmCO3Zaq/2Ob6YCzDW0WUGbkbpgILZ1J1dfjORij8f36+2hh2TvzY4KPz7FtCzy+4sEiCH9ewIDAQAB";
    public static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA256WithRSA";
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String ORDERID = "orderId";
    public static final String TYPE = "type";
    public static final String OrderInfo = "orderInfo";
}
