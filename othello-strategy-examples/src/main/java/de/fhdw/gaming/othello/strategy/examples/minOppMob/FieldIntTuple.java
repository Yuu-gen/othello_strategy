package de.fhdw.gaming.othello.strategy.examples.minOppMob;

import de.fhdw.gaming.othello.core.domain.OthelloField;

public class FieldIntTuple {
    /**
     * Place to store an Integer.
     */
    private Integer value;
    /**
     * Place to store an OthelloField.
     */
    private OthelloField field;

    /**
     * Constructor for FieldIntTuple.
     *
     * @param Value
     * @param field
     */
    public FieldIntTuple(final Integer value, final OthelloField field) {
        this.value = value;
        this.field = field;
    }

    /**
     * Gets the Value of the FieldIntTuple.
     *
     * @return
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * Gets the Value of the FieldIntTuple.
     *
     * @return
     */
    public OthelloField getField() {
        return this.field;
    }

    /**
     * Sets the Value of FieldIntTuple.
     *
     * @param value
     */
    public void setValue(final Integer value) {
        this.value = value;
    }

    /**
     * Sets the field of FieldIntTuple.
     *
     * @param field
     */
    public void setField(final OthelloField field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return this.field.toString() + " at value: " + this.value.toString();
    }

}
