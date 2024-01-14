package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.entity.News;
import com.nac.abc.mapper.NewsMapper;
import com.nac.abc.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public boolean addNews(News news) {

        int insert = newsMapper.insert(news);
        return insert==1;
    }

    @Override
    public boolean deleteNews(News news) {
        news.setIsDelete("1");
        int updateById = newsMapper.updateById(news);
        return updateById==1;
    }

    @Override
    public boolean updateNews(News news) {
        int updateById = newsMapper.updateById(news);
        return updateById==1;
    }

    @Override
    public News selectLatestNews() {
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createTime").last("LIMIT 1");
        wrapper.eq("isDelete","0");
        return newsMapper.selectOne(wrapper);
    }

    @Override
    public List<News> getAllNews() {
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createTime");
        return newsMapper.selectList(wrapper);
    }
}
