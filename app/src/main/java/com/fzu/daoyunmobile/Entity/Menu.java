package com.fzu.daoyunmobile.Entity;

public class Menu {
    public int icon;
    public String menuName;

    public Menu(int icon, String menuName) {
        this.icon = icon;
        this.menuName = menuName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
