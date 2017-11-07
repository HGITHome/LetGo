package com.dgut.main.member.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Member;

/**
 * 好友申请service接口
 * Created by PUNK on 2017/1/28.
 */
public interface ApplyMng {


    void saveOrUpdate(Member publisher, Member receiver, String apply_reason);

    ApplyFriend findById(int i);

    ApplyFriend update(ApplyFriend apply);

    Integer countUnhandleApplication(Member member);

    Pagination findByUser(Integer userid, int pageNo, int pageSize);

    Pagination getApplyList(String username,String status,Integer pageNo,Integer pageSize);
}
