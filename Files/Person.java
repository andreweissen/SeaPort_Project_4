/**
 * Person.java - Class for <code>Person</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project4;

import java.util.Scanner;

/**
 * A subclass of <code>Thing</code>, the <code>Person</code> class represents dock workers located
 * at specific <code>SeaPort</code>s. Each worker has a specific profession, notated in the class
 * itself as <code>skill</code>, and is placed in a <code>SeaPort</code>'s <code>persons</code>
 * <code>ArrayList</code>. Class contains setter/getter and the overridden <code>toString()</code>
 * method as per the rubric.
 * <br />
 * <br />
 * For the Project 3 design, the author was initially wary of adding any fields related to the
 * various <code>Job</code> threads to the <code>Person</code> class. However, after the taxing
 * method of removing and readding <code>SeaPort.persons</code> entries for each thread was removed,
 * the author simply added a single <code>isWorking</code> <code>boolean</code> flag to denote the
 * worker's employment status.
 * <br />
 * <br />
 * For the Project 4 design, the author originally pursued a design ethos similar in style to that
 * previously employed by the <code>Job</code> class in Project 3. As such, this class contained a
 * number of GUI-related methods that enable the main GUI class, <code>SeaPortProgram</code>, to
 * assemble a "row" related to this worker and add it to a <code>JTable</code>-esque pseudo-table in
 * the GUI. However, upon re-reading the project rubric, the author scrapped this design and
 * reverted to the display of <code>ResourcePool</code>s as pseudo-rows rather than person objects.
 * This is more in line with the specifics of the rubric, vague though they are.
 * <br />
 * <br />
 * Class extends <code>Thing</code>
 * @see project4.Thing
 * @author Andrew Eissen
 */
final class Person extends Thing {

    // Rubric-required field
    private String skill;

    // Additional fields
    private volatile boolean isWorking; // Should be volatile?

    /**
     * Parameterized constructor
     * @param scannerContents Contents of the <code>.txt</code> file
     */
    protected Person(Scanner scannerContents) {
        super(scannerContents);

        if (scannerContents.hasNext()) {
            this.setSkill(scannerContents.next());
        } else {
            this.setSkill("Error");
        }

        this.setIsWorking(false);
    }

    // Setters

    /**
     * Setter for <code>skill</code>
     * @param skill <code>String</code>
     * @return void
     */
    private void setSkill(String skill) {
        this.skill = skill;
    }

    /**
     * Setter for <code>isWorking</code>
     * @param isWorking <code>boolean</code>
     * @return void
     */
    protected void setIsWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }

    // Getters

    /**
     * Getter for <code>skill</code>
     * @return <code>this.skill</code>
     */
    protected String getSkill() {
        return this.skill;
    }

    /**
     * Getter for <code>isWorking</code>
     * @return <code>this.isWorking</code>
     */
    protected boolean getIsWorking() {
        return this.isWorking;
    }

    // Overridden method

    /**
     * @inheritdoc
     * @return <code>String</code>
     */
    @Override
    public String toString() {
        return "Person: " + super.toString() + " " + this.getSkill();
    }
}