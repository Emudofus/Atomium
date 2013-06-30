package org.atomium.criterias;

/**
 * @author Blackrush
 */
public abstract class BinaryCriteria implements CriteriaInterface {
    private final CriteriaInterface left, right;

    protected BinaryCriteria(CriteriaInterface left, CriteriaInterface right) {
        this.left = left;
        this.right = right;
    }

    public CriteriaInterface getLeft() {
        return left;
    }

    public CriteriaInterface getRight() {
        return right;
    }
}
