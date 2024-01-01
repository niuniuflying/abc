package com.nac.abc.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nac.abc.entity.Result;
import com.nac.abc.mapper.UserMapper;
import com.nac.abc.entity.User;
import com.nac.abc.service.IEmailService;
import com.nac.abc.service.IUserService;
import com.nac.abc.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${file.url}")
    private String url;//C:\\D\\file\\

    @Value("${file.size}")
    private Integer size;//1MB

    @Autowired
    private IUserService iUserService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IEmailService iEmailService;

    //登录 把用户Id 和用户邮箱放入Token中 一并返回前端
    @GetMapping("/login")
    public Result<String> login(@RequestBody Map<String,Object> stringObjectMap) {
        System.out.println("接收登录数据（email+password）："+stringObjectMap);
        String email = String.valueOf(stringObjectMap.get("email"));
        String password = String.valueOf(stringObjectMap.get("password"));
        String md5Password = Md5Util.getMD5String(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email)
                .eq("password", md5Password)
                .eq("isDelete","0");
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("id",user.getId());
            hashMap.put("email",user.getEmail());
            String token = JwtUtil.genToken(hashMap);
            System.out.println("登录生成的token："+token);
            return new Result<>(200, "登录成功", token);
        }
        return new Result<>(500,"登录失败",null);
    }

    //注册时发送的验证码并存到Redis
    @PostMapping("/email/{email}")
    public Result<String> sendMailAndSetRedis(@PathVariable String email){
        String code = CodeUtils.code();
        System.out.println("发送的验证码："+code);
        iEmailService.sendCode(email,code);
        iUserService.setRedisCode(email,code);
        return new Result<>(200,"发送验证码并存入Redis","success");
    }

    //注册
    @PostMapping("/register")
    public Result<Map<String,Object>> register(@RequestBody Map<String,Object> stringObjectMap){
        System.out.println("接收注册数据"+stringObjectMap);
        String email = String.valueOf(stringObjectMap.get("email"));
        String code = String.valueOf(stringObjectMap.get("code"));
        String password = String.valueOf(stringObjectMap.get("password"));
        String username = String.valueOf(stringObjectMap.get("username"));
        //校验用户名密码长度是否合法
        if (password.matches("^\\S{5,16}$")&&username.matches("^\\S{5,16}$")){
            //密码经过MD5加密存入数据库
            String md5Password = Md5Util.getMD5String(password);
            String selectCodeByEmail = iUserService.selectCodeByEmail(email);
            User user = iUserService.selectUserByEmail(email);
            //user==null 未注册过
            //user.getIsdelete().equals("1")  注册过但注销了
            //code.equals(selectCodeByEmail)  需保持接收到的验证码和所填写的验证码一致
            if ((user==null||user.getIsDelete().equals("1"))&&code.equals(selectCodeByEmail)){
                String toJSONString = JSON.toJSONString(stringObjectMap);
                User user1 = JSON.parseObject(toJSONString, User.class);
                user1.setPassword(md5Password);
                boolean save = iUserService.save(user1);
                if (save){
                    return new Result<>(200,"注册成功",null);
                }else{
                    return new Result<>(500,"注册失败",null);
                }
            }else {
                return new Result<>(500,"注册失败",null);
            }
        }else {
            return new Result<>(500,"注册失败",null);
        }
    }

    //①查找该用户信息
    /*@GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token){
        //根据邮箱查询用户所有信息
        Map<String, Object> stringObjectMap = JwtUtil.parseToken(token);
        String email = String.valueOf(stringObjectMap.get("email"));
        User user = iUserService.selectUserByEmail(email);
        return new Result<>(200,"查询用户信息成功",user);
    }*/

    //②查找该用户信息 引入ThreadLocal Test中有用例
    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        //根据邮箱查询用户所有信息
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        String email = (String) stringObjectMap.get("email");
        User user = iUserService.selectUserByEmail(email);
        return new Result<>(200,"查询用户信息成功",user);
    }

    //修改个人信息 还可用于注销账号 即逻辑删除 修改isDelete改为1 其中修改密码较为特殊 要单独拎出来进行加密处理
    @PutMapping("/updateUserInfo")
    public Result<User> updateUserInfo(@RequestBody Map<String,Object> stringObjectMap){
        System.out.println("接收到的要修改的信息(必须包含此用户id）："+stringObjectMap);
        String string = JSON.toJSONString(stringObjectMap);
        User user = JSON.parseObject(string, User.class);
        String password = String.valueOf(stringObjectMap.get("password"));
        if (password.isEmpty()){
            boolean updateUserInfo = iUserService.updateUserInfo(user);
            if (updateUserInfo){
                return new Result<>(200,"修改用户信息成功",null);
            }else {
                return new Result<>(500,"修改用户信息失败",null);
            }
        }else {
            String md5Password = Md5Util.getMD5String(password);
            user.setPassword(md5Password);
            boolean updateUserInfo = iUserService.updateUserInfo(user);
            if (updateUserInfo){
                return new Result<>(200,"修改用户信息成功",null);
            }else {
                return new Result<>(500,"修改用户信息失败",null);
            }
        }
    }

    //    上传头像
    @GetMapping("avatarUpload")
    public Result<String> avatarUpload(@RequestParam("img") MultipartFile file) throws Exception {
        //size==1MB
        if (file.getSize()>size){
            return new Result<>(500,"最大上限为1MB","lose");
        }
//        获取文件名称
        String oldFileName=file.getOriginalFilename();
        System.out.println("原文件名称："+oldFileName);
//        设置新名称
        assert oldFileName != null;
        if (oldFileName.substring(oldFileName.lastIndexOf(".")).equals(".jpg")
                ||oldFileName.substring(oldFileName.lastIndexOf(".")).equals(".png")){
            String newFileName= UUIDUtil.generateRandomUUID() +oldFileName.substring(oldFileName.lastIndexOf("."));
            System.out.println("新文件名称:"+newFileName);
//            保存文件路径
            String path=url+newFileName;
            System.out.println("当前保存路径:"+path);
            File file1=new File(path);
            InputStream inputStream=file.getInputStream();
            try {
                OutputStream outputStream=new FileOutputStream(file1);
                int len=0;
                byte[] buffer=new byte[10240];
                while ((len=inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }

                outputStream.close();
                inputStream.close();

                //向数据库中存入图片名称
                Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
                String email = String.valueOf(stringObjectMap.get("email"));
                User user = iUserService.selectUserByEmail(email);
                user.setAvatar(newFileName);
                iUserService.updateUserInfo(user);

            }catch (Exception e){
                e.printStackTrace();
            }
            return new Result<>(200,"上传成功",newFileName);
        }else {
            return new Result<>(500,"请上传JPG或PNG格式！","格式错误");
        }
    }

    //    展示图片
    @GetMapping("avatarShow")
    public Result<String> avatarShow(HttpServletResponse response) throws Exception {
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        String email = String.valueOf(stringObjectMap.get("email"));
        User user = iUserService.selectUserByEmail(email);
        String avatar = user.getAvatar();
        String path=url+avatar;
        File file=new File(path);
        if (file.exists()) {
            FileCopyUtils.copy(new FileInputStream(path),response.getOutputStream());
            return new Result<>(500,"展示成功",null);
        }else {
            return new Result<>(500,"展示失败",null);
        }
    }

    //    修改头像(先删除原有的头像及地址 再重新上传头像和地址）
    @GetMapping("updateAvatar")
    public Result<String> updateAvatar(@RequestParam("img") MultipartFile file) throws Exception {
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        String email = String.valueOf(stringObjectMap.get("email"));
        User user = iUserService.selectUserByEmail(email);
        String avatar = user.getAvatar();
        System.out.println("删除文件名称:"+avatar);
        String newUrl=url+avatar;
        File f=new File(newUrl);
        if (f.exists()){
            Path path= Paths.get(newUrl);
            boolean flag = Files.deleteIfExists(path);
            System.out.println("是否删除成功:"+flag);
            if (flag){
                //调用上传图像方法
                Result<String> result = avatarUpload(file);
                Integer code = result.getCode();
                if (code==200){
                    return new Result<>(200,"修改成功",result.getData());
                }else {
                    return new Result<>(200,"修改失败", result.getData());
                }
            }else {
                return new Result<>(200,"修改失败", null);
            }
        }else {
            return new Result<>(200,"修改失败", null);
        }
    }

    //    下载图片
    @GetMapping("downloadAvatar")
    public Result<String> downloadAvatar(HttpServletResponse response)throws Exception{
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        String email = String.valueOf(stringObjectMap.get("email"));
        User user = iUserService.selectUserByEmail(email);
        String avatar = user.getAvatar();
        String newFileUrl=url+avatar;
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            //获取图片存放路径
            File file = new File(newFileUrl);
            if(!file.exists()) {
                return new Result<>(500,"文件不存在",null);
            }
            ips = new FileInputStream(file);
            response.setContentType("multipart/form-data");
            out = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[10240];
            while ((len = ips.read(buffer)) != -1){
                out.write(buffer,0,len);
            }
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            assert out != null;
            out.close();
            ips.close();
        }
        return null;
    }


}






