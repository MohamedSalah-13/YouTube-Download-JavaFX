package com.hamza.youtubedownload.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TreeList {

    @Getter
    private final TreeView<String> treeView;

    public TreeList() {
        treeView = new TreeView<>();
        TreeItem<String> rootItem = new TreeItem<>("All Downloads");
        treeView.setRoot(rootItem);
        addItems();
    }

    private void addItems() {
        List<String> stringList = List.of("مضغوطة", "مستندات", "موسيقى", "برامج", "فيديو");
        List<TreeItem<String>> addTreeMain = new ArrayList<>();
        for (String s : stringList) {
            TreeItem<String> treeItem = new TreeItem<>(s);
            addTreeMain.add(treeItem);
        }
        treeView.getRoot().getChildren().addAll(addTreeMain);
    }
}
