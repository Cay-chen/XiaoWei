package cay.com.xiaowei.Bean;

/**
 * Created by Cay on 2016/4/21.
 */
public class Tap {
    private int title;
    private int icon;
    private Class fragement;


    public Tap(int icon, Class fragement, int title) {
        this.title = title;
        this.icon = icon;
        this.fragement = fragement;
    }


    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragement() {
        return fragement;
    }

    public void setFragement(Class fragement) {
        this.fragement = fragement;
    }


}
