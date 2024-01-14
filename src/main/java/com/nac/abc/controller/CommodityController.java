package com.nac.abc.controller;

import com.alibaba.fastjson2.JSON;
import com.nac.abc.entity.Commodity;
import com.nac.abc.entity.Result;
import com.nac.abc.service.impl.CommodityServiceImpl;
import com.nac.abc.utils.FileUtils;
import com.nac.abc.utils.ThreadLocalUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.security.auth.message.AuthStatus;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Value("${file.url}")
    private String url;//C:\\D\\file\\

    @Autowired
    private CommodityServiceImpl commodityService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //添加商品->图片与商品信息一并添加
    @PostMapping("/addCommodity")
    public Result<String> addCommodity(@RequestParam("img") MultipartFile file,@ModelAttribute Commodity commodity) throws IOException {
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        Integer userId = (Integer) stringObjectMap.get("id");
        String userID = String.valueOf(userId);
//        String userID = (String) stringObjectMap.get("id");

        //查看今天此用户是否已经发布三个商品 是->不许再次添加 否->允许添加
        stringRedisTemplate.opsForHash().increment(userID , "commodity", -1);
        String outdated = Objects.requireNonNull(stringRedisTemplate.opsForHash().get(userID, "commodity")).toString();
        if (outdated.equals("0")){
            return new Result<>(500,"今日添加商品数量已达上限",null);
        }

        commodity.setUserId(userId);
        boolean addCommodity = commodityService.addCommodity(commodity);
        if (addCommodity){
            Commodity c = commodityService.selectOneCommodity(commodity);
            String imageNewName = FileUtils.uploadImage(file, url);
            c.setImage(imageNewName);
            boolean updateCommodityById = commodityService.updateCommodityById(c);
            if (updateCommodityById){
                return new Result<>(200,"添加商品成功",null);
            }else {
                return new Result<>(500,"添加商品失败",null);
            }
        }else {
            return new Result<>(500,"添加商品失败",null);
        }
    }

    //逻辑删除商品
    @PutMapping("/deleteCommodity")
    public Result<String> deleteCommodity(@RequestBody Commodity commodity) throws IOException {
        //前端传过来的image为"commodity/commodityImgShow/04db4c2d2ecd46468bbeee6510ff3fe2.jpg" 先要从中获取字符串“04db4c2d2ecd46468bbeee6510ff3fe2.jpg”
        // 找到文件名的起始位置
        int fileNameStartIndex = commodity.getImage().lastIndexOf("/") + 1;
        // 使用substring方法获取文件名
        String fileName = commodity.getImage().substring(fileNameStartIndex);
        commodity.setImage(fileName);
        //逻辑删除
        boolean b = commodityService.deleteCommodity(commodity);
        if (b){
            FileUtils.deleteImage(url, commodity.getImage());
            return new Result<>(200,"删除商品成功",null);
        }else{
            return new Result<>(500,"删除商品失败",null);
        }
    }

    //修改商品 只能修改商品信息 不能修改图片
    @PutMapping("/updateCommodity")
    public Result<String> updateCommodity(@RequestBody Commodity commodity){
        Commodity c = commodityService.selectOneById(commodity.getId());
        String image = c.getImage();
        commodity.setImage(image);
        boolean b = commodityService.updateCommodity(commodity);
        if (b){
            return new Result<>(200,"修改商品成功",null);
        }else {
            return new Result<>(500,"修改商品失败",null);
        }

    }

    //展示商品图片
    @GetMapping("/commodityImgShow/{imageUrl}")
    public boolean commodityImgShow (@PathVariable String imageUrl, HttpServletResponse response) throws Exception {
        return FileUtils.showImage(url,imageUrl,response);
    }

    //条件查询商品
    @GetMapping("/getCommodityByCondition")
    public Result<List<Commodity>> getCommodityByCondition (@RequestBody Map<String,Object> stringObjectMap){
        String string = JSON.toJSONString(stringObjectMap);
        Commodity commodity = JSON.parseObject(string, Commodity.class);
        List<Commodity> commodityList = commodityService.selectCommodityByCondition(commodity);
        commodityList.forEach(c -> c.setImage("commodity/commodityImgShow/"+c.getImage()));
        return new Result<>(200,"条件查询成功",commodityList);
    }

    //查询个人所有闲置商品
    @GetMapping("/getMyCommodity")
    public Result<List<Commodity>> getMyCommodity(){
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        Integer userId = (Integer) stringObjectMap.get("id");
        List<Commodity> myCommodityList = commodityService.getMyCommodity(userId);
        myCommodityList.forEach(c -> c.setImage("commodity/commodityImgShow/"+c.getImage()));
        return new Result<>(200,"查询个人闲置商品",myCommodityList);
    }
}
