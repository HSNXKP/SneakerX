package top.naccl.service;

import top.naccl.model.vo.Result;


public interface LoginService {


    Result login(String username, String password);
}
