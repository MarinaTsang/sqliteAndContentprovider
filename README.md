# sqliteAndContentprovider

## ContentProvider

##### 概念：

内容提供器，Android 四大组件之一。

##### 作用：

可以操作非本应用程序的数据，实现跨进程数据共享。

##### 优点：

可以在保证数据的安全性的情况下实现数据跨进程共享。因为contentprovider规范了统一的数据访问接口。

对底层数据存储方式抽象，即如果您将底层数据存储方式修改对数据应用层不会有影响。

##### 原理：

底层实现原理是Binder机制，Binder实现原理是通过Binder类，实现IBinder接口。Binder机制原理是：Binder驱动在内核空间创建一块数据缓存区，并调用系统mmap()方法实现内存映射，发送进程调用系统方法发送数据到虚拟内存区域，由于内核缓存区和接收进程空间地址存在映射关系，所以也就相当于发送到了接收进程的用户空间地址，实现了跨进程通信。 发送进程（client）和接收进程（server）通过ServiceManager与Binder驱动沟通。

##### 用法：

- 通过contentresolver进行增删改查操作。使用contentresolver的原因：可以统一管理provider
- 通过URI表示需要获取数据的表名，uri的固定形式：content：//**Authority**/path/id
  - **Authority**:授权信息，用以区别不同的contentprovider。
  - path：表名，用以区分contentprovider的不同的数据表。
  - ID：id号，用以区别表中不同的数据。
- UriMatchar:固定格式，单条记录：vnd.android.cursor.item/自定义，多条记录：vnd.android.cursor.dir/自定义

##### 用法示例1：

**读取手机中的通讯录信息**，涉及运行时权限申请详见官方文档：https://developer.android.com/training/permissions/requesting?hl=zh-cn

```
//读取通讯录主要逻辑代码
private void readContact(){
   
    Cursor cursor = null;
    try{
     //获取contentresolver
         ContentResolver resolver = context.getContentResolver();
         // //查询通信录的数据
         cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
         //遍历数据获取具体字段
            if (cursor!=null){
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contcacts.add(name+"\n"+number);
                }
                adapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_activated_1, contcacts);
                lv.setAdapter(adapter);
            }
    }catch(Exception e){
        e.printStackTrace();
    }finally{
        if(cursor!=null){
            cursor.close();
        }
    }
}
```

##### 用法示例2:

实现跨进程数据共享。步骤如下：

- 创建好数据库

- 在进程A中创建Provider，注意需要注册，并且设置可以对外。
- 在provider中定义好UriMatcher和authorty(与B进程通信的链接)。
- 实现6大方法。
- 在B进程通过上下文获取contentresolver，通过resolver对A进程数据库进程增删改查操作。


## sqlite

##### 基本介绍：

http://tiimor.cn/Android%E9%BB%84%E9%87%91%E7%AF%87-SQLite%E6%95%B0%E6%8D%AE%E5%BA%93/

是android存储方式的一种，一个轻量级的关系型数据库。运算速度非常快，占用资源小，一般只需要几百k内存。存储位置是/data/data/<pakageName>/databases,**适用于存储 结构型关系型数据**。

##### 特点：

- 轻量级
- 独立性：sqlite的核心引擎不需要依赖第三方软件，即无需“安装”。
- 隔离性：sqlite的所有信息包含在一个文件夹内，方便管理。
- 跨平台：支持大部分操作系统，如Android和iOS
- 安全性：sqlite通过数据库级上的独占性和共享锁来实现独立事务处理，这意味着多个进程可以在同一时间读取数据库的数据，但只能有一个可以写入数据。
- 弱类型字段：同一列中的数据可以是不同类型。

#### 使用

##### 创建/打开数据库的两大种方式：

- 继承SQLiteOpenHelper,一般是创建数据库.一般用于操作内部数据库。
- 使用SQLiteDatabase的静态方法openOrCreateDatabase(String path,CursorFactory factory)打开一个在sd卡的数据库 。具体方法：继承ContextWrapper,重写openOrCreateDatabase方法。
- 上下文调用SQLiteDatabase抽象方法openOrCreateDatabase(String name,int mode,CursorFactory factory); 如果不存在数据库则创建，其中参数name在一个包下是唯一的。

##### 调试工具：

- SQLiteExpert
- 命令行adb shell 查看。

##### 使用示例：

- 创建一个用户类数据库，包括字段：uid,name,email，level，isFirst。
- 默认创建的文件夹在/data/data/<pakageName>/databases/，可以自定义表的存储位置。
- 使用SQL语句创建表：create table table_name (创建的字段)
- SQL语句可能包含的字段类型：

```
 NOT NULL ：非空
  UNIQUE ： 唯一
  PRIMARY KEY ：主键
  FOREIGN KEY : 外键
  CHECK ：条件检查
  DEFAULT : 默认
```

- getReadableDatabase()和getWritableDatabase()的区别

  - 两者都是创建或打开（不存在则返回）一个数据库并返回可对数据库进行读写的对象。
  - getReadableDatabase()：当数据库不可写入的时候，以只读的方式打开数据库。
  - getWritableDatabase()：当数据库不可写入的时候，报异常。

