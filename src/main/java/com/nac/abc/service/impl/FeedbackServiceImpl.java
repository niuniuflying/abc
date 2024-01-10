package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.entity.Feedback;
import com.nac.abc.mapper.FeedbackMapper;
import com.nac.abc.service.IFeedbackService;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {
}
