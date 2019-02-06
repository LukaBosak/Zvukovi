package hr.bosak.zvukovi;

import java.io.Serializable;

public class Folder implements Serializable {

    private String title;

    public Folder(String title){

        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
