package com.dgut.main.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.entity.Accusation;
import com.dgut.main.entity.AccusationCategory;
import com.dgut.main.entity.AccusationType;
import com.dgut.main.member.entity.Member;

/**
 * Created by PUNK on 2017/3/21.
 */
public interface AccusationMng {
    Pagination getCategory(String queryName, int pageNo, int pageSize);


    AccusationCategory addCategory(AccusationCategory bean);

    AccusationCategory findCategoryById(int id);

    AccusationCategory updateCategory(AccusationCategory bean);

    AccusationCategory findCategoryByName(String categoryName);

    AccusationCategory deleteCategory(Integer id);

    Pagination getTypeList(String categoryId,String typeName, int pageNo, int pageSize);

    AccusationType findTypeByName(String categoryId, String name);

    AccusationType  saveType(String categoryId, AccusationType type);

    AccusationType findTypeById(Integer id);

    AccusationType updateType(AccusationType type);

    AccusationType deleteType(String s);

    Accusation addAccusation(Member member, Member respondent, String content,AccusationType type);

    Pagination getAccusationList(String reporter, String respondent,Integer queryType, String status, int pageNo, int pageSize);

    Accusation findAccusationById(int id);

    Accusation update(String id, String reply);
}
