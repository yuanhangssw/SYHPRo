package com.tj.common.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 * 
 * @author ruoyi
 */
public class UserTimePassException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserTimePassException()
    {
        super("user.timepass", null);
    }
}