- contentvalues也是将用户传入的值取出并进行拼接SQL语句，不推荐开发者直接使用SQL语句的原因是不能返回值。

  - contentvalues是一个初始化容量为8固定key值是字符串的map集合，实现了parcelable接口。

  ##### 1、创建表

  ```
  /**
   * 数据库创建及版本管理类
   */
  
  public class MySqlHelper extends SQLiteOpenHelper {
  
      private static final int VERSION = 1;
  
      public static final String DBNAME = "user.db";   //存放在默认的目录下
  
      public static final String TABLE_NAME = "user";
  
      //如果有需求可以自定义存放的路径
  //    private static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sqlitetest/";
  
      public MySqlHelper(Context context, String name) {
          super(context, name, null, VERSION);
      }
  
      /**
       * 如果没有创建数据库时才调用
       * @param db
       */
      @Override
      public void onCreate(SQLiteDatabase db) {
          //创建路径：如有需要
  //        File file = new File(FILE_DIR);
  //        if (!file.exists()){
  //            file.mkdir();
  //        }
  
          //使用sql语句创建表
          db.execSQL("create table if not exists "+TABLE_NAME+"(uid integer primary key autoincrement ," +
                  "nickname text," +
                  "egpoint real," +
                  "gender bit default 1 )");
  
      }
  
      /**
       * 只有当版本号不一致时才会调用，在这里执行更新操作
       * @param db
       * @param oldVersion
       * @param newVersion
       */
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          //更新：先删除旧表在创建新表
          if (newVersion>oldVersion){
              String sql = "DROP TABLE IF EXISTS "+TABLE_NAME;
              db.execSQL(sql);
              onCreate(db);
          }
      }
  }
  ```

  ##### 2、常用方法

  ```
  /**
   * 数据库操作类：增删改查操作
   */
  public class UserDao {
  
      private MySqlHelper helper;
  
      public UserDao(Context context) {
          helper = new MySqlHelper(context, MySqlHelper.DBNAME);
      }
  
      /**
       * 插入数据
       *
       * @param userInfo
       * @return 返回受影响行号
       */
      public long addDatas(UserInfo userInfo) {
          //1。获取可操作的数据库对象
          SQLiteDatabase readableDatabase = helper.getReadableDatabase();
          //构建需要添加的数据
          ContentValues values = new ContentValues();
          values.clear();
          values.put("nickname", userInfo.getNickName());
          values.put("egpoint", userInfo.getEgpoint());
          values.put("gender", userInfo.getGender());
  
          return readableDatabase.insert(MySqlHelper.TABLE_NAME, null, values);
      }
   /**
       * 更新的数据，
       *
       * @param userInfo 更新后的数据
       * @param where  需要更新的条件
       * @return
       */
      public int updateData(UserInfo userInfo, String where, String[] whereAgrs) {
          SQLiteDatabase database = helper.getReadableDatabase();
          ContentValues values1= new ContentValues();
          values1.put("nickName",userInfo.getNickName());
          values1.put("egpoint", userInfo.getEgpoint());
          values1.put("gender", userInfo.getGender());
          return database.update(MySqlHelper.TABLE_NAME, values1, where, whereAgrs);
      }
    /**
       * 删除数据
       * @param where
       * @param whereAgrs
       * @return
       */
      public int deleteData(String where, String[] whereAgrs) {
          SQLiteDatabase readableDatabase = helper.getReadableDatabase();
          return readableDatabase.delete(MySqlHelper.TABLE_NAME, where, whereAgrs);
      }
     /**
       * 查询数据
       * @param where
       * @param whereAgrs
       * @return
       */
      public List<UserInfo> queryDataByWhere(String where, String[] whereAgrs){
          SQLiteDatabase database = helper.getReadableDatabase();
  
          Cursor query = null;
          List<UserInfo> userInfos = null;
          try {
              query = database.query(MySqlHelper.TABLE_NAME, null, where, whereAgrs, null,null,null);
              if (query!=null){
                  while (query.moveToNext()){
                      int uid = query.getInt(query.getColumnIndex("uid"));
                      String nickname = query.getString(query.getColumnIndex("nickname"));
                      double egpoint = query.getDouble(query.getColumnIndex("egpoint"));
                      int genders = query.getInt(query.getColumnIndex("gender"));
  
                      UserInfo userInfo = new UserInfo();
                      userInfo.setUid(uid);
                      userInfo.setNickName(nickname);
                      userInfo.setEgpoint(egpoint);
                      userInfo.setGender(genders);
  
                      if (userInfos==null){
                          userInfos = new ArrayList<>();
                      }
  
                      userInfos.add(userInfo);
                  }
              }
          }catch (Exception e){
              e.printStackTrace();
          }finally {
              if (query!=null){
                  query.close();
              }
          }
          return userInfos;
      }
  }
  ```

  ##### 判断表是否存在

  sqlite_master是系统表，sqlitedatabase会为每一个数据库创建一个这样的表，目的是记录用户自己创建的表的索引。如果用户进行了表的增删改查操作，这个表都会相应的进行索引的改动，可以通过查询sqlite_master判断表是否存在。

  ```
  private boolean isTableExist(){
      String sql = "select count(*) as c from sqlite_master where type = 'table' and name = 'tableName'";
      Cursor cursor = db.rawQuery(sql,null);
      if(cursor.moveToNext()){
          int count = cursor.getInt(0);
          if(count >0){
              return true;
          }
      }
    retrun false;
  }
  ```


