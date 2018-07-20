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
    public static final String interfaceurl = "http://192.168.1.110:8055";
    //public static final String interfaceurl="http://123.207.50.130:8081";
    //endregion

    //region 接口地址

    //登录
    public static final String logininterface = interfaceurl + "/account/user/login";
    //注册
    public static final String Registerinterface = interfaceurl + "/account/user/register";
    //获取用户信息
    public static final String GetUserInfointerface = interfaceurl + "/user/getUserInfo";
    //首页列表
    public static final String IndexCircleInterface = interfaceurl + "/index/findIndex";
    //关注好友
    public static final String ApplyFriendInterface = interfaceurl + "/circle/friendApply";
    //动态点赞
    public static final String CircleZanInterface = interfaceurl + "/circle/circleZan";
    //取消点赞
    public static final String CancelCircleZanInterface = interfaceurl + "/circle/cancelCircleZan";
    //好友动态
    public static final String FriendCircleInterface = interfaceurl + "/circle/friendsCircle";
    //官方文章列表
    public static final String ArticleListInterface = interfaceurl + "/article/articlelist";
    //官方文章点赞
    public static final String ArticleZanInterface = interfaceurl + "/article/articlezan";
    //官方文章取消点赞
    public static final String CancelArticleZanInterface = interfaceurl + "/article/delarticlezan";
    //好友列表
    public static final String MyFriendsListInterface = interfaceurl + "/user/friendsList";
    //好友申请列表
    public static final String FriendsApplyListInterface=interfaceurl+"/user/friendsApplyList";
    //好友操作
    public static final String FriendsOperationInterface=interfaceurl+"/user/friendsOperation";
    //发布动态
    public static final String SendCircleInterface=interfaceurl+"/circle/sendcircle";
    //修改昵称
    public static final String SetNickNameInterface=interfaceurl+"/user/updateNickName";
    //修改签名
    public static final String SetSignInterface=interfaceurl+"/user/updateSign";
    //修改密码
    public static final String UpdatePwdInterface=interfaceurl+"/user/updatePwd";
    //修改手机号码
    public static final String SetMobileInterface=interfaceurl+"/user/updatemobile";
    //修改电子邮箱
    public static final String SetEMailInterface=interfaceurl+"/user/updateemail";
    //上传头像
    public static final String UpdateLogoInterface=interfaceurl+"/user/updatelogo";
    //动态内容图片上传
    public static final String PublishTalkImageInterface=interfaceurl+"/circle/publishtalkimage";
    //发送动态评论
    public static final String circlecommentInterface=interfaceurl+"/circle/circleComment";
    //endregion
}
