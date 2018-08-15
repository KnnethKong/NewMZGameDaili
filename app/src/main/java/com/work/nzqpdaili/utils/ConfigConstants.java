package com.work.nzqpdaili.utils;

/**
 * Created by zhang on 2016/10/20.
 */

public class ConfigConstants {
    // 是否打印日志
    public static boolean IS_LOG = true;

    //正式地址
//    public static String APP_SERVER_API = "http://daili.zai0312.com/";//
    public static String APP_SERVER_API = "http://nanzhaoqipai.zai0312.com:114/";
    public static String APP_SERVER_API_LOGIN = "http://nanzhaoqipai.zai0312.com:114/";//kxf 新增登陆


    //测试
  //  public static String APP_SERVER_API = "http://ceshi.zai0312.com/";//
//    public static String APP_SERVER_API = "http://59.110.174.75:81/mahjong-shunping/index.php";//
    //加载列表条数
    public static  int PAGE_SIZE = 10;

    // 配置图片选择张数
    public static final int SELECT_IMG_NUM = 9;
    public static final int SELECT_IMG_NUMS = 1000;

    public static final int SN_TAKE_PICTURE = 1;// 拍照
    public static final int SN_RESULT_LOAD_IMAGE = 2;// 相册选择
    public static final int SN_CUT_PHOTO_REQUEST = 3;// 剪切返回
    public static final int SN_SELECT_ALBUM = 4;// 相册返回

    public static final int FX_TAKE_PICTURE = 1;// 拍照
    public static final int FX_RESULT_LOAD_IMAGE = 0x12;// 相册选择
    public static final int FX_CUT_PHOTO_REQUEST = 0x13;// 剪切返回
    public static final int FX_SELECT_ALBUM = 0x14;// 相册返回

    public static final int XQ_TAKE_PICTURE = 0x21;// 拍照
    public static final int XQ_RESULT_LOAD_IMAGE = 0x22;// 相册选择
    public static final int XQ_CUT_PHOTO_REQUEST = 0x23;// 剪切返回
    public static final int XQ_SELECT_ALBUM = 0x24;// 相册返回

    public static String API_SERVER_ERR = "服务器链接错误~";

    public static String NETWORK_STATE = "亲，您的手机网络不太顺畅喔~";
    public static String NETWORK_STATE_NO_USE = "亲，您的手机网络不可用~";

    public static String NO_MORE_DATA = "没有更多数据了~";
    public static String NO_SEARCH_DATA = "没有搜索到数据了~";
    public static String GO_OTHER_LOGIN = "该用户在其他设备登录。如果非本人操作请尽快修改密码";
    public static String LOGIN = "?g=client&m=public&a=login";//登录
    public static String ZHUCE = "?g=client&m=public&a=register";//注册
    public static String  FORGET = "?g=client&m=public&a=getpwd";//忘记密码
    public static String CHILD = "?g=client&m=my&a=children";//孩子信息
    public static String QIANDAO = "?g=client&m=my&a=qiandao";//签到
    public static String TRAIL = "?g=client&m=trail&a=publish";//发布今日小指导/记录/轨迹
    public static String PINGLUN = "?g=client&m=article&a=submit";//文章评论
    public static String VPINGLUN = "?g=client&m=video&a=submit";//视频评论
    public static String ZAN = "?g=client&m=article&a=zan";//文章评论点赞
    public static String VZAN = "?g=client&m=video&a=comment_zan";//视频评论点赞
    public static String VZAN1 = "?g=client&m=video&a=video_zan";//视频点赞
    public static String WENZHANGSHOOUCANG = "?g=client&m=article&a=love";//文章收藏
    public static String WENZHANGDZAN = "?g=client&m=article&a=artzan";//文章点赞
    public static String VIDEOSHOOUCANG = "?g=client&m=video&a=love";//视频收藏
    public static String ZHUANJSHOOUCANG = "?g=client&m=expert&a=love";//专家收藏
    public static String TIWEN = "?g=client&m=expert&a=ask";//提问专家
    public static String QUANZI = "?g=client&m=quanzi&a=publish";//发布圈子
    public static String FQUANZI = "?g=client&m=quanzi&a=submit";//发布圈子评论
    public static String QUANZIZAN = "?g=client&m=quanzi&a=zan";//发布圈子评论
    public static String QUANZISC= "?g=client&m=quanzi&a=love";//圈子收藏
    public static String ADDRESS= "?g=client&m=address&a=update";//用户增加/修改收货地址
    public static String ADDRESSM= "?g=client&m=address&a=setrdefault";//用户收货地址设为默认
    public static String TIJIAO= "?g=client&m=bar&a=submit";//提交订单
    public static String PAY= "?g=client&m=bar&a=gopay";//去支付
    public static String BOOKTJ= "?g=client&m=bar&a=love";//图书收藏
    public static String CLASS= "?g=client&m=study&a=apply";//课程申请
    public static String YJFK= "?g=client&m=my&a=feedback";//在线反馈
    public static String QIZHI= "?g=client&m=my&a=savetest";//在线反馈
    public static String Touxiang= "?g=client&m=my&a=update";//在线反馈

}
