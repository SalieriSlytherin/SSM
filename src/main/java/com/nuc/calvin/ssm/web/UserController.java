package com.nuc.calvin.ssm.web;

import com.nuc.calvin.ssm.dto.LoginOk;
import com.nuc.calvin.ssm.dto.Ok;
import com.nuc.calvin.ssm.entity.User;
import com.nuc.calvin.ssm.entity.UserCustom;
import com.nuc.calvin.ssm.service.RelationService;
import com.nuc.calvin.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Calvin
 * @Description:
 */
@Controller("userController")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RelationService relationService;

    /**
     * 用户登录
     *
     * @param
     * @return
     */

    @RequestMapping("/managerLogin")
    public String managerLogin(String account, String password, HttpSession sessiona) {
        List<UserCustom> users = userService.queryAllUser();
        sessiona.setAttribute("users", users);
        return "main";
    }


    @ResponseBody
    @RequestMapping("/loginUser")
    public List<LoginOk> loginUser(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserCustom user = new UserCustom();
        user = userService.getEmail(email);
        int articleCount = userService.queryArticleCount(user.getUserId());
        int followCount = userService.queryFollowCount(user.getUserId());
        int fansCount = userService.queryFansCount(user.getUserId());
        user.setFollowCount(followCount);
        user.setFansCount(fansCount);
        user.setArticleCount(articleCount);
        List<LoginOk> list = new ArrayList<>();
        if (user != null && user.getPassword().equals(password)) {
            list.add(new LoginOk(1, "登录成功!", user));
            System.out.println("登录成功");
        } else {
            list.add(new LoginOk(0, "登录失败！", null));
        }
        return list;
    }

    @ResponseBody
    @RequestMapping("/userRegister")
    public Ok userRegister(HttpServletRequest request) {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = userService.getEmail(email);
        if (user != null) {
            return new Ok(2, "该账号已存在，可直接登录!");
        } else {
            User user1 = new User();
            user1.setUsername(username);
            user1.setEmail(email);
            user1.setPassword(password);
            userService.singUpUser(user1);
            return new Ok(1, "注册成功!");
        }
    }

    @ResponseBody
    @RequestMapping("/queryAllUser")
    public List<UserCustom> queryAllUser() {
        List<UserCustom> userCustomList = userService.queryAllUser();
        List<UserCustom> list = new ArrayList<>();
        UserCustom userCustom = new UserCustom();
        for (int i = 0; i < userCustomList.size(); i++) {
            userCustom = userCustomList.get(i);
            int articleCount = userService.queryArticleCount(userCustom.getUserId());
            int followCount = userService.queryFollowCount(userCustom.getUserId());
            int fansCount = userService.queryFansCount(userCustom.getUserId());
            userCustom.setArticleCount(articleCount);
            userCustom.setFansCount(fansCount);
            userCustom.setFollowCount(followCount);
            list.add(userCustom);
        }
        return list;
    }

    @ResponseBody
    @RequestMapping("/queryUserExSelf")
    public List<UserCustom> queryUserExSelf(HttpServletRequest request) {
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        List<UserCustom> userCustomList = userService.queryUserExSelf(userId);
        List<UserCustom> result = new ArrayList<>();
        UserCustom user = new UserCustom();
        for (int i = 0; i < userCustomList.size(); i++) {
            user = userCustomList.get(i);
            int articleCount = userService.queryArticleCount(user.getUserId());
            int followCount = userService.queryFollowCount(user.getUserId());
            int fansCount = userService.queryFansCount(user.getUserId());
            user.setArticleCount(articleCount);
            user.setFansCount(fansCount);
            user.setFollowCount(followCount);
            result.add(user);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/queryUserByWord")
    public List<UserCustom> queryUserByWord(HttpServletRequest request) {
        String keyWord = request.getParameter("keyWord");
        List<UserCustom> userList = userService.queryUserByWord(keyWord);
        return userList;
    }

}
