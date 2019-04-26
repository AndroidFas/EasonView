package eason.rxsomthing.http;

public enum Tips {
    DEFAULT("请求中，请稍候…"),
    LOGIN("登录中，请稍候…"),
    SENDING("发送中，请稍候…"),
    SUBMIT("提交中，请稍候…"),
    REQUEST("请求中，请稍候…"),
    REGISTER("注册中，请稍候…"),
    LOADEMORE("加载中，请稍候…"),
    LONGTIME("数据加载时间较长，请稍候…"),
    UPLOAD_IMAGE("准备上传图片，请稍候…"),
    UPLOAD_HUIBEI("转换中，请稍候…"),
    UPLOADING_IMAGE("上传中，请稍候…"),

    ORDERINFO_LOADING("订单信息获取中..."),
    GOODSINFO_LOADING("套餐信息获取中..."),
    ATTENTION_SHOP("正在添加关注..."),
    REFUND("正在执行退款操作..."),
    COLLECTION_GOODS("正在收藏中...");


    Tips(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }
}