package com.hamza.youtubedownload.tableSetting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;

public class TableColumnAnnotation {

    public <S> void getTable(TableView<S> tableView, Class<S> aClass) {
        for (Field f : aClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(ColumnData.class)) {
                ColumnData annotation = f.getAnnotation(ColumnData.class);
                String canonicalName = f.getType().getCanonicalName();
                TableColumn<S, ?> var10000 = switch (canonicalName) {
                    case "java.lang.Integer" -> extracted(f, annotation, Integer.class);
                    case "java.lang.Double" -> extracted(f, annotation, Double.class);
                    default -> extracted(f, annotation, String.class);
                };
                if (annotation.index() != 0)
                    tableView.getColumns().add(annotation.index(), var10000);
                else
                    tableView.getColumns().add(var10000);
            }
        }
        ObservableList<S> list = FXCollections.observableArrayList();
        tableView.setItems(list);
    }

    private <S, E> TableColumn<S, E> extracted(Field f, ColumnData annotation, E type) {
        TableColumn<S, E> tableColumn = new TableColumn<>(annotation.titleName());
        String anm = f.getName();
        if (!annotation.columnName().isEmpty()) anm = annotation.columnName();
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(anm));
        return tableColumn;
    }

}
