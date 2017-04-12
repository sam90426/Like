package com.sam.like.Common;

import android.app.Application;

/**
 * Created by wuxianxin on 2017/1/4.
 */

public class InterfaceUrl extends Application {

    //region 签名加密秘钥
    public static final String md5key = "SamLikewxx90426";
    //endregion

    //region 接口域名
    //public static final String interfaceurl = "http://192.168.1.24:8086";
    //public static final String interfaceurl="http://192.168.1.100:8081";
    public static final String interfaceurl="http://123.207.50.130:8081";
    //endregion

    //region 接口地址

    //登录
    public static final String logininterface = interfaceurl + "/account/login";
    //注册
    public static final String Registerinterface = interfaceurl + "/account/register";
    //获取用户信息
    public static final String GetUserInfointerface = interfaceurl + "/user/getuserinfo";
    //首页列表
    public static final String IndexCircleInterface = interfaceurl + "/index/index";
    //关注好友
    public static final String ApplyFriendInterface = interfaceurl + "/circle/friendrequests";
    //动态点赞
    public static final String CircleZanInterface = interfaceurl + "/circle/circlezan";
    //取消点赞
    public static final String CancelCircleZanInterface = interfaceurl + "/circle/cancelcirclezan";
    //好友动态
    public static final String FriendCircleInterface = interfaceurl + "/circle/friendscircle";
    //官方文章列表
    public static final String ArticleListInterface = interfaceurl + "/article/articlelist";
    //官方文章点赞
    public static final String ArticleZanInterface = interfaceurl + "/article/articlezan";
    //官方文章取消点赞
    public static final String CancelArticleZanInterface = interfaceurl + "/article/delarticlezan";
    //好友列表
    public static final String MyFriendsListInterface = interfaceurl + "/user/friendslist";
    //好友申请列表
    public static final String FriendsApplyListInterface=interfaceurl+"/user/friendsapplylist";
    //好友操作
    public static final String FriendsOperationInterface=interfaceurl+"/user/friendsoperation";
    //发布动态
    public static final String SendCircleInterface=interfaceurl+"/circle/sendcircle";
    //修改昵称
    public static final String SetNickNameInterface=interfaceurl+"/user/updatenickname";
    //修改签名
    public static final String SetSignInterface=interfaceurl+"/user/updatesign";
    //修改密码
    public static final String UpdatePwdInterface=interfaceurl+"/user/updatepwd";
    //修改手机号码
    public static final String SetMobileInterface=interfaceurl+"/user/updatemobile";
    //修改电子邮箱
    public static final String SetEMailInterface=interfaceurl+"/user/updateemail";
    //上传头像
    public static final String UpdateLogoInterface=interfaceurl+"/user/updatelogo";
    //动态内容图片上传
    public static final String PublishTalkImageInterface=interfaceurl+"/circle/publishtalkimage";
    //发送动态评论
    public static final String circlecommentInterface=interfaceurl+"/circle/circlecomment";
    //endregion
}
