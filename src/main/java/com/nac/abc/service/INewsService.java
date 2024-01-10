package com.nac.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nac.abc.entity.News;

import java.util.List;

public interface INewsService extends IService<News> {
    boolean addNews(News news);

    boolean deleteNews(News news);

    boolean updateNews(News news);

    News selectLatestNews();

    List<News> getAllNews();


}
