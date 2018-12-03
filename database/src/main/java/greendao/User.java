package greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

    @Id(autoincrement = true)    //主键自增，
    private Long id; //必须使用Long类型

    private String name;

    @Unique
    private String uid;

    private Double egpoint;

    @Property
    private Long gameId;

    private String serverId;

    private String roleId;

    private int level;


    @Transient
    private String avaUrl;

    @Generated(hash = 767985296)
    public User(Long id, String name, String uid, Double egpoint, Long gameId,
            String serverId, String roleId, int level) {
        this.id = id;
        this.name = name;
        this.uid = uid;
        this.egpoint = egpoint;
        this.gameId = gameId;
        this.serverId = serverId;
        this.roleId = roleId;
        this.level = level;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getEgpoint() {
        return this.egpoint;
    }

    public void setEgpoint(Double egpoint) {
        this.egpoint = egpoint;
    }

    public Long getGameId() {
        return this.gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getServerId() {
        return this.serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
