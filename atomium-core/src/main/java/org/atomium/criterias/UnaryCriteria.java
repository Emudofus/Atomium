package org.atomium.criterias;

/**
 * @author Blackrush
 */
public abstract class UnaryCriteria implements CriteriaInterface {
    private final CriteriaInterface enclosingCriteria;

    protected UnaryCriteria(CriteriaInterface enclosingCriteria) {
        this.enclosingCriteria = enclosingCriteria;
    }

    public CriteriaInterface getEnclosingCriteria() {
        return enclosingCriteria;
    }
}
