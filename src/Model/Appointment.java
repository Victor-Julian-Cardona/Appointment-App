package Model;

import Utility.Converters;

import java.sql.Timestamp;

/**
 * Appointment class that holds all the information of a given appointment
 */
public class Appointment {

    private final int appId;
    private final String title;
    private final String desc;
    private final String locat;
    private final String type;
    private final Timestamp start;
    private final Timestamp end;
    private final Timestamp createDate;
    private final String creator;
    private final Timestamp updateDate;
    private final String updator;
    private final int custId;
    private final int userId;
    private final int contId;

    /**
     * Constructor for Appointment class
     * @param appId
     * @param title
     * @param desc
     * @param locat
     * @param type
     * @param start
     * @param end
     * @param createDate
     * @param creator
     * @param updateDate
     * @param updator
     * @param custId
     * @param userId
     * @param contId
     */
    public Appointment(int appId, String title, String desc, String locat, String type, Timestamp start, Timestamp end, Timestamp createDate, String creator, Timestamp updateDate, String updator, int custId, int userId, int contId) {
        this.appId = appId;
        this.title = title;
        this.desc = desc;
        this.locat = locat;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.creator = creator;
        this.updateDate = updateDate;
        this.updator = updator;
        this.custId = custId;
        this.userId = userId;
        this.contId = contId;
    }

    /**
     * getter for appId
     * @return
     */
    public int getAppId() {
        return appId;
    }

    /**
     * getter for title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter for description
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * getter for location
     * @return
     */
    public String getLocat() {
        return locat;
    }

    /**
     * getter for type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * getter for start
     * @return
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * getter for end
     * @return
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * getter for create date
     * @return
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * getter for created by
     * @return
     */
    public String getCreator() {
        return creator;
    }

    /**
     * getter for update date
     * @return
     */
    public Timestamp getUpdateDate() {
        return updateDate;
    }

    /**
     * getter for update by
     * @return
     */
    public String getUpdator() {
        return updator;
    }

    /**
     * getter for customer id
     * @return
     */
    public int getCustId() {
        return custId;
    }

    /**
     * getter for user id
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     * getter for contact id
     * @return
     */
    public int getContId() {
        return contId;
    }
}
