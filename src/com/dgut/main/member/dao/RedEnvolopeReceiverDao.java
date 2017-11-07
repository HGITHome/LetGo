package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.main.member.entity.RedEnvolopeReceiver;

/**
 * Created by PUNK on 2017/4/4.
 */
public interface RedEnvolopeReceiverDao {
    RedEnvolopeReceiver updateByUpdater(Updater<RedEnvolopeReceiver> updater);
}
