package com.hatit.visual.beige.criteria;

import com.hatit.data.criteria.Criteria;
import com.hatit.data.criteria.CriteriaType;
import com.hatit.data.tournament.Tournament;
import com.hatit.visual.ResourceUtil;
import com.hatit.visual.ScenePartChangeListener;
import com.hatit.visual.StyleUtil;
import com.hatit.visual.common.BoxListView;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class CriteriaOverview extends HBox {
    //_______________________________________________ Parameters
    private static final String ID = "criteria-overview";

    //_______________________________________________ Initialize
    public CriteriaOverview(Tournament tournament) {
        setId(ID);
        BoxListView<Criteria> criteriaListView = new BoxListView<>(tournament.propCriteria(), ResourceUtil.ADD_CRITERIA, Criteria::of, CriteriaRegion::new);
        CriteriaView criteriaView = new CriteriaView(criteriaListView.propSelectedItem());

        getChildren().addAll(criteriaListView, criteriaView);
    }

    //_______________________________________________ Methods
    //_______________________________________________ Inner Classes
    private static class CriteriaRegion extends VBox {
        private final Label nameLabel = StyleUtil.h1("Name");
        private final Label typeLabel = StyleUtil.h2("Typ");

        private final Criteria criteria;

        public CriteriaRegion(Criteria criteria) {
            this.criteria = criteria;
            getChildren().addAll(nameLabel, typeLabel);
            sceneProperty().addListener(new ScenePartChangeListener(this::onShow, this::onHide));
        }

        private void onShow() {
            nameLabel.textProperty().bind(criteria.propName());
            typeLabel.textProperty().bind(new CriteriaTypeToStringBinding(criteria.propType()));
        }

        private void onHide() {
            nameLabel.textProperty().unbind();
            typeLabel.textProperty().unbind();
        }
    }

    private static class CriteriaTypeToStringBinding extends StringBinding implements InvalidationListener {
        private final ObjectProperty<CriteriaType> criteriaTypeProperty;

        private CriteriaTypeToStringBinding(ObjectProperty<CriteriaType> criteriaTypeProperty) {
            this.criteriaTypeProperty = criteriaTypeProperty;
            this.criteriaTypeProperty.addListener(this);
        }

        @Override
        public void dispose() {
            criteriaTypeProperty.removeListener(this);
        }

        @Override
        public void invalidated(Observable observable) {
            invalidate();
        }

        @Override
        protected String computeValue() {
            CriteriaType criteriaType = criteriaTypeProperty.get();
            return criteriaType == null ? "" : criteriaType.getName();
        }
    }

    //_______________________________________________ End
}
