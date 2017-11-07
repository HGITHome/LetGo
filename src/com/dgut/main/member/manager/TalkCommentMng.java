package com.dgut.main.member.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.TalkComment;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
public interface TalkCommentMng {

    TalkComment findById(Integer id);

    void disabledComment(TalkComment comment);

    void setCommentValid(TalkComment comment);

    Pagination getComments(String username, String status, int pageNo, int pageSize);

    List<Object[]> getSexInfo();
}
