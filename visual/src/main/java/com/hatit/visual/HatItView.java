package com.hatit.visual;

import com.hatit.visual.beige.BeigeView;
import com.hatit.visual.black.CornerView;
import com.hatit.visual.blue.BlueView;
import com.hatit.visual.rose.RoseView;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class HatItView extends GridPane {
    //_______________________________________________ Parameters
    private final CornerView cornerView;
    private final BlueView blueView;
    private final RoseView roseView;
    private final BeigeView beigeView;

    //_______________________________________________ Initialize
    public HatItView(Enviroment enviroment) {
        cornerView   = new CornerView(enviroment);
        blueView = new BlueView(enviroment);
        roseView = new RoseView(enviroment);
        beigeView = new BeigeView(enviroment);

        setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        initContent();
        initRows();
        initColumns();

    }

    //_______________________________________________ Methods
    private void initContent() {
        add(cornerView, 0, 0);
        add(blueView, 0, 1);
        add(roseView, 1, 0);
        add(beigeView, 1, 1);
    }

    private void initRows() {
        RowConstraints firstRow = new RowConstraints();
        RowConstraints secondRow = new RowConstraints();
        firstRow.setVgrow(Priority.NEVER);
        firstRow.setFillHeight(true);
        secondRow.setVgrow(Priority.ALWAYS);
        secondRow.setFillHeight(true);
        getRowConstraints().addAll(firstRow, secondRow);
    }

    private void initColumns() {
        ColumnConstraints firstColumn = new ColumnConstraints();
        ColumnConstraints secondColumn = new ColumnConstraints();
        firstColumn.setHgrow(Priority.NEVER);
        firstColumn.setFillWidth(true);
        secondColumn.setHgrow(Priority.ALWAYS);
        secondColumn.setFillWidth(true);
        getColumnConstraints().addAll(firstColumn, secondColumn);
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
