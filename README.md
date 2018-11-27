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

