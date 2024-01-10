package com.nac.abc.controller;

import com.nac.abc.entity.News;
import com.nac.abc.entity.Result;
import com.nac.abc.service.INewsService;
import com.nac.abc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private INewsService newsService;

    @PostMapping("/addNews")
    public Result<String> addNews(@RequestBody News news){
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        String email = (String) stringObjectMap.get("email");
        news.setEmail(email);
        boolean addNews = newsService.addNews(news);
        if (addNews){
            return new Result<>(200,"添加成功",null);
        }else {
            return new Result<>(500,"添加失败",null);
        }
    }

    @PutMapping("/deleteNews")
    public Result<String> deleteNews(@RequestBody News news){
        boolean deleteNews = newsService.deleteNews(news);
        if (deleteNews){
            return new Result<>(200,"删除成功",null);
        }else {
            return new Result<>(500,"删除成失败",null);
        }
    }

    @PutMapping("/updateNews")
    public Result<String> updateNews(@RequestBody News news){
        boolean updateNews = newsService.updateNews(news);
        if (updateNews){
            return new Result<>(200,"修改成功",null);
        }else {
            return new Result<>(500,"修改失败",null);
        }
    }

    @GetMapping("/selectLatestNews")
    public Result<News> selectLatestNews(){
        News news = newsService.selectLatestNews();
        return new Result<>(200,"最新公告",news);
    }

    @GetMapping("/getAllNews")
    public Result<List<News>> getAllNews(){
        List<News> allNews = newsService.getAllNews();
        return new Result<>(200,"排序新闻",allNews);
    }

}
