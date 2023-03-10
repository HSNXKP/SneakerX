package top.naccl.service.impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.Url;
import top.naccl.mapper.UrlMapper;
import top.naccl.service.UrlService;

import java.util.List;

/**
 * @author: wdd
 * @date: 2023/3/2 16:39
 */
@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    UrlMapper urlMapper;

    @Override
    public List<Url> getAllUrl() {
        return urlMapper.getAllUrl();
    }
}
