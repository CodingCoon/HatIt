package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.common.BoxListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class EditCriteriaView extends HBox {
    //_______________________________________________ Parameters
    private static final String ID = "edit-criteria-view";

    //_______________________________________________ Initialize
    public EditCriteriaView(Tournament tournament) {
        setId(ID);
        BoxListView<Criteria> criteriaListView = new BoxListView<>(tournament.propCriteria(), Criteria::of, CriteriaRegion::new);
        CriteriaView criteriaView = new CriteriaView(criteriaListView.propSelectedItem());

        getChildren().addAll(criteriaListView, criteriaView);
    }

    //_______________________________________________ Methods
    //_______________________________________________ Inner Classes
    private static class CriteriaRegion extends VBox {
        private final Label nameLabel = StyleUtil.h0("Name");
        private final Label typeLabel = StyleUtil.h2("Typ");

        private final Criteria criteria;

        public CriteriaRegion(Criteria criteria) {
            this.criteria = criteria;
            getChildren().addAll(nameLabel, typeLabel);
            sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
        }

        private void onShow() {
            nameLabel.textProperty().bind(criteria.propName());
            typeLabel.textProperty().bind(criteria.propType().asString());
        }

        private void onHide() {
            nameLabel.textProperty().unbind();
            typeLabel.textProperty().unbind();
        }
    }

    //_______________________________________________ End
}
