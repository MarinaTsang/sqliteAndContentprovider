package greendao;

import com.example.greendao.UserDao;
import com.example.zeng.database.MyApp;

import java.util.List;

/**
 * greendao 实现的增删改查操作
 */
public class MyUserDao {

    /**
     * 增加数据，如果存在则覆盖
     * @param user
     */
    public static void insertUser(User user){
        MyApp.getDaoSession().getUserDao().insert(user);
    }

    /**
     * 根据ID删除数据
     * @param id
     */
    public static void deleteUser(long id){
        MyApp.getDaoSession().getUserDao().deleteByKey(id);
    }


    /**
     * 更新数据
     * @param user
     */
    public static void updateUser(User user){
        MyApp.getDaoSession().getUserDao().update(user);
    }


    /**
     * 根据特定条件查询
     * @return
     */
    public static List<User> queryHightLevel(){
        return  MyApp.getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Level.gt(5)).list();
    }

    public static List<User> queryById(){
        return MyApp.getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Id.ge(30),
                UserDao.Properties.Id.le(40)).list();
    }

    public static List<User> queryAll(){
        return MyApp.getDaoSession().getUserDao().loadAll();
    }

}
