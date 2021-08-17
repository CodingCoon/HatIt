package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.ConstrainedRatingSetting;
import com.hatit.data.criteria.Criteria;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.beige.criteria.CriteriaView.TypeViewBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

class ConstrainedRatingViewBuilder implements TypeViewBuilder {
    //_______________________________________________ Parameters
    private final ConstrainedRatingSetting setting;

    private final Label fromLabel = StyleUtil.createLabel("Von:");
    private final Label toLabel   = StyleUtil.createLabel("Bis:");

    private final TextField fromField = new TextField();
    private final TextField toField = new TextField();

    //_______________________________________________ Initialize
    ConstrainedRatingViewBuilder(Criteria criteria) {
        this.setting = (ConstrainedRatingSetting) criteria.propSettings().get();

        fromField.setText("" + setting.getRange().getMin());
        fromField.textProperty().addListener((observable, oldValue, newValue) -> updateRange());
        toField  .setText("" + setting.getRange().getMax());
        toField.textProperty().addListener((observable, oldValue, newValue) -> updateRange());
    }

    //_______________________________________________ Methods
    @Override
    public void addUI(GridPane pane) {
        pane.add(fromLabel, 0, 2);
        pane.add(fromField, 1, 2);
        pane.add(toLabel  , 0, 3);
        pane.add(toField  , 1, 3);
    }

    @Override
    public void removeUI(GridPane pane) {
        pane.getChildren().removeAll(fromLabel, fromField, toLabel, toField);
    }

    private void updateRange() {
        int from = getRangePart(fromField);
        int to   = getRangePart(toField);
        ConstrainedRatingSetting.Range range = createUsefulRange(from, to);
        setting.setRange(range);
    }

    private ConstrainedRatingSetting.Range createUsefulRange(int from, int to) {
        if (from < 0) from = 0;
        if (to < 0) to = 0;
        if (to <= from) to = from + 1;
        return new ConstrainedRatingSetting.Range(from, to);
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
