package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.QualitativeSetting;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class QualitativeTypeContentView extends VBox {
    //_______________________________________________ Parameters
    private final QualitativeSetting setting;

    private final TextField fromField = new TextField();
    private final TextField toField = new TextField();

    //_______________________________________________ Initialize
    public QualitativeTypeContentView(Criteria criteria) {
        this.setting = (QualitativeSetting) criteria.propSettings().get();

        initUI();

        fromField.setText("" + setting.propRange().get().getMin());
        fromField.textProperty().addListener((observable, oldValue, newValue) -> updateRange());
        toField  .setText("" + setting.propRange().get().getMax());
        toField.textProperty().addListener((observable, oldValue, newValue) -> updateRange());
    }

    //_______________________________________________ Methods
    private void initUI() {
        setSpacing(12);
        getChildren().addAll(
                new Label("Von:"),
                fromField,
                new Label("Bis:"),
                toField);
    }

    private void updateRange() {
        int from = getRangePart(fromField);
        int to   = getRangePart(toField);
        QualitativeSetting.Range range = createUsefulRange(from, to);
        setting.propRange().setValue(range);
    }

    private QualitativeSetting.Range createUsefulRange(int from, int to) {
        if (from < 0) from = 0;
        if (to < 0) to = 0;
        if (to <= from) to = from + 1;
        return new QualitativeSetting.Range(from, to);
    }

    private int getRangePart(TextField field) {
        String text = field.getText();
        if (text != null && ! text.isBlank()) {
            try {
                return Integer.parseInt(text);
            }
            catch (NumberFormatException e) {
                // some parts are not parseable as int, fallthrough to 0
            }
        }
        return 0;
    }

    //_______________________________________________ Inner Classes
    //_______________________________________________ End
}
