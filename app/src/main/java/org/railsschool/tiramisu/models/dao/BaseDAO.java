package org.railsschool.tiramisu.models.dao;

import io.realm.Realm;

/**
 * @class BaseDAO
 * @brief
 */
abstract class BaseDAO {
    private Realm _dal;

    public BaseDAO(Realm dal) {
        this._dal = dal;
    }

    public Realm getDAL() {
        return _dal;
    }
}
