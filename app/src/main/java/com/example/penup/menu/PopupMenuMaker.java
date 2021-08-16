package com.example.penup.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.penup.R;
import com.example.penup.viewmodel.ListFolderViewModel;

public class PopupMenuMaker {
    public static PopupMenu makeFolderMenuItem(Context context, View anchor){
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.inflate(R.menu.folder_item_menu);
        MenuItem item = popup.getMenu().findItem(R.id.menu_rename);
        item.setVisible(false);
        return popup;
    }
}
