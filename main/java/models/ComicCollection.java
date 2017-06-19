package models;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by ganger on 2017/6/17.
 */
public class ComicCollection extends DataSupport {

    private int id;
    private String title;
    private String url;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
